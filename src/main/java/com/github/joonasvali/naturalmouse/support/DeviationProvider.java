package com.github.joonasvali.naturalmouse.support;

/**
 * DeviationProvider implementation should be immutable.
 */
public interface DeviationProvider {

  DoublePoint getDeviation(double totalDistanceInPixels, double completionFraction);
}
