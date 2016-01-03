# Space Invaders 104


## Description

Space Invaders 104 is the last stage of [Kevin Glass] ' famous Series of Java2D Game Programming Tutorials.
See the [original tutorial] for further explanations.

## Features
This Version extends the [101 maven tutorial] by [marcliberatore] to handles native Dependencies required for [OpenGL]
using [Apache Maven] as Dependency and Build Tool. It integrates the Shift from GAGE-Timer to System.nanoTime() from the [102 maven tutorial].
The [maven natives] plugin as demonstrated by the [maven-nativedependencies-example] creates a Distribution containing Native Libraries for Windows and *nix-Plattforms. These Libraries are required by [LWJGL] and [JOGL] for work. More Details can be found at [LWJGL Maven Integration]. Please note, that it currently still uses the older [LWJGL] 2.x-Series.

### OpenGL on Windows and Ubuntu
There's is a straight Difference of Java2D-Implementations between Windows 7 and Ubuntu 14.04. Although I used on both Systems nearly the same JDK-Version (1.8.0_60 for Windows, 1.8.0._66 at Ubuntu), I see virtually no Difference on Windows between the Java2D and the OpenGL-Versions of the Game. That's not the case with Ubuntu. Here you 'll find a notably tremor with Java2D but it runs smooth with the OpenGL-Versions.

### JOGL on Windows and Ubuntu
It's a big deal to update [Kevin Glass]' original Sources to comply with the new [JOGL] API. But, again, here applies the same difference between both OS and the JDK-Versions. Where it works without Problems with Windows 7, I wasn't able to quit the Game as expected under Ubuntu. There seems to be a seriuos Issue with Java AWT and X11, yielding the following Message:

In fact, I needed to kill the Process manually the hard way. After some recherches I found an alternative Approach using a [JOGL]-custom [GLWindow] as OpenGL Rendering-Stage. At least this works under Ubuntu, but the way User Interactions with the Keyboard are handled do not fit - at far as I can see - in the flow that [Kevin Glass] once designed. The GLWindow Eventqueue and the old AWT-Eventqueue do not like each other, anyway.

## Build Instructions
You'll need Apache Maven 3.0+ and JDK 1.8+. Clone the repository locally and run afterwards in your Terminal:
```bash 
cd spaceinvaders-104 
mvn package
```
to create an archive file called `spaceinvaders-04.zip` in your `target` folder. 

## Running the Game
Execute the Game by first unpacking the created archive, then step into the extracted Directory and pick the proper start script File. 
On *nix-Plattforms, type 
```bash 
./run.sh
```
and on Windows
```bash 
run.bat
```
to start. The Script will load everything needed to run the Game (LWJGL Libs+Natives)


## License

All Java code placed in the public domain by [Kevin Glass].
All additions placed in public domain by [M3ssman]

Sprites taken from [SpriteLib], 
licensed under the Common Public License 1.0.

[Kevin Glass]:http://www.cokeandcode.com/
[original tutorial]:http://www.cokeandcode.com/info/tut2d-4.html
[101 maven tutorial]:https://github.com/marcliberatore/spaceinvaders-101-java
[marcliberatore]:https://github.com/marcliberatore
[Apache Maven]:https://maven.apache.org/
[OpenGL]:https://www.opengl.org/
[102 maven tutorial]:https://github.com/marcliberatore/spaceinvaders-102-java
[maven natives]:https://code.google.com/p/mavennatives/
[maven-nativedependencies-example]:http://mavennatives.googlecode.com/svn/trunk/maven-nativedependencies-example/
[LWJGL]:http://legacy.lwjgl.org/
[JOGL]:https://jogamp.org/
[LWJGL Maven Integration]:http://wiki.lwjgl.org/index.php?title=LWJGL_use_in_Maven
[GLWindow]:https://jogamp.org/deployment/jogamp-next/javadoc/jogl/javadoc/com/jogamp/newt/opengl/GLWindow.html
[M3ssman]:https://github.com/M3ssman/
[SpriteLib]:http://www.widgetworx.com/widgetworx/portfolio/spritelib.html
