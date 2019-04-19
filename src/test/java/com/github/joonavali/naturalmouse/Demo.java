package com.github.joonavali.naturalmouse;

import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import com.github.joonasvali.naturalmouse.util.FactoryTemplates;

import java.awt.*;

public class Demo {
  public static void main(String[] args) throws InterruptedException, AWTException {

    int x = 100;
    int y = 300;

    MouseMotionFactory factory = FactoryTemplates.createFastGamerMotionFactory();
    while(true) {
      try {
        x = (int) (Math.random() * 1000);
        y = (int) (Math.random() * 1000);
        factory.move(x, y);
        System.out.println("End of movement!");
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
