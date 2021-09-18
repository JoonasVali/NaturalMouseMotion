## NaturalMouseMotion ##

This library provides a way to move cursor to specified coordinates on screen reliably,
while being randomly arced to look like real hand moved it there by using a mouse.
The default settings should look good enough for most cases, but if user wishes, 
they can heavily customize the settings and implementations responsible for the trajectory 
of the cursor for specific use cases.

Some of the features NaturalMouseMotion contains:

  * **Deviation**: Deviation leads the mouse away from direct trajectory, creating and arc instead of straight line
  * **Noise**: Noise creates errors in the movement, this can simulate hand shakiness, someone using a non accurate mouse or bad surface under the mouse.
  * **Speed** and **flow**: Speed and flow are defining the progressing of the mouse at given time, for example it's possible that movement starts slow and then gains speed, or is just variating.
  * **Overshoots**: Overshoots happen if user is not 100% accurate with the mouse and hits an area next to the target instead, requiring to adjust the cursor to reach the actual target.
  * **Coordinate translation**: Coordinate translation allows to specify offset and dimensions to restrict a movement in a different area than the screen or in a virtual screen inside the real screen.

## Demonstration video (2.0.0): ## 
https://www.youtube.com/watch?v=CuG9LvQ0fdQ

## Maven: ##

```xml
<dependency>
  <groupId>com.github.joonasvali.naturalmouse</groupId>
  <artifactId>naturalmouse</artifactId>
  <version>2.0.3</version>
</dependency>
```

## Running: ##

NaturalMouseMotion needs at least **java 8** to run.

You start by creating a new `com.github.joonasvali.naturalmouse.api.MouseMotionFactory`
or using the default instance, by calling `MouseMotionFactory.getDefault();`

To build a `MouseMotion` instance you use the factory instance:
`MouseMotion motion = mouseMotionFactory.build(int xDest, int yDest);`

And the reusable `MouseMotion` can be run by using the
`motion.move();` blocking method, which then moves the cursor.
This instance can be saved to call later or repeatedly.

Shorthand method for moving the mouse can be also found from the factory,
so a quick access can be found by calling `MouseMotionFactory.getDefault().move(int xDest, int yDest);`

## Precooked templates ##

NaturalMouseMotion includes [FactoryTemplates](https://github.com/JoonasVali/NaturalMouseMotion/blob/master/src/main/java/com/github/joonasvali/naturalmouse/util/FactoryTemplates.java) class which contains a variety of behaviors for your simulations.

  * **GrannyMotionFactory** is a slow and clumsy mouse movement with a lot of noise.
  * **FastGamerMotionFactory** is a fast and persistent movement with low noise, simulating behavior of someone who knows where the click should land, like a professional gamer.
  * **AverageComputerUserMotionFactory** is a medium speed movement with average settings, slower than fast gamer, but much faster than granny motion.
  * **DemoRobotMotionFactory** is featureless constant speed robotic movement with no mistakes.
  
## Translating coordinates from real screen to virtual screen ##

Sometimes you might need to simulate the mouse movement somewhere else than in the real screen or keep the mouse in a smaller area in the screen. 
In this case it is possible to perform coordinate translation by limiting the screen to certain area only. The following code is restricting the 
movement to **500*500** box at coordinates **(50,50)** on screen.
```java
    MouseMotionFactory factory = new MouseMotionFactory();
    Dimension screenSize = new Dimension(500, 500);
    Point offset = new Point(50, 50);
    factory.setNature(new ScreenAdjustedNature(screenSize, offset));
    // Rest of the factory settings here
```

For example by calling `factory.move(10, 10)`, the mouse is going to be moved to **(60, 60)** on real screen as the offset is **(50, 50)**.
Attempting to move the mouse outside the **500x500** box with larger values is going to make it hit the imaginary wall at real screen coordinates **(550, 550)**.

NB: By setting the nature in the factory all previously set nature settings are lost because the instance is reassigned, which means setting nature should be done 
as a first thing in the factory, before any other settings are touched (as the settings are stored in the nature).

## How do I get a mouse that works on multiple screens?

You use the previously mentioned `ScreenAdjustedNature` and configure it to extend the main screen area to the side which the other screen resides.

Extending main screen to left
```java
    // How to extend screen to left. This means your main monitor is in the right and additional screen is left
    // of the main screen.
    Dimension screenOnTheLeft = new Dimension(2000, 1080);
    Dimension mainScreen = new Dimension(2000, 1080);

    ScreenAdjustedNature nature = new ScreenAdjustedNature(
        // The new dimension of the screen is a sum of both screen dimensions:
        new Dimension(screenOnTheLeft.width + mainScreen.width, bothScreenHeight),
        // Adjust the (0, 0) point to be at the left upper corner of the left screen:
        new Point(-screenOnTheLeft.width, 0)
    );

    // Creates a factory with the screen adjusted nature.
    MouseMotionFactory factory = FactoryTemplates.createAverageComputerUserMotionFactory(nature);

    // Move mouse to the left upper corner of the left screen
    factory.move(0, 0);
    // Move mouse to the left upper corner of the right screen
    factory.move(screenOnTheLeft.width + 1, 0);
```

Extending main screen to right
```java
    // How to extend screen to right. This means your main monitor is in the left and additional screen is right
    // of the main screen.
    Dimension screenOnTheRight = new Dimension(2000, 1080);
    Dimension mainScreen = new Dimension(2000, 1080);

    ScreenAdjustedNature nature = new ScreenAdjustedNature(
        // The new dimension of the screen is a sum of both screen dimensions:
        new Dimension(screenOnTheRight.width + mainScreen.width, bothScreenHeight),
        // Adjust the (0, 0) point to be at the left upper corner of the main screen:
        new Point(0, 0)
    );

    // Creates a factory with the screen adjusted nature.
    MouseMotionFactory factory = FactoryTemplates.createAverageComputerUserMotionFactory(nature);

    // Move mouse to the left upper corner of the main (left) screen
    factory.move(0, 0);
    // Move mouse to the left upper corner of the right screen
    factory.move(mainScreen.width + 1, 0);
```

## What if I want to move something else than a mouse on something else than a screen?

In this case you need to provide your own `SystemCalls` and `MouseInfoAccessor` to the factory. 

In `SystemCalls` you need to implement the `setMousePosition(int x, int y)` so it will call the necessary device with new coordinates.

In `MouseInfoAccessor` you need to implement the `getMousePosition()` method to get the cursor coordinates from the specified device.

And that's basically it, rest is handled by NaturalMouseMotion internals.

### I want to run NaturalMouseMotion in a headless environment

See previous section. You need to provide your own `SystemCalls` and `MouseInfoAccessor` objects.
If you attempt to use default implementations in a headless environment, then `java.awt.AWTException` will be thrown.

## Troubleshooting

### The mouse gets stuck trying to move to destination or won't end up on correct pixel.

There are several possible causes to that behavior:

* Some JDK versions are actually buggy and the `Robot` class, which is moving the mouse by default in NaturalMouseMotion does not work that well on them, depending on the hardware in the client computer. You can use `SystemDiagnosis.validateMouseMovement();` to test if the mouse positioning works as intended.
[Java bug on windows 10 (fixed since Java 11)](https://bugs.openjdk.java.net/browse/JDK-8196030) - [stackoverflow discussion](https://stackoverflow.com/questions/48837741/java-robot-mousemovex-y-not-producing-correct-results/48847100)

* There is some other program moving the mouse in a separate process or other thread moving the mouse in the same process working against the goal of the library. Solution is to make sure this doesn't happen.
* You accidentally move the mouse manually during the process. (This shouldn't cause a lasting effect, just temporary hiccup in the trajectory)
* You have altered MouseMotionFactory configuration in a way that causes it to get stuck.
* There might be some security setting in your computer preventing the mouse from moving:
   [Robot issue on Mac OS Mojave](https://bugs.openjdk.java.net/browse/JDK-8218487)
* There's an actual bug somewhere in the NaturalMouseMotion library that needs fixing on the library side.

