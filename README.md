<h2 align="center">Android MVP 快速集成框架</h2>

## 集成方式

### 一、启用视图绑定

将 viewBinding 元素添加到其 build.gradle 文件中

```groovy
android {
    buildFeatures {
        viewBinding true
    }
}
```

### 二、引入Hilt

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

### 三、引用本项目依赖

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
    implementation 'com.github.wxw-9527:MvpArms:2.0.5'
}
```

### 四、使用版本更新功能

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

### 五、条码解析服务

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
4)在Activity的onBarcodeEvent(event: BarcodeEvent)方法中调用EventBus.getDefault()
.cancelEventDelivery(event)可取消事件继续传递

### 六、华为崩溃信息收集服务、分析服务集成

1、将“agconnect-services.json”文件拷贝到Android Studio项目的应用级根目录下

2、在settings.gradle添加仓库地址

```groovy
pluginManagement {
    repositories {
        maven { url 'https://developer.huawei.com/repo/' } // 华为
    }
}
dependencyResolutionManagement {
    repositories {
        maven { url 'https://developer.huawei.com/repo/' } // 华为
    }
}
```

3、在项目的build.gradle文件中添加以下依赖项

```groovy
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath 'com.android.tools.build:gradle:7.4.1' // 华为
        classpath 'com.huawei.agconnect:agcp:1.9.0.300' // 华为
    }
}
```

4、在应用级build.gradle文件添加

```groovy
plugins {
    id 'com.huawei.agconnect' // 华为
}

// 启用华为分析服务
agcp {
    enableAPMS true
}
```

5、在AndroidManifest.xml文件中<application>标签内添加渠道信息

```xml
<!-- 华为分析服务 -->
<meta-data android:name="install_channel" android:value="${channel}" />
```

6、登录成功后调用以下方法上传用户信息

```kotlin
// 记录userId
HuaweiUtil.setUserId(userId)
```

### 七、邀请码获取域名配置

1、继承BaseSplashActivity创建SplashActivity页

```xml

<activity android:name="包名.SplashActivity"
    android:configChanges="keyboardHidden|screenSize|orientation" android:exported="true"
    android:theme="@style/ThemeSplash">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />

        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

2、继承BaseDomainActivity创建DomainConfigActivity页

```xml

<activity android:name=".main.DomainConfigActivity"
    android:configChanges="keyboardHidden|screenSize|orientation"
    android:theme="@style/FullScreenTheme.White" />
```

3、实现IUrlModel接口，并提供使用邀请码获取相关域名的接口地址

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object UrlModule : IUrlModule {

    @Provides
    @Singleton
    @GetDomainConfigurationUrl
    override fun provideDomainConfigurationUrl(): String {
        return "http://dev.zk-work.com/stage-api/system/customer/validCode/"
    }
}
```

4、使用DomainUtils相关方法存取token及域名信息

### 八、集成个推

#### 1、配置 Maven 库地址

在项目根目录settings.gradle文件的dependencyResolutionManagement.repositories节点下，添加个推maven库地址

```groovy
dependencyResolutionManagement {
    repositories {
        maven { url "https://mvn.getui.com/nexus/content/repositories/releases/" } // 个推
    }
}
```

#### 2、配置 SDK 依赖及应用参数

```groovy
android {
    defaultConfig {
        manifestPlaceholders = [
                //从 3.1.2.0 版本开始，APPID 占位符从 GETUI_APP_ID 切换为 GETUI_APPID 
                //后续所有产品的 APPID 均统一配置为 GETUI_APPID 占位符
                GETUI_APPID: "your appid",
        ]
    }
}
dependencies {
    implementation 'com.getui:gtsdk:3.2.18.0'  //个推SDK
    implementation 'com.getui:gtc:3.2.6.0'  //个推核心组件
}  
```

#### 3、设置通知图标

为了修改默认的通知图标以及通知栏顶部提示小图标，请务必在资源目录的
res/drawable-ldpi/、res/drawable-mdpi/、res/drawable-hdpi/、res/drawable-xhdpi/、res/drawable-xxhdpi/
等各分辨率目录下，放置相应尺寸的文件名为 push.png 和 push_small.png 的图片（该图片内容为您应用自定义的图标文件）
建议的 push.png 图片尺寸和 push_small.png 图片尺寸分别如下：

//push.png 图片尺寸
ldpi:    48*48
mdpi:    64*64
hdpi:    96*96
xhdpi:   128*128
xxhdpi:  192*192

//push_small.png 图片尺寸
ldpi:    18*18
mdpi:    24*24
hdpi:    36*36
xhdpi:   48*48
xxhdpi:  72*72
xxxhdpi:  96*96

#### 4、资源精简配置

如果您的工程启用了资源精简，即如果在 app/build.gradle 的 android.buildTypes.release 下配置了
shrinkResources true，为了避免个推 SDK 所需资源被错误精简导致功能异常，需要在项目资源目录 res/raw 中添加
keep.xml 文件，并在 keep.xml 文件中使用 tools:keep 定义哪些资源需要被保留（资源之间用“,”隔开），如
tools:keep="@drawable/push,@drawable/push_small,...,"，此处 @drawable/push、@drawable/push_small
通知图标的名称应为您当前放着于应用中的图标名称，如下：

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources xmlns:tools="http://schemas.android.com/tools" tools:keep="......,
    @drawable/push,
    @drawable/push_small" />
```

#### 5、自定义接收推送服务事件

在项目源码中添加一个继承自 com.igexin.sdk.GTIntentService 的类，用于接收 CID、透传消息以及其他推送服务事件。

```kotlin
/**
 * author : Saxxhw
 * email  : xingwangwang@cloudinnov.com
 * time   : 2023/6/28 15:34
 * desc   :
 */
class IntentService : GTIntentService() {

    /**
     * 个推进程启动成功回调该函数。
     */
    override fun onReceiveServicePid(context: Context, pid: Int) {
        super.onReceiveServicePid(context, pid)
    }

    /**
     * 个推初始化成功回调该函数并返回 cid。
     */
    override fun onReceiveClientId(context: Context, clientid: String) {
        super.onReceiveClientId(context, clientid)
    }

    /**
     * 此方法用于接收和处理透传消息。
     * 透传消息个推只传递数据，不做任何处理，客户端接收到透传消息后需要自己去做后续动作处理，如通知栏展示、弹框等。
     * 如果开发者在客户端将透传消息创建了通知栏展示，建议将展示和点击回执上报给个推。
     */
    override fun onReceiveMessageData(context: Context, pushMessage: GTTransmitMessage) {
        super.onReceiveMessageData(context, pushMessage)
    }

    /**
     * cid 在线状态变化时回调该函数
     */
    override fun onReceiveOnlineState(context: Context, online: Boolean) {
        super.onReceiveOnlineState(context, online)
    }

    /**
     * 调用设置标签、绑定别名、解绑别名、自定义回执操作的结果返回
     * action结果值说明：
     *  10009：设置标签的结果回执
     *  10010：绑定别名的结果回执
     *  10011：解绑别名的结果回执
     *  10012: 查询标签的结果回执
     *  10006：自定义回执的结果回执
     */
    override fun onReceiveCommandResult(context: Context, cmdMessage: GTCmdMessage) {
        super.onReceiveCommandResult(context, cmdMessage)
    }

    /**
     * 通知到达，只有个推通道下发的通知会回调此方法
     */
    override fun onNotificationMessageArrived(
        context: Context,
        notificationMessage: GTNotificationMessage
    ) {
        super.onNotificationMessageArrived(context, notificationMessage)
    }

    /**
     *  通知点击，只有个推通道下发的通知会回调此方法
     */
    override fun onNotificationMessageClicked(
        context: Context,
        notificationMessage: GTNotificationMessage
    ) {
        super.onNotificationMessageClicked(context, notificationMessage)
    }

    /**
     * 检测用户设备是否开启通知权限
     */
    override fun areNotificationsEnabled(context: Context, enable: Boolean) {
        super.areNotificationsEnabled(context, enable)
        if (!enable) {
            PushManager.getInstance().openNotification(context)
        }
    }
}
```

在 AndroidManifest.xml 中配置上述 IntentService 类，如下：

```xml

<service android:name="包名.IntentService" />
```
