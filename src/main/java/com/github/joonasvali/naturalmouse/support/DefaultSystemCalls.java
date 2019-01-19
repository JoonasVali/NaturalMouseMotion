package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.SystemCalls;

import java.awt.*;

public class DefaultSystemCalls implements SystemCalls {
  private final Robot robot;

  public DefaultSystemCalls(Robot robot) {
    this.robot = robot;
  }

  @Override
  public long currentTimeMillis() {
    return System.currentTimeMillis();
  }

  @Override
  public void sleep(long time) throws InterruptedException {
    Thread.sleep(time);
  }

  @Override
  public Dimension getScreenSize() {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }

  @Override
  public void setMousePosition(int x, int y) {
    robot.mouseMove(x, y);
  }
}
