package com.github.joonasvali.naturalmouse.support.mousemotion;


import com.github.joonasvali.naturalmouse.support.Flow;

public class Movement {
  public final int destX;
  public final int destY;
  public final double distance;
  public final int xDistance;
  public final int yDistance;
  public final long time;
  public final Flow flow;

  public Movement(int destX, int destY, double distance, int xDistance, int yDistance, long time, Flow flow) {
    this.destX = destX;
    this.destY = destY;
    this.distance = distance;
    this.xDistance = xDistance;
    this.yDistance = yDistance;
    this.time = time;
    this.flow = flow;
  }

  @Override
  public String toString() {
    return "Movement{" +
        "destX=" + destX +
        ", destY=" + destY +
        ", xDistance=" + xDistance +
        ", yDistance=" + yDistance +
        ", time=" + time +
        '}';
  }
}