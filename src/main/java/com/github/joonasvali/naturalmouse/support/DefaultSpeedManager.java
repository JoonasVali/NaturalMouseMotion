package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.SpeedManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
public class DefaultSpeedManager implements SpeedManager {

  private final List<Flow> flows = new ArrayList<>();
  private int mouseMovementTimeMs = 400;

  public DefaultSpeedManager(Collection<Flow> flows) {
    this.flows.addAll(flows);
  }

  public DefaultSpeedManager() {
    this(Arrays.asList(
        new Flow(FlowCharacteristics.constantSpeed()),
        new Flow(FlowCharacteristics.variatingFlow()),
        new Flow(FlowCharacteristics.interruptedFlow()),
        new Flow(FlowCharacteristics.slowStartupFlow()),
        new Flow(FlowCharacteristics.slowStartup2Flow()),
        new Flow(FlowCharacteristics.jaggedFlow())
    ));
  }

  @Override
  public Flow getFlow(double distance, long plannedMouseMovementTimeMs) {
    return flows.get((int) (Math.random() * flows.size()));
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
