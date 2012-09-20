---
layout: default
title: Transfuse
---

### Documentation

#### Motivation

The Android API has a common theme throughout its components.  Each component must be extended to implement your application's specific functionality.  Although this approach works, it has subtle side effects.  If your component implements many separate features your component classes quickly becomes a mismatch of behavior.  This results in hard to read and hard to tests classes.  Also, any 3rd party library based on the component lifecycle or functionality provided by a Context must extend the given component class.  Because of Java's single extension policy, this action makes these 3rd party libraries that leverage context components incomparable with each other.

Additionally, each component must be registered individually in the AndroidManifest.xml file.  How many times have you forgotten to register a new component only to find out after it is deployed to your emulator or device?  This duplication of registration and declaration violates the Do Not Repeat Yourself principle.

These are just a couple of issues resolved by Transfuse.  Transfuse changes the model of Android components into POJOs, allowing you to develop your components the way you want.  No more extending Activity, Service, etc in order to implement the corresponding component.  You simply need to annotate the component classes to register them in your Android application.  This registration action tells Transfuse to add the component to the Android Manifest, essentially eliminating manual editing and management of the Manifest.

Transfuse also offers a compile time dependency injection framework based on JSR-330.  This is the same standard implemented by the leading DI frameworks Guice, Spring, Seam, etc.  Dependency injection allows you to eliminate boilerplate plumbing code in your application further, as well as encourages well formed application architecture.  Transfuse implements dependency injection differently than the mentioned frameworks, it performs all analysis and code generation during compile time.  This reduces the critical startup time of your application, especially any lag based on runtime startup of Transfuse.

It is Transfuse's mission to make Android a better API using performance sensitive techniques.

#### High level

A Transfuse application is built using a series of components analogous to the set of Android components.  These components are declared using the Transfuse API annotations on the class type level.

Transfuse moves the declaration of Manifest metadata to the class level.  This combines declaration of the Transfuse component with registration as an application Component.  Transfuse writes and manages the AndroidManifest.xml for you.

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

Annotating your Activity class begins the process of developing your Transfuse application.  A common next step is to associate the Activity with a layout.  In standard Android this is done by defining the layout in the onCreate() method.  Transfuse allows you to define the layout by annotating your @Activity class like so:

{% highlight java %}
@Activity
@Layout(R.id.example_layout)
public class Example {}
{% endhighlight %}

A key feature of Transfuse is defining the AndroidManifest.xml metadata within the Java class declaration.  All manifest metadata is available either as parameters of the @Activity annotation or as additional annotations on the class level.  This follows the Don't-Repeat-Yourself principle, keeping the declaration and configuration of your Activity in one place.

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


##### Listener Registration

Another common event to be raised by the Android system are by listeners on view components.  Transfuse allows you to easily define and register any of the listeners in the Android ecosystem with the corresponding View object.  The following example associates an anonymous inner OnClickListener with the R.id.button view object:

{% highlight java %}
@Activity
@Layout(R.id.example_layout)
public class Example{
    @RegisterListener(value = R.id.button5)
    private View.OnClickListener listener = new View.OnClickListener() {
        public void onClick(View v) {
            Log.i("Example Info", "Button Clicked");
        }
    };
}
{% endhighlight %}

<hr/>

##### @Service

Annotating your class with the Service annotation Tells Transfuse to use the class as an Android Service.  As with the Activity annotaiton, annotating a Service class will allow you to define all manifest metadata on the class level.  This includes IntentFilters and MetaData:

{% highlight java %}
@Service
@IntentFilter
public class ExampleService {}
{% endhighlight %}

Transfuse Service classes have the following lifecycle events defined, analogous to the Android lifecycle events:

{% highlight java %}
@OnCreate
@OnStart
@OnDestroy

{% endhighlight %}

Service may be injected into using JSR330 injections as described in the Injection section:

{% highlight java %}
@Service
public class ExampleService {
	@Inject
	public ExampleService(ADependency dependency) {
        ...
	}
}
{% endhighlight %}

<hr/>

##### @BroadcastReceiver

Annotating your class with the BroadcastReceiver annotaiton activates the class as an Android Broadcast Receiver.

The most important event handled by the Broadcast Receiver is onReceieve.  Transfuse maps this event to the @OnReceive annotation.  As with the other components, you may define the Manifest metadata on the class level.  This means that the intents that the broadcast receiver responds to are defined at the class level.

{% highlight java %}
@BroadcastReceiver
@Intent(type = IntentType.ACTION, name = android.content.Intent.ACTION_BOOT_COMPLETED)
public class Startup{
	@OnReceive
	public void bootup(){
	}
}
{% endhighlight %}

<hr/>

#### Dependency Injection

Transfuse implements JSR330, the same standard many of the leading dependency injection frameworks implement.  The following annotations are available:

##### @Inject

Transfuse allows you to inject into the constructor, methods and fields of a class.  These injections may be public, package private, protected or private.  You should prefer constructor injection, then method then field injection.  Likewise, for performance reasons, you should prefer public, package private or protected injections over private.  Private injections requires Transfuse to use reflection at runtime and for large dependency graphs may significantly affect performance.

##### Provider

Providers may be used when you want to manually resolve the dependencies of a class.  The Provider will be used to resolve both the injection of the provider and the injection of the type the provider returns:

Provider:
{% highlight java %}
public void ExampleProvider implements Provider<Example>
	public Example get(){
		return new Example();
	}
}
{% endhighlight %}

Injections:
{% highlight java %}
public void TestInjections{
	@Inject
	private Example example; //calls .get() to resolve example
	@Inject
	private Provider<Example> exampleProvider; // determines the provider type by generics
	@Inject
	private ExampleProvider concreteInjection;
}
{% endhighlight %}

To map a Provider to a type, define the provider binding in the TransfuseModule:

{% highlight java %}
@TransfuseModule
public interface Module{
	@BindProvider(ExampleProvider.class)
	Example mapExampleProvider();
}
{% endhighlight %}


##### @Singleton

Any class annotated with @Singleton will, when injected, reference a single instance in the runtime.  This makes it easy to define singletons in your application, eliminating the boilerplate of defining the singleton behavior:

{% highlight java %}
@Singleton
public class SingletonExample{
	private String singletonData;
}
{% endhighlight %}

##### @Named

Named support is pending

<hr/>

#### Events
Transfuse offers an additional global event bus in addition to the mapping of the Android lifecycle and callthrough events.

Any type may be used as an event.  Event observer methods may be defined by either annotating the method or the parameter of a method in a component or injected class with the @Observes annotation:

{% highlight java %}
public class Event{}

public class ListenerExample{
	@Observes
	public void observeEvent(Event event){}

	public void observeEvent2(@Observes Event event){}
}
{% endhighlight %}

Events are triggered by using the EventManager.trigger() method.  Simply call this method with the given event and all the available annotated methods will be triggered.

{% highlight java %}
public class Trigger{
	@Inject
	private EventManager event Manager;

	public void trigger(){
		eventManager.trigger(new Event());
	}
}
{% endhighlight %}

Keep in mind that events may contain any relevant data and behavior.  Is it, after all, completely definable by the developer.  Also, the Observing methods are not called in any particular order, so make sure that their operations are not dependent on each other.

<hr/>
#### Parcels
Transfuse offers a new way of defining Parcelable classes.  The typical implementation of a Parcelable class in Android is riddled with boilerplate.  Not only do you have to define the serialization manually, but you must define a public static final CREATOR class that implements the Parcelable.Creator interface.  Transfuse takes care of all of this for you.  You simply have to annotate the class with the @Parcel. annotation.  Transfuse will detect all getter/setter pairs in your class, map it to the designated Bundle serialization method, and produce a Parcelable class for you:

{% highlight java %}
@Parcel
public void CleanParcel{
	public String name;
	public int age;
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	@Transient //don't serialize in parcelable
	public int getAge(){
		return age;
	}
	public void setAge(int age){
		this.age = age;
	}
}
{% endhighlight %}


If there is a parameter that you do not want serialized, annotate the getter or setter with @Transient.

Parcels are useful when passing data between Android components.  Therefore, when you use the IntentFactory, Transfuse will automatically detect if a class is annotated with @Parcel and wrap it with the appropriate Parcelable implementation.

<hr/>
#### Legacy Support

It is nice when you are able to start developing an application from a blank slate, but often we are stuck with a legacy code base.  It is expected that you may start a Transfuse application from an existing Android application.  Therefore, the AndroidManifest.xml management is flexible enough to mix Transfuse components with regular Android components.  The following options are available when dealing with legacy Android applications:

You may define your Android components as normal, and register them in the AndroidManifest.xml yourself.  You will not be able to use a majority of Transfuse's features and Transfuse will not register your component in the AndroidManifest.xml file.  However, if you manually add a component to the AndroidManifest.xml file Transfuse will detect your additions, preserve them and work around them.

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

