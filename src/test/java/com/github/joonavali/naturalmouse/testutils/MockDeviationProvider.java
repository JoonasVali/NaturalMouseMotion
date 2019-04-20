package com.github.joonavali.naturalmouse.testutils;


import com.github.joonasvali.naturalmouse.api.DeviationProvider;
import com.github.joonasvali.naturalmouse.support.DoublePoint;

public class MockDeviationProvider implements DeviationProvider {
  @Override
  public DoublePoint getDeviation(double totalDistanceInPixels, double completionFraction) {
    return new DoublePoint(0, 0);
  }
}