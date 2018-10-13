## Natural Mouse Motion by Joonas Vali (2018) ##

This library provides a way to move cursor to specified coordinates on screen reliably,
while being randomly arced to look like real hand moved it there by using a mouse.
The default settings should look good enough for most cases, but if user wishes, 
they can heavily customize the settings and implementations responsible for the trajectory 
of the cursor for specific use cases.

## Demonstration video: ##
Not available yet


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


## LICENSE: ##

This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to http://unlicense.org
