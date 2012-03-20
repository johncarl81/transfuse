---
layout: default
title: Transfuse
---

##### Dependency Injection
First and foremost, Transfuse is a [[JSR-330](http://www.jcp.org/en/jsr/detail?id=330)] compliant dependency injection framework.  This means that Transfuse offers the standard set of injection annotations with the common functionality found in the top DI frameworks (Guice, Seam, Spring).  The difference with Transfuse is the implementation.  Dependency Injection in Transfuse is implemented statically during compile time making it as fast as possible, often avoiding reflection.

##### Composition over Extension
Transfuse breaks you free of the Android's extension based API.  This gives you the freedom to write your application the way you want.  You will be amazed how small and succinct your Activities and supporting code will be.

##### Integration
Transfuse brings together the common Android Services with your defined Resources along with an event system following the Android lifecycle.  The motto is, if Transfuse built it, Transfuse can inject it.

##### Zero Overhead
Transfuse uses the Java 6 annotation processor and Sun CodeModel library to produce scaffolding around your annotated classes during compile time.  This technique allows Transfuse, in most cases, to avoid reflection during runtime and allows for byte-code generation in the Dalvik VM.  The result is an extremely lightweight platform specifically tuned for your application.

##### AOP
On top of dependency injection Transfuse offers robust Aspect Oriented Programming facilities.  This feature gives you the ability to pull out cross-cutting concerns from your code in a relatively easy way.  In addition, Transfuse defines a set of default AOP annotations for common features like asynchronous method calls and SQLite transactions.

##### Manifest Management
How many times have you written an Activity and deployed it to your emulator only to find out that you failed to define the Activity in your manifest.  Transfuse centralizes the definition of your Android components in one spot, the class, so you never have to handle the Android Manifest again.

##### All Together
Transfuse brings all these features together to make a platform to enable lightweight, decoupled, clean and streamlined applications.
