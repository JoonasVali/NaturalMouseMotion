package com.github.joonasvali.naturalmouse.support;

import java.util.Random;

public interface NoiseProvider {
  DoublePoint getNoise(Random random, double distance);
}
