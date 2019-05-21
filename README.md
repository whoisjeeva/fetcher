[![](https://jitpack.io/v/anyms/fetcher.svg)](https://jitpack.io/#anyms/fetcher)
[![Build Status](https://travis-ci.org/anyms/fetcher.svg?branch=master)](https://travis-ci.org/anyms/fetcher)
[![License](https://img.shields.io/github/license/anyms/fetcher.svg)](https://github.com/anyms/fetcher/blob/master/LICENSE)
[![codecov.io](https://codecov.io/github/anyms/fetcher/coverage.svg?branch=master)](https://codecov.io/github/anyms/fetcher)

# Fetcher - A Human Friendly HTTP Library for Android

Fetcher is for human by human.

![image](https://i.ibb.co/tpB7DqP/black-german-shepherd-d-4.jpg)

# Getting Started

Add it in your root build.gradle at the end of repositories

```css
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency

```css
dependencies {
        implementation 'com.github.anyms:fetcher:Tag'
}
```

# Using it so Simple.

Create a fetcher instance.

```kotlin
val fetcher = Fetcher()
```

Now you are ready to see the power of fetcher.

```kotlin
// simple GET request
fetcher.get("http://httpbin.org/get")
    .ifSucceed { response -> }
    .ifFailed { response -> }

// simple POST request
fetcher.post("http://httpbin.org/post")
    .ifSucceed { response -> }
    .ifFailed { response -> }

// sending parameters with your request
val argument = Argument()
argument.params["name"] = "Fetcher"
argument.params["age"] = 1
fetcher.get("http://httpbin.org/get", argument)
    .ifSucceed { response -> }
    .ifFailed { response -> }
```

Download binary files

```kotlin
val argument = Argument()
argument.isStream = true
fetcher.get("http://httpbin.org/get", argument)
    .ifSucceed { response -> }
    .ifFailed { response -> }
    .ifStream { bytes ->
        if (bytes != null) {
            // writing bytes to a file
            ...
        } else {
            // closing the opened file
            ...
        }
    }
```
