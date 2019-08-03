package com.github.joonasvali.naturalmouse.util;

import java.util.Arrays;
import java.util.function.Function;

public class FlowUtil {
  /**
   * Stretch flow to longer length. Tries to fill the caps with averages.
   *
   * This is an unintuitive method, because it turns out that, for example, array size of 3
   * scales better to array size of 5 than it does to array size of 6. [1, 2, 3] can be
   * easily scaled to [1, 1.5, 2, 2.5, 3], but it's not possible without recalculating middle number (2)
   * with array size of 6, simplistic solutions quickly would run to trouble like this  [1, 1.5, 2, 2.5, 3, (3)? ]
   * or maybe: [1, 1.5, 2, 2.5, ..., 3 ]. The correct solution would correctly scale the middle numbers
   * @param flow the original flow
   * @param targetLength the resulting flow length
   * @return the resulting flow
   */
  public static double[] stretchFlow(double[] flow, int targetLength) {
    return stretchFlow(flow, targetLength, a -> a);
  }

  /**
   * Stretch flow to longer length. Tries to fill the caps with averages.
   *
   * This is an unintuitive method, because it turns out that, for example, array size of 3
   * scales better to array size of 5 than it does to array size of 6. [1, 2, 3] can be
   * easily scaled to [1, 1.5, 2, 2.5, 3], but it's not possible without recalculating middle number (2)
   * with array size of 6, simplistic solutions quickly would run to trouble like this  [1, 1.5, 2, 2.5, 3, (3)? ]
   * or maybe: [1, 1.5, 2, 2.5, ..., 3 ]. The correct solution would correctly scale the middle numbers
   * over several indexes.
   * @param flow the original flow
   * @param targetLength the resulting flow length
   * @param modifier modifies the resulting values, you can use this to provide noise or amplify
   *                 the flow characteristics.
   * @return the resulting flow
   */
  public static double[] stretchFlow(double[] flow, int targetLength, Function<Double, Double> modifier) {
    if (targetLength < flow.length) {
      throw new IllegalArgumentException("Target bucket length smaller than flow. " +
          "" + targetLength + " vs " + flow.length);
    }
    double[] result;
    int tempLength = targetLength;

    if (flow.length != 1 && (tempLength - flow.length) % (flow.length - 1) != 0) {
      tempLength = (flow.length - 1) * (tempLength - flow.length) + 1;
    }

    result = new double[tempLength];
    int insider = flow.length - 2;
    int stepLength = (int)((tempLength - 2) / (double)(insider + 1)) + 1;
    int countToNextStep = stepLength;
    int fillValueIndex = 0;
    for (int i = 0; i < tempLength; i++) {
      double fillValueBottom = flow[fillValueIndex];
      double fillValueTop = fillValueIndex + 1 < flow.length ? flow[fillValueIndex + 1] : flow[fillValueIndex];

      double completion = (stepLength - countToNextStep) / (double)stepLength;

      result[i] = fillValueBottom * (1 - completion) + fillValueTop * completion;

      countToNextStep--;

      if (countToNextStep == 0) {
        countToNextStep = stepLength;
        fillValueIndex++;
      }
    }

    if (tempLength != targetLength) {
      result = reduceFlow(result, targetLength);
    }

    return Arrays.stream(result).map(modifier::apply).toArray();
  }

  /**
   * Reduction causes loss of information, so the resulting flow is always 'good enough', but is not quaranteed
   * to be equivalent, just a shorter version of the original flow
   * @param flow the original flow
   * @param targetLength the resulting array length
   * @return the resulting flow
   */
  public static double[] reduceFlow(double[] flow, int targetLength) {
    if (flow.length <= targetLength) {
      throw new IllegalArgumentException("Bad arguments [" + flow.length + ", " + targetLength + "]");
    }

    double multiplier = targetLength / (double) flow.length;
    double[] result = new double[targetLength];
    for (int i = 0; i < flow.length; i++) {
      double index = (i * multiplier);
      double untilIndex = (i + 1) * multiplier;
      int indexInt = (int)index;
      int untilIndexInt = (int)untilIndex;
      if (indexInt != untilIndexInt) {
        double resultIndexPortion = 1 - (index - indexInt);
        double nextResultIndexPortion = untilIndex - untilIndexInt;
        result[indexInt] += flow[i] * resultIndexPortion;
        if (untilIndexInt < result.length) {
          result[untilIndexInt] += flow[i] * nextResultIndexPortion;
        }
      }
      else {
        result[indexInt] += flow[i] * (untilIndex - index);
      }
    }

    return result;
  }
}
