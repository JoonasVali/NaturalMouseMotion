package com.github.joonasvali.naturalmouse.util;

import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import com.github.joonasvali.naturalmouse.support.DefaultNoiseProvider;
import com.github.joonasvali.naturalmouse.support.DefaultSpeedManager;
import com.github.joonasvali.naturalmouse.support.DoublePoint;
import com.github.joonasvali.naturalmouse.support.Flow;
import com.github.joonasvali.naturalmouse.support.SinusoidalDeviationProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FactoryTemplates {

  /**
   * <h1>Stereotypical granny using a computer with non-optical mouse from the 90s.</h1>
   * Low speed, variating flow, lots of noise in movement.
   * @return the factory
   */
  public static MouseMotionFactory createGrannyMotionFactory() {
    MouseMotionFactory factory = new MouseMotionFactory();
    List<Flow> flows = new ArrayList<>();
    flows.add(new Flow(FlowTemplates.jaggedFlow()));
    flows.add(new Flow(FlowTemplates.random()));
    flows.add(new Flow(FlowTemplates.interruptedFlow()));
    flows.add(new Flow(FlowTemplates.stoppingFlow()));
    DefaultSpeedManager manager = new DefaultSpeedManager(flows);
    factory.setDeviationProvider(new SinusoidalDeviationProvider(9));
    factory.setNoiseProvider(new DefaultNoiseProvider(1.5));
    factory.getNature().setReactionTimeBaseMs(100);
    manager.setMouseMovementBaseTimeMs(1700);
    factory.setOvershoots(5);
    factory.setSpeedManager(manager);
    return factory;
  }

  /**
   * <h1>Robotic fluent movement.</h1>
   * Custom speed, constant movement, no mistakes, no overshoots.
   * @param motionTimeMs approximate time a movement takes
   * @return the factory
   */
  public static MouseMotionFactory createDemoRobotMotionFactory(long motionTimeMs) {
    MouseMotionFactory factory = new MouseMotionFactory();
    List<Flow> flows = new ArrayList<>();
    flows.add(new Flow(FlowTemplates.constantSpeed()));
    DefaultSpeedManager manager = new DefaultSpeedManager(flows);
    factory.setDeviationProvider((totalDistanceInPixels, completionFraction) -> DoublePoint.ZERO);
    factory.setNoiseProvider(((random, xStepSize, yStepSize) -> DoublePoint.ZERO));
    manager.setMouseMovementBaseTimeMs(motionTimeMs);
    factory.setOvershoots(0);
    factory.setSpeedManager(manager);
    return factory;
  }

  /**
   * <h1>Gamer with fast reflexes and quick mouse movements.</h1>
   * Quick movement, low noise, some deviation, lots of overshoots.
   * @return the factory
   */
  public static MouseMotionFactory createFastGamerMotionFactory() {
    MouseMotionFactory factory = new MouseMotionFactory();
    List<Flow> flows = new ArrayList<>(Arrays.asList(
        new Flow(FlowTemplates.variatingFlow()),
        new Flow(FlowTemplates.slowStartupFlow()),
        new Flow(FlowTemplates.slowStartup2Flow()),
        new Flow(FlowTemplates.jaggedFlow())
    ));
    DefaultSpeedManager manager = new DefaultSpeedManager(flows);
    factory.setDeviationProvider(new SinusoidalDeviationProvider(SinusoidalDeviationProvider.DEFAULT_SLOPE_DIVIDER));
    factory.setNoiseProvider(new DefaultNoiseProvider(DefaultNoiseProvider.DEFAULT_NOISINESS_DIVIDER));
    factory.getNature().setReactionTimeVariationMs(100);
    manager.setMouseMovementBaseTimeMs(350);
    factory.setOvershoots(4);
    factory.setSpeedManager(manager);
    return factory;
  }

  /**
   * <h1>Standard computer user with average speed and movement mistakes</h1>
   * medium noise, medium speed, medium noise and deviation.
   * @return the factory
   */
  public static MouseMotionFactory createStandardComputerUserMotionFactory() {
    MouseMotionFactory factory = new MouseMotionFactory();
    List<Flow> flows = new ArrayList<>(Arrays.asList(
        new Flow(FlowTemplates.constantSpeed()),
        new Flow(FlowTemplates.variatingFlow()),
        new Flow(FlowTemplates.interruptedFlow()),
        new Flow(FlowTemplates.slowStartupFlow()),
        new Flow(FlowTemplates.slowStartup2Flow()),
        new Flow(FlowTemplates.jaggedFlow()),
        new Flow(FlowTemplates.stoppingFlow())
    ));
    DefaultSpeedManager manager = new DefaultSpeedManager(flows);
    factory.setDeviationProvider(new SinusoidalDeviationProvider(SinusoidalDeviationProvider.DEFAULT_SLOPE_DIVIDER));
    factory.setNoiseProvider(new DefaultNoiseProvider(DefaultNoiseProvider.DEFAULT_NOISINESS_DIVIDER));
    manager.setMouseMovementBaseTimeMs(750);
    factory.setOvershoots(4);
    factory.setSpeedManager(manager);
    return factory;
  }
}
