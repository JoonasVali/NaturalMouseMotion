package com.github.joonavali.naturalmouse;

import com.github.joonasvali.naturalmouse.api.DeviationProvider;
import com.github.joonasvali.naturalmouse.api.MouseInfoAccessor;
import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import com.github.joonasvali.naturalmouse.api.NoiseProvider;
import com.github.joonasvali.naturalmouse.api.SpeedManager;
import com.github.joonasvali.naturalmouse.api.SystemCalls;
import com.github.joonasvali.naturalmouse.support.DoublePoint;
import com.github.joonasvali.naturalmouse.support.Flow;
import com.github.joonasvali.naturalmouse.util.Pair;
import org.junit.Assert;
import org.junit.Before;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class MouseMotionTestBase {
  protected static final double SMALL_DELTA = 0.000001;
  protected static final int SCREEN_WIDTH = 800;
  protected static final int SCREEN_HEIGHT = 500;
  protected MouseMotionFactory factory;
  protected SystemCalls systemCalls;
  protected DeviationProvider deviationProvider;
  protected NoiseProvider noiseProvider;
  protected SpeedManager speedManager;
  protected Random random;
  protected MockMouse mouse;

  @Before
  public void setup() {
    mouse = new MockMouse();
    factory = new MouseMotionFactory();
    systemCalls = new MockSystemCalls(mouse);
    deviationProvider = new MockDeviationProvider();
    noiseProvider = new MockNoiseProvider();
    speedManager = new MockSpeedManager();
    random = new MockRandom();

    factory.setSystemCalls(systemCalls);
    factory.setDeviationProvider(deviationProvider);
    factory.setNoiseProvider(noiseProvider);
    factory.setSpeedManager(speedManager);
    factory.setRandom(random);

    factory.setMouseInfo(mouse);
  }

  protected void assertMousePosition(int x, int y) {
    Point pos = mouse.getMousePosition();
    Assert.assertEquals(x, pos.getX(), SMALL_DELTA);
    Assert.assertEquals(y, pos.getY(), SMALL_DELTA);
  }

  protected static class MockSystemCalls implements SystemCalls {
    private final MockMouse mockMouse;

    public MockSystemCalls(MockMouse mockMouse) {
      this.mockMouse = mockMouse;
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
      return new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    @Override
    public void setMousePosition(int x, int y) {
      mockMouse.mouseMove(x, y);
    }
  }

  protected static class MockDeviationProvider implements DeviationProvider {
    @Override
    public DoublePoint getDeviation(double totalDistanceInPixels, double completionFraction) {
      return new DoublePoint(0, 0);
    }
  }

  protected static class MockSpeedManager implements SpeedManager {

    @Override
    public Pair<Flow, Long> getFlowWithTime(double distance) {
      double[] characteristics = {100};
      return new Pair<Flow, Long>(new Flow(characteristics), 10L);
    }
  }

  protected SpeedManager createMockSpeedManager() {
    double[] characteristics = {100};
    return distance -> new Pair<Flow, Long>(new Flow(characteristics), 10L);
  }

  protected static class MockNoiseProvider implements NoiseProvider {
    @Override
    public DoublePoint getNoise(Random random, double xStepSize, double yStepSize) {
      return new DoublePoint(0, 0);
    }
  }

  protected static class MockMouse implements MouseInfoAccessor {
    private final ArrayList<Point> mouseMovements = new ArrayList<>();

    MockMouse() {
      mouseMovements.add(new Point(0, 0));
    }

    MockMouse(int posX, int posY) {
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

  protected static class MockRandom extends Random {
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
