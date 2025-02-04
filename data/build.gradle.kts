import java.util.Properties

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}

val localProperties = Properties().apply {
    rootProject.file("local.properties").inputStream().use { inputStream ->
        load(inputStream)
    }
}

android {
    namespace = "com.applemango.data"
    compileSdk = 33

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField("String", "BASE_URL", "\"https://runnerbe.shop\"")
        buildConfigField("String", "KAKAO_API_KEY", localProperties["KAKAO_API_KEY"].toString())
        buildConfigField("String", "REST_API_KEY", localProperties["REST_API_KEY"].toString())
        buildConfigField("String", "WITHDRAWAL_API_KEY", localProperties["WITHDRAWAL_API_KEY"].toString())
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        buildConfig = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":domain"))

    // Preferences DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.2")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.46")
    kapt("com.google.dagger:hilt-compiler:2.46")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // Paging3
    implementation("androidx.paging:paging-common:3.1.1")

    // https://github.com/square/retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // https://github.com/square/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.9.0")

    // https://github.com/square/moshi
    implementation("com.squareup.moshi:moshi:1.15.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    // https://github.com/square/okhttp/tree/master/okhttp-logging-interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    implementation("com.google.firebase:firebase-messaging-ktx:23.1.0")
    implementation(platform("com.google.firebase:firebase-bom:30.1.0"))
    implementation("com.google.firebase:firebase-storage-ktx")

    implementation("com.navercorp.nid:oauth:5.10.0") // jdk 11
    implementation("com.kakao.sdk:v2-all:2.20.0")

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}