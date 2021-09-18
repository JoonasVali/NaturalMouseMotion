package com.github.joonavali.naturalmouse.screenadjustednature;

import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import com.github.joonasvali.naturalmouse.support.DefaultOvershootManager;
import com.github.joonasvali.naturalmouse.support.ScreenAdjustedNature;
import com.github.joonavali.naturalmouse.testutils.MockDeviationProvider;
import com.github.joonavali.naturalmouse.testutils.MockMouse;
import com.github.joonavali.naturalmouse.testutils.MockNoiseProvider;
import com.github.joonavali.naturalmouse.testutils.MockRandom;
import com.github.joonavali.naturalmouse.testutils.MockSpeedManager;
import com.github.joonavali.naturalmouse.testutils.MockSystemCalls;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;

public class ScreenAdjustedNatureTest {
  MouseMotionFactory factory;
  MockMouse mouse;

  @BeforeEach
  public void setup() {
    factory = new MouseMotionFactory();
    factory.setNature(new ScreenAdjustedNature(new Dimension(100, 100), new Point(50, 50)));
    ((DefaultOvershootManager)factory.getOvershootManager()).setOvershoots(0);
    mouse = new MockMouse(60, 60);
    factory.setSystemCalls(new MockSystemCalls(mouse, 800, 500));
    factory.setNoiseProvider(new MockNoiseProvider());
    factory.setDeviationProvider(new MockDeviationProvider());
    factory.setSpeedManager(new MockSpeedManager());
    factory.setRandom(new MockRandom(new double[]{0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1}));
    factory.setMouseInfo(mouse);
  }

  @Test
  public void testOffsetAppliesToMouseMovement() throws InterruptedException {
    factory.move(50, 50);

    ArrayList<Point> moves = mouse.getMouseMovements();
    Assertions.assertEquals(new Point(60, 60), moves.get(0));
    Assertions.assertEquals(new Point(100, 100), moves.get(moves.size() - 1));
    Point lastPos = new Point(0, 0);
    for (Point p : moves) {
      Assertions.assertTrue(lastPos.x < p.x, lastPos.x +  " vs " + p.x);
      Assertions.assertTrue(lastPos.y < p.y, lastPos.y +  " vs " + p.y);
      lastPos = p;
    }
  }

  @Test
  public void testDimensionsLimitScreenOnLargeSide() throws InterruptedException {
    // Arbitrary large movement attempt: (60, 60) -> (1060, 1060)
    factory.move(1000, 1000);

    ArrayList<Point> moves = mouse.getMouseMovements();
    Assertions.assertEquals(new Point(60, 60), moves.get(0));
    // Expect the screen size to be only 100x100px, so it gets capped on 150, 150.
    // But NaturalMouseMotion allows to move to screen length - 1, so it's [149, 149]
    Assertions.assertEquals(new Point(149, 149), moves.get(moves.size() - 1));
  }

  @Test
  public void testOffsetLimitScreenOnSmallSide() throws InterruptedException {
    // Try to move out of the specified screen
    factory.move(-1, -1);

    ArrayList<Point> moves = mouse.getMouseMovements();
    Assertions.assertEquals(new Point(60, 60), moves.get(0));
    // Expect the offset to limit the mouse movement to 50, 50
    Assertions.assertEquals(new Point(50, 50), moves.get(moves.size() - 1));
  }



}
