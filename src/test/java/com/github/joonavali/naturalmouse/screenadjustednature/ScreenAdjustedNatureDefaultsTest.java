package com.github.joonavali.naturalmouse.screenadjustednature;

import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import com.github.joonasvali.naturalmouse.support.DefaultMouseInfoAccessor;
import com.github.joonasvali.naturalmouse.support.DefaultOvershootManager;
import com.github.joonasvali.naturalmouse.support.DefaultSystemCalls;
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
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.awt.*;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;


/**
 * This should run same tests as ScreenAdjustedNatureTest with difference in setup.
 * These tests verify that the offsets and dimensions are properly set when user does not explicitly
 * set MouseInfoAccessor and SystemCalls, but rely on the default version in DefaultMouseMotionNature
 */
public class ScreenAdjustedNatureDefaultsTest {
  private MouseMotionFactory factory;
  private MockMouse mouse;

  @BeforeEach
  public void setup() throws Exception {

    mouse = new MockMouse(60, 60);
    DefaultSystemCalls mockSystemCalls = new MockSystemCalls(mouse, 800, 500);

    Answer<?> mouseAnswer = invocationOnMock -> {
      Method method = mouse.getClass().getMethod(invocationOnMock.getMethod().getName(), invocationOnMock.getMethod().getParameterTypes());
      return method.invoke(mouse, invocationOnMock.getArguments());
    };

    Answer<?> systemCallAnswer = invocationOnMock -> {
      Method method = mockSystemCalls.getClass().getMethod(invocationOnMock.getMethod().getName(), invocationOnMock.getMethod().getParameterTypes());
      return method.invoke(mockSystemCalls, invocationOnMock.getArguments());
    };

    try (
        // Not sure how to proxy constructor mocks to returns mock objects directly, but this ugly workaround seems to work as well.
        MockedConstruction<DefaultMouseInfoAccessor> mouseConstruction = mockConstruction(DefaultMouseInfoAccessor.class, (mock, context) -> {
          // Direct all calls to 'mouse'
          when(mock.getMousePosition()).thenAnswer(mouseAnswer);
        });
        MockedConstruction<DefaultSystemCalls> mockedDefaultSystemCalls = mockConstruction(DefaultSystemCalls.class, (mock, context) -> {
          // Direct all calls to 'mockSystemCalls'
          when(mock.currentTimeMillis()).thenAnswer(systemCallAnswer);
          when(mock.getScreenSize()).thenAnswer(systemCallAnswer);
          Mockito.doAnswer(systemCallAnswer).when(mock).setMousePosition(anyInt(), anyInt());
        });

    ) {
      factory = new MouseMotionFactory();
      factory.setNature(new ScreenAdjustedNature(new Dimension(100, 100), new Point(50, 50)));
      ((DefaultOvershootManager) factory.getOvershootManager()).setOvershoots(0);
      factory.setNoiseProvider(new MockNoiseProvider());
      factory.setDeviationProvider(new MockDeviationProvider());
      factory.setSpeedManager(new MockSpeedManager());
      factory.setRandom(new MockRandom(new double[]{0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1}));
    }
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
