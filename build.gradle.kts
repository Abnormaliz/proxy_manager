// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.8.0" apply false
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
//    id("com.google.devtools.ksp") version "2.0.0-1.0.22" apply false
    kotlin("kapt") version "2.0.0" apply false
}

buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.8.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.0")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
        // classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:2.0.0-1.0.22")
    }
}