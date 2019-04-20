package com.github.joonavali.naturalmouse.testutils;

import com.github.joonasvali.naturalmouse.api.SpeedManager;
import com.github.joonasvali.naturalmouse.support.Flow;
import com.github.joonasvali.naturalmouse.util.Pair;

public class MockSpeedManager implements SpeedManager {

  @Override
  public Pair<Flow, Long> getFlowWithTime(double distance) {
    double[] characteristics = {100};
    return new Pair<>(new Flow(characteristics), 10L);
  }
}