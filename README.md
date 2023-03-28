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
    implementation 'com.github.wxw-9527:MvpArms:1.6.4'
}
```

四、使用版本更新功能

在需要检测新版本的页面P层调用以下方法检查新版本
```kotlin
/**
 * 获取版本更新信息
 * @param clientType 客户端标志
 * @param clientName 项目名称
 * @param channel 渠道名
 */
fun getUpdateInfo(clientType: String = "android", clientName: String, channel: String)
```
重写showUpdateInfo(updateInfo: UpdateInfo)方法可自定义版本更新处理逻辑


五、条码解析服务

1、必须：
    在需要解析条码的Activity、Fragment的父Activity类增加@BarcodeScanningReceiverEnabled标记

2、仅Activity中使用：
    1)仅在该Activity上增加@EventBusEnabled标记
    2)实现showBarcodeInfo(barcodeInfo: BarcodeInfoVO)方法处理条码数据

3、仅Fragment中使用：
    1)仅在该Fragment上增加@EventBusEnabled标记
    2)实现showBarcodeInfo(barcodeInfo: BarcodeInfoVO)方法处理条码数据

4、高阶使用
    1)覆写Activity或Fragment中的onBarcodeEvent(event: BarcodeEvent)方法可自行处理条码内容
    2)覆写Presenter中的getBarcodeInfo(barcode: String)方法可自行处理条码解析方法
    3)覆写Presenter中的handleBarcodeInfo(barcodeInfo: BarcodeInfoVO)方法可自行处理条码上下文数据
    4)在Activity的onBarcodeEvent(event: BarcodeEvent)方法中调用EventBus.getDefault().cancelEventDelivery(event)可取消事件继续传递