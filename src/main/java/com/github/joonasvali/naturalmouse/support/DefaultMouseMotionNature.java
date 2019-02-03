package com.github.joonasvali.naturalmouse.support;

import java.awt.*;

import static com.github.joonasvali.naturalmouse.support.DefaultNoiseProvider.DEFAULT_NOISINESS_DIVIDER;
import static com.github.joonasvali.naturalmouse.support.SinusoidalDeviationProvider.DEFAULT_SLOPE_DIVIDER;

public class DefaultMouseMotionNature extends MouseMotionNature {

  private static final int MIN_DISTANCE_FOR_OVERSHOOTS = 50;
  private static final int TIME_TO_STEPS_DIVIDER = 8;
  private static final int MIN_STEPS = 10;
  private static final double OVERSHOOT_SPEEDUP_DIVIDER = 1.8;
  private static final int MIN_OVERSHOOT_MOVEMENT_MS = 40;
  private static final int OVERSHOOT_RANDOM_MODIFIER_DIVIDER = 20;
  private static final int EFFECT_FADE_STEPS = 15;
  private static final int REACTION_TIME_BASE_MS = 20;
  private static final int REACTION_TIME_VARIATION_MS = 120;

  public DefaultMouseMotionNature() {
    try {
      setSystemCalls(new DefaultSystemCalls(new Robot()));
    } catch (AWTException e) {
      throw new RuntimeException(e);
    }

    setDeviationProvider(new SinusoidalDeviationProvider(DEFAULT_SLOPE_DIVIDER));
    setNoiseProvider(new DefaultNoiseProvider(DEFAULT_NOISINESS_DIVIDER));
    setSpeedManager(new DefaultSpeedManager());
    setOvershoots(4);
    setEffectFadeSteps(EFFECT_FADE_STEPS);
    setMinDistanceForOvershoots(MIN_DISTANCE_FOR_OVERSHOOTS);
    setMinSteps(MIN_STEPS);
    setMinOvershootMovementMs(MIN_OVERSHOOT_MOVEMENT_MS);
    setMouseInfo(new DefaultMouseInfoAccessor());
    setReactionTimeBaseMs(REACTION_TIME_BASE_MS);
    setReactionTimeVariationMs(REACTION_TIME_VARIATION_MS);
    setOvershootRandomModifierDivider(OVERSHOOT_RANDOM_MODIFIER_DIVIDER);
    setOvershootSpeedupDivider(OVERSHOOT_SPEEDUP_DIVIDER);
    setTimeToStepsDivider(TIME_TO_STEPS_DIVIDER);
  }
}
