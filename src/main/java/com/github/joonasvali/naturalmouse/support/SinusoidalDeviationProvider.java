package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.DeviationProvider;

public class SinusoidalDeviationProvider implements DeviationProvider {
  public static final int DEFAULT_SLOPE_DIVIDER = 10;
  private final double slopeDivider;

  public SinusoidalDeviationProvider(double slopeDivider) {
    this.slopeDivider = slopeDivider;
  }

  @Override
  public DoublePoint getDeviation(double totalDistanceInPixels, double completionFraction) {
    double deviationFunctionResult = (1 - Math.cos(completionFraction * Math.PI * 2)) / 2;

    double deviationX = totalDistanceInPixels / slopeDivider;
    double deviationY = totalDistanceInPixels / slopeDivider;

    return new DoublePoint(deviationFunctionResult * deviationX, deviationFunctionResult * deviationY);
  }
}
