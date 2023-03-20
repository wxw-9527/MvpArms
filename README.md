<h2 align="center">Android MVP 快速集成框架</h2>

## 集成方式

一、启用视图绑定

将 viewBinding 元素添加到其 build.gradle 文件中

```groovy
android {
    buildFeatures {
        viewBinding true
    }
}
```

二、引入Hilt

1、将 hilt-android-gradle-plugin 插件添加到项目的根级 build.gradle 文件中

```groovy
plugins {
    id 'com.google.dagger.hilt.android' version '2.45' apply false
}
```

2、应用 Gradle 插件并在 app/build.gradle 文件中添加以下依赖项

```groovy
plugins {
    id 'kotlin-kapt'
    id 'com.google.dagger.hilt.android'
}

android {

}

dependencies {
    implementation "com.google.dagger:hilt-android:2.45"
    kapt "com.google.dagger:hilt-compiler:2.45"
}

// Allow references to generated code
kapt {
    correctErrorTypes true
}
```

3、所有使用 Hilt 的应用都必须包含一个带有 @HiltAndroidApp 注解的 Application 类。

```kotlin
@HiltAndroidApp
class Application : BaseApplication() {}
```

三、引用本项目依赖

1、在settings.gradle添加JitPack仓库地址

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url 'https://jitpack.io' } // JitPack
    }
}
```

2、添加依赖

```groovy
dependencies {
    implementation 'com.github.wxw-9527:MvpArms:1.5.3'
}
```