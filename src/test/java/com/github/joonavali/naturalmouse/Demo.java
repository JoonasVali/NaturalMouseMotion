package com.github.joonavali.naturalmouse;

import com.github.joonasvali.naturalmouse.api.MouseMotion;
import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;

import java.awt.*;

public class Demo {

  public static void main(String[] args) throws AWTException, InterruptedException {
    MouseMotionFactory factory = new MouseMotionFactory();
    factory.setOvershoots(3);
    MouseMotion motion1 = factory.build(500, 500);
    MouseMotion motion2 = factory.build(1500, 800);
    for (int i = 0; i< 100; i++) {
      Thread.sleep(1000);
      motion1.move();
      Thread.sleep(1000);
      motion2.move();
    }
  }
}
