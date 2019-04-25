package com.github.joonasvali.naturalmouse.tools;

import com.github.joonasvali.naturalmouse.api.MouseInfoAccessor;
import com.github.joonasvali.naturalmouse.api.SystemCalls;
import com.github.joonasvali.naturalmouse.support.DefaultMouseInfoAccessor;
import com.github.joonasvali.naturalmouse.support.DefaultSystemCalls;

import java.awt.*;

public class SystemDiagnosis {

  public static void main(String[] args) {
    new SystemDiagnosis().validateMouseMovement();
  }

  public void validateMouseMovement() {
    try {
      Robot robot = new Robot();
      validateMouseMovement(new DefaultSystemCalls(robot), new DefaultMouseInfoAccessor());
    } catch (AWTException e) {
      throw new RuntimeException(e);
    }
  }

  public void validateMouseMovement(SystemCalls system, MouseInfoAccessor accessor) {
    Dimension dimension = system.getScreenSize();
    for (int y = 0; y < dimension.height; y += 50) {
      for (int x = 0; x < dimension.width; x += 50) {
        system.setMousePosition(x, y);

        try {
          Thread.sleep(1);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }

        Point p = accessor.getMousePosition();
        if (x != p.x || y != p.y) {
          throw new IllegalArgumentException(
              "Tried to move mouse to (" + x + ", " + y + "). Actually moved to (" + p.x + ", " + p.y + ")" +
              "This means NaturalMouseMotion is not able to work optimally on this system as the cursor move " +
              "calls may miss the target pixels on the screen."
          );
        }
      }
    }
  }
}
