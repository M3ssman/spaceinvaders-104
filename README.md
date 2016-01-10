# Space Invaders 104

## Description

Space Invaders 104 is the last stage of [Kevin Glass] ' famous Series of Java2D Game Programming Tutorials.
See the [original tutorial] for further explanations.

## Features
* Extends [101 maven tutorial] by [marcliberatore] to handles native Dependencies for [OpenGL]
* Uses [Apache Maven] as Dependency and Build Tool
* Follows [OpenGL 2] - Specification
* Integrates Shift from GAGE-Timer to System.nanoTime() introduced by [102 maven tutorial].

The [maven natives] plugin as demonstrated by the [maven-nativedependencies-example] creates a Distribution containing Native Libraries for Windows and *nix-Plattforms. These Libraries are required by [LWJGL] and [JOGL]. More Details can be found at [LWJGL Maven Integration].

For Development I used a Machine with Ubuntu 14.04-LTS (8 GB Ram, Intel(R) Core(TM) i5-4690T CPU @ 2.50GHz) using OpenGL 3.0 (Mesa 10.1.3).

### OpenGL on Windows and Ubuntu
There's is an obvious Difference between the Java2D Implementations for Windows 7 and Ubuntu 14.04. 
Although I used on both Systems nearly the same 64bit JDK Versions (1.8.0_60 for Windows, 1.8.0.0_66 at Ubuntu), I see virtually no Difference on Windows between the Java2D and the OpenGL-Versions of the Game. That's not the case with Ubuntu. Here you 'll find a notably tremor with Java2D but it runs smooth with the OpenGL-Versions.

### JOGL on Windows and Ubuntu
It's not a big deal to update [Kevin Glass]' original Sources to comply with the new [JOGL] API. 
But, again, here applies the same difference between with OS and JDK-Versions. It works without Problems with Windows 7, but I wasn't able to quit the Game as expected under Ubuntu. 

Sadly, I was (and still am) confronted with a LibEGL-Warning complaining "failed to create a pipe screen for i965", which means that JOGL and the Intel Graphics-Card (Intel Corporation Xeon E3-1200 v3/4th Gen Core Processor Integrated Graphics Controller (rev 06)) of my little Tuxedo don't really like each other.

#### JOGL and AWT
There seems to be a serious Issue with Java AWT and X11, yielding the following Message:

```bash 
X11Util.Display: Shutdown (JVM shutdown: true, open (no close attempt): 1/1, reusable (open, marked uncloseable): 0, pending (open in creation order): 1)
X11Util: Open X11 Display Connections: 1
X11Util: Open[0]: NamedX11Display[:0, 0x7f7600534f20, refCount 1, unCloseable false]
```
The Game Window freezes but won't go away. In fact, I needed to kill the Process manually the hard way. 

#### JOGL and the newt-Package
I found an alternative Approach using a [JOGL]-custom [GLWindow] as OpenGL Rendering-Stage. At least this closes the Java Window. But this fails to finish everything fine, too, presenting a slightly modified X11-Message:

```bash 
X11Util.Display: Shutdown (JVM shutdown: true, open (no close attempt): 2/2, reusable (open, marked uncloseable): 0, pending (open in creation order): 2)
X11Util: Open X11 Display Connections: 2
X11Util: Open[0]: NamedX11Display[:0, 0x7fd52c001b90, refCount 1, unCloseable false]
X11Util: Open[1]: NamedX11Display[:0, 0x7fd52c015f60, refCount 1, unCloseable false]

```
This time, pressing Ctrl+C gently releases them Ghosts.

#### JOGL, the Frames, the Display and it's Context

No matter, I wasn't the only one facing these Problems. Reading [How to close a JOGL window] put me on the right track
to destroy the GLWindow or the Frame when Game Rendering stops. The X11Util-Message went away, but unfortunatly I still need to Ctrl+C to get the Terminal back. Not a clean Solution, but the Best I found so far.


### JOGL Events

The way User Interactions are handled by [JOGL]s [GLWindow] don't fit in the once designed flow. 
The GLWindow Event Queue and the old AWT - Event Queue ignore each other, so it was necessary to set up a custom Handler with custom Mappings from com.jogamp.newt.event.KeyEvent 
to java.awt.event.KeyEvent.

## Build Instructions
You'll need Apache Maven 3.0+ and JDK 1.8+. Clone the repository locally and run afterwards in your Terminal:
```bash 
cd spaceinvaders-104 
mvn package
```
to create an archive file called `spaceinvaders-104-release.zip` in your `target` folder. 

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
[OpenGL 2]:https://www.opengl.org/sdk/docs/man2/
[102 maven tutorial]:https://github.com/marcliberatore/spaceinvaders-102-java
[maven natives]:https://code.google.com/p/mavennatives/
[maven-nativedependencies-example]:http://mavennatives.googlecode.com/svn/trunk/maven-nativedependencies-example/
[LWJGL]:http://legacy.lwjgl.org/
[JOGL]:https://jogamp.org/
[LWJGL Maven Integration]:http://wiki.lwjgl.org/index.php?title=LWJGL_use_in_Maven
[GLWindow]:https://jogamp.org/deployment/jogamp-next/javadoc/jogl/javadoc/com/jogamp/newt/opengl/GLWindow.html
[M3ssman]:https://github.com/M3ssman/
[SpriteLib]:http://www.widgetworx.com/widgetworx/portfolio/spritelib.html
[How to close a JOGL window]:http://stackoverflow.com/questions/28930675/how-do-you-close-a-jogl-newt-glwindow-completely
