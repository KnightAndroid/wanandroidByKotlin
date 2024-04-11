// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.devtools.ksp") version com.knight.wanandroid.buildsrc.Dependencies.Version.ANDROID_KSP_VERSION
}
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven( url="https://jitpack.io")
        maven(url = "https://dl.google.com/dl/android/maven2/")
        gradlePluginPortal()
    }
    dependencies {
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.ANDROID_TOOLS_PLUGIN)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.KOTLIN_PLUGIN)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.GO_ROUTER_REGISTER)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.HILT_PLUGIN)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.ANDROID_AOP)
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://jitpack.io")
        mavenCentral()
        gradlePluginPortal()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

