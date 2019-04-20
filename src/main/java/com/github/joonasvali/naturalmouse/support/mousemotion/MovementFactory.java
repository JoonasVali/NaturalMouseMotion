package com.github.joonasvali.naturalmouse.support.mousemotion;

import com.github.joonasvali.naturalmouse.api.OvershootManager;
import com.github.joonasvali.naturalmouse.api.SpeedManager;
import com.github.joonasvali.naturalmouse.support.Flow;
import com.github.joonasvali.naturalmouse.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Iterator;

public class MovementFactory {
  private static final Logger log = LoggerFactory.getLogger(MovementFactory.class);
  private final int xDest;
  private final int yDest;
  private final SpeedManager speedManager;
  private final OvershootManager overshootManager;
  private final Dimension screenSize;

  public MovementFactory(int xDest, int yDest, SpeedManager speedManager,
                         OvershootManager overshootManager, Dimension screenSize) {
    this.xDest = xDest;
    this.yDest = yDest;
    this.speedManager = speedManager;
    this.overshootManager = overshootManager;
    this.screenSize = screenSize;
  }

  public ArrayDeque<Movement> createMovements(Point currentMousePosition) {
    ArrayDeque<Movement> movements = new ArrayDeque<>();
    int lastMousePositionX = currentMousePosition.x;
    int lastMousePositionY = currentMousePosition.y;
    int xDistance = xDest - lastMousePositionX;
    int yDistance = yDest - lastMousePositionY;

    double initialDistance = Math.hypot(xDistance, yDistance);
    Pair<Flow, Long> flowTime = speedManager.getFlowWithTime(initialDistance);
    Flow flow = flowTime.x;
    long mouseMovementMs = flowTime.y;
    int overshoots = overshootManager.getOvershoots(flow, mouseMovementMs, initialDistance);

    if (overshoots == 0) {
      log.debug("No overshoots for movement from ({}, {}) -> ({}, {})", currentMousePosition.x, currentMousePosition.y, xDest, yDest);
      movements.add(new Movement(xDest, yDest, initialDistance, xDistance, yDistance, mouseMovementMs, flow));
      return movements;
    }

    for (int i = overshoots; i > 0; i--) {
      Point overshoot = overshootManager.getOvershootAmount(
          xDest - lastMousePositionX, yDest - lastMousePositionY, mouseMovementMs, i
      );
      int currentDestinationX = limitByScreenWidth(xDest + overshoot.x);
      int currentDestinationY = limitByScreenHeight(yDest + overshoot.y);
      xDistance = currentDestinationX - lastMousePositionX;
      yDistance = currentDestinationY - lastMousePositionY;
      double distance = Math.hypot(xDistance, yDistance);
      flow = speedManager.getFlowWithTime(distance).x;
      movements.add(
          new Movement(currentDestinationX, currentDestinationY, distance, xDistance, yDistance, mouseMovementMs, flow)
      );
      lastMousePositionX = currentDestinationX;
      lastMousePositionY = currentDestinationY;
      // Apply for the next overshoot if exists.
      mouseMovementMs = overshootManager.deriveNextMouseMovementTimeMs(mouseMovementMs, i - 1);
    }

    Iterator<Movement> it = movements.descendingIterator();

    boolean remove = true;
    // Remove overshoots from the end, which are matching the final destination, but keep those in middle of motion.
    while (it.hasNext() && remove) {
      Movement movement = it.next();
      if (movement.destX == xDest && movement.destY == yDest) {
        lastMousePositionX = movement.destX - movement.xDistance;
        lastMousePositionY = movement.destY - movement.yDistance;
        log.trace("Pruning 0-overshoot movement (Movement to target) from the end. " + movement);
        it.remove();
      } else {
        remove = false;
      }
    }

    xDistance = xDest - lastMousePositionX;
    yDistance = yDest - lastMousePositionY;
    double distance = Math.hypot(xDistance, yDistance);
    Pair<Flow, Long> movementToTargetFlowTime = speedManager.getFlowWithTime(distance);
    long finalMovementTime = overshootManager.deriveNextMouseMovementTimeMs(movementToTargetFlowTime.y, 0);
    Movement finalMove = new Movement(
        xDest, yDest, distance, xDistance, yDistance, finalMovementTime, movementToTargetFlowTime.x
    );
    movements.add(finalMove);

    log.debug("{} movements returned for move ({}, {}) -> ({}, {})", movements.size(), currentMousePosition.x, currentMousePosition.y, xDest, yDest);
    log.trace("Movements are: {} ", movements);

    return movements;
  }

  private int limitByScreenWidth(int value) {
    return Math.max(0, Math.min(screenSize.width - 1, value));
  }

  private int limitByScreenHeight(int value) {
    return Math.max(0, Math.min(screenSize.height - 1, value));
  }


}
