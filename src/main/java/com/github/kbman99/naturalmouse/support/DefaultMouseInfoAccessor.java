package com.github.kbman99.naturalmouse.support;

import com.github.kbman99.naturalmouse.api.MouseInfoAccessor;

import java.awt.*;

public class DefaultMouseInfoAccessor implements MouseInfoAccessor {

  @Override
  public Point getMousePosition() {
    return MouseInfo.getPointerInfo().getLocation();
  }
}
