# Unison Gradle Plugin [![Latest Release](https://img.shields.io/github/tag/dant3/unison-gradle-plugin.svg?label=Latest Release on JitPack)](https://jitpack.io/#dant3/unison-gradle-plugin)

Plugin for posting comments as part of your build.

This plugin for Gradle allows you to post comments to [Unison](https://unison.com).
To use it, simply include the dependencies via `buildscript {}` and `'apply'` the plugin:

```groovy
buildscript {
  repositories {
    jcetner()
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
  login 'foo'
  password 'bar'
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

## Tasks

Plugin adds the following tasks to your project:

* `listRooms` - lists all rooms available with supplied credentials. This is a helper task to find a room you want to post in.
* `listTopics` - lists all topics in room you've selected by configuration. This is a helper task to fild a topic you want to post in.
* `createComment` - creates a comment with supplied text in a room and it's topic provided by configuration.

## If tasks are not enough

Sometimes you need more then just tasks though. Imagine you want to report about build failures. You can hook `gradle.buildFinished`, but then you can't use
Gradle tasks since build is already finished. No worries, we got you covered. Plugin extends `unison` extension with some methods which are duplicating it's tasks
functionality. In this case we could do it like this:

```groovy
gradle.buildFinished { result ->
    if (result.failure) {
        StringWriter stacktrace = new StringWriter();
        result.failure.printStackTrace(new PrintWriter(stacktrace));
        unison.createComment("Build of $project FAILED. Reason is:\n${stacktrace}".
            replaceAll("\n", "<br>")
            /* this is needed since unison uses subset of html for markup */
        )
    }
}
```
