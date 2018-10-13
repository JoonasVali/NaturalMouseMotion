package com.github.joonasvali.naturalmouse.support;

import java.awt.*;

public interface SystemCalls {
  long currentTimeMillis();
  void sleep(long time) throws InterruptedException;
  Dimension getScreenSize();
}
