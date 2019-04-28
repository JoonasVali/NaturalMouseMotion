package com.github.joonasvali.naturalmouse.api;

import com.github.joonasvali.naturalmouse.support.DoublePoint;

/**
 * Creates arcs or deviation into mouse movement.
 *
 * DeviationProvider implementation should be immutable.
 *
 */
public interface DeviationProvider {

  /**
   * Gets the deviation for current trajectory. Deviation is an offset from the original straight trajectory.
   *
   * Deviation is different from the Noise because it works like a mathematical function, the resulting
   * Point is added to single trajectory point and it will not have any effect in the next
   * mouse movement step, making it easy to implement this as a formula based on the input parameters.
   * e.g the something like 'deviation = totalDistanceInPixels / completionFraction', resulting in smooth movement.
   * (Don't actually use this formula), 'Noise' is generating an offset from the original trajectory and is accumulating.
   *
   * As deviation should be deterministic and return same result for same parameters, it should not include Random
   * behaviour, thus Random is not included as a parameter.
   *
   * It is recommended that deviation is decreasing when completionFraction nears 1, but MouseMotion itself
   * also makes sure that the effect of deviation is reduced when the mouse is nearing its destination.
   *
   * @param totalDistanceInPixels the total pixels between target and mouse initial position
   * @param completionFraction the completed fraction of mouse movement total distance, value from 0...1 (0;1]
   * @return a point which describes how much the mouse is going to deviate from the straight trajectory between
   * target and initial position. This is not the final deviation of the mouse as MouseMotion will randomly decide
   * to either amplify or decrease it over the whole mouse movement, making the resulting arc stand out more or less,
   * or is flipped negatively.
   * @see com.github.joonasvali.naturalmouse.api.NoiseProvider
   */
  DoublePoint getDeviation(double totalDistanceInPixels, double completionFraction);
}
