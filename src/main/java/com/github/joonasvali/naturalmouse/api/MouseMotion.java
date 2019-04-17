package com.github.joonasvali.naturalmouse.api;

import com.github.joonasvali.naturalmouse.support.DoublePoint;
import com.github.joonasvali.naturalmouse.support.Flow;
import com.github.joonasvali.naturalmouse.support.MouseMotionNature;
import com.github.joonasvali.naturalmouse.util.MathUtil;
import com.github.joonasvali.naturalmouse.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.awt.*;
import java.util.Random;

/**
 * Contains instructions to move cursor smoothly to the destination coordinates from where ever the cursor
 * currently is. The class is reusable, meaning user can keep calling it and the cursor returns in a random,
 * but reliable way, described in this class, to the destination.
 */
public class MouseMotion {
  private static final Logger log = LoggerFactory.getLogger(MouseMotion.class);
  private final int minSteps;
  private final int effectFadeSteps;
  private final int reactionTimeBaseMs;
  private final int reactionTimeVariationMs;
  private final double timeToStepsDivider;
  private final Dimension screenSize;
  private final SystemCalls systemCalls;
  private final DeviationProvider deviationProvider;
  private final NoiseProvider noiseProvider;
  private final SpeedManager speedManager;
  private final OvershootManager overshootManager;
  private final int xDest;
  private final int yDest;
  private final Random random;
  private final MouseInfoAccessor mouseInfo;
  private Point mousePosition;

  /**
   * @param nature the nature that defines how mouse is moved
   * @param xDest  the x-coordinate of destination
   * @param yDest  the y-coordinate of destination
   * @param random the random used for unpredictability
   */
  public MouseMotion(MouseMotionNature nature, Random random, int xDest, int yDest) {
    this.deviationProvider = nature.getDeviationProvider();
    this.noiseProvider = nature.getNoiseProvider();
    this.systemCalls = nature.getSystemCalls();
    this.screenSize = systemCalls.getScreenSize();
    this.xDest = limitByScreenWidth(xDest);
    this.yDest = limitByScreenHeight(yDest);
    this.random = random;
    this.mouseInfo = nature.getMouseInfo();
    this.speedManager = nature.getSpeedManager();
    this.timeToStepsDivider = nature.getTimeToStepsDivider();
    this.minSteps = nature.getMinSteps();
    this.effectFadeSteps = nature.getEffectFadeSteps();
    this.reactionTimeBaseMs = nature.getReactionTimeBaseMs();
    this.reactionTimeVariationMs = nature.getReactionTimeVariationMs();
    this.overshootManager = nature.getOvershootManager();
  }

  /**
   * Blocking call, starts to move the cursor to the specified location from where it currently is.
   *
   * @throws InterruptedException when interrupted
   */
  public void move() throws InterruptedException {
    move((x, y) -> {
    });
  }

  /**
   * Blocking call, starts to move the cursor to the specified location from where it currently is.
   *
   * @param observer Provide observer if you are interested receiving the location of mouse on every step
   * @throws InterruptedException when interrupted
   */
  public void move(MouseMotionObserver observer) throws InterruptedException {
    updateMouseInfo();
    log.info("Starting to move mouse to ({}, {}), current position: ({}, {})", xDest, yDest, mousePosition.x, mousePosition.y);

    ArrayDeque<Movement> movements = createMovements();
    int overshoots = movements.size() - 1;
    while (mousePosition.x != xDest || mousePosition.y != yDest) {
      if (movements.isEmpty()) {
        // This shouldn't usually happen, but it's possible that somehow we won't end up on the target,
        // Then just re-attempt from mouse new position.
        updateMouseInfo();
        log.debug("Populating movement array.");
        movements = createMovements();
      }

      Movement movement = movements.removeFirst();
      if (!movements.isEmpty()) {
        log.debug("Using overshoots ({} out of {}), aiming at ({}, {})",
            overshoots - movements.size() + 1, overshoots, movement.destX, movement.destY);
      }

      double distance = movement.distance;
      long mouseMovementMs = movement.time;
      Flow flow = movement.flow;
      double xDistance = movement.xDistance;
      double yDistance = movement.yDistance;
      log.debug("Movement arc length computed to {} and time predicted to {} ms", (int) distance, mouseMovementMs);

      /* Number of steps is calculated from the movement time and limited by minimal amount of steps
         (should have at least MIN_STEPS) and distance (shouldn't have more steps than pixels travelled) */
      int steps = (int) Math.ceil(Math.min(distance, Math.max(mouseMovementMs / timeToStepsDivider, minSteps)));

      long startTime = systemCalls.currentTimeMillis();
      long stepTime = (long) (mouseMovementMs / (double) steps);

      updateMouseInfo();
      double simulatedMouseX = mousePosition.x;
      double simulatedMouseY = mousePosition.y;

      double deviationMultiplierX = (random.nextDouble() - 0.5) * 2;
      double deviationMultiplierY = (random.nextDouble() - 0.5) * 2;

      double completedXDistance = 0;
      double completedYDistance = 0;
      double noiseX = 0;
      double noiseY = 0;

      for (int i = 0; i < steps; i++) {
        // All steps take equal amount of time. This is a value from 0...1 describing how far along the process is.
        double timeCompletion = i / (double) steps;

        double effectFadeStep = Math.max(i - (steps - effectFadeSteps) + 1, 0);
        // value from 0 to 1, when effectFadeSteps remaining steps, starts to decrease to 0 linearly
        // This is here so noise and deviation wouldn't add offset to mouse final position, when we need accuracy.
        double effectFadeMultiplier = (effectFadeSteps - effectFadeStep) / effectFadeSteps;

        double xStepSize = flow.getStepSize(xDistance, steps, timeCompletion);
        double yStepSize = flow.getStepSize(yDistance, steps, timeCompletion);

        completedXDistance += xStepSize;
        completedYDistance += yStepSize;
        double completedDistance = Math.sqrt(Math.pow(completedXDistance, 2) + Math.pow(completedYDistance, 2));
        double completion = Math.min(1, completedDistance / distance);
        log.trace("Step: x: {} y: {} tc: {} c: {}", xStepSize, yStepSize, timeCompletion, completion);

        DoublePoint noise = noiseProvider.getNoise(random, xStepSize, yStepSize);
        DoublePoint deviation = deviationProvider.getDeviation(distance, completion);

        noiseX += noise.getX();
        noiseY += noise.getY();
        simulatedMouseX += xStepSize;
        simulatedMouseY += yStepSize;

        log.trace("EffectFadeMultiplier: {}", effectFadeMultiplier);
        log.trace("SimulatedMouse: [{}, {}]", simulatedMouseX, simulatedMouseY);

        long endTime = startTime + stepTime * (i + 1);
        int mousePosX = MathUtil.roundTowards(simulatedMouseX +
            deviation.getX() * deviationMultiplierX * effectFadeMultiplier +
            noiseX * effectFadeMultiplier, movement.destX);
        int mousePosY = MathUtil.roundTowards(simulatedMouseY +
            deviation.getY() * deviationMultiplierY * effectFadeMultiplier +
            noiseY * effectFadeMultiplier, movement.destY);

        mousePosX = limitByScreenWidth(mousePosX);
        mousePosY = limitByScreenHeight(mousePosY);

        systemCalls.setMousePosition(
            mousePosX,
            mousePosY
        );

        // Allow other action to take place or just observe, we'll later compensate by sleeping less.
        observer.observe(mousePosX, mousePosY);

        long timeLeft = endTime - systemCalls.currentTimeMillis();
        sleepAround(Math.max(timeLeft, 0), 0);
      }
      updateMouseInfo();
      if (mousePosition.x != movement.destX || mousePosition.y != movement.destY) {
        sleepAround(reactionTimeBaseMs, reactionTimeVariationMs);
      }
      log.debug("Steps completed, mouse at " + mousePosition.x + " " + mousePosition.y);
    }
    log.info("Mouse movement to ({}, {}) completed", xDest, yDest);
  }

  private ArrayDeque<Movement> createMovements() {
    ArrayDeque<Movement> movements = new ArrayDeque<>();
    int lastMousePositionX = mousePosition.x;
    int lastMousePositionY = mousePosition.y;
    int xDistance = xDest - lastMousePositionX;
    int yDistance = yDest - lastMousePositionY;

    double initialDistance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
    Pair<Flow, Long> flowTime = speedManager.getFlowWithTime(initialDistance);
    Flow flow = flowTime.x;
    long mouseMovementMs = flowTime.y;
    int overshoots = overshootManager.getOvershoots(flow, mouseMovementMs, initialDistance);

    if (overshoots == 0) {
      movements.add(new Movement(xDest, yDest, initialDistance, xDistance, yDistance, mouseMovementMs, flow));
      return movements;
    }

    for (int i = overshoots; i > 0; i--) {
      Point overshoot = overshootManager.getOvershootAmount(
          xDest - lastMousePositionX, yDest - lastMousePositionY, mouseMovementMs, i
      );
      int currentDestinationX = limitByScreenWidth(xDest + overshoot.x);
      int currentDestinationY = limitByScreenHeight(yDest + overshoot.y);
      xDistance = currentDestinationX - lastMousePositionX;
      yDistance = currentDestinationY - lastMousePositionY;
      double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
      flow = speedManager.getFlowWithTime(distance).x;
      movements.add(
          new Movement(currentDestinationX, currentDestinationY, distance, xDistance, yDistance, mouseMovementMs, flow)
      );
      lastMousePositionX = currentDestinationX;
      lastMousePositionY = currentDestinationY;
      // Apply for the next overshoot if exists.
      mouseMovementMs = overshootManager.deriveNextMouseMovementTimeMs(mouseMovementMs, i - 1);
    }

    xDistance = xDest - lastMousePositionX;
    yDistance = yDest - lastMousePositionY;
    double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
    movements.add(
        new Movement(xDest, yDest, distance, xDistance, yDistance, mouseMovementMs, flow)
    );

    return movements;
  }

  private int limitByScreenWidth(int value) {
    return Math.max(0, Math.min(screenSize.width - 1, value));
  }

  private int limitByScreenHeight(int value) {
    return Math.max(0, Math.min(screenSize.height - 1, value));
  }

  private void sleepAround(long sleepMin, long randomPart) throws InterruptedException {
    long sleepTime = (long) (sleepMin + random.nextDouble() * randomPart);
    if (log.isTraceEnabled() && sleepTime > 0) {
      updateMouseInfo();
      log.trace("Sleeping at ({}, {}) for {} ms", mousePosition.x, mousePosition.y, sleepTime);
    }
    systemCalls.sleep(sleepTime);
  }

  private void updateMouseInfo() {
    mousePosition = mouseInfo.getMousePosition();
  }

  private static class Movement {
    private final int destX;
    private final int destY;
    private final double distance;
    private final double xDistance;
    private final double yDistance;
    private final long time;
    private final Flow flow;

    public Movement(int destX, int destY, double distance, double xDistance, double yDistance, long time, Flow flow) {
      this.destX = destX;
      this.destY = destY;
      this.distance = distance;
      this.xDistance = xDistance;
      this.yDistance = yDistance;
      this.time = time;
      this.flow = flow;
    }
  }
}
