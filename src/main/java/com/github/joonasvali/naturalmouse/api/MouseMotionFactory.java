package com.github.joonasvali.naturalmouse.api;

import com.github.joonasvali.naturalmouse.support.DefaultMouseInfoAccessor;
import com.github.joonasvali.naturalmouse.support.DefaultNoiseProvider;
import com.github.joonasvali.naturalmouse.support.DefaultSpeedManager;
import com.github.joonasvali.naturalmouse.support.DefaultSystemCalls;
import com.github.joonasvali.naturalmouse.support.SinusoidalDeviationProvider;

import java.awt.*;
import java.util.Random;

import static com.github.joonasvali.naturalmouse.support.DefaultNoiseProvider.DEFAULT_CHANCE_OF_NOISE;
import static com.github.joonasvali.naturalmouse.support.DefaultNoiseProvider.DEFAULT_DISTANCE_DIVIDER;
import static com.github.joonasvali.naturalmouse.support.SinusoidalDeviationProvider.DEFAULT_SLOPE_DIVIDER;

/**
 * This class should be used for creating new MouseMotion-s
 * The default instance is available via getDefault(), but can create new instance via constructor.
 */
public class MouseMotionFactory {
  private static final MouseMotionFactory defaultFactory = new MouseMotionFactory();

  private SystemCalls systemCalls;
  private DeviationProvider deviationProvider = new SinusoidalDeviationProvider(DEFAULT_SLOPE_DIVIDER);
  private NoiseProvider noiseProvider = new DefaultNoiseProvider(DEFAULT_CHANCE_OF_NOISE, DEFAULT_DISTANCE_DIVIDER);
  private SpeedManager speedManager = new DefaultSpeedManager();

  private Random random = new Random();
  private MouseInfoAccessor mouseInfo = new DefaultMouseInfoAccessor();
  private int overshoots = 4;

  public MouseMotionFactory() {
    try {
      systemCalls = new DefaultSystemCalls(new Robot());
    } catch (AWTException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Builds the MouseMotion, which can be executed instantly or saved for later.
   *
   * @param xDest the end position x-coordinate for the mouse
   * @param yDest the end position y-coordinate for the mouse
   * @return the MouseMotion which can be executed instantly or saved for later. (Mouse will be moved from its
   * current position, not from the position where mouse was during building.)
   */
  public MouseMotion build(int xDest, int yDest) {
    return new MouseMotion(
        deviationProvider, noiseProvider, systemCalls, xDest, yDest, random, mouseInfo, speedManager, overshoots);
  }

  /**
   * Start moving the mouse to specified location. Blocks until done.
   * @param xDest the end position x-coordinate for the mouse
   * @param yDest the end position y-coordinate for the mouse
   * @throws InterruptedException if something interrupts the thread.
   */
  public void move(int xDest, int yDest) throws InterruptedException {
    build(xDest, yDest).move();
  }

  /**
   * Get the default factory implementation.
   * @return the factory
   */
  public static MouseMotionFactory getDefault() {
    return defaultFactory;
  }

  /**
   * Get a system call interface, which MouseMotion uses internally
   * @return the interface
   */
  public SystemCalls getSystemCalls() {
    return systemCalls;
  }

  /**
   * Set a system call interface, which MouseMotion uses internally
   * @param systemCalls the interface
   */
  public void setSystemCalls(SystemCalls systemCalls) {
    this.systemCalls = systemCalls;
  }

  /**
   * Get the provider which is used to define how the MouseMotion trajectory is being deviated or arced
   * @return the provider
   */
  public DeviationProvider getDeviationProvider() {
    return deviationProvider;
  }

  /**
   * Set the provider which is used to define how the MouseMotion trajectory is being deviated or arced
   * @param deviationProvider the provider
   */
  public void setDeviationProvider(DeviationProvider deviationProvider) {
    this.deviationProvider = deviationProvider;
  }

  /**
   * Get the provider which is used to make random mistakes in the trajectory of the moving mouse
   * @return the provider
   */
  public NoiseProvider getNoiseProvider() {
    return noiseProvider;
  }

  /**
   * set the provider which is used to make random mistakes in the trajectory of the moving mouse
   * @param noiseProvider the provider
   */
  public void setNoiseProvider(NoiseProvider noiseProvider) {
    this.noiseProvider = noiseProvider;
  }

  /**
   * Get the random used whenever randomized behavior is needed in MouseMotion
   * @return the random
   */
  public Random getRandom() {
    return random;
  }

  /**
   * Set the random used whenever randomized behavior is needed in MouseMotion
   * @param random the random
   */
  public void setRandom(Random random) {
    this.random = random;
  }

  /**
   * Get the accessor object, which MouseMotion uses to detect the position of mouse on screen.
   * @return the accessor
   */
  public MouseInfoAccessor getMouseInfo() {
    return mouseInfo;
  }

  /**
   * Set the accessor object, which MouseMotion uses to detect the position of mouse on screen.
   * @param mouseInfo
   */
  public void setMouseInfo(MouseInfoAccessor mouseInfo) {
    this.mouseInfo = mouseInfo;
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
   * the target close enough, then overshooting is cancelled and real destination will get reached.   *
   * @param overshoots the number of maximum overshoots used
   */
  public void setOvershoots(int overshoots) {
    this.overshoots = overshoots;
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
   * @param speedManager the SpeedManager
   */
  public void setSpeedManager(SpeedManager speedManager) {
    this.speedManager = speedManager;
  }
}
