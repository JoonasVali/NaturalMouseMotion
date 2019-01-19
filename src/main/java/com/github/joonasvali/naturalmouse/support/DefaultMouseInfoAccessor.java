package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.MouseInfoAccessor;

import java.awt.*;

public class DefaultMouseInfoAccessor implements MouseInfoAccessor {

  @Override
  public Point getMousePosition() {
    return MouseInfo.getPointerInfo().getLocation();
  }
}
