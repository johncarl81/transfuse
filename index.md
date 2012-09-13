---
layout: default
title: Transfuse
---


### About

Transfuse is a Java dependency injection and integration library geared specifically for the Google Android API.  With Transfuse you are able to develop Android components in a POJO way, allowing your Android code to be more testable, decoupled and flexible.  For example, an Activity built using Transfuse looks like the following:

{% highlight java %}
// Example Transfuse Activity
@Activity(label = "@string/app_name")
@Layout(R.layout.main)
public class HelloTransfuse {

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

A couple key points highlighted in this code:

<ul class="square">
<li>Activities no longer extend the android.app.Activity class.</li>
<li>The Android Activity lifecycle is handled via lightweight events.  Any component within the injection graph may define event methods to be called during those phases of the lifecycle.</li>
<li>Dependency Injection is implemented using the standard Java JSR 330 injection annotation library.  All specialty injections, such as injecting View elements, are designated with qualifier injections (@View, @Resource, etc.).</li>
<li>Activity Manifest metadata, such as the label, are now defined on the Class level instead of the AndroidManifest.xml file.  This puts all the relevant information regarding the Activity in one place.  Transfuse handles the job of registering the Activity and all the associated metadata in the AndroidManifest.xml file.</li>
</ul>

Transfuse was designed from the ground up to be extremely performant.  The code supporting a Transfuse application is generated at compile time, allowing the library to avoid overhead associated with runtime reflection and bytecode generation.  In addition, using this compile time code generation technique Transfuse puts a variety of AOP features within reach.

Transfuse is also extremely easy to use.  All you need is the Transfuse API and core library on your classpath during compilation, the Java Annotation Processor handles the rest.

Interested?  Read more about the features in the [[Documentation](documentation.html)] and [[Motivation](motivation.html)] sections and give Transfuse a spin for yourself.  If you're new to the library or Android the [[Getting Started](getting_started.html)] section will get you up and running in a flash.


### Download

You can get Transfuse from the [Download page][1], 

via git:

{% highlight bash %}

git clone git@github.com:johncarl81/transfuse.git

{% endhighlight %}

or via Maven:

{% highlight xml %}

<dependency>
    <groupId>org.androidtransfuse</groupId>
    <artifactId>transfuse</artifactId>
    <version>{{site.currentVersion}}</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>org.androidtransfuse</groupId>
    <artifactId>transfuse-api</artifactId>
    <version>{{site.currentVersion}}</version>
</dependency>

{% endhighlight %}

[1]: https://github.com/johncarl81/transfuse/downloads
