package com.github.kbman99.naturalmouse.api;

import java.awt.*;

/**
 * Abstracts ordinary static System calls away
 */
public interface SystemCalls {
  long currentTimeMillis();
  void sleep(long time) throws InterruptedException;
  Dimension getScreenSize();
  void setMousePosition(Component source, int x, int y);
  Point getMousePosition();
}
