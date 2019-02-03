package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.DeviationProvider;
import com.github.joonasvali.naturalmouse.api.MouseInfoAccessor;
import com.github.joonasvali.naturalmouse.api.NoiseProvider;
import com.github.joonasvali.naturalmouse.api.SpeedManager;
import com.github.joonasvali.naturalmouse.api.SystemCalls;

public class MouseMotionNature {
  private int minDistanceForOvershoots;
  private double timeToStepsDivider;
  private int minSteps;
  private long minOvershootMovementMs;
  private int effectFadeSteps;
  private int reactionTimeBaseMs;
  private int reactionTimeVariationMs;
  private int overshoots;
  private double overshootRandomModifierDivider;
  private double overshootSpeedupDivider;
  private DeviationProvider deviationProvider;
  private NoiseProvider noiseProvider;
  private MouseInfoAccessor mouseInfo;
  private SystemCalls systemCalls;
  private SpeedManager speedManager;

  /**
   * TBD
   * @return
   */
  public int getMinDistanceForOvershoots() {
    return minDistanceForOvershoots;
  }

  /**
   * TBD
   * @param minDistanceForOvershoots
   */
  public void setMinDistanceForOvershoots(int minDistanceForOvershoots) {
    this.minDistanceForOvershoots = minDistanceForOvershoots;
  }

  /**
   * TBD
   * @return
   */
  public double getTimeToStepsDivider() {
    return timeToStepsDivider;
  }

  /**
   * TBD
   * @param timeToStepsDivider
   */
  public void setTimeToStepsDivider(double timeToStepsDivider) {
    this.timeToStepsDivider = timeToStepsDivider;
  }

  /**
   * TBD
   * @return
   */
  public int getMinSteps() {
    return minSteps;
  }

  /**
   * TBD
   * @param minSteps
   */
  public void setMinSteps(int minSteps) {
    this.minSteps = minSteps;
  }

  /**
   * TBD
   * @return
   */
  public double getOvershootSpeedupDivider() {
    return overshootSpeedupDivider;
  }

  /**
   * TBD
   * @param overshootSpeedupDivider
   */
  public void setOvershootSpeedupDivider(double overshootSpeedupDivider) {
    this.overshootSpeedupDivider = overshootSpeedupDivider;
  }

  /**
   * TBD
   * @return
   */
  public long getMinOvershootMovementMs() {
    return minOvershootMovementMs;
  }

  /**
   * TBD
   * @param minOvershootMovementMs
   */
  public void setMinOvershootMovementMs(int minOvershootMovementMs) {
    this.minOvershootMovementMs = minOvershootMovementMs;
  }

  /**
   * TBD
   * @return
   */
  public double getOvershootRandomModifierDivider() {
    return overshootRandomModifierDivider;
  }

  /**
   * TBD
   * @param overshootRandomModifierDivider
   */
  public void setOvershootRandomModifierDivider(int overshootRandomModifierDivider) {
    this.overshootRandomModifierDivider = overshootRandomModifierDivider;
  }

  /**
   * TBD
   * @return
   */
  public int getEffectFadeSteps() {
    return effectFadeSteps;
  }

  /**
   * TBD
   * @param effectFadeSteps
   */
  public void setEffectFadeSteps(int effectFadeSteps) {
    this.effectFadeSteps = effectFadeSteps;
  }

  /**
   * TBD
   * @return
   */
  public int getReactionTimeBaseMs() {
    return reactionTimeBaseMs;
  }

  /**
   * TBD
   * @param reactionTimeBaseMs
   */
  public void setReactionTimeBaseMs(int reactionTimeBaseMs) {
    this.reactionTimeBaseMs = reactionTimeBaseMs;
  }

  /**
   * TBD
   * @return
   */
  public int getReactionTimeVariationMs() {
    return reactionTimeVariationMs;
  }

  /**
   * TBD
   * @param reactionTimeVariationMs
   */
  public void setReactionTimeVariationMs(int reactionTimeVariationMs) {
    this.reactionTimeVariationMs = reactionTimeVariationMs;
  }

  /**
   * Get the provider which is used to define how the MouseMotion trajectory is being deviated or arced
   *
   * @return the provider
   */
  public DeviationProvider getDeviationProvider() {
    return deviationProvider;
  }

  /**
   * Set the provider which is used to define how the MouseMotion trajectory is being deviated or arced.
   * Alters the underlying nature instance in this factory.
   *
   * @param deviationProvider the provider
   */
  public void setDeviationProvider(DeviationProvider deviationProvider) {
    this.deviationProvider = deviationProvider;
  }

  /**
   * Get the provider which is used to make random mistakes in the trajectory of the moving mouse
   *
   * @return the provider
   */
  public NoiseProvider getNoiseProvider() {
    return noiseProvider;
  }

  /**
   * set the provider which is used to make random mistakes in the trajectory of the moving mouse.
   * Alters the underlying nature instance in this factory.
   *
   * @param noiseProvider the provider
   */
  public void setNoiseProvider(NoiseProvider noiseProvider) {
    this.noiseProvider = noiseProvider;
  }

  /**
   * Get the accessor object, which MouseMotion uses to detect the position of mouse on screen.
   *
   * @return the accessor
   */
  public MouseInfoAccessor getMouseInfo() {
    return mouseInfo;
  }

  /**
   * Set the accessor object, which MouseMotion uses to detect the position of mouse on screen.
   *
   * @param mouseInfo
   */
  public void setMouseInfo(MouseInfoAccessor mouseInfo) {
    this.mouseInfo = mouseInfo;
  }

  /**
   * Get a system call interface, which MouseMotion uses internally
   *
   * @return the interface
   */
  public SystemCalls getSystemCalls() {
    return systemCalls;
  }

  /**
   * Set a system call interface, which MouseMotion uses internally.
   *
   * @param systemCalls the interface
   */
  public void setSystemCalls(SystemCalls systemCalls) {
    this.systemCalls = systemCalls;
  }

  /**
   * Get the speed manager. SpeedManager controls how long does it take to complete a movement and within that
   * time how slow or fast the cursor is moving at a particular moment, the flow of movement.
   * @return the SpeedManager
   */
  public SpeedManager getSpeedManager() {
    return speedManager;
  }

  /**
   * Sets the speed manager. SpeedManager controls how long does it take to complete a movement and within that
   * time how slow or fast the cursor is moving at a particular moment, the flow of movement.
   *
   * @param speedManager the SpeedManager
   */
  public void setSpeedManager(SpeedManager speedManager) {
    this.speedManager = speedManager;
  }

  /**
   * Get the maximum amount of overshoots the cursor does before reaching its final destination.
   * Overshoots provide a realistic way to simulate user trying to reach the destination with mouse, but miss.
   * This only happens when mouse is far away enough from the destination, then random points around the destination
   * are produced which will be hit before the mouse hits the real destination. If mouse happens to accidentally hit
   * the target close enough, then overshooting is cancelled and real destination will get reached.
   * @return the number of maximum overshoots used
   */
  public int getOvershoots() {
    return overshoots;
  }

  /**
   * Set the maximum amount of overshoots the cursor does before reaching its final destination.
   * Overshoots provide a realistic way to simulate user trying to reach the destination with mouse, but miss.
   * This only happens when mouse is far away enough from the destination, then random points around the destination
   * are produced which will be hit before the mouse hits the real destination. If mouse happens to accidentally hit
   * the target close enough, then overshooting is cancelled and real destination will get reached.
   * Alters the underlying nature instance in this factory.
   * @param overshoots the number of maximum overshoots used
   */
  public void setOvershoots(int overshoots) {
    this.overshoots = overshoots;
  }
}
