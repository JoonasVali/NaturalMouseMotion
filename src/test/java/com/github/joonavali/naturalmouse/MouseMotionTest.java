package com.github.joonavali.naturalmouse;

import com.github.joonasvali.naturalmouse.support.DefaultOvershootManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

public class MouseMotionTest extends MouseMotionTestBase {

  @Test
  public void linearMotionNoOvershoots() throws InterruptedException {
    assertMousePosition(0, 0);
    ((DefaultOvershootManager)factory.getOvershootManager()).setOvershoots(0);
    factory.move(50, 50);
    assertMousePosition(50, 50);

    ArrayList<Point> points = mouse.getMouseMovements();
    // The chosen 5 is 'good enough value' for 0,0 -> 50,50 for this test. we don't expect it to
    // be any certain value, because it can be changed in the future how the implementation actually works,
    // but based on gut feeling anything below 5 is too low.
    Assertions.assertTrue(points.size() > 5);
    // We don't want to verify every pixel what the mouse visits
    // instead we make sure its path is linear, as this is what we can expect from this test.
    Point lastPoint = new Point();
    for (Point p : points) {
      Assertions.assertEquals(p.x, p.y);
      Assertions.assertTrue(p.x >= lastPoint.x, "p.x  = " + p.x + " lastPoint.x = " + lastPoint.x);
      Assertions.assertTrue(p.y >= lastPoint.y, "p.y  = " + p.y + " lastPoint.y = " + lastPoint.y);
      lastPoint = p;
    }
  }

  @Test
  public void cantMoveOutOfScreenToNegative_noOverShoots() throws InterruptedException {
    assertMousePosition(0, 0);
    ((DefaultOvershootManager)factory.getOvershootManager()).setOvershoots(0);
    factory.move(-50, -50);

    ArrayList<Point> points = mouse.getMouseMovements();
    for (Point p : points) {
      Assertions.assertTrue(p.getX() >= 0 && p.getY() >= 0);
    }
    assertMousePosition(0, 0);
  }

  @Test
  public void cantMoveUpToScreenWidth_noOvershoots() throws InterruptedException {
    // This helps to make sure that the test detects if used height instead of width or vice versa in implementation
    Assertions.assertNotEquals(SCREEN_WIDTH, SCREEN_HEIGHT);

    assertMousePosition(0, 0);
    ((DefaultOvershootManager)factory.getOvershootManager()).setOvershoots(0);
    factory.move(SCREEN_WIDTH + 100, SCREEN_HEIGHT - 100);

    ArrayList<Point> points = mouse.getMouseMovements();
    for (Point p : points) {
      Assertions.assertTrue(p.getX() < SCREEN_WIDTH);
    }
    assertMousePosition(SCREEN_WIDTH - 1, SCREEN_HEIGHT - 100);
  }

  @Test
  public void cantMoveUpToScreenWidth_withOvershoots() throws InterruptedException {
    // This helps to make sure that the test detects if used height instead of width or vice versa in implementation
    Assertions.assertNotEquals(SCREEN_WIDTH, SCREEN_HEIGHT);

    assertMousePosition(0, 0);
    ((DefaultOvershootManager)factory.getOvershootManager()).setOvershoots(100);
    factory.move(SCREEN_WIDTH - 1, SCREEN_HEIGHT - 100);

    ArrayList<Point> points = mouse.getMouseMovements();
    for (Point p : points) {
      Assertions.assertTrue(p.getX() < SCREEN_WIDTH);
    }
    assertMousePosition(SCREEN_WIDTH - 1, SCREEN_HEIGHT - 100);
  }

  @Test
  public void cantMoveUpToScreenHeight_noOvershoots() throws InterruptedException {
    // This helps to make sure that the test detects if used height instead of width or vice versa in implementation
    Assertions.assertNotEquals(SCREEN_WIDTH, SCREEN_HEIGHT);

    assertMousePosition(0, 0);
    ((DefaultOvershootManager)factory.getOvershootManager()).setOvershoots(0);
    factory.move(SCREEN_WIDTH - 100, SCREEN_HEIGHT + 100);

    ArrayList<Point> points = mouse.getMouseMovements();
    for (Point p : points) {
      Assertions.assertTrue(p.getY() < SCREEN_HEIGHT);
    }
    assertMousePosition(SCREEN_WIDTH - 100, SCREEN_HEIGHT - 1);
  }

  @Test
  public void cantMoveUpToScreenHeight_withOvershoots() throws InterruptedException {
    // This helps to make sure that the test detects if used height instead of width or vice versa in implementation
    Assertions.assertNotEquals(SCREEN_WIDTH, SCREEN_HEIGHT);

    assertMousePosition(0, 0);
    ((DefaultOvershootManager)factory.getOvershootManager()).setOvershoots(100);
    factory.move(SCREEN_WIDTH - 100, SCREEN_HEIGHT - 1);

    ArrayList<Point> points = mouse.getMouseMovements();
    for (Point p : points) {
      Assertions.assertTrue(p.getY() < SCREEN_HEIGHT);
    }
    assertMousePosition(SCREEN_WIDTH - 100, SCREEN_HEIGHT - 1);
  }

  @Test
  public void cantMoveOutOfScreenToNegative_withOverShoots() throws InterruptedException {
    // setup mouse to 50,50
    mouse.mouseMove(50, 50);
    assertMousePosition(50, 50);

    // Moving mouse to 0,0 with large amount of overshoots, so it would be likely to hit negative if possible.
    ((DefaultOvershootManager)factory.getOvershootManager()).setOvershoots(100);
    factory.move(0, 0);

    ArrayList<Point> points = mouse.getMouseMovements();
    for (Point p : points) {
      Assertions.assertTrue(p.getX() >= 0 && p.getY() >= 0);
    }
    assertMousePosition(0, 0);
  }


}
