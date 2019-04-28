package com.github.joonasvali.naturalmouse.api;

import com.github.joonasvali.naturalmouse.support.DoublePoint;

import java.util.Random;

/**
 * Provides noise or mistakes in the mouse movement
 *
 * NoiseProvider implementation should be immutable.
 */
public interface NoiseProvider {
  /**
   * Noise is offset from the original trajectory, simulating user and physical errors on mouse movement.
   *
   * Noise is accumulating, so on average it should create an equal chance of either positive or negative movement
   * on each axis, otherwise the mouse movement will always be slightly offset to single direction.
   *
   * Deviation from DeviationProvider is different from the Noise
   * because it works like a mathematical function and is not accumulating.
   *
   * Not every step needs to add noise, use randomness to only add noise sometimes, otherwise return Point(0, 0).
   *
   * During the final steps of mouse movement, the effect of noise is gradually reduced, so the mouse
   * would finish on the intended pixel smoothly, thus the implementation of this class can safely ignore
   * and not know the beginning and end of the movement.
   *
   * @param random use this to generate randomness in the offset
   * @param xStepSize the step size that is taken horizontally
   * @param yStepSize the step size that is taken vertically
   * @return a point which describes how much the mouse offset is increased or decreased this step.
   * This value must not include the parameters xStepSize and yStepSize. For no change in noise just return (0,0).
   *
   * @see com.github.joonasvali.naturalmouse.api.DeviationProvider
   *
   */
  DoublePoint getNoise(Random random, double xStepSize, double yStepSize);
}
