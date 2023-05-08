# adjust start
-dontwarn android.content.pm.**
-dontwarn com.android.installreferrer
-keep public class com.adjust.sdk.** { *; }
-keep class com.adjust.sdk.** { *; }
-keep class com.google.android.gms.common.ConnectionResult {
    int SUCCESS;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
    com.google.android.gms.ads.identifier.AdvertisingIdClient$Info getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
    java.lang.String getId();
    boolean isLimitAdTrackingEnabled();
}
-keep public class com.android.installreferrer.** { *; }
# adjust end

-dontwarn com.google.android.gms.**

# google
-dontwarn com.google.**
-keep class com.google.**{*;}
# end google

# firebase
-dontwarn com.google.firebase.**
-keep class com.google.firebase.**{*;}
#-keep class com.google.firebase.provider.FirebaseInitProvider {*;}
# end firebase