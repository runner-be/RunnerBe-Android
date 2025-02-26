# Suppress warnings
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**

# Keep data class fields
-keepclassmembers class * {
    <init>(...);
    private <fields>;
    private <methods>;
}

# Keep all interfaces
-keep interface * {
    <methods>;
    <fields>;
}

# RunnerBe data classes
-keep class com.applemango.runnerbe.data.** { *; }

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

-keep,allowobfuscation,allowshrinking class retrofit2.Response

# OkHttp
-keep class okhttp3.Callback { *; }

# Moshi
-keep class com.squareup.moshi.** { *; }
-keep @com.squareup.moshi.JsonClass class * { *; }

# Parcelize
-keep @kotlinx.parcelize.Parcelize class * { *; }

# Kakao login
-keep class com.kakao.sdk.**.model.* { <fields>; }

# Naver login
-keep class com.navercorp.nid.oauth.** { *; }
-keep class com.navercorp.nid.profile.data.** { *; }

# Coroutine
-keep class kotlinx.coroutines.** { *; }

# Dagger Hilt
-keep class dagger.hilt.** { *; }