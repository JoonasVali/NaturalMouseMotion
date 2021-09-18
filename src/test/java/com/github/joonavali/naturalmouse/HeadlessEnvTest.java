package com.github.joonavali.naturalmouse;

import com.github.joonasvali.naturalmouse.api.MouseInfoAccessor;
import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import com.github.joonasvali.naturalmouse.api.SystemCalls;
import com.github.joonasvali.naturalmouse.support.DefaultMouseMotionNature;
import com.github.joonasvali.naturalmouse.support.MouseMotionNature;
import com.github.joonasvali.naturalmouse.util.FactoryTemplates;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import java.awt.*;
import java.lang.reflect.Field;

@Isolated
public class HeadlessEnvTest {
  @BeforeEach
  public void setUpHeadlessMode() throws NoSuchFieldException, IllegalAccessException {
    // It's cached internally, so just changing the headless property is not enough after initial loading.
    System.setProperty("java.awt.headless", "true");
    Field f = GraphicsEnvironment.class.getDeclaredField("headless");
    f.setAccessible(true);
    f.set(null, Boolean.TRUE);
  }

  @AfterEach
  public void tearDownHeadlessMode() throws NoSuchFieldException, IllegalAccessException {
    // It's cached internally, so just changing the headless property is not enough after initial loading.
    System.setProperty("java.awt.headless", "false");
    Field f = GraphicsEnvironment.class.getDeclaredField("headless");
    f.setAccessible(true);
    f.set(null, Boolean.FALSE);
  }

  private final Point mousePos = new Point(5, 5);
  SystemCalls systemCallsWithNoRobot = new SystemCalls() {
    @Override
    public long currentTimeMillis() {
      return System.currentTimeMillis();
    }

    @Override
    public void sleep(long time) throws InterruptedException {
      // No-op
    }

    @Override
    public Dimension getScreenSize() {
      return new Dimension(10, 10);
    }

    @Override
    public void setMousePosition(int x, int y) {
      mousePos.x = x;
      mousePos.y = y;
    }
  };

  MouseInfoAccessor mouseInfoAccessor = () -> mousePos;

  @Test
  public void isHeadlessEnv() {
    Assertions.assertTrue(GraphicsEnvironment.isHeadless());
  }

  @Test
  public void testCreatingMouseMotionFactoryWithSystemCallOverrides() throws InterruptedException {
    MouseMotionNature nature = new DefaultMouseMotionNature(systemCallsWithNoRobot, mouseInfoAccessor);
    MouseMotionFactory factory = new MouseMotionFactory(nature);
    factory.move(1, 1);
  }

  @Test
  public void testUsingFactoryTemplatesWithSystemCallOverrides() throws InterruptedException {
    MouseMotionNature nature = new DefaultMouseMotionNature(systemCallsWithNoRobot, mouseInfoAccessor);
    MouseMotionFactory factory = FactoryTemplates.createDemoRobotMotionFactory(nature, 10);
    factory.move(1, 1);
    factory = FactoryTemplates.createAverageComputerUserMotionFactory(nature);
    factory.move(2, 2);
  }

  @Test
  public void testUsingDefaultFactoryThrowsError() throws InterruptedException {
    RuntimeException exception = Assertions.assertThrows(RuntimeException.class, MouseMotionFactory::getDefault);
    Assertions.assertEquals(AWTException.class, exception.getCause().getClass());
  }
}
