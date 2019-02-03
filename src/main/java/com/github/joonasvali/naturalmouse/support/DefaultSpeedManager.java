package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.SpeedManager;
import com.github.joonasvali.naturalmouse.util.FlowTemplates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
public class DefaultSpeedManager implements SpeedManager {

  private final List<Flow> flows = new ArrayList<>();
  private long mouseMovementTimeMs = 400;

  public DefaultSpeedManager(Collection<Flow> flows) {
    this.flows.addAll(flows);
  }

  public DefaultSpeedManager() {
    this(Arrays.asList(
        new Flow(FlowTemplates.constantSpeed()),
        new Flow(FlowTemplates.variatingFlow()),
        new Flow(FlowTemplates.interruptedFlow()),
        new Flow(FlowTemplates.slowStartupFlow()),
        new Flow(FlowTemplates.slowStartup2Flow()),
        new Flow(FlowTemplates.jaggedFlow()),
        new Flow(FlowTemplates.stoppingFlow())
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

  public long getMouseMovementBaseTimeMs() {
    return mouseMovementTimeMs;
  }

  public void setMouseMovementBaseTimeMs(long mouseMovementSpeedMs) {
    this.mouseMovementTimeMs = mouseMovementSpeedMs;
  }
}
