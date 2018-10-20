package com.github.joonasvali.naturalmouse.support;

/**
 * Creates arcs or deviation into mouse movement.
 *
 * DeviationProvider implementation should be immutable.
 */
public interface DeviationProvider {

  DoublePoint getDeviation(double totalDistanceInPixels, double completionFraction);
}
