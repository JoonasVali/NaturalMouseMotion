package com.github.joonasvali.naturalmouse.support;

import java.awt.*;

/**
 * Abstracts ordinary static System calls away
 */
public interface SystemCalls {
  long currentTimeMillis();
  void sleep(long time) throws InterruptedException;
  Dimension getScreenSize();
}
