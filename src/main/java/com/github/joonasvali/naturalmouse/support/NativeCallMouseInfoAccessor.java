package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.MouseInfoAccessor;
import sun.awt.SunToolkit;

import java.awt.*;
import java.awt.peer.MouseInfoPeer;
import java.lang.reflect.Method;

/**
 * This is faster version of MouseInfoAccessor. Should be around 2x faster than
 * DefaultMouseInfoAccessor, because the latter also returns Device info
 * while we only care about position. This class also reuses the returned Point from
 * getMousePosition which is filled with the mouse data, so it doesn't create unnecessary temporary objects.
 *
 * Since this class uses internal API, it's experimental and
 * not guaranteed to work everywhere or all situations. Use with caution.
 * Generally DefaultMouseInfoAccessor should be preferred over this, unless faster version is required.
 */
public class NativeCallMouseInfoAccessor implements MouseInfoAccessor {
  private final MouseInfoPeer peer;
  private final Point p = new Point(0, 0);

  public NativeCallMouseInfoAccessor() {
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    MouseInfoPeer mp;
    try {
      Method method = SunToolkit.class.getDeclaredMethod("getMouseInfoPeer");
      method.setAccessible(true);
      mp = (MouseInfoPeer) method.invoke(toolkit);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    peer = mp;
  }

  @Override
  public Point getMousePosition() {
    peer.fillPointWithCoords(p);
    return p;
  }
}
