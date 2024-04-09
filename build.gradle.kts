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
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.AndroidToolsPlugin)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.KotlinPlugin)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.GoRouterRegister)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.HiltPlugin)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.AndroidAop)
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

