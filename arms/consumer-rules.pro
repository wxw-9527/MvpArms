# 保留继承了ViewBinding的类不被混淆
-keep class * extends androidx.viewbinding.ViewBinding { *; }

# PictureSelector：https://github.com/LuckSiege/PictureSelector
-keep class com.luck.picture.lib.** { *; }

# XXPermissions：https://github.com/getActivity/XXPermissions
-keep class com.hjq.permissions.** {*;}

# 保持相关模型类不被混淆
-keep class com.rouxinpai.arms.model.bean.** { <fields>; }
-keep class com.rouxinpai.arms.update.model.** { <fields>; }
-keep class com.rouxinpai.arms.barcode.model.** { <fields>; }
-keep class com.rouxinpai.arms.domain.model.** { <fields>; }
-keep class com.rouxinpai.arms.message.model.** { <fields>; }
-keep class com.rouxinpai.arms.print.model.** { <fields>; }
-keep class com.rouxinpai.arms.dict.model.** { <fields>; }

# AppGallery Connect
-ignorewarnings
-keep class com.huawei.agconnect.**{*;}
-dontwarn com.huawei.agconnect.**
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep interface com.huawei.hms.analytics.type.HAEventType{*;}
-keep interface com.huawei.hms.analytics.type.HAParamType{*;}
-keepattributes Exceptions, Signature, InnerClasses, SourceFile, LineNumberTable