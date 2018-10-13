package com.github.joonasvali.naturalmouse.support;

import java.awt.*;

public class DefaultSystemCalls implements SystemCalls {
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
}
