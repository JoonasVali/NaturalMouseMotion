package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.SpeedManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
public class DefaultSpeedManager implements SpeedManager {

  private final List<Speed> speeds = new ArrayList<>();
  private int mouseMovementSpeedMs = 500;

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
    return mouseMovementSpeedMs + (long)(Math.random() * mouseMovementSpeedMs);
  }

  public int getMouseMovementSpeedMs() {
    return mouseMovementSpeedMs;
  }

  public void setMouseMovementSpeedMs(int mouseMovementSpeedMs) {
    this.mouseMovementSpeedMs = mouseMovementSpeedMs;
  }
}
