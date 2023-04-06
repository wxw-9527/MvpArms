# PictureSelector：https://github.com/LuckSiege/PictureSelector
-keep class com.luck.picture.lib.** { *; }

# 保持相关模型类不被混淆
-keep class com.rouxinpai.arms.model.bean.** { <fields>; }
-keep class com.rouxinpai.arms.update.model.** { <fields>; }
-keep class com.rouxinpai.arms.barcode.model.** { <fields>; }