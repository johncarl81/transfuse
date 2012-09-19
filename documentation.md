---
layout: default
title: Transfuse
---

### Documentation

#### Introduction
It is Transfuse's mission to make Android a better API using performance- sensitive techniques.  The Android API has a common theme throughout its components; each component must be extended to implement the application's specific functionality.  Although this approach works, it has subtle side effects. If the component implements many separate features, the component classes quickly becomes a mismatch of behavior. This results in hard-to-read and hard-to-test classes. Also, any third party library based on the component lifecycle or functionality provided by a Context must extend the given component class.  Because of Java's single extension policy, this action makes these third party libraries that leverage Context components incomparable with each other.

Additionally, each component must be registered individually in the AndroidManifest.xml file.  It is easy to overlook the need to register a new component, only to remember after it is already deployed to a emulator or device.  This duplication of registration and declaration violates the Do Not Repeat Yourself principle.

Transfuse resolves these issues. The DI changes the model of Android components into POJOs, allowing users to develop components the way they want.  There is no need to keep extending Activity, Service, etc in order to implement the corresponding component. Now, all that is necessary is to annotate the component classes to register them in the Android application.  This registration action tells Transfuse to add the component to the Android Manifest, essentially eliminating manual editing and management of the Manifest.

Transfuse also offers a compile time DI framework based on JSR-330.  This is the same standard implemented by the leading DI frameworks Guice, Spring, Seam, etc. DI allows the elimination of boilerplate plumbing code in the application, and also encourages well formed application architecture.  However, Transfuse implements DI differently than the previously mentioned frameworks, in that it performs all analysis and code generation during compile time.  This reduces the critical startup time of an application, especially any lag based on runtime startup of Transfuse.

#### High level

A Transfuse application is built using a series of components analogous to the set of Android components.  These components are declared using the Transfuse API annotations on the class type level.

Transfuse moves the declaration of Manifest metadata to the class level.  This combines declaration of the Transfuse component with registration as an application Component.  Transfuse writes and manages the AndroidManifest.xml for the user.

Each Transfuse component has a corresponding lifecycle built upon the standard lifecycle of the corresponding Android component.  This lifecycle is implemented by lifecycle events.  Any Transfuse event may be handled on the component or any instance injected into it.

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

These annotations tell Transfuse to use the class as an Android Component.  This turns on a number of features, such as dependency injection and event mapping.

<hr/>

##### @Activity

Annotating an Activity class begins the process of developing a Transfuse application.  A common next step is to associate the Activity with a layout.  In standard Android this is done by defining the layout in the onCreate() method.  Transfuse allows the user to define the layout by annotating the @Activity class like so:

{% highlight java %}
@Activity
@Layout(R.id.example_layout)
public class Example {}
{% endhighlight %}

A key feature of Transfuse is defining the AndroidManifest.xml metadata within the Java class declaration.  All manifest metadata is available either as parameters of the @Activity annotation or as additional annotations on the class level.  This follows the Don't-Repeat-Yourself principle, keeping the declaration and configuration of the Activity in one place.

As an example, the label can be set to an Activity as follows:

{% highlight java %}
@Activity(label = "Transfuse Example")
@Layout(R.id.example_layout)
public class Example {}
{% endhighlight %}

Transfuse adds this property to the AndroidManifest.xml, resulting in the following entry in the AndroidManifest.xml:

{% highlight xml %}
<activity t:tag="+,l,n" android:label="Transfuse Example" android:name=".ExampleActivity">
</activity>
{% endhighlight %}

In addition to the manifest activity properties, users are able to define IntentFilters on the class which will be added to the AndroidManifest.xml file:

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


##### Lifecycle Events

Transfuse makes the entire Activity lifecycle available through a set of annotations.  Users may annotate zero, one, or many methods in the class.  In turn ,these will be called during that lifecycle event.

In the Example class below, the log() method is used as the annotation method:

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

NOTE: These lifecycle events will not be called in any predefined order.


During the onCreate lifecycle phase the log() method will be called.  Each method annotated by the given lifecycle event annotation is added to the generated component in that lifecycle method.  

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

Optionally, parameters may be added to the annotated lifecycle event methods that match the mapped event method.  For instance, the onCreate() method has a Bundle parameter.  If this parameter is added to the annotated method the bundle from the original onCreate() method will be passed in like the following:

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


##### Listener Registration

Another common event to be raised by the Android system are by listeners on view components.  Transfuse allows users to easily define and register any of the listeners in the Android ecosystem with the corresponding View object.  The following example associates an anonymous inner OnClickListener with the R.id.button view object:

{% highlight java %}
@Activity
@Layout(R.id.example_layout)
public class Example{
    @RegisterListener(value = R.id.button5)
    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.("Example Info", "Button Clicked");
        }
    };
}
{% endhighlight %}

<hr/>
#### Legacy Support

In an ideal world, users are able to develop a new application.  Realistically however, users are often stuck with a legacy code base.  Transfuse anticipates this, and the AndroidManifest.xml management is flexible enough to mix Transfuse components with regular Android components.  The following options are available when dealing with legacy Android applications:

<ul class="square">
<li>
 Define Android components as normal, and register them in the AndroidManifest.xml.  By using this option, users will not be able to use a majority of Transfuse's features and Transfuse will not register the component in the AndroidManifest.xml file.  However, if a component is manually added to the AndroidManifest.xml file, Transfuse will detect the additions, preserve them and work around them. </li>

<li> Define Android components as normal, and annotate it to be managed in the AndroidManifest.xml by Transfuse.  Transfuse detects if the annotated component extends an Android component, and if so, it will add it to the manifest.

NOTE: These components are still Android components and DI and the other code generation features will not be available. </li></ul>

The second option looks like the following:

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

