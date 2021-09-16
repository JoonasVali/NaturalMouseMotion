package com.github.joonasvali.naturalmouse.support;

import com.github.joonasvali.naturalmouse.api.MouseInfoAccessor;
import com.github.joonasvali.naturalmouse.api.SystemCalls;

import java.awt.*;
import java.util.Random;

import static com.github.joonasvali.naturalmouse.support.DefaultNoiseProvider.DEFAULT_NOISINESS_DIVIDER;
import static com.github.joonasvali.naturalmouse.support.SinusoidalDeviationProvider.DEFAULT_SLOPE_DIVIDER;

public class DefaultMouseMotionNature extends MouseMotionNature {

  public static final int TIME_TO_STEPS_DIVIDER = 8;
  public static final int MIN_STEPS = 10;
  public static final int EFFECT_FADE_STEPS = 15;
  public static final int REACTION_TIME_BASE_MS = 20;
  public static final int REACTION_TIME_VARIATION_MS = 120;

  public DefaultMouseMotionNature(SystemCalls systemCalls) {
    this(systemCalls, new DefaultMouseInfoAccessor());
  }

  /**
   * Create a default mouse motion nature with custom system calls and mouse info.
   * Use this when running somewhere where java Robot does not work.
   * @param systemCalls custom system calls to be used in the nature
   * @param mouseInfoAccessor custom mouse info accessor to be used in the nature
   */
  public DefaultMouseMotionNature(SystemCalls systemCalls, MouseInfoAccessor mouseInfoAccessor) {
    setSystemCalls(systemCalls);
    setDeviationProvider(new SinusoidalDeviationProvider(DEFAULT_SLOPE_DIVIDER));
    setNoiseProvider(new DefaultNoiseProvider(DEFAULT_NOISINESS_DIVIDER));
    setSpeedManager(new DefaultSpeedManager());
    setOvershootManager(new DefaultOvershootManager(new Random()));
    setEffectFadeSteps(EFFECT_FADE_STEPS);
    setMinSteps(MIN_STEPS);
    setMouseInfo(mouseInfoAccessor);
    setReactionTimeBaseMs(REACTION_TIME_BASE_MS);
    setReactionTimeVariationMs(REACTION_TIME_VARIATION_MS);
    setTimeToStepsDivider(TIME_TO_STEPS_DIVIDER);
  }

  public DefaultMouseMotionNature() {
    this(new DefaultSystemCalls(getRobot()));
  }

  private static Robot getRobot() {
    try {
      return new Robot();
    } catch (final AWTException e) {
      throw new RuntimeException(e);
    }
  }
}
