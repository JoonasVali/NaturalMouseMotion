## Natural Mouse Motion ##

This library provides a way to move cursor to specified coordinates on screen reliably,
while being randomly arced to look like real hand moved it there by using a mouse.
The default settings should look good enough for most cases, but if user wishes, 
they can heavily customize the settings and implementations responsible for the trajectory 
of the cursor for specific use cases.

## Demonstration video: ##
https://www.youtube.com/watch?v=wKJM2kFGMDk

## Maven: ##

```
<dependency>
  <groupId>com.github.joonasvali.naturalmouse</groupId>
  <artifactId>naturalmouse</artifactId>
  <version>1.0.3</version>
</dependency>
```

## Running: ##

Natural Mouse Motion needs at least java 8 to run.

You start by creating a new `com.github.joonasvali.naturalmouse.api.MouseMotionFactory`
or using the default instance, by calling `MouseMotionFactory.getDefault();`

To build a `MouseMotion` instance you use the factory instance:
`MouseMotion motion = mouseMotionFactory.build(int xDest, int yDest);`

And the reusable `MouseMotion` can be run by using the
`motion.move();` blocking method, which then moves the cursor.
This instance can be saved to call later or repeatedly.

Shorthand method for moving the mouse can be also found from the factory,
so a quick access can be found by calling `MouseMotionFactory.getDefault().move(int xDest, int yDest);`
