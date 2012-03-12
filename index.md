---
layout: default
title: Transfuse
---


### About
Transfuse is an open source lightweight integration framework for Google Android.

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

### Getting Started

##### Download Transfuse

You may either download Transfuse here or include it as a maven dependency as follows:

{% highlight xml %}

<dependency>
    <groupId>org.androidtransfuse</groupId>
    <artifactId>transfuse</artifactId>
    <version>0.1-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
<dependency>
    <groupId>org.androidtransfuse</groupId>
    <artifactId>transfuse-api</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>

{% endhighlight %}

##### Create a project

Create an Android project as you usually do in your favorite IDE.  Next, remove the application element and its children from the manifest.  This will pave the way for Transfuse to start managing the manifest.

##### Annotate your Activity

{% highlight java %}
// Example Transfuse Activity
@Activity(label = "@string/app_name")
@IntentFilters({
        @Intent(type = IntentType.ACTION, name = "android.intent.action.MAIN"),
        @Intent(type = IntentType.CATEGORY, name = "android.intent.category.LAUNCHER")
})
@Layout(R.layout.main)
public class HelloAndroid {

    @Inject @View(R.id.textview)
    private TextView textView;

    @Inject @Resource(R.string.hello)
    private String helloText;

    @OnCreate
    public void hello() {
        textView.setText(helloText);
    }
}

{% endhighlight %}

##### Deploy.
