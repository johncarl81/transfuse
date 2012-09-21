---
layout: default
title: Transfuse
---

### Documentation

#### Introduction
It is Transfuse's mission to make Android a better API using performance- sensitive techniques.  The Android API has a common theme throughout its components; each component must be extended to implement the application's specific functionality.  Although this approach works, it has subtle side effects. If the component implements many separate features, the component classes quickly becomes a mismatch of behavior. This results in hard-to-read and hard-to-test classes. Also, any third party library based on the component lifecycle or functionality provided by a Context must extend the given component class.  Because of Java's single extension policy, this action makes these third party libraries that leverage Context components incompatable with each other.

Additionally, each component must be registered individually in the AndroidManifest.xml file.  It is easy to overlook the need to register a new component, only to remember after it is already deployed to a emulator or device.  This duplication of registration and declaration violates the Do Not Repeat Yourself principle.

Transfuse resolves these issues. The DI changes the model of Android components into POJOs, allowing users to develop components the way they want.  There is no need to keep extending Activity, Service, etc. in order to implement the corresponding component. Now, all that is necessary is to annotate the component classes to register them in the Android application.  This registration action tells Transfuse to add the component to the Android Manifest, essentially eliminating manual editing and management of the Manifest.

Transfuse also offers a compile time DI framework based on JSR-330.  This is the same standard implemented by the leading DI frameworks Guice, Spring, Seam, etc. DI allows the elimination of boilerplate plumbing code in the application, and also encourages well-formed application architecture.  However, Transfuse implements DI differently than the previously mentioned frameworks, in that it performs all analysis and code generation during compile time.  This reduces the critical startup time of an application, especially any lag based on runtime startup of Transfuse.

It is Transfuse's mission to make Android a better API using performance sensitive techniques.

#### High level

A Transfuse application is built using a series of components analogous to the set of Android components.  These components are declared using the Transfuse API annotations on the class type level.

Transfuse moves the declaration of Manifest metadata to the class level.  This combines declaration of the Transfuse component with registration as an application Component.  Transfuse writes and manages the AndroidManifest.xml for the user.

Each Transfuse component has a corresponding lifecycle built upon the standard lifecycle of the corresponding Android component.  This lifecycle is implemented by lifecycle events.  Any Transfuse event may be handled on the component, or at any instance, injected into it.

#### Components

The Transfuse components are analogous to the standard set of Android components:  The Activity, Service, Application, and Broadcast Receiver.  Each of these components are defined by annotating a class as follows:

{% highlight java %}
@Activity
public class ExampleActivity {}
@Service
public class ExampleService {}
@BroadcastReceiver
public class ExampleBroadcastReceiver {}

{% endhighlight %} 

These annotations tell Transfuse to use the class as an Android component.  This turns on a number of features, such as DI and event mapping.

<hr/>

##### @Activity

Annotating an Activity class begins the process of developing a Transfuse application.  A common next step is to associate the Activity with a layout.  In standard Android this is done by defining the layout in the onCreate() method.  Transfuse allows the user to define the layout by annotating the @Activity class like so:

{% highlight java %}
@Activity
@Layout(R.id.example_layout)
public class Example {}
{% endhighlight %}

Transfuse follows the convention of delcaring the layout directly after the super.onCreate() call in the root Activity.

If your use cases require a more advanced layout declaration, like defining the layout programatically, you may use the @LayoutHandler annotation and LayoutHandlerDelegate interface:

{% highlight java %}
@Activity
@LayoutHandler(LunchTimeLayoutDelegate.class)
public class Example{}
{% endhighlight %}

{% highlight java %}
public class LunchTimeLayoutDelegate implements LayoutHandlerDelegate{
	@Inject private Activity activity;

	public void invokeLayout(){
		if(isLunchTime()){
			activity.setContentView(R.id.lunchLayout);
		}
		else{
			activity.setContentView(R.ld.regularLayout);
		}
	}
	...
}
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

Now that the basic Activity has been set up and declared in the Manifest, let's look at wiring the components up through the events raised by the Android system.

##### Lifecycle Events

Transfuse makes the entire Activity lifecycle available through a set of annotations.  Users may annotate zero, one, or many methods in the class.  In turn, these will be called during that lifecycle event.

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

Optionally, parameters may be added to the annotated lifecycle event methods that match the mapped event method.  For instance, the onCreate() method has a Bundle parameter.  If this parameter is added to the annotated method, the bundle from the original onCreate() method will be passed in like the following:

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
            Log.i("Example Info", "Button Clicked");
        }
    };
}
{% endhighlight %}

<hr/>

##### @Service

Annotating the class with the Service annotation tells Transfuse to use the class as an Android Service.  As with the Activity annotation, annotating a Service class will allow users to define all manifest metadata on the class level.  This includes IntentFilters and MetaData:

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

Service may be injected into JSR330 injections as described in the Injection section:

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

Annotating the class with the BroadcastReceiver annotation activates the class as an Android Broadcast Receiver.

The most important event handled by the Broadcast Receiver is onReceieve.  Transfuse maps this event to the @OnReceive annotation.  As with the other components, users may define the Manifest metadata on the class level.  This means that the intents that the broadcast receiver responds to are defined at the class level.

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

#### Dependency Injection (DI)

Transfuse implements JSR330, the same standard many of the leading DI frameworks implement.  The following annotations are available:

##### @Inject

Transfuse allows user to inject into the constructor, methods and fields of a class.  These injections may be public, package private, protected or private.  As a best practice, users should prefer (in order) constructor injection, method, and then field injection.  Likewise, for performance reasons, users should prefer(in order) public, package private or protected injections over private.  Private injections requires Transfuse to use reflection at runtime and for large dependency graphs may significantly affect performance.

##### Provider

Providers may be used to manually resolve the dependencies of a class.  The Provider will be used to resolve both the injection of the provider and the injection of the type the provider returns:

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

Any class annotated with @Singleton will, when injected, reference a single instance in the runtime.  This makes it easy to define singletons in the application, eliminating the boilerplate of defining the singleton behavior:

{% highlight java %}
@Singleton
public class SingletonExample{
	private String singletonData;
}
{% endhighlight %}

##### @Named

<i> Named support is pending. </i>

<hr/>

#### Events
Transfuse offers a global event bus in addition to the mapping of the Android lifecycle and callthrough events.

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

Keep in mind that events may contain any relevant data and behavior.  Is it completely definable by the user.  Also, the Observing methods are not called in any particular order, so make sure that the operations are not dependent on each other.

<hr/>
#### Parcels
Transfuse offers a new way of defining Parcelable classes.  The typical implementation of a Parcelable class in Android is riddled with boilerplate.  Not only do users have to define the serialization manually, but also must define a public static final CREATOR class that implements the Parcelable.Creator interface.  Transfuse takes care of all of this for the user.  Simply annotate the class with the @Parcel. annotation.  Transfuse will detect all getter/setter pairs in the class, map it to the designated Bundle serialization method, and produce a Parcelable class:

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


If there is a parameter that the user does not want serialized, annotate the getter or setter with @Transient.

Parcels are useful when passing data between Android components.  Therefore, when using the IntentFactory, Transfuse will automatically detect if a class is annotated with @Parcel and wrap it with the appropriate Parcelable implementation.

<hr/>
#### Injector

There may arise a need to build a dependency graph of a given type outside of a Transfuse dependecy graph.  To solve this, Transfuse offers the capability to define an Injector.  To define an Injector, simply define an inerface, including methods that return the type of the values you require built and annotate it with @Injector.  Transfuse will read the interface and implement the appropraite injections.

For instance, the following interface returns an Example type:

{% highlight java %}
@Injector
public interface TransfuseInjector{
	Example getExample();
}
{% endhighlight %}

To use it, you may inject the Injector or reference the built Injector directly:

{% highlight java %}
public class ExampleUsage{

	@Inject TransfuseInjector injector;

	public void use(){
		Example example = injector.getExample();
	}

	public void staticUsage(){
		Example example = InjectorRepository.get(TransfuseInjector.class).getExample();
	}
}
{% endhighlight %}
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

