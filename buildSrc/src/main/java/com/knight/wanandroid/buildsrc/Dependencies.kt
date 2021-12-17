package com.knight.wanandroid.buildsrc

/**
 * 项目依赖统一版本管理
 * @author Knight
 * @since 14/12/21 14:32 PM
 */
object Dependencies {


    /**
     * 依赖版本号
     * @author Knight
     * since 14/12/21 14:40 PM
     */
    object Version {
       //AndroidX----------------------------------------------
       const val AppCompat = "1.2.0"
       //核心依赖
       const val CoreKtx = "1.3.1"
       //约束布局
       const val ConstraintLayout = "2.0.1"
       //单元测试
       const val TestExtJunit = "1.1.2"
       const val TestEspresso = "3.3.0"
       //Activity
       const val ActivityKtx = "1.1.0"
       //Fragment
       const val FragmentKtx = "1.2.5"
       //分包 MultiDex
       const val MultiDex = "2.0.1"

       //Android-------------------------------------------------
       //单元测试
       const val Junit = "4.13"
       //Material样式
       const val Material = "1.2.0"

       //kotlin相关-----------------------------------------------
       const val Kotlin = "1.5.10"
       //协程
       const val Coroutines = "1.5.0"

       //JetPack--------------------------------------------------
       //Lifecycle相关（ViewModel & LiveData & Lifecycle）
       const val Lifecycle = "2.3.1"
       //DI框架-Hilt
       const val Hilt = "2.37"
       const val HiltAndroidx = "1.0.0"

       //GitHub相关--------------------------------------------------
       const val ARoute = "1.5.2"                          // 阿里路由
       const val ARouteCompiler = "1.5.2"                  // 阿里路由 APT
       const val OkHttp = "4.9.0"                          // OkHttp
       const val OkHttpInterceptorLogging = "4.9.1"        // OkHttp 请求Log拦截器
       const val Retrofit = "2.9.0"                        // Retrofit
       const val RetrofitConverterGson = "2.9.0"           // Retrofit Gson 转换器
       const val Gson = "2.8.7"                            // Gson
       const val MMKV = "1.2.9"                            // 腾讯 MMKV 替代SP
       const val AutoService = "1.0"                       // 自动生成SPI暴露服务文件
       const val EventBus = "3.3.1"                        // 事件总线
    }
    /**
     * AndroidX相关依赖
     *
     * @author Knight
     * @since 14/14/21 14:48 PM
     */
    object Androidx {
        //单元测试
        const val AndroidJUnitRunner = "androidx.test.runner.AndroidJUnitRunner"
        const val TestExtJunit = "androidx.test.ext:junit:${Version.TestExtJunit}"
        const val TestEspresso = "androidx.test.espresso:espresso-core:${Version.TestEspresso}"
        //AppCompat
        const val AppCompat = "androidx.appcompat:appcompat:${Version.AppCompat}"
        //CoreKts
        const val CoreKts = "androidx.core:core-ktx:${Version.CoreKtx}"
        //约束布局
        const val ConstraintLayout = "androidx.constraintlayout:constraintlayout:${Version.ConstraintLayout}"
        //ActivityKtx
        const val ActivityKtx = "androidx.activity:activity-ktx:${Version.ActivityKtx}"
        //FragmentKtx
        const val FragmentKtx = "androidx.fragment:fragment-ktx:${Version.FragmentKtx}"
        //MultiDex
        const val MultiDex = "androidx.multidex:multidex:${Version.MultiDex}"


    }


    /**
     * Android相关依赖
     *
     * @author Knight
     * @since 14/12/21 15:48 PM
     */
    object Android {
        const val Junit = "junit:junit:${Version.Junit}"
        const val Material = "com.google.android.material:material:${Version.Material}"
    }

    /**
     * Kotlin相关依赖
     *
     * @author Knight
     * @since 14/12/21 15:57 PM
     */
    object Kotlin {
        const val Kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Version.Kotlin}"
        const val CoroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.Coroutines}"
        const val CoroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.Coroutines}"
    }

    /**
     * JetPack相关依赖
     * @author Knight
     * @since 14/12/21 15:54 PM
     */
    object JetPack {
        const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.Lifecycle}"
        const val ViewModelSavedState =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.Lifecycle}"
        const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.Lifecycle}"
        const val Lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.Lifecycle}"
        const val LifecycleCompilerAPT =
            "androidx.lifecycle:lifecycle-compiler:${Version.Lifecycle}"
        const val HiltCore = "com.google.dagger:hilt-android:${Version.Hilt}"
        const val HiltApt = "com.google.dagger:hilt-compiler:${Version.Hilt}"
        const val HiltAndroidx = "androidx.hilt:hilt-compiler:${Version.HiltAndroidx}"
    }

    /**
     * GitHub及其他相关依赖
     *
     * @author Knight
     * @since 14/12/21 19:07 PM
     */
    object GitHub {
        const val ARoute = "com.alibaba:arouter-api:${Version.ARoute}"
        const val ARouteCompiler = "com.alibaba:arouter-compiler:${Version.ARouteCompiler}"
        const val AutoService = "com.google.auto.service:auto-service:${Version.AutoService}"
        const val AutoServiceAnnotations =
            "com.google.auto.service:auto-service-annotations:${Version.AutoService}"
        const val OkHttp = "com.squareup.okhttp3:okhttp:${Version.OkHttp}"
        const val Retrofit = "com.squareup.retrofit2:retrofit:${Version.Retrofit}"
        const val RetrofitConverterGson =
            "com.squareup.retrofit2:converter-gson:${Version.RetrofitConverterGson}"
        const val Gson = "com.google.code.gson:gson:${Version.Gson}"
        const val OkHttpInterceptorLogging = "com.squareup.okhttp3:logging-interceptor:${Version.OkHttpInterceptorLogging}"
        const val EventBus = "org.greenrobot:eventbus:${Version.EventBus}"
        const val EventBusAPT = "org.greenrobot:eventbus-annotation-processor:${Version.EventBus}"
    }



}