# Confectionery

[![GitHub release](https://img.shields.io/github/release/t0rr3sp3dr0/Confectionery-Android.svg)](/../../releases) [![GitHub tag](https://img.shields.io/github/tag/t0rr3sp3dr0/Confectionery-Android.svg)](/../../tags) [![GitHub issues](https://img.shields.io/github/issues/t0rr3sp3dr0/Confectionery-Android.svg)](/../../issues) [![GitHub contributors](https://img.shields.io/github/contributors/t0rr3sp3dr0/Confectionery-Android.svg)](/../../contributors) [![GitHub license](https://img.shields.io/github/license/t0rr3sp3dr0/Confectionery-Android.svg)](/LICENSE.txt) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.t0rr3sp3dr0/confectionery/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.t0rr3sp3dr0/confectionery) [![Release](https://jitpack.io/v/t0rr3sp3dr0/Confectionery-Android.svg)](https://jitpack.io/#t0rr3sp3dr0/Confectionery-Android) [![Javadocs](http://www.javadoc.io/badge/com.github.t0rr3sp3dr0/confectionery.svg)](https://www.javadoc.io/doc/com.github.t0rr3sp3dr0/confectionery)

A powerful Android Library that simplifies a lot of common tasks in application development.

* Eliminate calls to `Activity#setContentView`, `LayoutInflater#inflate`, `View#findViewById`, `DataBindingUtil#setContentView`, and `DataBindingUtil#inflate` by using Android Data Binding Library and Java Reflection API.
* No more `NotSerializableException` when trying to pass non-serializable objects to `Activity` or `Fragment` via `Bundle`. Now you can pass a `Map<String, Object>` instead of a regular `Bundle`.
* Forgetting to implement `OnFragmentInteractionListener` and `OnListFragmentInteractionListener` is no longer an issue. Their implementation of is now completely optional.
* To create a `RecyclerView` it's no longer necessary to create an `RecyclerView.Adapter`, `RecyclerView.ViewHolder`, link manually your data set to the UI, set the adapter, none of those things. It is only necessary to inform the data set, number of columns to be displayed, and what layout should be used to inflate. All other tasks are done on the fly.
* The task of adding, replacing, and restarting fragments is now way more simplified. It can be done by calling `#addFragment`, `#replaceFragment`, and `#restartFragment` from almost anywhere in your code. Optionally, you can also set default transaction animations to those methods.

## Extra Utilities

* `CircularImageView` - An `ImageView` that crops your image in a circular shape.
* `PhoneNumberEditText` - An `EditText` that uses the latest version of Google's common library for parsing, formatting, and validating international phone numbers.

## Getting Started

### Requirements

* API Level 17 or higher
* Data Binding enabled

### Build Environment

To get started with Data Binding, download the library from the Support repository in the Android SDK manager.

To configure your app to use data binding, add the `dataBinding` element to your `build.gradle` file in the app module.

Use the following code snippet to configure data binding:

```gradle
android {
    ...
    dataBinding {
        enabled true
    }
}
```

If you have an app module that depends on a library which uses data binding, your app module must configure data binding in its `build.gradle` file as well.

Also, make sure you are using a compatible version of Android Studio. **Android Studio 1.3** and later provides support for data binding as described in [Android Studio Support for Data Binding](https://developer.android.com/topic/libraries/data-binding/index.html#studio_support).

### Add Dependency

In Android Studio, add the dependency to your app-level `build.gradle` file.

```gradle
dependencies {
    ...
    compile 'com.github.t0rr3sp3dr0:confectionery:0.0.3'
}
```

#### Snapshots

Snapshots of the development version are available in [Sonatype's `snapshots` repository](https://oss.sonatype.org/content/repositories/snapshots/).

Use the following code snippet to be able to access snapshots:

```gradle
repositories {
    ...
    maven {
        url 'https://oss.sonatype.org/content/repositories/snapshots/'
    }
}

dependencies {
    ...
    compile 'com.github.t0rr3sp3dr0:confectionery:0.0.4-SNAPSHOT'
}

configurations.all {
    ...
    resolutionStrategy.cacheDynamicVersionsFor 0, 'seconds'
    resolutionStrategy.cacheChangingModulesFor 0, 'seconds'
}
```

### Migrating From Android Support Library

To prioritize compatibility, Confectionery extends the Android Support Library. That means all code written using the Android Support Library will be fully supported. Code that doesn't use it might need revision.
Initially it will be necessary to turn most of your layout files into data-binding layout files. It's a bit of extra work that will pay off in the future.

#### Data Binding Layout Files

Data-binding layout files are slightly different and start with a root tag of `layout` followed by a `data` element and a view root element. This view element is what your root would be in a non-binding layout file. A sample file looks like this:

###### Non-binding layout file

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    
    <TextView
        android:id="@+id/firstName"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
        
    <TextView
        android:id="@+id/lastName"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
</LinearLayout>
```

###### Data-binding layout file

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android">

    <data>

        <variable name="user" type="com.example.User" />
    </data>

    <LinearLayout
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <TextView
            android:id="@+id/firstName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text='@{user.firstName}' />

        <TextView
            android:id="@+id/lastName"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text='@{user.lastName}' />
    </LinearLayout>
</layout>
```

The user `variable` within `data` describes a property that may be used within this layout.

```xml
<variable name="user" type="com.example.User" />
```

Expressions within the layout are written in the attribute properties using the `'@{}'` syntax. Here, the `TextView` text is set to the `firstName` property of `user`:

```xml
<TextView
    android:id="@+id/firstName"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text='@{user.firstName}' />
```

Layout files don't require `'@{}'` syntax to become data-binding layout files. The `data` and `variable` elements are also optional. However, `layout` needs to become the new root element by encapsulating your root view element.

For more information about how to use the Data Binding Library to write declarative layouts and minimize the glue code necessary to bind your application logic and layouts, access: [https://developer.android.com/topic/libraries/data-binding/](https://developer.android.com/topic/libraries/data-binding/).

#### Activity

Sample files to demonstrate how to turn an `AppCompatActivity` into a `CandyActivity`:

###### SampleActivity.java

```java
package me.t0rr3sp3dr0.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);
    }
}
```

###### activity_sample.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_sample"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:paddingBottom="@dimen/activity_vertical_margin"
    android:paddingLeft="@dimen/activity_horizontal_margin"
    android:paddingRight="@dimen/activity_horizontal_margin"
    android:paddingTop="@dimen/activity_vertical_margin"
    tools:context="com.t0rr3sp3dr0.myapplication.SampleActivity">

    <TextView
        android:id="@+id/textView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Hello World!" />
</RelativeLayout>
```

For starters we will turn our non-binding layout file into a data-binding layout file like shown previously. Afterwards your layout file should look like this:

###### activity_sample.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <RelativeLayout
        android:id="@+id/activity_sample"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:paddingBottom="@dimen/activity_vertical_margin"
        android:paddingLeft="@dimen/activity_horizontal_margin"
        android:paddingRight="@dimen/activity_horizontal_margin"
        android:paddingTop="@dimen/activity_vertical_margin"
        tools:context="com.t0rr3sp3dr0.myapplication.SampleActivity">

        <TextView
            android:id="@+id/textView"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Hello World!" />
    </RelativeLayout>
</layout>
```

In your Java file, `SampleActivity` extends `AppCompatActivity`, but soon will extend `CandyActivity` instead.
`CandyActivity` is a Confectionery class that adds many helpful features.

First, we need to bind your data-binding layout file by passing layout's Binding Class as a generic type to `CandyActivity`. Then we will remove the `Activity#setContentView` call. Now your activity should work as before, but with all Confectionary features available.

###### SampleActivity.java

```java
package me.t0rr3sp3dr0.myapplication;

import android.os.Bundle;

import me.t0rr3sp3dr0.confectionery.abstracts.CandyActivity;
import me.t0rr3sp3dr0.myapplication.databinding.ActivitySampleBinding;

public class SampleActivity extends CandyActivity<ActivitySampleBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
```

From now on to access views we will use the `CandyActivity#getBinding` method, which returns the instance of your activity's layout's Binding Class. In this instance are all the views which have an id in the layout file. The id is used to access individual views.

In the code snippets below, the difference between standard and data-binding can be noticed. Now, no type casting or variable declaration is required, as `CandyActivity#getBinding` accesses the views in constant time.

###### SampleActivity.java

```java
package me.t0rr3sp3dr0.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import me.t0rr3sp3dr0.confectionery.abstracts.CandyActivity;
import me.t0rr3sp3dr0.myapplication.databinding.ActivitySampleBinding;

public class SampleActivity extends CandyActivity<ActivitySampleBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText("Activity#findViewById");

        getBinding().textView.setText("CandyActivity#getBinding");
    }
}
```

Finally, to pass non-serializable objects to another `CandyActivity` a "dummy" class will be created, which contains the text to be shown in a new activity. Confectionary allows you to pass data between activities using `Map<String, Object>` instead of `Bundle`. In this case, we will be adding an instance of our "dummy" class to the map. Then, after starting the activity, we can call `CandyActivity#getObject` passing the key from the map to retrieve our "dummy" object.

###### SampleActivity.java

```java
package me.t0rr3sp3dr0.myapplication;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

import me.t0rr3sp3dr0.confectionery.abstracts.CandyActivity;
import me.t0rr3sp3dr0.myapplication.databinding.ActivitySampleBinding;

public class SampleActivity extends CandyActivity<ActivitySampleBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Dummy dummy = (Dummy) getObject("dummy");

        if (dummy == null) {
            // If dummy is null, that means no map was passed to this activity
            // or that the map doesn't have anything in the key "dummy". In our
            // case, that means it's the original activity.

            Map<String, Object> map = new HashMap<>();
            map.put("dummy", new Dummy("CandyActivity#startActivity"));

            startActivity(SampleActivity.class, map);
        } else {
            // A non-null object was passed in the Map<String, Object> to this
            // activity. In our application, it means that it's the new
            // activity we just started

            getBinding().textView.setText(dummy.text);
        }
    }

    public class Dummy {
        public String text;

        public Dummy(String text) {
            this.text = text;
        }
    }
}
```

For more information about `CandyActivity` see [Confectionery Sample Application](/app/src/main/java/me/t0rr3sp3dr0/confectionery/example).

## Known Issues

* Data Binding does not support Jack builds yet.
* Custom Binding Class Names are currently not supported.
