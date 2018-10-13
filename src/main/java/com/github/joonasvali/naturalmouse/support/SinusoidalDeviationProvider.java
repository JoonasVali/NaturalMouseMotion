package com.github.joonasvali.naturalmouse.support;

public class SinusoidalDeviationProvider implements DeviationProvider {
  private final double slopeDivider;

  public SinusoidalDeviationProvider(double slopeDivider) {
    this.slopeDivider = slopeDivider;
  }

  @Override
  public DoublePoint getDeviation(double totalDistanceInPixels, double completionFraction, double multiplierX, double multiplierY) {
    double deviationFunctionResult = (1 - Math.cos(completionFraction * Math.PI * 2)) / 2;

    double deviationX = multiplierX * (totalDistanceInPixels / slopeDivider);
    double deviationY = multiplierY * (totalDistanceInPixels / slopeDivider);

    return new DoublePoint(deviationFunctionResult * deviationX, deviationFunctionResult * deviationY);
  }
}
