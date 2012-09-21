---
layout: default
title: Transfuse
---


### About

Transfuse is a Java Dependency Injection (DI) and integration library geared specifically for the Google Android API.  Transfuse gives users the ability to develop Android components in a Plain old Java Object (POJO) style, allowing the Android code to be more testable, decoupled and flexible.

There are several key features that make Transfuse a great framework to use with Android:


<ul class="square">
<li>The code supporting a Transfuse application is generated at compile time, rather than at runtime.  This allows the library to avoid overhead associated with runtime reflection and bytecode generation.  In addition, using this compile time code generation technique Transfuse puts a variety of AOP features within reach. </li>
<li>Transfuse is also extremely easy to use.  All that is needed is the Transfuse API and core library on the classpath during compilation.  The Java Annotation Processor handles the rest. </li>
<li> The Java DI and integration library allows the user more freedom with coding and with the framework. It removes much of the boiler plate code, and streamlines the coding.
</li> </ul>

### Example

Using Transfuse, an Android Activity looks like the following:


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


<ul class="square">

<li>Now, Activities no longer extend the android.app.Activity class.</li>
<li>The Android Activity lifecycle is handled via lightweight events.  Any component within the injection graph may define event methods to be called during those phases of the lifecycle.</li>
<li>DI is implemented using the standard Java JSR 330 injection annotation library.  All specialty injections, such as injecting View elements, are designated with qualifier injections (@View, @Resource, etc.).</li>
<li>Activity Manifest metadata, such as the label, are now defined on the Class level instead of the AndroidManifest.xml file.  This puts all the relevant information regarding the Activity in one place.  Transfuse handles the job of registering the Activity and all the associated metadata in the AndroidManifest.xml file.</li>
</ul>

### Download
Read more about the features in the [[Documentation](documentation.html)] and [[Motivation](motivation.html)] sections.

New to the library or Android? Visit the [[Getting Started](getting_started.html)] section.

Try Transfuse by downloading from the [Download page][1],



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
