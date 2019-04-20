package com.github.joonavali.naturalmouse.testutils;


import com.github.joonasvali.naturalmouse.api.NoiseProvider;
import com.github.joonasvali.naturalmouse.support.DoublePoint;

import java.util.Random;

public class MockNoiseProvider implements NoiseProvider {
  @Override
  public DoublePoint getNoise(Random random, double xStepSize, double yStepSize) {
    return new DoublePoint(0, 0);
  }
}