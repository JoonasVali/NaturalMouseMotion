package com.github.joonasvali.naturalmouse.api;

import com.github.joonasvali.naturalmouse.support.Flow;

/**
 * SpeedManager controls how long does it take to complete a mouse movement and within that
 * time how slow or fast the cursor is moving at a particular moment, the flow.
 * Flow controls how jagged or smooth, accelerating or decelerating, the movement is.
 */
public interface SpeedManager {
  /**
   * Get the Flow object which matches the provided distance and expected time planned for cursor movement.
   * @param distance the distance from where the cursor is now to the destination point
   * @param plannedMouseMovementTimeMs how long does it take to complete this cursor movement
   * @return the appropriate Flow object, where match is a SpeedManager implementation decision.
   */
  Flow getFlow(double distance, long plannedMouseMovementTimeMs);

  /**
   * Calculate the time which should be spent on moving cursor the given distance
   * @param distance the distance in pixels
   * @return the calculated time in ms.
   */
  long createMouseMovementTimeMs(double distance);
}
