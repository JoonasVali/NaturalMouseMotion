package com.github.joonasvali.naturalmouse.api;

import com.github.joonasvali.naturalmouse.support.DoublePoint;

import java.util.Random;

/**
 * Provides noise or mistakes in the mouse movement
 *
 * NoiseProvider implementation should be immutable.
 */
public interface NoiseProvider {
  DoublePoint getNoise(Random random, double xStepSize, double yStepSize);
}
