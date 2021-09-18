package com.github.joonavali.naturalmouse;

import com.github.joonasvali.naturalmouse.util.FlowUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.function.Function;

public class FlowUtilTest {

  private static final double SMALL_DELTA = 10e-6;

  @Test
  public void testStretchFlow_3to9() {
    double[] flow = {1, 2, 3};
    double[] result = FlowUtil.stretchFlow(flow, 9);
    Assertions.assertArrayEquals(
        new double[]{1.0, 1.25, 1.5, 1.75, 2.0, 2.25, 2.5, 2.75, 3.0}, result, SMALL_DELTA
    );

    assertArraySum(average(flow) * 9, result);
  }

  @Test
  public void testStretchFlow_1to9() {
    double[] flow = {1};
    double[] result = FlowUtil.stretchFlow(flow, 9);
    Assertions.assertArrayEquals(
        new double[]{1, 1, 1, 1, 1, 1, 1, 1, 1}, result, SMALL_DELTA
    );

    assertArraySum(average(flow) * 9, result);
  }

  @Test
  public void testStretchFlow_3to5() {
    double[] flow = {1, 2, 3};
    double[] result = FlowUtil.stretchFlow(flow, 5);
    Assertions.assertArrayEquals(
        new double[]{1.0, 1.5, 2.0, 2.5, 3}, result, SMALL_DELTA
    );

    assertArraySum(average(flow) * 5, result);
  }

  @Test
  public void testStretchFlow_3to5_withModifier() {
    double[] flow = {1, 2, 3};
    Function<Double, Double> modifier = value -> value * 2;
    double[] result = FlowUtil.stretchFlow(flow, 5, modifier);
    Assertions.assertArrayEquals(
        new double[]{2.0, 3.0, 4.0, 5.0, 6.0}, result, SMALL_DELTA
    );

    assertArraySum(average(flow) * 2 * 5, result);
  }

  @Test
  public void testStretchFlow_3to6_withModifier() {
    double[] flow = {1, 2, 3};
    Function<Double, Double> modifier = Math::floor;
    double[] result = FlowUtil.stretchFlow(flow, 6, modifier);
    Assertions.assertArrayEquals(
        new double[]{
            1, 1, 1, 2, 2, 2,
        }, result, SMALL_DELTA
    );
  }

  @Test
  public void testStretchFlow_2to9() {
    double[] flow = {1, 2};
    double[] result = FlowUtil.stretchFlow(flow, 9);
    Assertions.assertArrayEquals(
        new double[]{1.0, 1.125, 1.25, 1.375, 1.5, 1.625, 1.75, 1.875, 2.0}, result, SMALL_DELTA
    );

    assertArraySum(average(flow) * 9, result);
  }

  @Test
  public void testStretchFlow_2to8() {
    double[] flow = {1, 2};
    double[] result = FlowUtil.stretchFlow(flow, 8);

    Assertions.assertArrayEquals(
        new double[]{
            1.0, 1.142857, 1.285714, 1.428571,
            1.571428, 1.714285, 1.857142, 2.0
        }, result, SMALL_DELTA);

    assertArraySum(average(flow) * 8, result);
  }

  @Test
  public void testStretchFlow_3to6() {
    double[] flow = {1, 2, 3};
    double[] result = FlowUtil.stretchFlow(flow, 6);
    Assertions.assertArrayEquals(
        new double[]{
            1.047619, 1.428571, 1.809523,
            2.190476, 2.571428, 2.952380
        }, result, SMALL_DELTA);

    assertArraySum(average(flow) * 6, result);
  }


  @Test
  public void testStretchFlow_3to18() {
    double[] flow = {1.1, 1.2, 1.3};
    double[] result = FlowUtil.stretchFlow(flow, 18);
    Assertions.assertArrayEquals(
        new double[]{
            1.102795, 1.113978, 1.125161, 1.136774,
            1.148602, 1.159784, 1.170967, 1.183010,
            1.194408, 1.205591, 1.216989, 1.229032,
            1.240215, 1.251397, 1.263225, 1.274838,
            1.286021, 1.297204}, result, SMALL_DELTA);

    assertArraySum(average(flow) * 18, result);
  }


  @Test
  public void testReduceFlow_5to3() {
    double[] flow = {1, 1.5, 2, 2.5, 3};
    double[] result = FlowUtil.reduceFlow(flow, 3);
    Assertions.assertArrayEquals(
        new double[]{1.2, 2, 2.8}, result, SMALL_DELTA
    );

    assertArraySum(average(flow) * 3, result);
  }

  @Test
  public void testReduceFlow_10to3() {
    double[] flow = {5, 5, 4, 4, 3, 3, 2, 2, 1, 1};
    double[] result = FlowUtil.reduceFlow(flow, 3);
    Assertions.assertArrayEquals(
        new double[]{4.6, 3.0, 1.4}, result, SMALL_DELTA
    );

    assertArraySum(average(flow) * 3, result);
  }

  @Test
  public void testReduceFlow_10to1() {
    double[] flow = {5, 5, 4, 4, 3, 3, 2, 2, 1, 1};
    double[] result = FlowUtil.reduceFlow(flow, 1);
    Assertions.assertArrayEquals(
        new double[]{ 3.0 }, result, SMALL_DELTA
    );

    assertArraySum(average(flow) * 1, result);
  }

  private void assertArraySum(double expected, double[] actual) {
    Assertions.assertEquals(expected, Arrays.stream(actual).sum(), SMALL_DELTA);
  }

  private double average(double[] array) {
    return Arrays.stream(array).sum() / array.length;
  }

}
