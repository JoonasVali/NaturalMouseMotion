package com.github.joonasvali.naturalmouse.api;

import java.util.Random;

import com.github.joonasvali.naturalmouse.support.DefaultMouseMotionNature;
import com.github.joonasvali.naturalmouse.support.MouseMotionNature;

/**
 * This class should be used for creating new MouseMotion-s The default instance
 * is available via getDefault(), but can create new instance via constructor.
 */
public class MouseMotionFactory {
	private static MouseMotionFactory defaultFactory;
	private MouseMotionNature nature;
	private Random random = new Random();

	public MouseMotionFactory(final MouseMotionNature nature) {
		this.nature = nature;
	}

	public MouseMotionFactory() {
		this(new DefaultMouseMotionNature());
	}

	/**
	 * Builds the MouseMotion, which can be executed instantly or saved for later.
	 *
	 * @param xDest the end position x-coordinate for the mouse
	 * @param yDest the end position y-coordinate for the mouse
	 * @return the MouseMotion which can be executed instantly or saved for later.
	 *         (Mouse will be moved from its current position, not from the position
	 *         where mouse was during building.)
	 */
	public MouseMotion build(final int xDest, final int yDest) {
		return new MouseMotion(nature, random, xDest, yDest);
	}

	/**
	 * Start moving the mouse to specified location. Blocks until done.
	 *
	 * @param xDest the end position x-coordinate for the mouse
	 * @param yDest the end position y-coordinate for the mouse
	 * @throws InterruptedException if something interrupts the thread.
	 */
	public void move(final int xDest, final int yDest) throws InterruptedException {
		build(xDest, yDest).move();
	}

	/**
	 * Get the default factory implementation.
	 *
	 * @return the factory
	 */
	public static MouseMotionFactory getDefault() {
		if (defaultFactory == null) {
			defaultFactory = new MouseMotionFactory();
		}
		return defaultFactory;
	}

	/**
	 * see {@link MouseMotionNature#getSystemCalls()}
	 *
	 * @return the systemcalls
	 */
	public SystemCalls getSystemCalls() {
		return nature.getSystemCalls();
	}

	/**
	 * see {@link MouseMotionNature#setSystemCalls(SystemCalls)}
	 *
	 * @param systemCalls the systemcalls
	 */
	public void setSystemCalls(final SystemCalls systemCalls) {
		nature.setSystemCalls(systemCalls);
	}

	/**
	 * see {@link MouseMotionNature#getDeviationProvider()}
	 *
	 * @return the deviation provider
	 */
	public DeviationProvider getDeviationProvider() {
		return nature.getDeviationProvider();
	}

	/**
	 * see {@link MouseMotionNature#setDeviationProvider(DeviationProvider)}
	 *
	 * @param deviationProvider the deviation provider
	 */
	public void setDeviationProvider(final DeviationProvider deviationProvider) {
		nature.setDeviationProvider(deviationProvider);
	}

	/**
	 * see {@link MouseMotionNature#getNoiseProvider()}
	 *
	 * @return the noise provider
	 */
	public NoiseProvider getNoiseProvider() {
		return nature.getNoiseProvider();
	}

	/**
	 * see {@link MouseMotionNature#setNoiseProvider(NoiseProvider)}}
	 *
	 * @param noiseProvider the noise provider
	 */
	public void setNoiseProvider(final NoiseProvider noiseProvider) {
		nature.setNoiseProvider(noiseProvider);
	}

	/**
	 * Get the random used whenever randomized behavior is needed in MouseMotion
	 *
	 * @return the random
	 */
	public Random getRandom() {
		return random;
	}

	/**
	 * Set the random used whenever randomized behavior is needed in MouseMotion
	 *
	 * @param random the random
	 */
	public void setRandom(final Random random) {
		this.random = random;
	}

	/**
	 * see {@link MouseMotionNature#getMouseInfo()}
	 *
	 * @return the mouseInfo
	 */
	public MouseInfoAccessor getMouseInfo() {
		return nature.getMouseInfo();
	}

	/**
	 * see {@link MouseMotionNature#setMouseInfo(MouseInfoAccessor)}
	 *
	 * @param mouseInfo the mouseInfo
	 */
	public void setMouseInfo(final MouseInfoAccessor mouseInfo) {
		nature.setMouseInfo(mouseInfo);
	}

	/**
	 * see {@link MouseMotionNature#getSpeedManager()}
	 *
	 * @return the manager
	 */
	public SpeedManager getSpeedManager() {
		return nature.getSpeedManager();
	}

	/**
	 * see {@link MouseMotionNature#setSpeedManager(SpeedManager)}
	 *
	 * @param speedManager the manager
	 */
	public void setSpeedManager(final SpeedManager speedManager) {
		nature.setSpeedManager(speedManager);
	}

	/**
	 * The Nature of mousemotion covers all aspects how the mouse is moved.
	 *
	 * @return the nature
	 */
	public MouseMotionNature getNature() {
		return nature;
	}

	/**
	 * The Nature of mousemotion covers all aspects how the mouse is moved.
	 *
	 * @param nature the new nature
	 */
	public void setNature(final MouseMotionNature nature) {
		this.nature = nature;
	}

	/**
	 * see {@link MouseMotionNature#setOvershootManager(OvershootManager)}
	 *
	 * @param manager the manager
	 */
	public void setOvershootManager(final OvershootManager manager) {
		nature.setOvershootManager(manager);
	}

	/**
	 * see {@link MouseMotionNature#getOvershootManager()}
	 *
	 * @return the manager
	 */
	public OvershootManager getOvershootManager() {
		return nature.getOvershootManager();
	}
}
