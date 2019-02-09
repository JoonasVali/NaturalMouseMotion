package com.github.joonasvali.naturalmouse.api;

import com.github.joonasvali.naturalmouse.support.Flow;

import java.awt.*;

public interface OvershootManager {
  /**
   * Get the maximum amount of overshoots the cursor does before reaching its final destination.
   * Overshoots provide a realistic way to simulate user trying to reach the destination with mouse, but miss.
   * This only happens when mouse is far away enough from the destination, then random points around the destination
   * are produced which will be hit before the mouse hits the real destination. If mouse happens to accidentally hit
   * the target close enough, then overshooting is cancelled and real destination will get reached.
   * @return the number of maximum overshoots used or 0 if no overshoots
   */
  int getOvershoots(Flow flow, long mouseMovementMs, double distance);

  Point getOvershootAmount(double distanceToRealTargetX, double distanceToRealTargetY, long mouseMovementMs, int overshootsRemaining);

  long deriveNextMouseMovementTimeMs(long mouseMovementMs, int overshootsRemaining);
}
