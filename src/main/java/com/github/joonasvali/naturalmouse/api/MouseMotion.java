package com.github.joonasvali.naturalmouse.api;

import com.github.joonasvali.naturalmouse.support.DoublePoint;
import com.github.joonasvali.naturalmouse.support.Flow;
import com.github.joonasvali.naturalmouse.support.MouseMotionNature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Random;

/**
 * Contains instructions to move cursor smoothly to the destination coordinates from where ever the cursor
 * currently is. The class is reusable, meaning user can keep calling it and the cursor returns in a random,
 * but reliable way, described in this class, to the destination.
 */
public class MouseMotion {
  private static final Logger log = LoggerFactory.getLogger(MouseMotion.class);
  private final int minDistanceForOvershoots;
  private final int minSteps;
  private final long minOvershootMovementMs;
  private final int effectFadeSteps;
  private final int reactionTimeBaseMs;
  private final int reactionTimeVariationMs;
  private final double overshootSpeedupDivider;
  private final double timeToStepsDivider;
  private final double overshootRandomModifierDivider;
  private final Dimension screenSize;
  private final SystemCalls systemCalls;
  private final DeviationProvider deviationProvider;
  private final NoiseProvider noiseProvider;
  private final SpeedManager speedManager;
  private final int xDest;
  private final int yDest;
  private final Random random;
  private final MouseInfoAccessor mouseInfo;
  private final int overshoots;
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
    this.overshoots = nature.getOvershoots();
    this.speedManager = nature.getSpeedManager();
    this.minDistanceForOvershoots = nature.getMinDistanceForOvershoots();
    this.timeToStepsDivider = nature.getTimeToStepsDivider();
    this.minSteps = nature.getMinSteps();
    this.minOvershootMovementMs = nature.getMinOvershootMovementMs();
    this.overshootSpeedupDivider = nature.getOvershootSpeedupDivider();
    this.effectFadeSteps = nature.getEffectFadeSteps();
    this.reactionTimeBaseMs = nature.getReactionTimeBaseMs();
    this.reactionTimeVariationMs = nature.getReactionTimeVariationMs();
    this.overshootRandomModifierDivider = nature.getOvershootRandomModifierDivider();
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
    double initialDistance = Math.sqrt(Math.pow(xDest - mousePosition.x, 2) + Math.pow(yDest - mousePosition.y, 2));

    long mouseMovementMs = speedManager.createMouseMovementTimeMs(initialDistance);
    log.info("MouseMovementMs calculated to {} ms", mouseMovementMs);
    int overshoots = this.overshoots;

    while (mousePosition.x != xDest || mousePosition.y != yDest) {
      int xDistance = xDest - mousePosition.x;
      int yDistance = yDest - mousePosition.y;

      double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
      Flow flow = speedManager.getFlow(distance, mouseMovementMs);
      double speedPixelsPerSecond = distance / mouseMovementMs * 1000;
      if (overshoots > 0 && initialDistance > minDistanceForOvershoots) {
        // Let's miss the target a bit at first.
        double randomModifier = speedPixelsPerSecond / overshootRandomModifierDivider;
        int currentDestinationX = xDest + (int) (random.nextDouble() * randomModifier - randomModifier / 2) * overshoots;
        int currentDestinationY = yDest + (int) (random.nextDouble() * randomModifier - randomModifier / 2) * overshoots;
        xDistance = currentDestinationX - mousePosition.x;
        yDistance = currentDestinationY - mousePosition.y;
        distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
        log.debug("Using overshoots ({} out of {}), aiming at ({}, {})",
            overshoots, this.overshoots, currentDestinationX, currentDestinationY);
        mouseMovementMs /= overshootSpeedupDivider;
        mouseMovementMs = Math.max(mouseMovementMs, minOvershootMovementMs);
        overshoots--;
      }

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

        double effectFade = Math.max(i - (steps - effectFadeSteps), 0);
        // value from 0 to 1, when EFFECT_FADE_STEPS remaining steps, starts to decrease to 0 linearly
        double effectFadeMultiplier = (effectFadeSteps - effectFade) / effectFadeSteps;

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

        long endTime = startTime + stepTime * (i + 1);
        int mousePosX = (int) Math.round(simulatedMouseX +
            deviation.getX() * deviationMultiplierX * effectFadeMultiplier +
            noiseX * effectFadeMultiplier);
        int mousePosY = (int) Math.round(simulatedMouseY +
            deviation.getY() * deviationMultiplierY * effectFadeMultiplier +
            noiseY * effectFadeMultiplier);

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
      if (mousePosition.x != xDest || mousePosition.y != yDest) {
        sleepAround(reactionTimeBaseMs, reactionTimeVariationMs);
      }
      log.debug("Steps completed, mouse at " + mousePosition.x + " " + mousePosition.y);
    }
    log.info("Mouse movement to ({}, {}) completed", xDest, yDest);
  }

  private int limitByScreenWidth(int value) {
    return Math.max(0, Math.min(screenSize.width - 1, value));
  }

  private int limitByScreenHeight(int value) {
    return Math.max(0, Math.min(screenSize.height - 1, value));
  }

  private void sleepAround(long sleep, long around) throws InterruptedException {
    long sleepTime = (long) (sleep + random.nextDouble() * around);
    if (log.isTraceEnabled() && sleepTime > 0) {
      updateMouseInfo();
      log.trace("Sleeping at ({}, {}) for {} ms", mousePosition.x, mousePosition.y, sleepTime);
    }
    systemCalls.sleep(sleepTime);
  }

  private void updateMouseInfo() {
    mousePosition = mouseInfo.getMousePosition();
  }
}
