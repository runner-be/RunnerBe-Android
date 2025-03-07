plugins {
    id("java-library")
    id("org.jetbrains.kotlin.jvm")
    kotlin("kapt")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // Hilt
    implementation("com.google.dagger:hilt-core:2.46")
    kapt("com.google.dagger:hilt-compiler:2.46")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    // Paging3
    implementation("androidx.paging:paging-common:3.1.1")
}