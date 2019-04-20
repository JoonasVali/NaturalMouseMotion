package com.github.joonavali.naturalmouse.testutils;

import com.github.joonasvali.naturalmouse.support.DefaultMouseInfoAccessor;

import java.awt.*;
import java.util.ArrayList;

public class MockMouse extends DefaultMouseInfoAccessor {
  private final ArrayList<Point> mouseMovements = new ArrayList<>();

  public MockMouse() {
    mouseMovements.add(new Point(0, 0));
  }

  public MockMouse(int posX, int posY) {
    mouseMovements.add(new Point(posX, posY));
  }

  public synchronized void mouseMove(int x, int y) {
    mouseMovements.add(new Point(x, y));
  }

  @Override
  public Point getMousePosition() {
    return mouseMovements.get(mouseMovements.size() - 1);
  }

  public ArrayList<Point> getMouseMovements() {
    return mouseMovements;
  }
}
