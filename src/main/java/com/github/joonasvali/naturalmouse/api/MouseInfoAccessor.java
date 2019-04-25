package com.github.joonasvali.naturalmouse.api;

import java.awt.*;

/**
 * Abstraction for getting mouse position.
 */
public interface MouseInfoAccessor {
  /**
   * Get the current mouse position.
   * NB, for optimization reasons this method might return the same Point instance, but is not quaranteed to.
   * It is recommended not to save this Point anywhere as it may or may not change its coordinates.
   * @return the current mouse position
   */

  Point getMousePosition();
}
