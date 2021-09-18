package com.github.joonavali.naturalmouse.support.mousemotion;

import com.github.joonasvali.naturalmouse.support.DefaultOvershootManager;
import com.github.joonasvali.naturalmouse.support.Flow;
import com.github.joonavali.naturalmouse.testutils.MockRandom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Random;

public class DefaultOvershootManagerTest {
  @Test
  public void returnsSetOvershootNumber() {
    Random random = new MockRandom(new double[]{0.1, 0.2, 0.3, 0.4, 0.5});
    DefaultOvershootManager manager = new DefaultOvershootManager(random);

    int overshoots = manager.getOvershoots(new Flow(new double[]{100}), 200, 1000);
    Assertions.assertEquals(3, overshoots);

    manager.setOvershoots(10);
    overshoots = manager.getOvershoots(new Flow(new double[]{100}), 200, 1000);
    Assertions.assertEquals(10, overshoots);
  }

  @Test
  public void overshootSizeDecreasesWithOvershootsRemaining() {
    Point overshoot1;
    Point overshoot2;
    Point overshoot3;

    {
      Random random = new MockRandom(new double[]{0.1});
      DefaultOvershootManager manager = new DefaultOvershootManager(random);
      overshoot1 = manager.getOvershootAmount(1000, 500, 1000, 1);
    }

    {
      Random random = new MockRandom(new double[]{0.1});
      DefaultOvershootManager manager = new DefaultOvershootManager(random);
      overshoot2 = manager.getOvershootAmount(1000, 500, 1000, 2);
    }

    {
      Random random = new MockRandom(new double[]{0.1});
      DefaultOvershootManager manager = new DefaultOvershootManager(random);
      overshoot3 = manager.getOvershootAmount(1000, 500, 1000, 3);
    }

    Assertions.assertEquals(overshoot3.x, overshoot1.x * 3);
    Assertions.assertEquals(overshoot2.x, overshoot1.x * 2);
  }

  @Test
  public void nextMouseMovementTimeIsBasedOnCurrentMouseMovementMs() {
    Random random = new MockRandom(new double[]{0.1, 0.2, 0.3, 0.4, 0.5});
    DefaultOvershootManager manager = new DefaultOvershootManager(random);

    {
      // DEFAULT VALUE
      long nextTime = manager.deriveNextMouseMovementTimeMs(
          (long) (DefaultOvershootManager.OVERSHOOT_SPEEDUP_DIVIDER * 500), 3
      );
      Assertions.assertEquals(500, nextTime);
    }

    {
      manager.setOvershootSpeedupDivider(2);
      long nextTime = manager.deriveNextMouseMovementTimeMs(1000, 3);
      Assertions.assertEquals(500, nextTime);
    }

    {
      manager.setOvershootSpeedupDivider(4);
      long nextTime = manager.deriveNextMouseMovementTimeMs(1000, 3);
      Assertions.assertEquals(250, nextTime);
    }
  }

  @Test
  public void nextMouseMovementTimeHasMinValue() {
    Random random = new MockRandom(new double[]{0.1, 0.2, 0.3, 0.4, 0.5});
    DefaultOvershootManager manager = new DefaultOvershootManager(random);

    {
      manager.setOvershootSpeedupDivider(2);
      manager.setMinOvershootMovementMs(1500);
      long nextTime = manager.deriveNextMouseMovementTimeMs(1000, 3);
      Assertions.assertEquals(1500, nextTime);
    }
  }
}
