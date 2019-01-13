package com.github.joonasvali.naturalmouse.api;

import com.github.joonasvali.naturalmouse.support.DeviationProvider;
import com.github.joonasvali.naturalmouse.support.DoublePoint;
import com.github.joonasvali.naturalmouse.support.MouseInfoAccessor;
import com.github.joonasvali.naturalmouse.support.MouseMotionObserver;
import com.github.joonasvali.naturalmouse.support.NoiseProvider;
import com.github.joonasvali.naturalmouse.support.SystemCalls;
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
  private static final int MOUSE_MOVEMENT_FLUCTATION_MS = 150;
  private static final int MIN_DISTANCE_FOR_OVERSHOOTS = 50;
  private static final int TIME_TO_STEPS_DIVIDER = 8;
  private static final int MIN_STEPS = 10;
  private static final double OVERSHOOT_SPEEDUP_DIVIDER = 1.8;
  private static final int MIN_OVERSHOOT_MOVEMENT_MS = 40;
  public static final int OVERSHOOT_RANDOM_MODIFIER_DIVIDER = 20;
  private final long mouseMovementBaseMs;
  private final Dimension screenSize;
  private final SystemCalls systemCalls;
  private final DeviationProvider deviationProvider;
  private final NoiseProvider noiseProvider;
  private final int xDest;
  private final int yDest;
  private final Random random;
  private final Robot robot;
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
   * @param robot               the robot used for moving the mouse cursor
   * @param mouseInfo           the accessor for reading cursor position on screen
   * @param mouseMovementBaseMs approximate time in ms it takes to finish a single trajectory. (In reality it takes longer)
   * @param overshoots          the number of overshoots or false destinations the cursor makes at most, before arriving to destination
   */
  public MouseMotion(DeviationProvider deviationProvider, NoiseProvider noiseProvider, SystemCalls systemCalls,
                     int xDest, int yDest, Random random, Robot robot, MouseInfoAccessor mouseInfo, long mouseMovementBaseMs,
                     int overshoots) {
    this.deviationProvider = deviationProvider;
    this.noiseProvider = noiseProvider;
    this.systemCalls = systemCalls;
    this.screenSize = systemCalls.getScreenSize();
    this.xDest = limitByScreenWidth(xDest);
    this.yDest = limitByScreenHeight(yDest);
    this.random = random;
    this.robot = robot;
    this.mouseInfo = mouseInfo;
    this.overshoots = overshoots;
    this.mouseMovementBaseMs = mouseMovementBaseMs;
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

    long mouseMovementMs = this.mouseMovementBaseMs + (long) (random.nextDouble() * MOUSE_MOVEMENT_FLUCTATION_MS);
    log.info("MouseMovementMs calculated to {} ms", mouseMovementMs);
    int overshoots = this.overshoots;

    while (mousePosition.x != xDest || mousePosition.y != yDest) {
      int xDistance = xDest - mousePosition.x;
      int yDistance = yDest - mousePosition.y;

      double distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
      double speedPixelsPerSecond = distance / mouseMovementMs * 1000;
      if (overshoots > 0 && initialDistance > MIN_DISTANCE_FOR_OVERSHOOTS) {
        // Let's miss the target a bit at first.
        double randomModifier = speedPixelsPerSecond / OVERSHOOT_RANDOM_MODIFIER_DIVIDER;
        int overshootDestX = xDest + (int) (random.nextDouble() * randomModifier - randomModifier / 2) * overshoots;
        int overshootDestY = yDest + (int) (random.nextDouble() * randomModifier - randomModifier / 2) * overshoots;
        xDistance = overshootDestX - mousePosition.x;
        yDistance = overshootDestY - mousePosition.y;
        distance = Math.sqrt(Math.pow(xDistance, 2) + Math.pow(yDistance, 2));
        log.debug("Using overshoots ({} out of {}), aiming at ({}, {})", overshoots, this.overshoots, overshootDestX, overshootDestY);
        mouseMovementMs /= OVERSHOOT_SPEEDUP_DIVIDER;
        mouseMovementMs = Math.max(mouseMovementMs, MIN_OVERSHOOT_MOVEMENT_MS);
        overshoots--;
      }

      /* Number of steps is calculated from the movement time and limited by minimal amount of steps
         (should have at least MIN_STEPS) and distance (shouldn't have more steps than pixels travelled) */
      int steps = (int) Math.min(distance, Math.max(mouseMovementMs / TIME_TO_STEPS_DIVIDER, MIN_STEPS));
      double xStepSize = (xDistance / (double) steps);
      double yStepSize = (yDistance / (double) steps);

      long startTime = systemCalls.currentTimeMillis();
      long stepTime = (long) (mouseMovementMs / (double) steps);

      updateMouseInfo();
      double simulatedMouseX = mousePosition.x;
      double simulatedMouseY = mousePosition.y;

      double deviationMultiplierX = (random.nextDouble() - 0.5) * 2;
      double deviationMultiplierY = (random.nextDouble() - 0.5) * 2;


      for (int i = 0; i < steps; i++) {
        double completion = i / (double) steps;

        simulatedMouseX += xStepSize;
        simulatedMouseY += yStepSize;

        DoublePoint noise = noiseProvider.getNoise(random, distance);
        DoublePoint deviation = deviationProvider.getDeviation(distance, completion);

        long endTime = startTime + stepTime * (i + 1);
        int mousePosX = (int) Math.round(simulatedMouseX + deviation.getX() * deviationMultiplierX + noise.getX());
        int mousePosY = (int) Math.round(simulatedMouseY + deviation.getY() * deviationMultiplierY + noise.getY());

        mousePosX = limitByScreenWidth(mousePosX);
        mousePosY = limitByScreenHeight(mousePosY);
        robot.mouseMove(
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
