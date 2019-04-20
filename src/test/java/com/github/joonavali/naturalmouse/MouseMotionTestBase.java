package com.github.joonavali.naturalmouse;

import com.github.joonasvali.naturalmouse.api.DeviationProvider;
import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import com.github.joonasvali.naturalmouse.api.NoiseProvider;
import com.github.joonasvali.naturalmouse.api.SpeedManager;
import com.github.joonasvali.naturalmouse.api.SystemCalls;
import com.github.joonavali.naturalmouse.testutils.MockDeviationProvider;
import com.github.joonavali.naturalmouse.testutils.MockMouse;
import com.github.joonavali.naturalmouse.testutils.MockNoiseProvider;
import com.github.joonavali.naturalmouse.testutils.MockRandom;
import com.github.joonavali.naturalmouse.testutils.MockSpeedManager;
import com.github.joonavali.naturalmouse.testutils.MockSystemCalls;
import org.junit.Assert;
import org.junit.Before;

import java.awt.*;
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
    systemCalls = new MockSystemCalls(mouse, SCREEN_WIDTH, SCREEN_HEIGHT);
    deviationProvider = new MockDeviationProvider();
    noiseProvider = new MockNoiseProvider();
    speedManager = new MockSpeedManager();
    random = new MockRandom(new double[]{0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1});

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


}
