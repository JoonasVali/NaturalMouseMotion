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
   * 'Noise' is generating an offset from straight movement, and is accumulating, so on average it should create
   * an equal chance of either positive or negative movement, otherwise the mouse movement will always be slightly
   * offset to single direction.
   *
   * Deviation from {@link (com.github.joonasvali.naturalmouse.api.DeviationProvider)} is different from the Noise
   * because it works like a mathematical function, the resulting Point is added to single trajectory point and it
   * will not have any effect in the next mouse movement step, making it easy to implement this as a formula
   * based on the input parameters.
   *
   * @param random use this to generate randomness in the offset
   * @param xStepSize the step size that is taken horizontally
   * @param yStepSize the step size that is taken vertically
   * @return a point which describes how much the mouse offset is increased or decreased, depending on previous state.
   *
   */
  DoublePoint getNoise(Random random, double xStepSize, double yStepSize);
}
