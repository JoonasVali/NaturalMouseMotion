package com.github.joonavali.naturalmouse;

import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import com.github.joonasvali.naturalmouse.support.DeviationProvider;
import com.github.joonasvali.naturalmouse.support.DoublePoint;
import com.github.joonasvali.naturalmouse.support.MouseInfoAccessor;
import com.github.joonasvali.naturalmouse.support.NoiseProvider;
import com.github.joonasvali.naturalmouse.support.SystemCalls;
import org.junit.Assert;
import org.junit.Before;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MouseMotionTestBase {
  public static final double SMALL_DELTA = 0.000001;
  protected static final int SCREEN_WIDTH = 800;
  protected static final int SCREEN_HEIGHT = 500;
  protected MouseMotionFactory factory;
  protected SystemCalls systemCalls;
  protected DeviationProvider deviationProvider;
  protected NoiseProvider noiseProvider;
  protected Random random;
  protected MockMouse mouse;

  @Before
  public void setup() {
    factory = new MouseMotionFactory();
    systemCalls = new MockSystemCalls();
    deviationProvider = new MockDeviationProvider();
    noiseProvider = new MockNoiseProvider();
    random = new MockRandom();

    factory.setSystemCalls(systemCalls);
    factory.setDeviationProvider(deviationProvider);
    factory.setNoiseProvider(noiseProvider);
    factory.setRandom(random);

    try {
      mouse = new MockMouse();
    } catch (AWTException e) {
      throw new RuntimeException(e);
    }

    factory.setMouseInfo(mouse);
    factory.setRobot(mouse);
  }

  protected void assertMousePosition(int x, int y) {
    Point pos = mouse.getMousePosition();
    Assert.assertEquals(x, pos.getX(), SMALL_DELTA);
    Assert.assertEquals(y, pos.getY(), SMALL_DELTA);
  }

  protected class MockSystemCalls implements SystemCalls {
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
      return new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    }
  }

  protected class MockDeviationProvider implements DeviationProvider {
    @Override
    public DoublePoint getDeviation(double totalDistanceInPixels, double completionFraction) {
      return new DoublePoint(0, 0);
    }
  }

  protected class MockNoiseProvider implements NoiseProvider {
    @Override
    public DoublePoint getNoise(Random random, double distance) {
      return new DoublePoint(0, 0);
    }
  }

  protected class MockMouse extends Robot implements MouseInfoAccessor {
    private final ArrayList<Point> mouseMovements = new ArrayList<>();

    MockMouse() throws AWTException {
      mouseMovements.add(new Point(0, 0));
    }

    @Override
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

  protected class MockRandom extends Random {
    int index = 0;
    double[] nums = new double[]{0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1};

    @Override
    public double nextDouble() {
      int nextIndex = index++;
      index %= nums.length;
      return nums[nextIndex];
    }
  }
}
