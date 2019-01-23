package com.github.joonasvali.naturalmouse.support;

import java.util.Arrays;

public class FlowCharacteristics {
  public static double[] variatingFlow() {
    return new double[]{
        10, 15, 20, 12, 20, 100, 100, 50, 50, 100, 90, 100, 70, 60, 70, 100, 100, 100, 30, 60, 70, 50,
        100, 100, 90, 95, 91, 100, 80, 70, 60, 50, 65, 54, 50, 20, 25, 15, 15, 1, 12, 2,
        11, 15, 10, 9, 6, 1, 4, 1, 20, 1, 0, 2
    };
  }

  public static double[] interruptedFlow() {
    return new double[]{
        10, 20, 10, 20, 60, 90, 5, 10, 5, 10, 5, 10, 5, 10, 10, 20, 40, 60, 60, 80, 50, 10, 1, 5, 10, 20
    };
  }

  public static double[] slowStartupFlow() {
    return new double[]{
        10, 1, 2, 3, 4, 10, 11, 12, 13, 14, 15, 16, 20, 20, 20, 20, 20, 20, 20, 20, 20,
        20, 20, 20, 20, 20, 20, 20, 20, 20, 40, 40, 40, 40, 40, 40, 90, 100, 90, 50, 10
    };
  }


  public static double[] slowStartup2Flow() {
    return new double[]{
        10, 1, 2, 3, 4, 10, 11, 12, 13,10, 1, 2, 3, 4, 10, 11, 12, 13,10, 1, 2, 3, 4, 10, 11, 12, 13,
        14, 15, 16, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 40, 40,
        40, 40, 40, 40, 90, 100, 90, 50, 10, 100, 10, 20, 10, 20, 50, 10, 2
    };
  }

  public static double[] jaggedFlow() {
    return new double[]{
        57, 79, 83, 6, 13, 90, 31, 4, 65, 36, 28, 71, 91, 65, 88, 6, 75, 28, 50, 14, 65, 86, 74, 55, 62, 45, 30,
        78, 76, 17, 48, 84, 55, 74, 45, 91, 95, 99, 42, 90, 23, 39, 14, 49, 59, 45, 10, 73, 54, 22, 70, 26, 44,
        85, 53, 90, 64, 26, 78, 84, 99, 89, 99, 57, 4, 93, 64, 50, 62, 46, 68, 38, 80, 91, 27, 86, 33, 64, 59,
        97, 95, 41, 38, 73, 74, 47, 26, 6, 71, 33, 67, 70, 68, 30, 36, 25, 16, 72, 82, 30,
    };
  }

  public static double[] random() {
    double[] result = new double[100];
    for (int i = 0; i < result.length; i++) {
      result[i] = (int) (Math.random() * 100);
    }
    return result;
  }

  public static double[] constantSpeed() {
    double[] flowBuckets = new double[100];
    Arrays.fill(flowBuckets, 100);
    return flowBuckets;
  }
}
