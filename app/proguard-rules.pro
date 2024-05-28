# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.adcolony.sdk.AdColony
-dontwarn com.adcolony.sdk.AdColonyAdSize
-dontwarn com.adcolony.sdk.AdColonyAdView
-dontwarn com.adcolony.sdk.AdColonyAdViewListener
-dontwarn com.adcolony.sdk.AdColonyAppOptions
-dontwarn com.adcolony.sdk.AdColonyInterstitial
-dontwarn com.adcolony.sdk.AdColonyInterstitialListener
-dontwarn com.adcolony.sdk.AdColonyRewardListener
-dontwarn com.chartboost.sdk.Chartboost
-dontwarn com.chartboost.sdk.ChartboostDelegate
-dontwarn com.chartboost.sdk.Libraries.CBLogging$Level
-dontwarn com.chartboost.sdk.a
-dontwarn com.google.ads.interactivemedia.v3.api.Ad
-dontwarn com.google.ads.interactivemedia.v3.api.AdDisplayContainer
-dontwarn com.google.ads.interactivemedia.v3.api.AdError
-dontwarn com.google.ads.interactivemedia.v3.api.AdErrorEvent$AdErrorListener
-dontwarn com.google.ads.interactivemedia.v3.api.AdErrorEvent
-dontwarn com.google.ads.interactivemedia.v3.api.AdEvent$AdEventListener
-dontwarn com.google.ads.interactivemedia.v3.api.AdEvent$AdEventType
-dontwarn com.google.ads.interactivemedia.v3.api.AdEvent
-dontwarn com.google.ads.interactivemedia.v3.api.AdsLoader$AdsLoadedListener
-dontwarn com.google.ads.interactivemedia.v3.api.AdsLoader
-dontwarn com.google.ads.interactivemedia.v3.api.AdsManager
-dontwarn com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent
-dontwarn com.google.ads.interactivemedia.v3.api.AdsRenderingSettings
-dontwarn com.google.ads.interactivemedia.v3.api.AdsRequest
-dontwarn com.google.ads.interactivemedia.v3.api.CompanionAdSlot
-dontwarn com.google.ads.interactivemedia.v3.api.ImaSdkFactory
-dontwarn com.google.ads.interactivemedia.v3.api.ImaSdkSettings
-dontwarn com.google.ads.interactivemedia.v3.api.player.AdMediaInfo
-dontwarn com.google.ads.interactivemedia.v3.api.player.ContentProgressProvider
-dontwarn com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer$VideoAdPlayerCallback
-dontwarn com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer
-dontwarn com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate
-dontwarn com.google.android.gms.ads.AdListener
-dontwarn com.google.android.gms.ads.AdLoader$Builder
-dontwarn com.google.android.gms.ads.AdLoader
-dontwarn com.google.android.gms.ads.AdRequest
-dontwarn com.google.android.gms.ads.AdSize
-dontwarn com.google.android.gms.ads.AdView
-dontwarn com.google.android.gms.ads.FullScreenContentCallback
-dontwarn com.google.android.gms.ads.MobileAds
-dontwarn com.google.android.gms.ads.OnUserEarnedRewardListener
-dontwarn com.google.android.gms.ads.interstitial.InterstitialAd
-dontwarn com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
-dontwarn com.google.android.gms.ads.nativead.MediaView
-dontwarn com.google.android.gms.ads.nativead.NativeAd$Image
-dontwarn com.google.android.gms.ads.nativead.NativeAd$OnNativeAdLoadedListener
-dontwarn com.google.android.gms.ads.nativead.NativeAd
-dontwarn com.google.android.gms.ads.nativead.NativeAdView
-dontwarn com.google.android.gms.ads.rewarded.RewardedAd
-dontwarn com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
-dontwarn com.unity3d.ads.IUnityAdsInitializationListener
-dontwarn com.unity3d.ads.IUnityAdsLoadListener
-dontwarn com.unity3d.ads.IUnityAdsShowListener
-dontwarn com.unity3d.ads.UnityAds
-dontwarn com.unity3d.services.banners.BannerView$IListener
-dontwarn com.unity3d.services.banners.BannerView
-dontwarn com.unity3d.services.banners.UnityBannerSize
-dontwarn com.oracle.svm.core.annotate.AutomaticFeature
-dontwarn com.oracle.svm.core.annotate.Delete
-dontwarn com.oracle.svm.core.annotate.Substitute
-dontwarn com.oracle.svm.core.annotate.TargetClass
-dontwarn com.oracle.svm.core.configure.ResourcesRegistry
-dontwarn org.graalvm.nativeimage.ImageSingletons
-dontwarn org.graalvm.nativeimage.hosted.Feature$BeforeAnalysisAccess
-dontwarn org.graalvm.nativeimage.hosted.Feature

#Retrofit Rules
-dontnote retrofit2.Platform
-keepattributes Signature
-keepattributes Exceptions
-dontwarn kotlinx.coroutines.**
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# With R8 full mode generic signatures are stripped for classes that are not kept.
-keep,allowobfuscation,allowshrinking class retrofit2.Response

