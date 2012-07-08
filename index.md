---
layout: default
title: Transfuse
---


### About
Transfuse is a Java dependency injection and integration library geared specifically for the Google Android API.  Transfuse gives you the ability to develop Android components in a POJO way, catapulting your Android code into a testable, decoupled and flexible style.  For example, an Activity built using Transfuse looks like the following:

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

Transfuse has been designed from the ground up to be extremely lightweight and performant.  The code enabling the features of Transfuse is generated at compile time, which allows the library to avoid overhead associated with runtime bytecode generation.  In addition, using this compile time code generation technique Transfuse puts a variety of AOP features within arm's reach.

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
    <version>0.1-SNAPSHOT</version>
    <scope>compile</scope>
</dependency>
<dependency>
    <groupId>org.androidtransfuse</groupId>
    <artifactId>transfuse-api</artifactId>
    <version>0.1-SNAPSHOT</version>
</dependency>

{% endhighlight %}
Note: Transfuse is not yet released to Maven central, so you will have to download the project and install it manually for the time being.

[1]: https://github.com/johncarl81/transfuse/downloads
