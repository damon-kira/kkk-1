# ===============================================================================
# Kira Learning App - ProGuard 配置文件
# ===============================================================================

# ==== 基础优化配置 ====
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-dontpreverify
-verbose
-allowaccessmodification

# 优化配置 - 移除过于激进的优化选项
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*,!method/inlining/*

# 调试信息保留
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod,*Annotation*,JavascriptInterface

# ==== Android 核心组件保护 ====
-keep public class * extends android.app.Application
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends androidx.appcompat.app.AppCompatActivity

# View 相关保护
-keepclassmembers class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(***);
    *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# ==== Kotlin & Coroutines 支持 ====
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# ==== Compose 支持 ====
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep interface androidx.compose.runtime.Composer
-keepclassmembers class androidx.compose.** {
    *;
}

# Compose Compiler 生成的类
-keep class *ComposerKt { *; }
-keep class *$Companion { *; }

# ==== Hilt/Dagger 依赖注入 ====
-keep class dagger.** { *; }
-keep class javax.inject.** { *; }
-keep class dagger.hilt.** { *; }
-keepclasseswithmembers class * {
    @dagger.** <methods>;
    @javax.inject.** <methods>;
}

# Hilt 生成的类
-keep class **_HiltModules { *; }
-keep class **_HiltComponents { *; }
-keep class **_Impl { *; }
-keep class **_MembersInjector { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }

# ==== 项目特定包保护 ====
# 主应用包
-keep class com.kira.learning.** { *; }

# 模型类保护
-keep class com.kira.learning.model.** { *; }
-keep class com.kira.learning.module.coding.** { *; }

# 网络模型和API
-keep class com.kira.learning.network.** { *; }
-keepclassmembers class com.kira.learning.network.** {
    <fields>;
}

# ViewBinding
-keep class com.kira.learning.databinding.** { *; }
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
  public static ** inflate(android.view.LayoutInflater);
  public static ** bind(android.view.View);
}

# ==== 第三方库配置 ====

# Retrofit & OkHttp
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.logging.**
-dontwarn retrofit2.**

# Gson 序列化
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer
-keepattributes Signature
-keepattributes *Annotation*

# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**

# Crashlytics
-keep class com.google.firebase.crashlytics.** { *; }
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# WorkManager
-keep class androidx.work.** { *; }
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.ListenableWorker
-keepclassmembers class * extends androidx.work.Worker {
    public <init>(android.content.Context,androidx.work.WorkerParameters);
}

# Navigation Component
-keep class androidx.navigation.** { *; }
-keep class * extends androidx.navigation.Navigator

# Lifecycle Components
-keep class androidx.lifecycle.** { *; }
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}

# Room Database
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# Coil 图片加载
-keep class coil.** { *; }
-keep interface coil.** { *; }

# Material Design
-keep class com.google.android.material.** { *; }
-dontwarn com.google.android.material.**

# ==== 通用保护规则 ====

# 枚举类保护
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Parcelable 实现
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Serializable 实现
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# JNI 方法保护
-keepclasseswithmembernames class * {
    native <methods>;
}

# 反射相关
-keep class **.R$* { *; }
-keep @interface *

# ==== 日志优化 ====
# 移除 Log 调用以减小包大小（Release 版本）
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# 移除 println 调用
-assumenosideeffects class java.io.PrintStream {
    public void println(...);
    public void print(...);
}

# ==== WebView 支持 ====
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keep class android.webkit.** { *; }

# ==== 加密和安全 ====
# Conscrypt TLS 提供者
-keep class org.conscrypt.** { *; }
-keep interface org.conscrypt.** { *; }
-dontwarn org.conscrypt.**

# BouncyCastle 加密
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

# ==== 警告抑制 ====
-dontwarn android.support.**
-dontwarn androidx.**
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn java.lang.management.**

# JNA 相关警告（可能用于某些第三方库）
-dontwarn com.sun.jna.**
-dontwarn org.apache.logging.log4j.**
-dontwarn org.ietf.jgss.**
-dontwarn org.newsclub.net.unix.**
-dontwarn org.slf4j.**

# ==== 特殊处理 ====
# 防止过度优化导致的问题
-keep class * extends java.util.ListResourceBundle {
    protected java.lang.Object[][] getContents();
}

# 保留泛型信息
-keep class * extends com.google.gson.reflect.TypeToken
-keep public class * implements java.lang.reflect.Type

# 内部类保护
-keepattributes InnerClasses
-keep class *$* { *; }
