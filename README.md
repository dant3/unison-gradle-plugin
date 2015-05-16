# Unison Gradle Plugin [![Latest Release](https://img.shields.io/github/tag/dant3/unison-gradle-plugin.svg?label=Latest Release on JitPack)](https://jitpack.io/#dant3/unison-gradle-plugin)

Plugin for posting comments as part of your build.

This plugin for Gradle allows you to post comments to [Unison](https://unison.com).
To use it, simply include the dependencies via `buildscript {}` and `'apply'` the plugin:

```groovy
buildscript {
  repositories {
    maven { url "https://jitpack.io" } 
  }
  dependencies {
    classpath "com.github.dant3:unison-gradle-plugin:${latest_version}"
  }
}

version "0.1"
group "example"

apply plugin: "unison"

unison {
  login = 'foo'
  password = 'bar'
  roomID 'abcdef12345'
  topicID 'zxcvbn98765'
  commentText 'Hello, world!'
}
```

Parameter `commentText` can also be a closure which helps you to define your comment text depending on current context:

```groovy
unison {
  commentText {
    if (someFile.exists) {
       "File someFile exists!"
    } else {
       "No file found! Exiting!"
    }
  }
}
```
