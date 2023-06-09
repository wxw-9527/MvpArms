# PictureSelector：https://github.com/LuckSiege/PictureSelector
-keep class com.luck.picture.lib.** { *; }

# 保持相关模型类不被混淆
-keep class com.rouxinpai.arms.model.bean.** { <fields>; }
-keep class com.rouxinpai.arms.update.model.** { <fields>; }
-keep class com.rouxinpai.arms.barcode.model.** { <fields>; }

# 友盟
-keep class com.umeng.** { *; }
-keep class org.repackage.** { *; }
-keep class com.uc.** { *; }
-keep class com.efs.** { *; }
-keepclassmembers class *{
     public<init>(org.json.JSONObject);
}
-keepclassmembers enum *{
      publicstatic**[] values();
      publicstatic** valueOf(java.lang.String);
}