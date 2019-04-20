package com.github.joonavali.naturalmouse.testutils;

import java.util.Random;

/**
 * This only mocks nextDouble() for now...
 */
public class MockRandom extends Random {
  private final double[] doubles;
  private int i = 0;

  public MockRandom(double[] doubles) {
    this.doubles = doubles;
  }

  @Override
  public double nextDouble() {
    return doubles[i++ % doubles.length];
  }
}
