package com.github.joonasvali.naturalmouse.api;

import com.github.joonasvali.naturalmouse.support.DoublePoint;
import com.github.joonasvali.naturalmouse.support.Flow;
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
  private static final int MIN_DISTANCE_FOR_OVERSHOOTS = 50;
  private static final int TIME_TO_STEPS_DIVIDER = 8;
  private static final int MIN_STEPS = 10;
  private static final double OVERSHOOT_SPEEDUP_DIVIDER = 1.8;
  private static final int MIN_OVERSHOOT_MOVEMENT_MS = 40;
  private static final int OVERSHOOT_RANDOM_MODIFIER_DIVIDER = 20;
  private static final int EFFECT_FADE_STEPS = 15;
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
   * @param deviationProvider   creates arc or other disfigurement in otherwise straight trajectory
   * @param noiseProvider       creates random noise in the trajectory
   * @param systemCalls         interface for calling static system related methods
   * @param xDest               the x-coordinate of destination
   * @param yDest               the y-coordinate of destination
   * @param random              the random used for unpredictability
   * @param mouseInfo           the accessor for reading cursor position on screen
   * @param speedManager        provides flow characteristics and speed to the movement of mouse
   * @param overshoots          the number of overshoots or false destinations the cursor makes at most, before arriving to destination
   */
  public MouseMotion(DeviationProvider deviationProvider, NoiseProvider noiseProvider, SystemCalls systemCalls,
                     int xDest, int yDest, Random random, MouseInfoAccessor mouseInfo,
                     SpeedManager speedManager, int overshoots) {
    this.deviationProvider = deviationProvider;
    this.noiseProvider = noiseProvider;
    this.systemCalls = systemCalls;
    this.screenSize = systemCalls.getScreenSize();
    this.xDest = limitByScreenWidth(xDest);
    this.yDest = limitByScreenHeight(yDest);
    this.random = random;
    this.mouseInfo = mouseInfo;
    this.overshoots = overshoots;
    this.speedManager = speedManager;
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
      if (overshoots > 0 && initialDistance > MIN_DISTANCE_FOR_OVERSHOOTS) {
        // Let's miss the target a bit at first.
        double randomModifier = speedPixelsPerSecond / OVERSHOOT_RANDOM_MODIFIER_DIVIDER;
        int currentDestinationX = xDest + (int) (random.nextDouble() * randomModifier - randomModifier / 2) * overshoots;
        int currentDestinationY = yDest + (int) (random.nextDouble() * randomModifier - randomModifier / 2) * overshoots;
        xDistance = currentDestinationX - mousePosition.x;
        yDistance = currentDestinationY - mousePosition.y;
        distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
        log.debug("Using overshoots ({} out of {}), aiming at ({}, {})",
            overshoots, this.overshoots, currentDestinationX, currentDestinationY);
        mouseMovementMs /= OVERSHOOT_SPEEDUP_DIVIDER;
        mouseMovementMs = Math.max(mouseMovementMs, MIN_OVERSHOOT_MOVEMENT_MS);
        overshoots--;
      }

      /* Number of steps is calculated from the movement time and limited by minimal amount of steps
         (should have at least MIN_STEPS) and distance (shouldn't have more steps than pixels travelled) */
      int steps = (int) Math.ceil(Math.min(distance, Math.max(mouseMovementMs / TIME_TO_STEPS_DIVIDER, MIN_STEPS)));

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

        double effectFade = Math.max(i - (steps - EFFECT_FADE_STEPS), 0);
        // value from 0 to 1, when EFFECT_FADE_STEPS remaining steps, starts to decrease to 0 linearly
        double effectFadeMultiplier = (EFFECT_FADE_STEPS - effectFade) / EFFECT_FADE_STEPS;

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
      sleepAround(20, 120);
      updateMouseInfo();
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
