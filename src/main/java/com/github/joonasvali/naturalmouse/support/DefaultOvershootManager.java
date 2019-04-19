package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.OvershootManager;

import java.awt.*;
import java.util.Random;

public class DefaultOvershootManager implements OvershootManager {
  public static final double OVERSHOOT_SPEEDUP_DIVIDER = 1.8;
  public static final int MIN_OVERSHOOT_MOVEMENT_MS = 40;
  public static final int OVERSHOOT_RANDOM_MODIFIER_DIVIDER = 20;
  public static final int MIN_DISTANCE_FOR_OVERSHOOTS = 10;
  public static final int DEFAULT_OVERSHOOT_AMOUNT = 3;

  private long minOvershootMovementMs = MIN_OVERSHOOT_MOVEMENT_MS;
  private long minDistanceForOvershoots = MIN_DISTANCE_FOR_OVERSHOOTS;
  private double overshootRandomModifierDivider  = OVERSHOOT_RANDOM_MODIFIER_DIVIDER;
  private double overshootSpeedupDivider = OVERSHOOT_SPEEDUP_DIVIDER;
  private int overshoots = DEFAULT_OVERSHOOT_AMOUNT;
  private final Random random;

  public DefaultOvershootManager(Random random) {
    this.random = random;
  }

  @Override
  public int getOvershoots(Flow flow, long mouseMovementMs, double distance) {
    if (distance < minDistanceForOvershoots) {
      return 0;
    }
    return overshoots;
  }

  @Override
  public Point getOvershootAmount(double distanceToRealTargetX, double distanceToRealTargetY, long mouseMovementMs, int overshootsRemaining) {
    double distanceToRealTarget = Math.hypot(distanceToRealTargetX, distanceToRealTargetY);

    double randomModifier = distanceToRealTarget / overshootRandomModifierDivider;
    //double speedPixelsPerSecond = distanceToRealTarget / mouseMovementMs * 1000; // TODO utilize speed
    int x = (int)(random.nextDouble() * randomModifier - randomModifier / 2d) * overshootsRemaining;
    int y = (int)(random.nextDouble() * randomModifier - randomModifier / 2d) * overshootsRemaining;
    return new Point(x, y);
  }

  @Override
  public long deriveNextMouseMovementTimeMs(long mouseMovementMs, int overshootsRemaining) {
    return Math.max((long)(mouseMovementMs / overshootSpeedupDivider), minOvershootMovementMs);
  }

  public long getMinOvershootMovementMs() {
    return minOvershootMovementMs;
  }

  public void setMinOvershootMovementMs(long minOvershootMovementMs) {
    this.minOvershootMovementMs = minOvershootMovementMs;
  }

  public double getOvershootRandomModifierDivider() {
    return overshootRandomModifierDivider;
  }

  public void setOvershootRandomModifierDivider(double overshootRandomModifierDivider) {
    this.overshootRandomModifierDivider = overshootRandomModifierDivider;
  }

  public double getOvershootSpeedupDivider() {
    return overshootSpeedupDivider;
  }

  public void setOvershootSpeedupDivider(double overshootSpeedupDivider) {
    this.overshootSpeedupDivider = overshootSpeedupDivider;
  }

  public int getOvershoots() {
    return overshoots;
  }

  public void setOvershoots(int overshoots) {
    this.overshoots = overshoots;
  }

  public long getMinDistanceForOvershoots() {
    return minDistanceForOvershoots;
  }

  public void setMinDistanceForOvershoots(long minDistanceForOvershoots) {
    this.minDistanceForOvershoots = minDistanceForOvershoots;
  }
}
