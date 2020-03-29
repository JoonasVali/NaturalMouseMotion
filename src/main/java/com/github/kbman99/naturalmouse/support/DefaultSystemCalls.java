package com.github.kbman99.naturalmouse.support;

import com.github.kbman99.naturalmouse.api.SystemCalls;

import java.awt.*;
import java.awt.event.MouseEvent;

public class DefaultSystemCalls implements SystemCalls {
  private final Robot robot;
  private Point mousePosition;

  public DefaultSystemCalls(Robot robot, Point mousePosition) {
    this.robot = robot;
    this.mousePosition = mousePosition;
  }

  @Override
  public long currentTimeMillis() {
    return System.currentTimeMillis();
  }

  @Override
  public void sleep(long time) throws InterruptedException {
    Thread.sleep(time);
  }

  @Override
  public Dimension getScreenSize() {
    return Toolkit.getDefaultToolkit().getScreenSize();
  }

  /**
   * <p>Moves the mouse to specified pixel using the provided Robot.</p>
   *
   * <p>It seems there is a certain delay, measurable in less than milliseconds,
   * before the mouse actually ends up on the requested pixel when using a Robot class.
   * this usually isn't a problem, but when we ask the mouse position right after this call,
   * there's extremely low but real chance we get wrong information back. I didn't add sleep
   * here as it would cause overhead to sleep always, even when we don't instantly use
   * the mouse position, but just acknowledged the issue with this warning.
   * (Use fast unrestricted loop of Robot movement and checking the position after every move to invoke the issue.)</p>
   *
   * @param x the x-coordinate
   * @param y the y-coordinate
   */
  @Override
  public void setMousePosition(Component source, int x, int y) {
    MouseEvent mouseMoved =
            new MouseEvent(source, MouseEvent.MOUSE_MOVED, System.currentTimeMillis(), 0, x, y, 0, false, 0);
    source.dispatchEvent(mouseMoved);
    mousePosition = new Point(x, y);
  }

  @Override
  public Point getMousePosition() {
    return mousePosition;
  }
}
