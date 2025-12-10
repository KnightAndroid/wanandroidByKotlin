// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.ksp) apply false //声明这个插件可以被其他 module 使用，但当前 module 不应用这个插件。
    alias(libs.plugins.kotlin.serialization) apply false
}


buildscript {
    repositories {
        google()



        mavenCentral()
        maven {
            //com.android.tools.r8.internal.Jc: Sealed classes are not supported as program classes
            url = uri("https://storage.googleapis.com/r8-releases/raw")
        }
        jcenter()
        maven(url="https://jitpack.io")
        maven(url = "https://dl.google.com/dl/android/maven2/")
        maven (url = "https://maven.aliyun.com/repository/public/" ) //为了解决loadSir版本下载不了问题
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.android.gradlePlugin)
        classpath(libs.kotlin.gradlePlugin)
        classpath(libs.gorouter.gradlePlugin)
        classpath(libs.hilt.android.gradlePlugin)
        classpath(libs.android.aop.gradlePlugin)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()

        maven(url = "https://jitpack.io")


        jcenter()
        maven (url = "https://maven.aliyun.com/repository/public/" ) //为了解决loadSir版本下载不了问题
        gradlePluginPortal()
    }
}



tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

