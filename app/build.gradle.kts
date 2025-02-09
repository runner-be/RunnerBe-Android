import java.util.Properties
import java.io.FileInputStream
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.firebase.crashlytics")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
}

val composeCompilerVersion: String by rootProject.extra
val composeVersion: String by rootProject.extra
val versionPropsFile = rootProject.file("build-counter.properties")
val versionProps = Properties()

if (versionPropsFile.exists()) {
    FileInputStream(versionPropsFile).use { fis ->
        versionProps.load(fis)
    }
} else {
    versionProps["BUILD_COUNT"] = "0"
}

val currentDate = SimpleDateFormat("yyyyMMdd").format(Date())
if (!versionProps.containsKey("LAST_BUILD_DATE") || versionProps["LAST_BUILD_DATE"] != currentDate) {
    versionProps["LAST_BUILD_DATE"] = currentDate
    versionProps["BUILD_COUNT"] = "0"
}

val buildCount = String.format("%02d", versionProps["BUILD_COUNT"].toString().toInt() + 1)
versionProps["BUILD_COUNT"] = buildCount

val buildVersionCode = ("$currentDate$buildCount").toInt()

// build-counter.properties 파일 저장
FileOutputStream(versionPropsFile).use { fos ->
    versionProps.store(fos, null)
}

// local.properties 파일 로드
val localProperties = Properties().apply {
    rootProject.file("local.properties").inputStream().use { inputStream ->
        load(inputStream)
    }
}

android {
    namespace = "com.applemango.app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.applemango.runnerbe"
        minSdk = 28
        targetSdk = 34
        versionCode = buildVersionCode
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "KAKAO_API_KEY", localProperties["KAKAO_API_KEY"].toString())
        buildConfigField("String", "REST_API_KEY", localProperties["REST_API_KEY"].toString())
    }

    signingConfigs {
        create("release") {
            storeFile = file("runnerBe-Key.jks")
            storePassword = "runnerbe2023"
            keyAlias = "key0"
            keyPassword = "runnerbe2023"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
        viewBinding = true
        dataBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeCompilerVersion
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    flavorDimensions("server")
    productFlavors {
        create("dev") {
            dimension = "server"
            buildConfigField("String", "BASE_URL", "\"https://runnerbe.shop\"")
        }
        create("real") {
            dimension = "server"
            buildConfigField("String", "BASE_URL", "\"https://runnerbe.shop\"")
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeCompilerVersion
    }
}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":presentation"))

    implementation("androidx.compose.runtime:runtime-livedata:$composeVersion")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-compiler:2.46")

    implementation("com.kakao.sdk:v2-all:2.20.0")
    implementation("com.google.firebase:firebase-storage-ktx")

    implementation("com.navercorp.nid:oauth:5.10.0")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}