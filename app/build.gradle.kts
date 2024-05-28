plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("appmetrica-plugin")
    id("com.google.gms.google-services")
}

appmetrica {
    postApiKey = { applicationVariant -> "2853adba-8526-4bd4-8cef-4810d384388d" }
    enable = { applicationVariant -> true }    // Optional.
    setMappingBuildTypes(listOf("release"))            // Optional.
    setOffline(false)                            // Optional.
    mappingFile = { applicationVariant -> null }   // Optional.
    enableAnalytics = true                     // Optional.
    allowTwoAppMetricas = { applicationVariant -> false }  // Optional.
}

android {
    namespace = "ir.flyap.music_a"
    compileSdk = 34

    defaultConfig {
        applicationId = "ir.flyap.music_a"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.messaging.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    annotationProcessor(libs.androidx.room.compiler)
    ksp(libs.androidx.room.compiler)

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-android-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    //lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    //media
    //implementation("androidx.media3:media3-exoplayer:1.2.1")
    //implementation("androidx.media3:media3-ui:1.2.1")
    implementation("androidx.media:media:1.6.0")
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("com.google.android.exoplayer:extension-mediasession:2.19.1")

    // Tapsell
    implementation("ir.tapsell.plus:tapsell-plus-sdk-android:2.2.4")

    // Poolakey
    implementation("com.github.cafebazaar.Poolakey:poolakey:2.2.0")

    // AppMetrica SDK.
    implementation("io.appmetrica.analytics:analytics:6.1.0")
    implementation("io.appmetrica.analytics:push:3.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
}