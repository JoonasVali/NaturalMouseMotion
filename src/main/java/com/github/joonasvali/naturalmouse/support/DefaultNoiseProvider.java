package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.NoiseProvider;

import java.util.Random;

public class DefaultNoiseProvider implements NoiseProvider {
  public static final double DEFAULT_NOISINESS_DIVIDER = 2;
  private static final double SMALL_DELTA = 10e-6;
  private final double noisinessDivider;

  /**
   * @param noisinessDivider bigger value means less noise.
   */
  public DefaultNoiseProvider(double noisinessDivider) {
    this.noisinessDivider = noisinessDivider;
  }

  @Override
  public DoublePoint getNoise(Random random, double xStepSize, double yStepSize) {
    if (Math.abs(xStepSize - 0) < SMALL_DELTA && Math.abs(yStepSize - 0) < SMALL_DELTA) {
      return DoublePoint.ZERO;
    }
    double noiseX = 0;
    double noiseY = 0;
    double stepSize = Math.hypot(xStepSize, yStepSize);
    double noisiness = Math.max(0, (8 - stepSize)) / 50;
    if (random.nextDouble() < noisiness) {
      noiseX = (random.nextDouble() - 0.5) * Math.max(0, (8 - stepSize)) / noisinessDivider;
      noiseY = (random.nextDouble() - 0.5) * Math.max(0, (8 - stepSize)) / noisinessDivider;
    }
    return new DoublePoint(noiseX, noiseY);
  }
}
