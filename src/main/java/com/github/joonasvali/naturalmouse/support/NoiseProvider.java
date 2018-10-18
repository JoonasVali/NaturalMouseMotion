package com.github.joonasvali.naturalmouse.support;

import java.util.Random;

/**
 * NoiseProvider implementation should be immutable.
 */
public interface NoiseProvider {
  DoublePoint getNoise(Random random, double distance);
}
