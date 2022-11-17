<h2 align="center">Android MVP 快速集成框架</h2>

<p align="center">
   <a href="https://bintray.com/jessyancoding/maven/MVPArms/_latestVersion">
    <img src="https://img.shields.io/badge/Jcenter-v2.5.2-brightgreen.svg?style=flat-square" alt="Latest Stable Version" />
  </a>
  <a href="https://travis-ci.org/JessYanCoding/MVPArms">
    <img src="https://travis-ci.org/JessYanCoding/MVPArms.svg?branch=master" alt="Build Status" />
  </a>
  <a href="https://developer.android.com/about/versions/android-4.0.html">
    <img src="https://img.shields.io/badge/API-14%2B-blue.svg?style=flat-square" alt="Min Sdk Version" />
  </a>
  <a href="http://www.apache.org/licenses/LICENSE-2.0">
    <img src="http://img.shields.io/badge/License-Apache%202.0-blue.svg?style=flat-square" alt="License" />
  </a>
  <a href="https://www.jianshu.com/u/1d0c0bc634db">
    <img src="https://img.shields.io/badge/Author-JessYan-orange.svg?style=flat-square" alt="Author" />
  </a>
  <a href="https://shang.qq.com/wpa/qunwpa?idkey=7e59e59145e6c7c68932ace10f52790636451f01d1ecadb6a652b1df234df753">
    <img src="https://img.shields.io/badge/QQ%E7%BE%A4-455850365%20%7C%20301733278-orange.svg?style=flat-square" alt="QQ Group" />
  </a>
</p>

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
    id 'com.google.dagger.hilt.android' version '2.44.2' apply false
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
    implementation "com.google.dagger:hilt-android:2.44.2"
    kapt "com.google.dagger:hilt-compiler:2.44.2"
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
    implementation 'com.github.wxw-9527:MvpArms:0.0.11'
}
```