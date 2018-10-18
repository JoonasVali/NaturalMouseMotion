package com.github.joonasvali.naturalmouse.support;

import java.util.Random;

public class DefaultNoiseProvider implements NoiseProvider {
  public static final double DEFAULT_CHANCE_OF_NOISE = 0.1;
  public static final double DEFAULT_DISTANCE_DIVIDER = 150;
  private final double chanceOfNoise;
  private final double distanceDivider;

  /**
   * @param chanceOfNoise a value between 0 and 1, both included
   */
  public DefaultNoiseProvider(double chanceOfNoise, double distanceDivider) {
    this.chanceOfNoise = chanceOfNoise;
    this.distanceDivider = distanceDivider;
  }

  @Override
  public DoublePoint getNoise(Random random, double distance) {
    double noiseX = 0;
    double noiseY = 0;
    if (random.nextDouble() < chanceOfNoise) {
      noiseX = (random.nextDouble() - 0.5) * distance / distanceDivider;
      noiseY = (random.nextDouble() - 0.5) * distance / distanceDivider;
    }
    return new DoublePoint(noiseX, noiseY);
  }
}
