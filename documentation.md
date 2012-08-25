---
layout: default
title: Transfuse
---

### Documentation

#### Components

A Transfuse application is made up of a set of components.  These components are analagous to the standard set of Android components:  The Activity, Service, Application, and Broadcast Receiver.  Each of these components are defined by annotating a class as follows:

{% highlight java %}
@Activity
public class ExampleActivity {}
@Service
public class ExampleService {}
@BroadcastReceiver
public class ExampleBroadcastReceiver {}
{% endhighlight %} 

This tells Transfuse to use the class as an Activity.  This turns on a number of features, such as depdendency injection and event mapping.

##### @Activity

Annotating your class begins the process of developing your Transfuse application.  A common next step is to associate the Activity with a layout.  In standard Android this is done by defining the layout in the onCreate() method.  Transfuse allows you to define the layout by annotating your @Activity class like so:

{% highlight java %}
@Activity
@Layout(R.id.example_layout)
public class Example {}
{% endhighlight %}

A key feature of Transfuse is defining the AndroidManifest.xml metadata within the Java class declaration.  All manifest metadata is available either as parameters of the @Activity annotation or as additional annotations on the class level.

As an example, we can set the label to our example Activity as follows:

{% highlight java %}
@Activity(label = "Transfuse Example")
@Layout(R.id.example_layout)
public class Example {}
{% endhighlight %}

Transfuse will add this property to the AndroidManifest.xml for you, resulting in the following entry in the AndroidManifest.xml:

{% highlight xml %}
<activity t:tag="+,l,n" android:label="Transfuse Example" android:name=".ExampleActivity">
</activity>
{% endhighlight %}

In addion to the manifest activity properties you are able to define IntentFilters on the class which will be added to the AndroidManifest.xml file:

{% highlight java %}
@Activity(label = "Transfuse Example")
@Layout(R.id.example_layout)
@IntentFilter({
        @Intent(type = IntentType.ACTION, name = android.content.Intent.ACTION_MAIN),
        @Intent(type = IntentType.CATEGORY, name = android.content.Intent.CATEGORY_LAUNCHER)
})
public class Example {}
{% endhighlight %}

This sets up the Activity as the home screen and adds it to the list of applications on the phone. 

<hr/>

#### Legacy Support

It is nice when you are able to start developing an application from a blank slate, but often we are stuck with a legacy code base.  It is expected that you may start a Transfuse application from an existing Android application.  Therefore, the AndroidManifest.xml managment is flexable enough to mix Transfuse components with regular Android components.  The following options are available when dealing with legacy Android applicatoins:

You may define your Android components as normal, and register them in the AndroidManifest.xml yourself.  Transfuse will detect your additions to the AndroidManifest.xml file and work around them.

You may define your Android component as normal and annotate it to be managed in the AndroidManifest.xml by Transfuse.  Transfuse detects if the annotated component extends an Android component, and if so it will add it to the manifest.  Please note that these components are still Android components and dependency injection and the other code generation featurs will not be available.  This looks like the following:

{% highlight java %}
@Activity(label = "Transfuse Example")
@IntentFilter({
        @Intent(type = IntentType.ACTION, name = android.content.Intent.ACTION_MAIN),
        @Intent(type = IntentType.CATEGORY, name = android.content.Intent.CATEGORY_LAUNCHER)
})
public class Example extends Activity {
    ...
}
{% endhighlight %}


#### Reference
<a href="javadocs/api/index.html">API Javadocs</a>
<br/><br/>
<a href="javadocs/main/index.html">Main Javadocs</a>

