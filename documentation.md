---
layout: default
title: Transfuse
---

### Documentation

#### Introduction
The Android API has a common theme throughout its components; each component must be implemented as an extension of a base component.  Although this approach works, it has subtle side effects. If the component implements many separate features, the component class quickly becomes a mismatch of behavior, resulting in hard-to-read and hard-to-test classes. Also, any third party library based on the component lifecycle or functionality provided by a Context must extend the given component class.  Because of Java's single extension policy, this action makes these third party libraries that leverage Context components incompatible with each other.

Additionally, each component must be registered individually in the AndroidManifest.xml file.  It is easy to overlook the need to register a new component, only to remember after it is already deployed to a emulator or device.  This duplication of registration and declaration violates the Don't-Repeat-Yourself (DRY) principle.

Transfuse resolves these issues in a number of different ways. First, Transfuse changes the model of Android components into POJOs, allowing users to develop components the way they want.  There is no need to keep extending Activity, Service, etc. in order to implement the corresponding component. Now, all that is necessary is to annotate the component classes to register them in the Android application.  This registration action tells Transfuse to add the component to the Android Manifest, essentially eliminating manual editing and management of the Manifest.

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

Transfuse follows the convention of declaring the layout directly after the super.onCreate() call in the root Activity.

If the use cases require a more advanced layout declaration, like defining the layout programatically, use the @LayoutHandler annotation and LayoutHandlerDelegate interface:

{% highlight java %}
@Activity
@LayoutHandler(LunchTimeLayoutDelegate.class)
public class Example{}
{% endhighlight %}

{% highlight java %}
public class LunchTimeLayoutDelegate implements LayoutHandlerDelegate{
	@Inject Activity activity;

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

A key feature of Transfuse is defining the AndroidManifest.xml metadata within the Java class declaration.  All manifest metadata is available either as parameters of the @Activity annotation or as additional annotations on the class level.  This follows the DRY principle, keeping the declaration and configuration of the Activity in one place.

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

<div class="note">
<h5>Note</h5>
To track changes to the manifest, Transfuse adds to the managed xml tags the t:tag parameter.
</div>

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

In the Example class below, the log() method is executed during the onCreate phase of the Activity lifecycle:

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

<div class="note">
<h5>Note</h5>
Lifecycle events will not be called in any predefined order.
</div>

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

##### Injection Qualifiers

There are a number of qualified injections available within the Activity injection graph.  Each qualifier designates a different source to draw the injection from.

###### @Extra

Extras are defined by a string name and the given type and may be declared optional if applicable.  Using this qualifier along with the Intent Factory helps enforce the contract specified on the Intent.

The following Extra injection:

{% highlight java %}
@Activity
public class Example{
	@Inject @Extra("one")
	String one;
	@Inject @Extra(value = "two", optional = true)
	String two;
}
{% endhighlight %}

First, requires a string value named one to be provided in the Bundle while starting the Example Activity.  Secondarily has the option to inject an extra String value named two.  If the Extra two is not provided in the Intent starting the Example Activity, two will be null.

###### @Resource

The Resource qualifier specifies the given injection draws from the Application's Resources found on the Context.getResources() method call.  Each resource is looked up by name and by type.  For instance, the following injection:

{% highlight java %}
@Activity
public class Example{
	@Inject @Resource(R.string.app_name)
	String appName;
}
{% endhighlight %}
 
looks up the appName resource by String type:

{% highlight java %}
getApplication().getResources().getString(R.string.app_name);
{% endhighlight  %}

###### @View

The View qualifier identifies the widget to inject from the view higherarchy set up during the onCreate phase.  As an example, we may look up an TextView by id with the following:

{% highlight java %}
@Activity
public class Example{
	@Inject @View(R.id.viewText)
	ViewText viewText;
}
{% endhighlight %}

optionally the view may be injected by tag value:

{% highlight java %}
@Activity
public class Example{
	@Inject @View(tag = "taggedView")
	ViewText viewText;
}
{% endhighlight %}

The View qualifier does perform the necessary casting from the getViewById() method, but makes the assumption that the type declared is correct.  This may cause issues when the type is incorrectly assocaited with the given View widget.

##### @Preference

The Preference qualifier draws a value by type and name from the application's shared preferences.  The following example highlights injecting a preferenced named "favorite_color" and String type:

{% highlight java %}
@Activity
public class Example{
	@Inject @Preference(value = "favorite_color", default = "green")
	String favColor;
}
{% endhighlight %}

A default value must be provided with each preference injection.  These defaults will only be used if the preference is not specified.

###### @SystemService

All system services are mapped into the injection context by type:

{% highlight java %}
@Activity
public class Example{
	@Inject
	LocationManager locationManager;
}
{% endhighlight %}

Optionally you may inject into a base type of the given system service, but the system service type must be specified.  This may be helpful if the given system service is not mapped by Transfuse by type:

{% highlight java %}
@Activity
public class Example{
	@Inject @SystemService(Context.LOCATION_SERVICE)
	LocationManager locationManager;
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

Service may be injected as described in the Injection section:

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

Annotating the class with the BroadcastReceiver annotation activates the class as an Android Broadcast Receiver component.

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

##### @Application

Annotating the class with the Activity annotation activates the class as an Android Application component.  There may be only one of these components through a Transfuse application.

The annotated application class has the following lifecycle events available via the lifecycle event annotations:

{% highlight java %}
@OnCreate
@OnLowMemory
@OnTerminate
@OnConfigurationChanged
{% endhighlight %}

These annotations correspond to the similarly named lifecycle events available on the Application class.

<hr/>

#### Intent Factory

Both Service and Activities may be started with a set of named Extras.  These Extras represent a contract on the intent used to start the given component.  To enforce this contract, Transfuse offers an Intent Factory to build intents based on a structured parameter; the Intent Strategy.  An Intent Strategy is generated for each component defined in Transfuse.

The following Activity has two types of Extras, required and optional:

{% highlight java %}
@Activity
public class ExtraActivity{
	@Inject @Extra("name") 
	String name;
	@Inject @Extra(value="age", optional=true)
	Integer age;
}
{% endhighlight %}

You may build and start the ExtraActivity with the IntentFactory:

{% highlight java %}
@Activity
public class CallingActivity{
	@Inject
	IntentFactory intentFactory;
	public void openExtraActivity() {
		intentFactory.start(new ExtraActivityIntentStrategy("Andy").setAge(42));
	}
}
{% endhighlight %}

Required injections are given using the IntentStrategy constructor as optional parameters are given using setters.

<hr/>

#### Dependency Injection (DI)

Transfuse implements JSR330, the same standard many of the leading DI frameworks implement.  The following annotations are available:

##### @Inject

Transfuse allows user to inject into the constructor, methods and fields of a class.  These injections may be public, package private, protected or private.  Users should prefer (in order) constructor injection, method, and then field injection.  Likewise, for performance reasons, users should prefer public, package private or protected injections over private.  Private injections requires Transfuse to use reflection at runtime and for large dependency graphs may significantly affect performance.

<div class="note">
<h5>Note</h5>
This documentation highlights using package private field injection because it is the most succinct.  Public constructor injection should be prefered.
</div>

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
	Example example; //calls .get() to resolve example
	@Inject
	Provider<Example> exampleProvider; // determines the provider type by generics
	@Inject
	ExampleProvider concreteInjection;
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
	...
}
{% endhighlight %}

##### @Named

Named support is pending.

##### Advanced

For completeness, Transfuse allows the declaration of dependency loops.  For Transfuse to instantiate depenency loops, at least one depenency in the loop must be injected via an interface.

###### @ImplementedBy

ImplementedBy support is pending

###### @ContextScope

With the need to instantiate one, and only one, instance of a class per Context, Transfuse allows the declaration of a Context singleton.

<hr/>

#### Method Interceptors

Transfuse offers a basic AOP facility of method interception.  This feature is based on the AOPAlliance MethodInterceptor specification.  A couple useful method interceptors are defined by transfuse:

##### @Asynchronous
Annotating a method with @Asynchronous tells Transfuse to proxy the execution of the method and execute it within its own thread.  The method will execute and the calling thread will return immediately.

##### @UIThread
Annotating a method with @UIthread will execute the given method through an Android Handler.  This puts the execution of the method back on the UI thread.

<div class="note">
<h5>Note</h5>
If a return value is declared on the intercepted method, the Asynchronous and UIThread interceptors will return null.
</div>
##### Configuration

Custom method interceptors may be defined by associating a MethodInterceptor class with a custom annotation.  These are assocaited in the TransfuseModule with the @BindInterceptor annotation.

Example:

{% highlight java %}
@Target(METHOD)
public @interface Log {}
{% endhighlight %}

{% highlight java %}
public class LogInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Log.i("Interception", "start");
        Object ret = invocation.proceed();
        Log.i("Interception", "finish");

        return ret;
    }
}
{% endhighlight %}

{% highlight java %}
@TransfuseModule
public interface Module{
	@BindInterceptor(Log.class)
	LogInterceptor getLogInterceptor();
}
{% endhighlight %}

This example shows an interceptor that logs the starting and ending points of a method call.  All that is needed to use this method is to annotated a method like so:

{% highlight java %}
public class Example{
	@Log
	public void methodCall() {
	}
}
{% endhighlight %}


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
	EventManager eventManager;

	public void trigger(){
		eventManager.trigger(new Event());
	}
}
{% endhighlight %}

Keep in mind that events may contain any relevant data and behavior.  Is it completely definable by the user.  Also, the Observing methods are not called in any particular order, so make sure that the operations are not dependent on each other.

<hr/>
#### Parcels

Transfuse offers a new way of defining Parcelable classes.  The typical implementation of a Parcelable class in Android is riddled with boilerplate.  Not only do users have to define the serialization manually, but also must define a public static final CREATOR class that implements the Parcelable.Creator interface.  Transfuse takes care of all of this.  Simply annotate the class with the @Parcel. annotation.  Transfuse will detect all java bean format getter/setter pairs, map it to the designated Bundle serialization method, and produce a Parcelable class:

{% highlight java %}
@Parcel
public void CleanParcel{
	private String name;
	private int age;
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

There may arise a need to build a dependency graph of a given type outside of a Transfuse dependecy graph.  To solve this, Transfuse offers the capability to define an Injector.  To define an Injector, simply define an inerface, including methods that return the type of the values users require built and annotate it with @Injector.  Transfuse will read the interface and implement the appropraite injections.

For instance, the following interface returns an Example type:

{% highlight java %}
@Injector
public interface TransfuseInjector{
	Example getExample();
}
{% endhighlight %}

To use it, inject the Injector or reference the built Injector directly:

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

<hr/>
#### Legacy Support

In an ideal world, users are able to develop a new application.  Realistically however, users are often stuck with a legacy code base.  Transfuse anticipates this, and the AndroidManifest.xml management is flexible enough to mix Transfuse components with regular Android components.  The following options are available when dealing with legacy Android applications:

<ul class="square">
<li>
 Define Android components as normal, and register them in the AndroidManifest.xml.  By using this option, users will not be able to use a majority of Transfuse's features and Transfuse will not register the component in the AndroidManifest.xml file.  However, if a component is manually added to the AndroidManifest.xml file, Transfuse will detect the additions, preserve them and work around them. </li>

<li> Define Android components as normal, and annotate it to be managed in the AndroidManifest.xml by Transfuse.  Transfuse detects if the annotated component extends an Android component, and if so, it will add it to the manifest.</li></ul>

<div class="note">
<h5>Note</h5>
Dependency Injection and the other code generation features are not available on legacy Android components
</div>

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

