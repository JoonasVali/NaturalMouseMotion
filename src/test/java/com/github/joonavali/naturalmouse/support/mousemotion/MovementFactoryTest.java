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
    SpeedManager speedManager = createConstantSpeedManager(100);
    OvershootManager overshootManager = createNoOvershootManager();
    MovementFactory factory = new MovementFactory(50, 51, speedManager, overshootManager, new Dimension(500, 500));

    ArrayDeque<Movement> movements = factory.createMovements(new Point(100, 100));
    Assert.assertEquals(1, movements.size());
    Assert.assertEquals(50, movements.getFirst().destX);
    Assert.assertEquals(51, movements.getFirst().destY);
    Assert.assertEquals(100, movements.getFirst().time);
    Assert.assertEquals(-50, movements.getFirst().xDistance, SMALL_DELTA);
    Assert.assertEquals(-49, movements.getFirst().yDistance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, movements.getFirst().flow.getFlowCharacteristics(), SMALL_DELTA);
  }

  @Test
  public void testMultipleMovement() {
    SpeedManager speedManager = createConstantSpeedManager(100);
    OvershootManager overshootManager = createMultiOvershootManager();
    MovementFactory factory = new MovementFactory(50, 150, speedManager, overshootManager, new Dimension(500, 500));

    ArrayDeque<Movement> movements = factory.createMovements(new Point(100, 100));
    Assert.assertEquals(3, movements.size());

    Movement first = movements.removeFirst();
    Assert.assertEquals(55, first.destX);
    Assert.assertEquals(155, first.destY);
    Assert.assertEquals(100, first.time);
    Assert.assertEquals(-45, first.xDistance, SMALL_DELTA);
    Assert.assertEquals(55, first.yDistance, SMALL_DELTA);
    Assert.assertEquals(Math.hypot(first.xDistance, first.yDistance), first.distance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, first.flow.getFlowCharacteristics(), SMALL_DELTA);

    Movement second = movements.removeFirst();
    Assert.assertEquals(45, second.destX);
    Assert.assertEquals(145, second.destY);
    Assert.assertEquals(50, second.time);
    Assert.assertEquals(-10, second.xDistance, SMALL_DELTA);
    Assert.assertEquals(-10, second.yDistance, SMALL_DELTA);
    Assert.assertEquals(Math.hypot(second.xDistance, second.yDistance), second.distance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, second.flow.getFlowCharacteristics(), SMALL_DELTA);


    Movement third = movements.removeFirst();
    Assert.assertEquals(50, third.destX);
    Assert.assertEquals(150, third.destY);
    Assert.assertEquals(50, third.time);
    Assert.assertEquals(5, third.xDistance, SMALL_DELTA);
    Assert.assertEquals(5, third.yDistance, SMALL_DELTA);
    Assert.assertEquals(Math.hypot(third.xDistance, third.yDistance), third.distance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, third.flow.getFlowCharacteristics(), SMALL_DELTA);
  }

  @Test
  public void testZeroOffsetOvershootsRemovedFromEnd() {
    SpeedManager speedManager = createConstantSpeedManager(64);
    OvershootManager overshootManager = createOvershootManagerWithZeroOffsets();
    MovementFactory factory = new MovementFactory(50, 150, speedManager, overshootManager, new Dimension(500, 500));

    ArrayDeque<Movement> movements = factory.createMovements(new Point(100, 100));
    Assert.assertEquals(4, movements.size()); // 3 overshoots and 1 final approach to destination

    Movement first = movements.removeFirst();
    Assert.assertEquals(55, first.destX);
    Assert.assertEquals(155, first.destY);
    Assert.assertEquals(64, first.time);
    Assert.assertEquals(-45, first.xDistance, SMALL_DELTA);
    Assert.assertEquals(55, first.yDistance, SMALL_DELTA);
    Assert.assertEquals(Math.hypot(first.xDistance, first.yDistance), first.distance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, first.flow.getFlowCharacteristics(), SMALL_DELTA);

    Movement second = movements.removeFirst(); // 0-offset in the middle is not removed, this one actually hits destination.
    Assert.assertEquals(50, second.destX);
    Assert.assertEquals(150, second.destY);
    Assert.assertEquals(32, second.time);
    Assert.assertEquals(-5, second.xDistance, SMALL_DELTA);
    Assert.assertEquals(-5, second.yDistance, SMALL_DELTA);
    Assert.assertEquals(Math.hypot(second.xDistance, second.yDistance), second.distance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, second.flow.getFlowCharacteristics(), SMALL_DELTA);

    Movement third = movements.removeFirst();
    Assert.assertEquals(51, third.destX);
    Assert.assertEquals(151, third.destY);
    Assert.assertEquals(16, third.time);
    Assert.assertEquals(1, third.xDistance, SMALL_DELTA);
    Assert.assertEquals(1, third.yDistance, SMALL_DELTA);
    Assert.assertEquals(Math.hypot(third.xDistance, third.yDistance), third.distance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, third.flow.getFlowCharacteristics(), SMALL_DELTA);

    Movement fourth = movements.removeFirst();
    Assert.assertEquals(50, fourth.destX);
    Assert.assertEquals(150, fourth.destY);
    Assert.assertEquals(32, fourth.time);
    Assert.assertEquals(-1, fourth.xDistance, SMALL_DELTA);
    Assert.assertEquals(-1, fourth.yDistance, SMALL_DELTA);
    Assert.assertEquals(Math.hypot(fourth.xDistance, fourth.yDistance), fourth.distance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, fourth.flow.getFlowCharacteristics(), SMALL_DELTA);
  }

  @Test
  public void testZeroOffsetOvershootsRemovedFromEndIfAllZero() {
    SpeedManager speedManager = createConstantSpeedManager(100);
    OvershootManager overshootManager = createOvershootManagerWithAllZeroOffsets();
    MovementFactory factory = new MovementFactory(50, 150, speedManager, overshootManager, new Dimension(500, 500));

    ArrayDeque<Movement> movements = factory.createMovements(new Point(100, 100));
    Assert.assertEquals(1, movements.size());

    Assert.assertEquals(50, movements.getFirst().destX);
    Assert.assertEquals(150, movements.getFirst().destY);
    Assert.assertEquals(50, movements.getFirst().time);
    Assert.assertEquals(-50, movements.getFirst().xDistance, SMALL_DELTA);
    Assert.assertEquals(50, movements.getFirst().yDistance, SMALL_DELTA);
    Assert.assertArrayEquals(new double[]{100}, movements.getFirst().flow.getFlowCharacteristics(), SMALL_DELTA);
  }

  protected SpeedManager createConstantSpeedManager(long time) {
    double[] characteristics = {100};
    return distance -> new Pair<>(new Flow(characteristics), time);
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
        return points.length;
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

  private OvershootManager createOvershootManagerWithZeroOffsets() {
    return new OvershootManager() {
      Point[] points = new Point[] {
          new Point(5, 5),
          new Point(0, 0),
          new Point(1, 1),
          new Point(0, 0),
          new Point(0, 0),
      };
      ArrayDeque<Point> deque = new ArrayDeque<>(Arrays.asList(points));
      @Override
      public int getOvershoots(Flow flow, long mouseMovementMs, double distance) {
        return points.length;
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

  private OvershootManager createOvershootManagerWithAllZeroOffsets() {
    return new OvershootManager() {
      Point[] points = new Point[] {
          new Point(0, 0),
          new Point(0, 0),
          new Point(0, 0),
      };
      ArrayDeque<Point> deque = new ArrayDeque<>(Arrays.asList(points));
      @Override
      public int getOvershoots(Flow flow, long mouseMovementMs, double distance) {
        return points.length;
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
