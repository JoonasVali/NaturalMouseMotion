package com.github.kbman99.naturalmouse.support;

import com.github.kbman99.naturalmouse.api.MouseInfoAccessor;
import com.github.kbman99.naturalmouse.api.SystemCalls;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * This nature translates mouse coordinates to specified offset and screen dimension.
 * Internally it wraps the SystemCalls and MouseInfoAccessor in proxies which handle the translations.
 */
public class ScreenAdjustedNature extends DefaultMouseMotionNature {
  private final Point offset;
  private final Dimension screenSize;
  private Point mousePosition = new Point(0, 0);

  public ScreenAdjustedNature(int x, int y, int x2, int y2) {
    this(new Dimension(x2 - x, y2 - y), new Point(x, y));
    if (y2 <= y || x2 <= x) {
      throw new IllegalArgumentException("Ind range " + x + " " + y + " " + x2 + " " + y2);
    }
  }

  public ScreenAdjustedNature(Dimension screenSize, Point mouseOffset) {
    this.screenSize = screenSize;
    this.offset = mouseOffset;
  }

  @Override
  public void setMouseInfo(MouseInfoAccessor mouseInfo) {
    super.setMouseInfo(new ProxyMouseInfo(mouseInfo));
  }

  @Override
  public void setSystemCalls(SystemCalls systemCalls) {
    super.setSystemCalls(new ProxySystemCalls(systemCalls));
  }

  private class ProxyMouseInfo implements MouseInfoAccessor {
    private final MouseInfoAccessor underlying;

    public ProxyMouseInfo(MouseInfoAccessor underlying) {
      this.underlying = underlying;
    }

    // This implementation reuses the point.
    private final Point p = new Point();

    @Override
    public Point getMousePosition() {
      Point realPointer = underlying.getMousePosition();
      p.setLocation(realPointer.x - offset.x, realPointer.y - offset.y);
      return p;
    }
  }

  private class ProxySystemCalls implements SystemCalls {
    private final SystemCalls underlying;

    public ProxySystemCalls(SystemCalls underlying) {
      this.underlying = underlying;
    }

    @Override
    public long currentTimeMillis() {
      return underlying.currentTimeMillis();
    }

    @Override
    public void sleep(long time) throws InterruptedException {
      underlying.sleep(time);
    }

    @Override
    public Dimension getScreenSize() {
      return screenSize;
    }

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
}
