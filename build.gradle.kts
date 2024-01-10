// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.devtools.ksp") version "1.8.10-1.0.9"
}
//tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
//    kotlinOptions {
//        jvmTarget = "1.8"
//    }
//}
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        // maven { url 'https://maven.google.com' }
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
       // classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.AndroidKsp)
       // classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.AspectJxPlugin)

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

