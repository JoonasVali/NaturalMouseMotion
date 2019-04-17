package com.github.joonasvali.naturalmouse.util;

public class MathUtil {
  /**
   * Rounds value towards target to exact integer value.
   * @param value the value to be rounded
   * @param target the target to be rounded towards
   * @return the rounded value
   */
  public static int roundTowards(double value, int target) {
    if (target > value) {
      return (int) Math.ceil(value);
    } else {
      return (int) Math.floor(value);
    }
  }
}
