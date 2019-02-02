package com.github.joonasvali.naturalmouse.support;

public class DoublePoint {
  public final static DoublePoint ZERO = new DoublePoint(0, 0);
  private final double x;
  private final double y;

  public DoublePoint(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
}
