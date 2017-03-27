# dynamator
This is simple Java application that captures stills from your webcam and creates a video, so you can create your own stop-motion animations.  It is far from finished or polished.

# Instructions
For the moment, this will only work for Windows, and has only been tested on Windows 10.
The OpenCV and FFMpeg libraries used to access the camera or create videos are "difficult". Probably for perfectly valid reasons, they don't programmatically give up a lot of information about the underlying hardware or codecs.  As a result this may or may not work on your setup.  So before you invest a significant amount of time in creating a video, please test on your computer. 

## To Build
* Download and install the [Java 8 JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html).
* Download the Dynamator source from here.
* `cd dynamator` 
* `./gradlew build`

This will download every else required to build and run the application. (Unless you want to build it into an native installation executable - that's yet to happen). 

## To Run
* `./gradlew jfxRun`

# Why Dynamator (the name)
Dynamation was the name given to a form of stop-motion animation created by Ray Harryhausen, which he used to great effect in Jason and the Argonauts and other films.

# Why Dynamator (the motivation)
I wanted to experiment with various tools and software, and try out ideas about software design.
Ideas such as:
* Bob Martin's ideas about [applications, components and interactors](https://www.youtube.com/watch?v=Nsjsiz2A9mg). Components conform to an interface that exposes their business functions.  The creation of components is separated from their business interface and only the main program knows how they were created.
* Classes relating to a component are collected together into a package. (Let's try to break the pattern of grouping classes together by function - controllers, views, etc.).  Package level scoping is used to hide everything other than the business interface to be used by other components, and the startup plumbing to be used by main.
* Where possible objects validate themselves.  In this code a null object reference is unexpected.  Where a reference is optional, then java.util.Optional is used.
* Read only domain model (as far as possible).  Annoyingly Java collections all have modification methods, so I wrote my own version of List called [Seq](./definitions/src/main/java/type/Seq.java).
* Using native libraries and building them into a Java application.  This uses [OpenCV](http://opencv.org/) via the [Javacpp project](https://github.com/bytedeco/javacpp).  OpenCV has some really clever computer vision stuff, but I'm only using their wrapper around FFMpeg's camera and video encoding functionality. Javacpp provides the native libraries and wrappers around a number of C projects, but can be a bit tricky to use if you don't know C.  
* JavaFX for building UI applications in Java that don't look like Swing.
* Gradle instead of Maven.  Great for spliting stuff up into sub-projects without that the hell of version numbers and the boilerplate XML.  Also easy to extend plugins within your build script without having to resource to Ant. 

# Design
![Design UML](./design/design.png) 
