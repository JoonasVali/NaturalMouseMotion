package com.github.joonavali.naturalmouse.testutils;

import com.github.joonasvali.naturalmouse.support.DefaultSystemCalls;

import java.awt.*;

public class MockSystemCalls extends DefaultSystemCalls {
  private final int screenWidth;
  private final int screenHeight;
  private final MockMouse mockMouse;

  public MockSystemCalls(MockMouse mockMouse, int screenWidth, int screenHeight) {
    super(null);
    this.mockMouse = mockMouse;
    this.screenWidth = screenWidth;
    this.screenHeight = screenHeight;
  }

  @Override
  public long currentTimeMillis() {
    return 0;
  }

  @Override
  public void sleep(long time) throws InterruptedException {
    // Do nothing.
  }

  @Override
  public Dimension getScreenSize() {
    return new Dimension(screenWidth, screenHeight);
  }

  @Override
  public void setMousePosition(int x, int y) {
    mockMouse.mouseMove(x, y);
  }
}