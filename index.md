---
layout: default
title: Transfuse
---


### About

Transfuse is a Java Dependency Injection (DI) and integration library geared specifically for the Google Android API.

There are several key features that make Transfuse a great framework to use with Android:

<ul class="square">
<li>Dependency Injection - Transfuse implements the JSR330 standard annotations</li>
<li>POJO Components - Transfuse gives users the ability to develop Android components in Plain Old Java Objects (POJO), enabling a testable, decoupled and flexible style.</li>
<li>Compile Time Code Generation - Transfuse is remarkably small, lightweight and fast due to the technique of generating supporting code at compile time.</li>
<li>Manifest Management - Transfuse manages the Android Manifest, eliminating the duplicated effort of declaring and registering components.</li>
</ul>

All of these features help eliminate boilerplate and make Android applications much easier to write.

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
<li>All specialty injections, such as injecting View elements, are designated with qualifier injections (@View, @Resource, etc.).</li>
<li>Activity Manifest metadata, such as the label, are now defined on the class level instead of the AndroidManifest.xml file.  This puts all the relevant information regarding the Activity in one place.  Transfuse handles the job of registering the Activity and all the associated metadata in the AndroidManifest.xml file.</li>
</ul>

Read more about the features in the [[Documentation](documentation.html)] and [[Introduction](motivation.html)] sections.

New to the library or Android? Visit the [[Getting Started](getting_started.html)] section.

### Download

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
