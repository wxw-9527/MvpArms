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

#### 1、将 hilt-android-gradle-plugin 插件添加到项目的根级 build.gradle 文件中

```groovy
plugins {
    id 'com.google.dagger.hilt.android' version '2.51.1' apply false
}
```

#### 2、应用 Gradle 插件并在 app/build.gradle 文件中添加以下依赖项

```groovy
plugins {
    id 'com.google.devtools.ksp'
    id 'com.google.dagger.hilt.android'
}

android {

}

dependencies {
    implementation "com.google.dagger:hilt-android:2.51.1"
    ksp "com.google.dagger:hilt-compiler:2.51.1"
}
```

#### 3、所有使用 Hilt 的应用都必须包含一个带有 `@HiltAndroidApp` 注解的 Application 类。

```kotlin
@HiltAndroidApp
class Application : BaseApplication() {}
```

### 三、引用本项目依赖

#### 1、在settings.gradle添加JitPack仓库地址

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url 'https://jitpack.io' } // JitPack
    }
}
```

#### 2、添加依赖

```groovy
dependencies {
    implementation 'com.github.wxw-9527:MvpArms:2.9.1.00'
}
```

### 四、使用版本更新功能

#### 1、在需要检测新版本的页面P层调用以下方法检查新版本

```kotlin
/**
 * 获取版本更新信息
 * @param clientType 客户端标志
 * @param clientName 项目名称
 * @param channel 渠道名
 */
fun getUpdateInfo(clientType: String = "android", clientName: String, channel: String)
```

#### 2、在需要检测新版本的页面调用以下方法

```kotlin
override fun onDestroy() {
    super.onDestroy()
    DownloadUtil.getInstance().onDestroy()
}
```

#### 3、重写`showUpdateInfo(updateInfo: UpdateInfo)`方法可自定义版本更新处理逻辑

### 五、条码解析服务

#### 1、在需要解析条码的Activity类或Fragment的父Activity类增加`@BarcodeScanningReceiverEnabled`标记，在解析条码的Activity或Fragment类增加`@EventBusEnabled`标记

#### 2、实现`showBarcodeInfo(barcodeInfo: BarcodeInfoVO)`方法处理条码数据

#### 3、高阶使用

1) 覆写Activity或Fragment中`的onBarcodeEvent(event: BarcodeEvent)`方法可自行处理条码内容
2) 覆写Presenter中的`getBarcodeInfo(barcode: String)`方法可自行处理条码解析方法
3) 覆写Presenter中的`handleBarcodeInfo(barcodeInfo: BarcodeInfoVO)`方法可自行处理条码上下文数据
4) 覆写Activity中的`onParseNfcIntent(intent: Intent?)`方法可自行处理NFC意图

### 六、华为崩溃信息收集服务、分析服务集成

#### 1、将“agconnect-services.json”文件拷贝到Android Studio项目的应用级根目录下

#### 2、在settings.gradle添加仓库地址

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

#### 3、在项目的build.gradle文件中添加以下依赖项

```groovy
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath 'com.android.tools.build:gradle:7.4.1' // 华为
        classpath 'com.huawei.agconnect:agcp:1.9.1.301' // 华为
    }
}
```

#### 4、在应用级build.gradle文件添加

```groovy
plugins {
    id 'com.huawei.agconnect' // 华为
}

// 启用华为分析服务
agcp {
    enableAPMS true
}
```

#### 5、在AndroidManifest.xml文件中<application>标签内添加渠道信息

```xml
<!-- 华为分析服务 -->
<meta-data 
    android:name="install_channel" 
    android:value="${channel}" />
```

#### 6、登录成功后调用以下方法上传用户信息

```kotlin
// 记录userId
HuaweiUtil.setUserId(userId)
```

### 七、邀请码获取域名配置

#### 1、继承BaseSplashActivity创建SplashActivity页

```xml
<activity 
    android:name="包名.SplashActivity"
    android:configChanges="keyboardHidden|screenSize|orientation"
    android:exported="true"
    android:theme="@style/ThemeSplash">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

#### 2、继承BaseDomainActivity创建DomainConfigActivity页

```xml
<activity 
    android:name=".main.DomainConfigActivity"
    android:configChanges="keyboardHidden|screenSize|orientation"
    android:theme="@style/FullScreenTheme.White" />
```

#### 3、实现IUrlModel接口，并提供使用邀请码获取相关域名的接口地址

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object UrlModule : IUrlModule {

    @Provides
    @Singleton
    @GetDomainConfigurationUrl
    override fun provideDomainConfigurationUrl(): String {
        return "http://test.zk-work.com/stage-api/system/customer/validCode/"
    }

    @Provides
    @Singleton
    @GetUpgradeUrl
    override fun provideUpgradeUrl(): String {
        return "http://test.zk-work.com/stage-api/system/client/info"
    }
}
```

#### 4、使用DomainUtils相关方法存取token及域名信息

### 八、集成消息通知

#### 1、创建YourWebSocketService类，继承框架提供的`com.rouxinpai.arms.ws.BaseWebSocketService`类

#### 2、在项目的`Application`类中调用`MessageUtil.init(YourWebSocketService::class.java)`方法初始化

#### 3、在AndroidManifest.xml文件中<application>标签内添加以下配置

```xml
<!-- WebSocket服务 -->
<service 
    android:name="包名.YourWebSocketService"
    android:directBootAware="true"
    android:exported="false" />
```

#### 4、注册通知需要启动的页面

```kotlin
class YourSocketService: BaseWebSocketService() {

    /**
     * 实现该方法注册通知需要启动的页面，多个Activity按顺序启动，被启动的页面启动模式需要设置为singleTask
     */
    override fun createIntents(context: Context): Array<Intent> {
        return arrayOf(
            Intent(context, AActivity::class.java),
            Intent(context, BActivity::class.java),
            Intent(context, CActivity::class.java),
        )
    }
}
```

#### 5、在需要启动服务的地方调用以下方法启动、销毁服务、发送消息

```kotlin
    // 启动服务
    MessageUtil.startService(context)
    // 销毁服务
    MessageUtil.stopService(context)
    // 发送消息
    MessageUtil.sendMessageToService(message)
```

### 九、打印机品牌、SDK、so包关联关系

| 品牌   | SDK                                                                                             | so             |
|:----:|:-----------------------------------------------------------------------------------------------:|:--------------:|
| 华辰联创 | HCCTG_printersdkv5.7.0.jar                                                                      |                |
| 汉印   | HPRT_CPCL_SDK_V1.22.01.jar、HPRT_lzo_V1.0.jar                                                    | libLZO.so      |
| 鹏研科技 | IBOWLDER_fat-generic-cpcl-bluetooth-classic-0.1.16-GA.jar、IBOWLDER_fat-psdk-patch-0.1.16-GA.jar | libpsdkextj.so |

### 十、双击退出程序

在需要双击退出程序的页面增加`@DoubleBackToExitEnabled`注解

### 十一、权限管理框架

- 如果项目是基于 **AndroidX** 包，请在项目 `gradle.properties` 文件中加入
  
  ```groovy
  # 表示将第三方库迁移到 AndroidX
  android.enableJetifier = true
  ```