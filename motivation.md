---
layout: default
title: Transfuse
---


### Motivation

The Android API requires the developer to write a lot of boilerplate code.  This makes the Android API difficult to use and hard to get right.  In addition, the typical approaches to eliminate boilerplate that work in server applicatoins bog down the critical startup time of your Android application.  It is Tranfuse's mission to make Android a better API in a performance sensitive way.   

Transfuse is a collection of techniques surrounding compile time code generation.  

##### Dependency Injection
First and foremost, Transfuse is a [[JSR-330](http://www.jcp.org/en/jsr/detail?id=330)] compliant dependency injection framework.  This means that Transfuse offers the standard set of injection annotations with the common functionality found in the top DI frameworks (Guice, Seam, Spring).  The difference with Transfuse is the implementation.  Dependency Injection in Transfuse is implemented statically during compile time making it as fast as possible, often avoiding reflection.

##### Composition over Extension
Transfuse breaks you free of the Android's extension based API.  This gives you the freedom to write your application the way you want.  You will be amazed how small and succinct your Activities and supporting code will be.

##### Zero Overhead
Transfuse uses the Java 6 annotation processor and Sun CodeModel library to produce scaffolding around your annotated classes during compile time.  This technique allows Transfuse, in most cases, to avoid reflection during runtime and allows for byte-code generation in the Dalvik VM.  The result is an extremely lightweight platform specifically tuned for your application.

##### AOP
On top of dependency injection Transfuse offers robust Aspect Oriented Programming facilities.  This feature gives you the ability to pull out cross-cutting concerns from your code in a relatively easy way.  In addition, Transfuse defines a set of default AOP annotations for common features like asynchronous method calls and SQLite transactions.

##### Manifest Management
How many times have you written an Activity and deployed it to your emulator only to find out that you failed to define the Activity in your manifest.  Transfuse centralizes the definition of your Android components in one spot, the class, so you never have to handle the Android Manifest again.
