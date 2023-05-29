# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-allowaccessmodification
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-renamesourcefileattribute Paramount
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod,*Annotation*

-dontwarn android.support.v4.**
-dontwarn android.support.v7.**
-dontwarn de.greenrobot.event.**

-keepclassmembers public class * extends android.view.View {*;}

-keepattributes Signature

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * extends android.database.sqlite.SQLiteOpenHelper {
    public void onDowngrade(...);
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep public class * implements android.os.Parcelable {
    public *;
}
-keep public class * implements java.io.Serializable {
    public *;
}

#------------------  下方是android平台自带的排除项，这里不要动         ----------------

-keep public class * extends androidx.appcompat.app.AppCompatActivity {
	public <fields>;
	public <methods>;
}
-keep public class * extends androidx.fragment.app.Fragment {
	public <fields>;
	public <methods>;
}
-keep public class * extends android.app.Application{
	public <fields>;
	public <methods>;
}
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepattributes *Annotation*

-keepclasseswithmembernames class *{
	native <methods>;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#------------------  下方是共性的排除项目         ----------------
# 方法名中含有“JNI”字符的，认定是Java Native Interface方法，自动排除
# 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除

-keepclasseswithmembers class * {
    ... *JNI*(...);
}

-keepclasseswithmembernames class * {
	... *JRI*(...);
}

-keep class **JNI* {*;}
#-keep public class com.project.Constant

# log配置  不能有关闭优化的配置-dontoptimize,否则失效
-assumenosideeffects class android.util.Log{
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}


# for appsflyerlib
#-dontwarn com.google.android.gms.ads.**.
#-dontwarn android.content.pm.**
#-dontwarn com.android.installreferrer
#-keep class com.appsflyer.** { *; }
#-keep public class com.google.firebase.iid.FirebaseInstanceId {
#    public *;
#}
# for appsflyer sdk end

-dontwarn com.google.android.gms.**

#viewbinding
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
  public static ** inflate(...);
  public static ** bind(***);
}

#Androidx
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
-keep class androidx.core.app.CoreComponentFactory { *; }


# Google Play Services library
-keep class * extends java.util.ListResourceBundle {
  protected Object[][] getContents();
}

-keep public class com.google.android.gms.common.internal.safeparcel.SafeParcelable {
  public static final *** NULL;
}
-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepclasseswithmembernames class * {
  @com.google.android.gms.common.annotation.KeepName *;
}

-keepnames class * implements android.os.Parcelable {
  public static final ** CREATOR;
}
#flurry adv end

# google
-dontwarn com.google.**
-keep class com.google.**{*;}
# end google

# firebase
-dontwarn com.google.firebase.**
-keep class com.google.firebase.**{*;}
#-keep class com.google.firebase.provider.FirebaseInitProvider {*;}
# end firebase

-dontwarn javax.annotation.**
-dontwarn javax.inject.**

# Retrofit
-dontnote retrofit2.Platform
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
-dontwarn retrofit2.Platform$Java8
-keepattributes Signature
-keepattributes Exceptions

# OkHttp3
-dontwarn okhttp3.logging.**
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**

# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
# RxPermission
-keep class com.tbruyelle.rxpermissions2.* {
*;
}

#bean
-keep class com.colombia.credit.bean.**{*;}

# Gson
-keep class com.google.gson.stream.** { *; }
-keepattributes EnclosingMethod

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#LiveData
-keep class androidx.lifecycle.LiveData.**{
   private int mVersion;
}

#banner
#-dontwarn androidx.viewpager2.**
#-keep class androidx.viewpager2.** {*;}
#-dontwarn androidx.recyclerview.widget.RecyclerView
#-keep class androidx.recyclerview.widget.RecyclerView{*;}
#-dontwarn com.finance.credit.banner.**
#-keep class com.finance.credit.banner.** {*;}

-keepattributes JavascriptInterface


-optimizations !method/inlining/*

-keepclasseswithmembers class * {
  public void onPayment*(...);
}

# 屏幕适配
-keep class me.jessyan.autosize.** { *; }
-keep interface me.jessyan.autosize.** { *; }

#------------------  下方是android平台自带的排除项，这里不要动         ----------------

-keep public class * extends androidx.appcompat.app.AppCompatActivity {
	public <fields>;
	public <methods>;
}
-keep public class * extends androidx.fragment.app.Fragment {
	public <fields>;
	public <methods>;
}
-keep public class * extends android.app.Application{
	public <fields>;
	public <methods>;
}
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
	public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepattributes *Annotation*

-keepclasseswithmembernames class *{
	native <methods>;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#------------------  下方是共性的排除项目         ----------------
# 方法名中含有“JNI”字符的，认定是Java Native Interface方法，自动排除
# 方法名中含有“JRI”字符的，认定是Java Reflection Interface方法，自动排除

-keepclasseswithmembers class * {
    ... *JNI*(...);
}

-keepclasseswithmembernames class * {
	... *JRI*(...);
}

-keep class **JNI* {*;}
#-keep public class com.project.Constant

# log配置  不能有关闭优化的配置-dontoptimize,否则失效
-assumenosideeffects class android.util.Log{
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
