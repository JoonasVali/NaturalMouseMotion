package com.github.joonasvali.naturalmouse.support;

import java.util.Random;

/**
 * Provides noise or mistakes in the mouse movement
 *
 * NoiseProvider implementation should be immutable.
 */
public interface NoiseProvider {
  DoublePoint getNoise(Random random, double distance);
}
