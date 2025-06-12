// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.google.devtools.ksp") version com.knight.wanandroid.buildsrc.Dependencies.Version.ANDROID_KSP_VERSION
    id("org.jetbrains.kotlin.plugin.serialization") version com.knight.wanandroid.buildsrc.Dependencies.Version.KOTLIN_SERIALIZATION
}
buildscript {
    repositories {
        google()



        mavenCentral()
        maven {
            url = uri("https://storage.googleapis.com/r8-releases/raw")
        }
        jcenter()
        maven(url="https://jitpack.io")
        maven(url = "https://dl.google.com/dl/android/maven2/")
        maven (url = "https://maven.aliyun.com/repository/public/" ) //为了解决loadSir版本下载不了问题
        gradlePluginPortal()
    }
    dependencies {
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.ANDROID_TOOLS_PLUGIN)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.KOTLIN_PLUGIN)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.GO_ROUTER_REGISTER)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.HILT_PLUGIN)
       // classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.KOTLIN_SERIALIZATION)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.ANDROID_AOP)
        classpath("com.android.tools:r8:8.2.24")
    }
}

allprojects {
    repositories {
        google()


        maven(url = "https://jitpack.io")

        mavenCentral()
        jcenter()
        maven (url = "https://maven.aliyun.com/repository/public/" ) //为了解决loadSir版本下载不了问题
        gradlePluginPortal()
    }
}



tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

