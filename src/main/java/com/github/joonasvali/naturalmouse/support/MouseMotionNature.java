package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.DeviationProvider;
import com.github.joonasvali.naturalmouse.api.MouseInfoAccessor;
import com.github.joonasvali.naturalmouse.api.NoiseProvider;
import com.github.joonasvali.naturalmouse.api.OvershootManager;
import com.github.joonasvali.naturalmouse.api.SpeedManager;
import com.github.joonasvali.naturalmouse.api.SystemCalls;

public class MouseMotionNature {
  private double timeToStepsDivider;
  private int minSteps;

  private int effectFadeSteps;
  private int reactionTimeBaseMs;
  private int reactionTimeVariationMs;
  private DeviationProvider deviationProvider;
  private NoiseProvider noiseProvider;
  private OvershootManager overshootManager;
  private MouseInfoAccessor mouseInfo;
  private SystemCalls systemCalls;
  private SpeedManager speedManager;

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

  public OvershootManager getOvershootManager() {
    return overshootManager;
  }

  public void setOvershootManager(OvershootManager overshootManager) {
    this.overshootManager = overshootManager;
  }

}
