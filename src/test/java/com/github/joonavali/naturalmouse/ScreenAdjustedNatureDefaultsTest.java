package com.github.joonavali.naturalmouse;

import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import com.github.joonasvali.naturalmouse.api.SystemCalls;
import com.github.joonasvali.naturalmouse.support.DefaultMouseInfoAccessor;
import com.github.joonasvali.naturalmouse.support.DefaultMouseMotionNature;
import com.github.joonasvali.naturalmouse.support.DefaultOvershootManager;
import com.github.joonasvali.naturalmouse.support.DefaultSystemCalls;
import com.github.joonasvali.naturalmouse.support.ScreenAdjustedNature;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.awt.*;
import java.util.ArrayList;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DefaultMouseMotionNature.class)
/**
 * This should run same tests as ScreenAdjustedNatureTest with difference in setup.
 * These tests verify that the offsets and dimensions are properly set when user does not explicitly
 * set MouseInfoAccessor and SystemCalls, but rely on the default version in DefaultMouseMotionNature
 */
public class ScreenAdjustedNatureDefaultsTest {
  private MouseMotionFactory factory;
  private MouseMotionTestBase.MockMouse mouse;

  @Before
  public void setup() throws Exception {
    mouse = new MouseMotionTestBase.MockMouse(60, 60);
    DefaultSystemCalls mockSystemCalls = new MouseMotionTestBase.MockSystemCalls(mouse);
    whenNew(DefaultSystemCalls.class).withAnyArguments().thenReturn(mockSystemCalls);
    whenNew(DefaultMouseInfoAccessor.class).withAnyArguments().thenReturn(mouse);

    // Mockito inserts the systemCalls and Mouse
    factory = new MouseMotionFactory();
    factory.setNature(new ScreenAdjustedNature(new Dimension(100, 100), new Point(50, 50)));
    ((DefaultOvershootManager)factory.getOvershootManager()).setOvershoots(0);
    factory.setNoiseProvider(new MouseMotionTestBase.MockNoiseProvider());
    factory.setDeviationProvider(new MouseMotionTestBase.MockDeviationProvider());
    factory.setSpeedManager(new MouseMotionTestBase.MockSpeedManager());
    factory.setRandom(new MouseMotionTestBase.MockRandom());

  }

  @Test
  public void testOffsetAppliesToMouseMovement() throws InterruptedException {
    factory.move(50, 50);

    ArrayList<Point> moves = mouse.getMouseMovements();
    Assert.assertEquals(new Point(60, 60), moves.get(0));
    Assert.assertEquals(new Point(100, 100), moves.get(moves.size() - 1));
    Point lastPos = new Point(0, 0);
    for (Point p : moves) {
      Assert.assertTrue(lastPos.x +  " vs " + p.x, lastPos.x < p.x);
      Assert.assertTrue(lastPos.y +  " vs " + p.y,lastPos.y < p.y);
      lastPos = p;
    }
  }

  @Test
  public void testDimensionsLimitScreenOnLargeSide() throws InterruptedException {
    // Arbitrary large movement attempt: (60, 60) -> (1060, 1060)
    factory.move(1000, 1000);

    ArrayList<Point> moves = mouse.getMouseMovements();
    Assert.assertEquals(new Point(60, 60), moves.get(0));
    // Expect the screen size to be only 100x100px, so it gets capped on 150, 150.
    // But NaturalMouseMotion allows to move to screen length - 1, so it's [149, 149]
    Assert.assertEquals(new Point(149, 149), moves.get(moves.size() - 1));
  }

  @Test
  public void testOffsetLimitScreenOnSmallSide() throws InterruptedException {
    // Try to move out of the specified screen
    factory.move(-1, -1);

    ArrayList<Point> moves = mouse.getMouseMovements();
    Assert.assertEquals(new Point(60, 60), moves.get(0));
    // Expect the offset to limit the mouse movement to 50, 50
    Assert.assertEquals(new Point(50, 50), moves.get(moves.size() - 1));
  }

}
