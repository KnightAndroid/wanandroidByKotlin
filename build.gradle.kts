// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
        mavenCentral()
        // maven { url 'https://maven.google.com' }
        maven(url = "https://dl.google.com/dl/android/maven2/")
        gradlePluginPortal()
    }

    dependencies {
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.AndroidToolsPlugin)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.KotlinPlugin)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.ARouterRegister)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.HiltPlugin)
        classpath(com.knight.wanandroid.buildsrc.ProjectPluginDencies.AspectJxPlugin)

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