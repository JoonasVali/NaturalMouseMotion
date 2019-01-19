package com.github.joonasvali.naturalmouse.api;

import com.github.joonasvali.naturalmouse.support.DoublePoint;

/**
 * Creates arcs or deviation into mouse movement.
 *
 * DeviationProvider implementation should be immutable.
 */
public interface DeviationProvider {

  DoublePoint getDeviation(double totalDistanceInPixels, double completionFraction);
}
