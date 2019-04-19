package com.github.joonavali.naturalmouse.support.mousemotion;

import com.github.joonasvali.naturalmouse.api.OvershootManager;
import com.github.joonasvali.naturalmouse.api.SpeedManager;
import com.github.joonasvali.naturalmouse.support.Flow;
import com.github.joonasvali.naturalmouse.support.mousemotion.Movement;
import com.github.joonasvali.naturalmouse.support.mousemotion.MovementFactory;
import com.github.joonasvali.naturalmouse.util.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Arrays;

public class MovementFactoryTest {
  private static final double SMALL_DELTA = 0.00000000001;

  @Test
  public void testSingleMovement() {
    SpeedManager speedManager = createConstantSpeedManager();
    OvershootManager overshootManager = createNoOvershootManager();
    MovementFactory factory = new MovementFactory(50, 51, speedManager, overshootManager, new Dimension(500, 500));

    ArrayDeque<Movement> movements = factory.createMovements(new Point(100, 100));
    Assert.assertEquals(1, movements.size());
    Assert.assertEquals(50, movements.getFirst().destX);
    Assert.assertEquals(51, movements.getFirst().destY);
    Assert.assertEquals(100, movements.getFirst().time);
    Assert.assertEquals(-50.0, movements.getFirst().xDistance, SMALL_DELTA);
    Assert.assertEquals(-49.0, movements.getFirst().yDistance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, movements.getFirst().flow.getFlowCharacteristics(), SMALL_DELTA);
  }

  @Test
  public void testMultipleMovement() {
    SpeedManager speedManager = createConstantSpeedManager();
    OvershootManager overshootManager = createMultiOvershootManager();
    MovementFactory factory = new MovementFactory(50, 150, speedManager, overshootManager, new Dimension(500, 500));

    ArrayDeque<Movement> movements = factory.createMovements(new Point(100, 100));
    Assert.assertEquals(3, movements.size());

    Movement first = movements.removeFirst();
    Assert.assertEquals(55, first.destX);
    Assert.assertEquals(155, first.destY);
    Assert.assertEquals(100, first.time);
    Assert.assertEquals(-45.0, first.xDistance, SMALL_DELTA);
    Assert.assertEquals(55.0, first.yDistance, SMALL_DELTA);
    Assert.assertEquals(Math.hypot(first.xDistance, first.yDistance), first.distance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, first.flow.getFlowCharacteristics(), SMALL_DELTA);

    Movement second = movements.removeFirst();
    Assert.assertEquals(45, second.destX);
    Assert.assertEquals(145, second.destY);
    Assert.assertEquals(50, second.time);
    Assert.assertEquals(-10.0, second.xDistance, SMALL_DELTA);
    Assert.assertEquals(-10.0, second.yDistance, SMALL_DELTA);
    Assert.assertEquals(Math.hypot(second.xDistance, second.yDistance), second.distance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, second.flow.getFlowCharacteristics(), SMALL_DELTA);


    Movement third = movements.removeFirst();
    Assert.assertEquals(50, third.destX);
    Assert.assertEquals(150, third.destY);
    Assert.assertEquals(25, third.time);
    Assert.assertEquals(5, third.xDistance, SMALL_DELTA);
    Assert.assertEquals(5, third.yDistance, SMALL_DELTA);
    Assert.assertEquals(Math.hypot(third.xDistance, third.yDistance), third.distance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, third.flow.getFlowCharacteristics(), SMALL_DELTA);
  }

  protected SpeedManager createConstantSpeedManager() {
    double[] characteristics = {100};
    return distance -> new Pair<>(new Flow(characteristics), 100L);
  }

  private OvershootManager createNoOvershootManager() {
    return new OvershootManager() {
      @Override
      public int getOvershoots(Flow flow, long mouseMovementMs, double distance) {
        return 0;
      }

      @Override
      public Point getOvershootAmount(double distanceToRealTargetX, double distanceToRealTargetY, long mouseMovementMs, int overshootsRemaining) {
        return null;
      }

      @Override
      public long deriveNextMouseMovementTimeMs(long mouseMovementMs, int overshootsRemaining) {
        return 0;
      }
    };
  }

  private OvershootManager createMultiOvershootManager() {
    return new OvershootManager() {
      Point[] points = new Point[] { new Point(5, 5),  new Point(-5, -5) };
      ArrayDeque<Point> deque = new ArrayDeque<>(Arrays.asList(points));
      @Override
      public int getOvershoots(Flow flow, long mouseMovementMs, double distance) {
        return 2;
      }

      @Override
      public Point getOvershootAmount(double distanceToRealTargetX, double distanceToRealTargetY, long mouseMovementMs, int overshootsRemaining) {
        return deque.removeFirst();
      }

      @Override
      public long deriveNextMouseMovementTimeMs(long mouseMovementMs, int overshootsRemaining) {
        return mouseMovementMs / 2;
      }
    };
  }
}
