package com.github.joonasvali.naturalmouse.api;

import com.github.joonasvali.naturalmouse.support.Flow;
import com.github.joonasvali.naturalmouse.util.Pair;

/**
 * SpeedManager controls how long does it take to complete a mouse movement and within that
 * time how slow or fast the cursor is moving at a particular moment, the flow.
 * Flow controls how jagged or smooth, accelerating or decelerating, the movement is.
 */
public interface SpeedManager {

  /**
   * Get the SpeedFlow object, which contains Flow and planned time for mouse movement in ms.
   * @param distance the distance from where the cursor is now to the destination point   *
   * @return the SpeedFlow object, which details are a SpeedManager implementation decision.
   */
  Pair<Flow, Long> getFlowWithTime(double distance);
}
