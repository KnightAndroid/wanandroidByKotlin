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
       const val AppCompat = "1.3.0"
       //核心依赖
       const val CoreKtx = "1.7.0"
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
       //智能排版
       const val FlexLayoutVersion = "2.0.1"


       //kotlin相关-----------------------------------------------
       const val Kotlin = "1.5.10"
       //协程
       const val Coroutines = "1.5.0"

       //JetPack--------------------------------------------------
       //Lifecycle相关（ViewModel & LiveData & Lifecycle）
       const val Lifecycle = "2.3.1"
       const val LifecycleExtension = "2.2.0"
       //DI框架-Hilt
       const val Hilt = "2.37"
       const val HiltAndroidx = "1.0.0"
       //数据库Room
       const val RoomVersion = "2.3.0"
       //camerax
       const val CameraxVersion = "1.0.2"
       //cameraView
       const val CameraViewVersion = "1.0.0-alpha26"
       //zxing
       const val ZingVersion = "3.4.1"

       //GitHub相关--------------------------------------------------
       const val ARoute = "1.5.2"                          // 阿里路由
       const val ARouteCompiler = "1.5.2"                  // 阿里路由 APT
       const val OkHttp = "4.9.0"                          // OkHttp
       const val OkHttpInterceptorLogging = "4.9.1"        // OkHttp 请求Log拦截器
       const val Retrofit = "2.9.0"                        // Retrofit
       const val RetrofitConverterGson = "2.9.0"           // Retrofit Gson 转换器
       const val Gson = "2.8.7"                            // Gson
       const val MMKV = "1.2.11"                           // 腾讯 MMKV 替代SP
       const val AutoService = "1.0"                       // 自动生成SPI暴露服务文件
       const val EventBus = "3.3.1"                        // 事件总线
       const val AutoSize = "1.2.1"                        // 屏幕适配
       const val BottomNavigation = "2.0.4"                // 底部导航栏
       const val MagicIndicator = "1.7.0"                  // 指示器
       const val Agent = "v5.0.0-alpha.1-androidx"         // Android WebView
       const val Sketch = "2.7.1"                          // 预览大图
       const val LoadSir = "1.3.8"                         // 全局视图加载
       const val Cookie = "v1.0.0"                         // Cookie管理
       const val Glide = "4.11.0"                          // Glide图像版本
       const val SmartRefresh = "2.0.3"                    // 智能刷新库
       const val BaseAdapter = "3.0.4"                     // Base适配器
       const val RecycleviewVersion = "1.3.2"              // recycleview
       const val BannerVersion = "2.2.2"                   // Banner
       const val ShimmerLayoutVersion = "2.1.0"            // shimmerLayout
       //第三方其他相关------------------------------------------------------
       const val Aspectj = "1.9.7"                         // aop相关
       const val LeakCanaryVersion = "2.8.1"               // 内存泄漏相关
       const val buglyVersion = "4.0.4"                    // bugly异常上报

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
        //flexLayout
        const val FlexLayout = "com.google.android:flexbox:${Version.FlexLayoutVersion}"
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
        const val LifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Version.LifecycleExtension}"
        const val CameraCore = "androidx.camera:camera-core:${Version.CameraxVersion}"
        const val CameraCamera2 = "androidx.camera:camera-camera2:${Version.CameraxVersion}"
        const val CameraLifecycle = "androidx.camera:camera-lifecycle:${Version.CameraxVersion}"
        const val CameraView = "androidx.camera:camera-view:${Version.CameraViewVersion}"
        const val Zxing = "com.google.zxing:core:${Version.ZingVersion}"
        const val LifecycleCompilerAPT =
            "androidx.lifecycle:lifecycle-compiler:${Version.Lifecycle}"
        const val HiltCore = "com.google.dagger:hilt-android:${Version.Hilt}"
        const val HiltApt = "com.google.dagger:hilt-compiler:${Version.Hilt}"
        const val HiltAndroidx = "androidx.hilt:hilt-compiler:${Version.HiltAndroidx}"
        const val RoomTime = "androidx.room:room-runtime:${Version.RoomVersion}"
        const val RoomCompiler = "androidx.room:room-compiler:${Version.RoomVersion}"
        const val RoomKtx = "androidx.room:room-ktx:${Version.RoomVersion}"
    }

    /**
     * GitHub及其他相关依赖
     *
     * @author Knight
     * @since 14/12/21 19:07 PM
     */
    object GitHub {
        //阿里ARouter https://github.com/alibaba/ARouter
        const val ARoute = "com.alibaba:arouter-api:${Version.ARoute}"
        const val ARouteCompiler = "com.alibaba:arouter-compiler:${Version.ARouteCompiler}"
        //google https://github.com/google/auto
        const val AutoService = "com.google.auto.service:auto-service:${Version.AutoService}"
        const val AutoServiceAnnotations =
            "com.google.auto.service:auto-service-annotations:${Version.AutoService}"
        //okhttp https://github.com/square/okhttp
        const val OkHttp = "com.squareup.okhttp3:okhttp:${Version.OkHttp}"
        //retrofit https://github.com/square/retrofit
        const val Retrofit = "com.squareup.retrofit2:retrofit:${Version.Retrofit}"

        const val RetrofitConverterGson =
            "com.squareup.retrofit2:converter-gson:${Version.RetrofitConverterGson}"
        //Gson https://github.com/google/gson
        const val Gson = "com.google.code.gson:gson:${Version.Gson}"
        const val OkHttpInterceptorLogging = "com.squareup.okhttp3:logging-interceptor:${Version.OkHttpInterceptorLogging}"
        //EventBus https://github.com/greenrobot/EventBus
        const val EventBus = "org.greenrobot:eventbus:${Version.EventBus}"
        const val EventBusAPT = "org.greenrobot:eventbus-annotation-processor:${Version.EventBus}"
        //屏幕适配 https://github.com/JessYanCoding/AndroidAutoSize
        const val AutoSize = "me.jessyan:autosize:${Version.AutoSize}"
        //底部导航栏 https://github.com/ittianyu/BottomNavigationViewEx
        const val bottomNavigate = "com.github.ittianyu:BottomNavigationViewEx:${Version.BottomNavigation}"
        //MMKV https://github.com/Tencent/MMKV
        const val MMKV = "com.tencent:mmkv:${Version.MMKV}"
        //滑动切换 https://github.com/hackware1993/MagicIndicator
        const val MagicIndicator = "com.github.hackware1993:MagicIndicator:${Version.MagicIndicator}"
        //网页加载库 https://github.com/Justson/AgentWeb
        const val AgentWeb = "com.github.Justson.AgentWeb:agentweb-core:${Version.Agent}"
        //预览大图插件 https://github.com/panpf/sketch
        const val SketchPhoto = "io.github.panpf.sketch:sketch:${Version.Sketch}"
        //aop
        const val aspectJ = "org.aspectj:aspectjrt:${Version.Aspectj}"
        //全局加载视图 https://github.com/KingJA/LoadSir
        const val LoadSir = "com.kingja.loadsir:loadsir:${Version.LoadSir}"
        //Cookie管理框架 https://github.com/franmontiel/PersistentCookieJar
        const val Cookie = "com.github.franmontiel:PersistentCookieJar:${Version.Cookie}"
        //图像加载库 https://github.com/bumptech/glide
        const val Glide = "com.github.bumptech.glide:glide:${Version.Glide}"
        //智能刷新库 核心依赖 https://github.com/scwang90/SmartRefreshLayout
        const val SmartKernel = "com.scwang.smart:refresh-layout-kernel:${Version.SmartRefresh}"
        //智能刷新库 经典刷新头
        const val SmartHeader = "com.scwang.smart:refresh-header-classics:${Version.SmartRefresh}"
        //智能刷新库 经典加载
        const val SmartFooter = "com.scwang.smart:refresh-footer-classics:${Version.SmartRefresh}"
        //智能刷新库 二级刷新头
        const val SmartHeaderTwoLevel = "com.scwang.smart:refresh-header-two-level:${Version.SmartRefresh}"
        //BaseAdapter https://github.com/CymChad/BaseRecyclerViewAdapterHelper
        const val BaseAdapterHelper = "com.github.CymChad:BaseRecyclerViewAdapterHelper:${Version.BaseAdapter}"
        //SwipeRecycleview https://github.com/yanzhenjie/SwipeRecyclerView
        const val SwipeRecyclerView = "com.yanzhenjie.recyclerview:x:${Version.RecycleviewVersion}"
        //Banner https://github.com/youth5201314/banner
        const val Banner = "io.github.youth5201314:banner:${Version.BannerVersion}"
        //骨架屏 https://github.com/team-supercharge/ShimmerLayout
        const val ShimmerLayout = "io.supercharge:shimmerlayout:${Version.ShimmerLayoutVersion}"
        //内存泄漏 https://github.com/square/leakcanary
        const val LeakCanary = "com.squareup.leakcanary:leakcanary-android:${Version.LeakCanaryVersion}"
    }

    /**
     * 第三方sdk
     *
     */
    object OtherUtils {
        //bugly https://bugly.qq.com/docs/release-notes/release-android-bugly
        const val Bugly = "com.tencent.bugly:crashreport:${Version.buglyVersion}"
    }



}