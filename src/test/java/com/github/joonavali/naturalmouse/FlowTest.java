package com.github.joonavali.naturalmouse;

import com.github.joonasvali.naturalmouse.support.Flow;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class FlowTest {

  private static final double SMALL_DELTA = 10e-6;

  @Test
  public void constantCharacteristicsGetNormalizedTo100() {
    double[] characteristics = new double[100];
    Arrays.fill(characteristics, 500d);
    Flow flow = new Flow(characteristics);

    double[] result = flow.getFlowCharacteristics();
    double sum = 0;
    for (int i = 0; i < result.length; i++) {
      Assertions.assertEquals(100, result[i], SMALL_DELTA);
      sum += result[i];
    }

    Assertions.assertEquals(100 * characteristics.length, sum, SMALL_DELTA);
  }

  @Test
  public void constantCharacteristicsGetNormalizedTo100withLargeArray() {
    double[] characteristics = new double[1000];
    Arrays.fill(characteristics, 500d);
    Flow flow = new Flow(characteristics);

    double[] result = flow.getFlowCharacteristics();
    double sum = 0;
    for (int i = 0; i < result.length; i++) {
      Assertions.assertEquals(100, result[i], SMALL_DELTA);
      sum += result[i];
    }

    Assertions.assertEquals(100 * characteristics.length, sum, SMALL_DELTA);
  }

  @Test
  public void constantCharacteristicsGetNormalizedTo100fromLowValues() {
    double[] characteristics = new double[100];
    Arrays.fill(characteristics, 5);
    Flow flow = new Flow(characteristics);

    double[] result = flow.getFlowCharacteristics();
    double sum = 0;
    for (int i = 0; i < result.length; i++) {
      Assertions.assertEquals(100, result[i], SMALL_DELTA);
      sum += result[i];
    }

    Assertions.assertEquals(100 * characteristics.length, sum, SMALL_DELTA);
  }

  @Test
  public void characteristicsGetNormalizedToAverage100() {
    double[] characteristics = {1, 2, 3, 4, 5};

    Flow flow = new Flow(characteristics);

    double[] result = flow.getFlowCharacteristics();
    double sum = 0;
    for (int i = 0; i < result.length; i++) {
      sum += result[i];
    }
    Assertions.assertEquals(33.33333333d, result[0], SMALL_DELTA);
    Assertions.assertEquals(66.66666666d, result[1], SMALL_DELTA);
    Assertions.assertEquals(100.00000000d, result[2], SMALL_DELTA);
    Assertions.assertEquals(133.33333333d, result[3], SMALL_DELTA);
    Assertions.assertEquals(166.66666666d, result[4], SMALL_DELTA);

    Assertions.assertEquals(100 * characteristics.length, sum, SMALL_DELTA);
  }

  @Test
  public void stepsAddUpToDistance_accelerating() {
    double[] characteristics = {1, 2, 3, 4, 5};
    Flow flow = new Flow(characteristics);
    double step1 = flow.getStepSize(100, 5, 0);
    double step2 = flow.getStepSize(100, 5, 0.2);
    double step3 = flow.getStepSize(100, 5, 0.4);
    double step4 = flow.getStepSize(100, 5, 0.6);
    double step5 = flow.getStepSize(100, 5, 0.8);
    double sum = step1 + step2 + step3 + step4 + step5;
    Assertions.assertEquals(100d, sum, SMALL_DELTA);
  }

  @Test
  public void stepsAddUpToDistance_decelerating() {
    double[] characteristics = {5, 4, 3, 2, 1};
    Flow flow = new Flow(characteristics);
    double step1 = flow.getStepSize(100, 5, 0);
    double step2 = flow.getStepSize(100, 5, 0.2);
    double step3 = flow.getStepSize(100, 5, 0.4);
    double step4 = flow.getStepSize(100, 5, 0.6);
    double step5 = flow.getStepSize(100, 5, 0.8);
    double sum = step1 + step2 + step3 + step4 + step5;
    Assertions.assertEquals(100d, sum, SMALL_DELTA);
  }

  @Test
  public void stepsAddUpToDistance_characteristics_not_dividable_by_steps_1() {
    double[] characteristics = {1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5};
    Flow flow = new Flow(characteristics);
    double step1 = flow.getStepSize(100, 5, 0);
    double step2 = flow.getStepSize(100, 5, 0.2);
    double step3 = flow.getStepSize(100, 5, 0.4);
    double step4 = flow.getStepSize(100, 5, 0.6);
    double step5 = flow.getStepSize(100, 5, 0.8);
    double sum = step1 + step2 + step3 + step4 + step5;
    Assertions.assertEquals(100d, sum, SMALL_DELTA);
  }

  @Test
  public void stepsAddUpToDistance_characteristics_not_dividable_by_steps_2() {
    double[] characteristics = {1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6};
    Flow flow = new Flow(characteristics);
    double step1 = flow.getStepSize(100, 5, 0);
    double step2 = flow.getStepSize(100, 5, 0.2);
    double step3 = flow.getStepSize(100, 5, 0.4);
    double step4 = flow.getStepSize(100, 5, 0.6);
    double step5 = flow.getStepSize(100, 5, 0.8);
    double sum = step1 + step2 + step3 + step4 + step5;
    Assertions.assertEquals(100d, sum, SMALL_DELTA);
  }

  @Test
  public void stepsAddUpToDistance_characteristics_not_dividable_by_steps_3() {
    double[] characteristics = {1, 1, 1, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 5, 6, 6, 6, 7, 7};
    Flow flow = new Flow(characteristics);
    double step1 = flow.getStepSize(100, 3, 0);
    double step2 = flow.getStepSize(100, 3, 1d/3d);
    double step3 = flow.getStepSize(100, 3, 1d/3d * 2);
    double sum = step1 + step2 + step3;
    Assertions.assertEquals(100d, sum, SMALL_DELTA);
  }

  @Test
  public void stepsAddUpToDistance_characteristics_array_smaller_than_steps_not_dividable() {
    double[] characteristics = {1, 2, 3};
    Flow flow = new Flow(characteristics);
    double step1 = flow.getStepSize(100, 5, 0);
    double step2 = flow.getStepSize(100, 5, 0.2);
    double step3 = flow.getStepSize(100, 5, 0.4);
    double step4 = flow.getStepSize(100, 5, 0.6);
    double step5 = flow.getStepSize(100, 5, 0.8);
    double sum = step1 + step2 + step3 + step4 + step5;
    Assertions.assertEquals(100d, sum, SMALL_DELTA);
  }

  @Test
  public void stepsAddUpToDistance_constantFlow() {
    double[] characteristics = {10, 10, 10, 10, 10};
    Flow flow = new Flow(characteristics);
    double step1 = flow.getStepSize(500, 5, 0);
    double step2 = flow.getStepSize(500, 5, 0.2);
    double step3 = flow.getStepSize(500, 5, 0.4);
    double step4 = flow.getStepSize(500, 5, 0.6);
    double step5 = flow.getStepSize(500, 5, 0.8);
    double sum = step1 + step2 + step3 + step4 + step5;
    Assertions.assertEquals(500d, sum, SMALL_DELTA);
  }

  @Test
  public void stepsAddUpToDistance_constantFlow_characteristics_to_steps_not_dividable() {
    double[] characteristics = {10, 10, 10, 10, 10, 10};
    Flow flow = new Flow(characteristics);
    double step1 = flow.getStepSize(500, 5, 0);
    double step2 = flow.getStepSize(500, 5, 0.2);
    double step3 = flow.getStepSize(500, 5, 0.4);
    double step4 = flow.getStepSize(500, 5, 0.6);
    double step5 = flow.getStepSize(500, 5, 0.8);
    double sum = step1 + step2 + step3 + step4 + step5;
    Assertions.assertEquals(500d, sum, SMALL_DELTA);
  }
}
