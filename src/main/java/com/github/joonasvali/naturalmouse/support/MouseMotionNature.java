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
   * Time to steps is how NaturalMouseMotion calculates how many locations need to be visited between
   * start and end point. More steps means more smooth movement. Thus increasing this divider means less
   * steps and decreasing means more steps.
   * @return the divider which is used to get amount of steps from the planned movement time
   */
  public double getTimeToStepsDivider() {
    return timeToStepsDivider;
  }

  /**
   * Time to steps is how NaturalMouseMotion calculates how many locations need to be visited between
   * start and end point. More steps means more smooth movement. Thus increasing this divider means less
   * steps and decreasing means more steps. The default value should be as smooth as needed for any real
   * purpose. So unless this really is the issue, you shouldn't touch this value.
   * @param timeToStepsDivider the divider which is used to get amount of steps from the planned movement time
   */
  public void setTimeToStepsDivider(double timeToStepsDivider) {
    this.timeToStepsDivider = timeToStepsDivider;
  }

  /**
   * Minimum amount of steps that is taken to reach the target, this is used when calculation otherwise would
   * lead to too few steps for smooth mouse movement, which can happen for very fast movements.
   * @return the minimal amount of steps used.
   */
  public int getMinSteps() {
    return minSteps;
  }

  /**
   * Minimum amount of steps that is taken to reach the target, this is used when calculation otherwise would
   * lead to too few steps for smooth mouse movement, which can happen for very fast movements.
   * The default value should cover your needs, usually no need to touch this.
   * @param minSteps the minimal amount of steps used
   */
  public void setMinSteps(int minSteps) {
    this.minSteps = minSteps;
  }

  /**
   * Effect fade decreases the noise and deviation effects linearly to 0 at the end of the mouse movement,
   * so mouse would end up in the intended target pixel even when noise or deviation would otherwise
   * add offset to mouse position.
   * @return the number of steps before last the effect starts to fade
   */
  public int getEffectFadeSteps() {
    return effectFadeSteps;
  }

  /**
   * Effect fade decreases the noise and deviation effects linearly to 0 at the end of the mouse movement,
   * so mouse would end up in the intended target pixel even when noise or deviation would otherwise
   * add offset to mouse position.
   * @param effectFadeSteps the number of steps before last the effect starts to fade
   */
  public void setEffectFadeSteps(int effectFadeSteps) {
    this.effectFadeSteps = effectFadeSteps;
  }

  /**
   * Get the minimal sleep time when overshoot or some other feature has caused mouse to miss the original target
   * to prepare for next attempt to move the mouse to target.
   * @return the sleep time
   */
  public int getReactionTimeBaseMs() {
    return reactionTimeBaseMs;
  }

  /**
   * Set the minimal sleep time when overshoot or some other feature has caused mouse to miss the original target
   * to prepare for next attempt to move the mouse to target.
   * @param reactionTimeBaseMs the sleep time
   */
  public void setReactionTimeBaseMs(int reactionTimeBaseMs) {
    this.reactionTimeBaseMs = reactionTimeBaseMs;
  }

  /**
   * Get the random sleep time when overshoot or some other feature has caused mouse to miss the original target
   * to prepare for next attempt to move the mouse to target. Random part of this is added to the reactionTimeBaseMs.
   * @return reactionTimeVariationMs the sleep time
   */
  public int getReactionTimeVariationMs() {
    return reactionTimeVariationMs;
  }

  /**
   * Set the random sleep time when overshoot or some other feature has caused mouse to miss the original target
   * to prepare for next attempt to move the mouse to target. Random part of this is added to the reactionTimeBaseMs.
   * @param reactionTimeVariationMs the sleep time
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
   * @param mouseInfo the accessor object
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
   * Get the manager that deals with overshoot properties.
   * Overshoots provide a realistic way to simulate user trying to reach the destination with mouse, but miss.
   * @return the manager
   */
  public OvershootManager getOvershootManager() {
    return overshootManager;
  }

  /**
   * Set the manager that deals with overshoot properties.
   * Overshoots provide a realistic way to simulate user trying to reach the destination with mouse, but miss.
   * @param overshootManager the manager
   */
  public void setOvershootManager(OvershootManager overshootManager) {
    this.overshootManager = overshootManager;
  }
}
