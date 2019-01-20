package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.SpeedManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
public class DefaultSpeedManager implements SpeedManager {

  private final List<Speed> speeds = new ArrayList<>();
  private int mouseMovementTimeMs = 500;

  public DefaultSpeedManager(Collection<Speed> speeds) {
    this.speeds.addAll(speeds);
  }

  public DefaultSpeedManager() {
    this(Arrays.asList(
        new Speed(SpeedCharacteristics.constantSpeed()),
        new Speed(SpeedCharacteristics.variatingSpeed()),
        new Speed(SpeedCharacteristics.interruptedSpeed()),
        new Speed(SpeedCharacteristics.slowStartupSpeed()),
        new Speed(SpeedCharacteristics.jaggedSpeed())
    ));
  }

  @Override
  public Speed getSpeed(double distance, long plannedMouseMovementTimeMs) {
    return speeds.get((int) (Math.random() * speeds.size()));
  }

  /**
   * @param distance the distance between mouse cursor and target pixel
   * @return mouse movement time in ms approximate time in ms it should take to finish a single trajectory.
   * (In reality it takes longer)
   */
  @Override
  public long createMouseMovementTimeMs(double distance) {
    return mouseMovementTimeMs + (long)(Math.random() * mouseMovementTimeMs);
  }

  public int getMouseMovementBaseTimeMs() {
    return mouseMovementTimeMs;
  }

  public void setMouseMovementBaseTimeMs(int mouseMovementSpeedMs) {
    this.mouseMovementTimeMs = mouseMovementSpeedMs;
  }
}
