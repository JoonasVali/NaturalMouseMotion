package com.github.joonavali.naturalmouse;

import com.github.joonasvali.naturalmouse.support.Speed;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class SpeedTest {

  private static final double SMALL_DELTA = 10e-6;

  @Test
  public void constantCharacteristicsGetNormalizedTo100() {
    double[] characteristics = new double[100];
    Arrays.fill(characteristics, 500d);
    Speed provider = new Speed(characteristics);

    double[] result = provider.getSpeedCharacteristics();
    double sum = 0;
    for (int i = 0; i < result.length; i++) {
      Assert.assertEquals(100, result[i], SMALL_DELTA);
      sum += result[i];
    }

    Assert.assertEquals(100 * characteristics.length, sum, SMALL_DELTA);
  }

  @Test
  public void constantCharacteristicsGetNormalizedTo100withLargeArray() {
    double[] characteristics = new double[1000];
    Arrays.fill(characteristics, 500d);
    Speed provider = new Speed(characteristics);

    double[] result = provider.getSpeedCharacteristics();
    double sum = 0;
    for (int i = 0; i < result.length; i++) {
      Assert.assertEquals(100, result[i], SMALL_DELTA);
      sum += result[i];
    }

    Assert.assertEquals(100 * characteristics.length, sum, SMALL_DELTA);
  }

  @Test
  public void constantCharacteristicsGetNormalizedTo100fromLowValues() {
    double[] characteristics = new double[100];
    Arrays.fill(characteristics, 5);
    Speed provider = new Speed(characteristics);

    double[] result = provider.getSpeedCharacteristics();
    double sum = 0;
    for (int i = 0; i < result.length; i++) {
      Assert.assertEquals(100, result[i], SMALL_DELTA);
      sum += result[i];
    }

    Assert.assertEquals(100 * characteristics.length, sum, SMALL_DELTA);
  }

  @Test
  public void characteristicsGetNormalizedToAverage100() {
    double[] characteristics = {1, 2, 3, 4, 5};

    Speed provider = new Speed(characteristics);

    double[] result = provider.getSpeedCharacteristics();
    double sum = 0;
    for (int i = 0; i < result.length; i++) {
      sum += result[i];
    }
    Assert.assertEquals(33.33333333d, result[0], SMALL_DELTA);
    Assert.assertEquals(66.66666666d, result[1], SMALL_DELTA);
    Assert.assertEquals(100.00000000d, result[2], SMALL_DELTA);
    Assert.assertEquals(133.33333333d, result[3], SMALL_DELTA);
    Assert.assertEquals(166.66666666d, result[4], SMALL_DELTA);

    Assert.assertEquals(100 * characteristics.length, sum, SMALL_DELTA);
  }

}
