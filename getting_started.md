---
layout: default
title: Transfuse
---

### Getting Started

The code produced by this tutorial is located at   [https://github.com/johncarl81/transfuse/tree/master/examples/helloAndroid][2]

#### Prerequisites

Transfuse requires a Java 6 (or greater) JDK and the [Android SDK][3].

#### Create an Android Project

Use the Maven archetype to run a new Android project, creating a basic Android application:

{% highlight bash %}

mvn archetype:generate \
  -DarchetypeArtifactId=android-quickstart \
  -DarchetypeGroupId=de.akquinet.android.archetypes \
  -DarchetypeVersion=1.0.8 \
  -DgroupId=org.androidtransfuse \
  -DartifactId=hello-transfuse

{% endhighlight %}

NOTE:  Transfuse will add the Application xml back and manage it for the user. Transfuse is legacy friendly and gives the user the ability to migrate Activities and other Android components into Transfuse as needed. For applications with an existing codebase the step below is not required, but some manual merging may be needed.

Remove the generated Activity class (HelloAndroidActivity.java) as well as the Application entry in the AndroidManifest.xml.


The AndroidManifest.xml should look like this:

{% highlight xml %}
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android" 
package="org.androidtransfuse" android:versionCode="1" android:versionName="1.0-SNAPSHOT">
</manifest>
{% endhighlight %}



#### Download Transfuse

Add the transfuse dependencies into the Maven pom:

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

Transfuse also can be downloaded directly from the [[Download page.](download.html)]

Note: The only runtime requirement for Transfuse is the api library.

#### Code

To build a Transfuse Activity, update the default layout (main.xml) in the res/layout folder:

{% highlight xml %}
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="fill_parent"
              android:layout_height="fill_parent">
    <TextView xmlns:android="http://schemas.android.com/apk/res/android"
              android:id="@+id/textview"
              android:layout_width="fill_parent"
              android:layout_height="fill_parent"/>
</LinearLayout>
{% endhighlight %}

And the accompanying Transfuse Activity:

{% highlight java %}
// Example Transfuse Activity
@Activity(label = "@string/app_name")
@IntentFilter({
        @Intent(type = IntentType.ACTION, name = android.content.Intent.ACTION_MAIN),
        @Intent(type = IntentType.CATEGORY, name = android.content.Intent.CATEGORY_LAUNCHER)
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

Although syntactically different than the standard Android Activity, this should seem familiar to the seasoned Android developer.  With Transfuse, the properties previously defined in the AndroidManifest.xml file are now defined and associated directly with the Activity code.  This follows the DRY principle, keeping the Activity definition in one place instead of split between  class files and xml resources.  There is no longer a requirement to extend Activity (or one of its extension classes) and define the typical boilerplate.  Transfuse allows development of Android components in a POJO way, unrestricted by the extension-based API of Android.

#### Build and Deploy

Transfuse requires a full build to generate the necessary class files and resources. Use Maven or Ant to cleanly build projects each time to deploy applications.

{% highlight bash %}

> mvn clean package android:deploy

{% endhighlight %}

A fully functional Android application has now been built using Transfuse.

[1]: http://developer.android.com/training/index.html
[2]: https://github.com/johncarl81/transfuse/tree/master/examples/helloAndroid
[3]: http://developer.android.com/sdk/index.html
