package com.github.joonasvali.naturalmouse.support;

public interface DeviationProvider {

  DoublePoint getDeviation(double totalDistanceInPixels, double completionFraction);
}
