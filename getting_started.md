---
layout: default
title: Transfuse
---

### Getting Started

The code produced by this tutorial is located here:  [https://github.com/johncarl81/transfuse/tree/master/examples/helloAndroid][2]

#### Prerequisites

Transfuse requires a Java 6 (or greater) JDK and the [Android SDK][3].

#### Create an Android project

An easy way to get up and running with a new Android project is to use the Maven archetype:

{% highlight bash %}

mvn archetype:generate \
  -DarchetypeArtifactId=android-quickstart \
  -DarchetypeGroupId=de.akquinet.android.archetypes \
  -DarchetypeVersion=1.0.8 \
  -DgroupId=org.androidtransfuse \
  -DartifactId=hello-transfuse

{% endhighlight %}

After running this you should have a basic Android application.

Remove the generated Activity class (HelloAndroidActivity.java) as well as the Application entry in the AndroidManifest.xml.  Don't worry, Transfuse will add the Application xml back and manage it for you.  Your AndroidManifest.xml should look like this:

{% highlight xml %}
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android" 
package="org.androidtransfuse" android:versionCode="1" android:versionName="1.0-SNAPSHOT">
</manifest>
{% endhighlight %}

Worth noting, Transfuse is legacy friendly and allows you to migrate your Activities and other Android components into Transfuse as you wish, so for applications with an existing codebase the previous step is not required but some manual merging may be needed.

#### Download Transfuse

Add the transfuse dependencies into your Maven pom:

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

You may also download Transfuse directly from the [[Download page.](download.html)]

Notice that the only runtime requirement for Transfuse is the api library.

#### Code

Now you're ready to build your first Transfuse Activity.  Update the default layout (main.xml) in the res/layout folder:

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

Although syntactically different than the standard Android Activity, this should seem familiar to the seasoned Android developer.  With Transfuse the properties previously defined in the AndroidManifest.xml file are now defined and associated directly with the Activity code.  This follows the DRY principle, keeping your Activity definition in one place instead of split between your class files and xml resources.  Also notice that you are no longer required to extend Activity (or one of its extension classes) and define the typical boilerplate.  Transfuse allows you to develop your Android components in a POJO way, unrestricted by the extension-based API of Android.

#### Build and Deploy

Transfuse requires a full build to generate the necessary class files and resources, so it is recommended to use Maven or Ant to cleanly build your project each time you want to deploy your application.

{% highlight bash %}

> mvn clean package android:deploy

{% endhighlight %}

You should now have a fully functional Android application built using Transfuse.

[1]: http://developer.android.com/training/index.html
[2]: https://github.com/johncarl81/transfuse/tree/master/examples/helloAndroid
[3]: http://developer.android.com/sdk/index.html
