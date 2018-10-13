package com.github.joonasvali.naturalmouse.support;

import java.util.Random;

public class DefaultNoiseProvider implements NoiseProvider {
  private final double chanceOfNoise;

  /**
   * @param chanceOfNoise a value between 0 and 1, both included
   */
  public DefaultNoiseProvider(double chanceOfNoise) {
    this.chanceOfNoise = chanceOfNoise;
  }

  @Override
  public DoublePoint getNoise(Random random, double distance) {
    double noiseX = 0;
    double noiseY = 0;
    if (random.nextDouble() < chanceOfNoise) {
      noiseX = (random.nextDouble() - 0.5) * distance / 150;
      noiseY = (random.nextDouble() - 0.5) * distance / 150;
    }
    return new DoublePoint(noiseX, noiseY);
  }
}
