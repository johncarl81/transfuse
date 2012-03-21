---
layout: default
title: Transfuse
---


### About
Transfuse is an open-source integration framework for Google Android.  Transfuse extends the Android API, to create a more natural, flexible and maintainable mobile platform. 

Interested?  Read more about the features in the [[Documentation](documentation.html)] and [[Motivation](motivation.html)] sections and give Transfuse a spin for yourself.

### Getting Started

##### Download Transfuse

You may either download the latest release of Transfuse [[here](download.html)] or as a maven dependency as follows:

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
