---
layout: default
title: Transfuse
---

### Documentation

#### Components

A Transfuse application is made up of a set of components.  These components are analogous to the standard set of Android components:  The Activity, Service, Application, and Broadcast Receiver.  Each of these components are defined by annotating a class as follows:

{% highlight java %}
@Activity
public class ExampleActivity {}
@Service
public class ExampleService {}
@BroadcastReceiver
public class ExampleBroadcastReceiver {}

{% endhighlight %} 

This tells Transfuse to use the class as an Android Component.  This turns on a number of features, such as dependency injection and event mapping.

##### @Activity

Annotating your Activity class begins the process of developing your Transfuse application.  A common next step is to associate the Activity with a layout.  In standard Android this is done by defining the layout in the onCreate() method.  Transfuse allows you to define the layout by annotating your @Activity class like so:

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

In addition to the manifest activity properties you are able to define IntentFilters on the class which will be added to the AndroidManifest.xml file:

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

So far we have seen how to set up a basic Activity and have it declared in the Manifest, lets now take a look at wiring the components up through the events raised by the Android system.

##### Lifecycle Events

Transfuse makes the entire Activity lifecycle available through a set of annotations.  You may annotate zero, one or many methods in your class.  In turn these will be called during that lifecycle event.

For instance, if we annotate the log() method in the Example class as follows:

{% highlight java %}
@Activity(label = "Transfuse Example")
@Layout(R.id.example_layout)
@IntentFilter({
    @Intent(type = IntentType.ACTION, name = android.content.Intent.ACTION_MAIN),
    @Intent(type = IntentType.CATEGORY, name = android.content.Intent.CATEGORY_LAUNCHER)
})
public class Example {
    @OnCreate
    public void log(){
        Log.i("Example Info", "OnCreate called");
    }
}
{% endhighlight %}

During the on create lifecycle phase the log() method will be called.  Each method annotated by the given lifecycle event annotation is added to the generated component in that lifecycle method.  Please note, these lifecycle events will not be called in any predefined order.

The following are lifecycle events supported by @Activity components:

{% highlight java %}
@OnCreate
@OnStart
@OnPause
@OnResume
@OnStop
@OnDestory
@OnBackPressed
@OnSaveInstanceState
@onRestoreInstanceState
{% endhighlight %}

Optionally, you may add parameters to the annotated lifecycle event methods that match the mapped event method.  For instance, the onCreate() method has a Bundle parameter.  If you add this parameter to the annotated method the bundle from the original onCreate() method will be passed in like the following:

{% highlight java %}
@Activity(label = "Transfuse Example")
@Layout(R.id.example_layout)
@IntentFilter({
    @Intent(type = IntentType.ACTION, name = android.content.Intent.ACTION_MAIN),
    @Intent(type = IntentType.CATEGORY, name = android.content.Intent.CATEGORY_LAUNCHER)
})
public class Example {
    @OnCreate
    public void log(Bundle bundle){
        Log.i("Example Info", "OnCreate called with value: " + bundle);
    }
}
{% endhighlight %}

<hr/>
#### Legacy Support

It is nice when you are able to start developing an application from a blank slate, but often we are stuck with a legacy code base.  It is expected that you may start a Transfuse application from an existing Android application.  Therefore, the AndroidManifest.xml management is flexible enough to mix Transfuse components with regular Android components.  The following options are available when dealing with legacy Android applications:

You may define your Android components as normal, and register them in the AndroidManifest.xml yourself.  You will not be able to use a majority of Transfuse's features and Transfuse will not register your component in teh AndroidManifest.xml file.  However, if you manually add a component to the AndroidManifest.xml file Transfuse will detect your additions, preserve them and work around them.

You may define your Android component as normal and annotate it to be managed in the AndroidManifest.xml by Transfuse.  Transfuse detects if the annotated component extends an Android component, and if so it will add it to the manifest.  Please note that these components are still Android components and dependency injection and the other code generation features will not be available.  This looks like the following:

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

