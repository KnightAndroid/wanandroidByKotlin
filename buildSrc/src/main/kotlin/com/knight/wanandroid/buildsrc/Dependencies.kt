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
       //构建相关-----------------------------------------------
       const val TOOL_BUILD_VERSION= "8.0.0"
       const val KOTLIN_GRADLE_PLUGIN_VERSION = "1.8.10"
       //AndroidX----------------------------------------------
       const val APPCOMPAT_VERSION = "1.3.0"
       //核心依赖
       const val CORE_KTX_VERSION = "1.7.0"
       //约束布局
       const val CONSTRAINTLAYOUT_VERSION = "2.0.1"
       //单元测试
       const val TEST_JUNIT_VERSION = "1.1.2"
       const val TEST_ESPRESSO_VERSION = "3.3.0"
       //Activity
       const val ACTIVITY_KTX_VERSION = "1.5.1"
       //Fragment
       const val FRAGMENT_KTX_VERSION = "1.5.2"
       //分包 MultiDex
       const val MULTIDEX_VERSION = "2.0.1"

       //Android-------------------------------------------------
       //单元测试
       const val JUNIT_VERSION = "4.13"
       //Material样式
       const val MATERIAL_VERSION = "1.5.0"
       //智能排版
       const val FLEX_LAYOUT_VERSION = "2.0.1"


       //kotlin相关-----------------------------------------------
       const val KOTLIN_VERSION = "1.8.10"
       //协程
       const val COROUTINES_VERSION = "1.7.0"

       //JetPack--------------------------------------------------
       //Lifecycle相关（ViewModel & LiveData & Lifecycle）
       const val LIFECYCLE = "2.5.1"
       const val LIFECYCLE_EXTENSION = "2.2.0"
       //DI框架-Hilt
       const val HILT = "2.47"
       const val HILT_ANDROIDX = "1.0.0"
       //数据库Room
       const val ROOM_VERSION = "2.4.3"
       //camerax
       const val CAMERAX_VERSION = "1.0.2"
       //cameraView
       const val CAMERA_VIEW_VERSION = "1.0.0-alpha26"
       //zxing
       const val ZXING_VERSION = "3.4.1"

       //GitHub相关--------------------------------------------------
       const val GO_ROUTER = "2.5.1"                          // GoRoute路由
       const val GO_ROUTER_COMPILER = "2.5.1"                  // 阿里路由 APT
       const val OKHTTP = "4.11.0"                         // OkHttp
       const val OKHTTP_INTERCEPTOR_LOGGING = "4.11.0"       // OkHttp 请求Log拦截器
       const val RETROFIT = "2.9.0"                        // Retrofit
       const val RETROFIT_FIT_CONVERTER_GSON = "2.9.0"           // Retrofit Gson 转换器
       const val GSON = "2.8.7"                            // Gson
       const val MM_KV = "1.2.11"                           // 腾讯 MMKV 替代SP
       const val AUTO_SERVICE = "1.0"                       // 自动生成SPI暴露服务文件
       const val EVENT_BUS = "3.3.1"                        // 事件总线
       const val AUTO_SIZE = "1.2.1"                        // 屏幕适配
       const val BOTTOM_NAVIGATION = "2.0.0"                // 底部导航栏
       const val MAGIC_INDICATOR = "1.7.0"                  // 指示器
       const val AGENT = "v5.0.0-alpha.1-androidx"         // Android WebView
       const val SKETCH = "2.7.1"                          // 预览大图
       const val LOAD_SIR = "1.3.8"                         // 全局视图加载
       const val COOKIE = "v1.0.0"                         // Cookie管理
       const val GLIDE = "4.11.0"                          // Glide图像版本
       const val SmartRefresh = "2.0.3"                    // 智能刷新库
       const val BaseAdapter = "3.0.4"                     // Base适配器
       const val RecycleviewVersion = "1.3.2"              // recycleview
       const val BannerVersion = "2.2.2"                   // Banner
       const val ShimmerLayoutVersion = "2.1.0"            // shimmerLayout
       //第三方其他相关------------------------------------------------------
       const val LeakCanaryVersion = "2.8.1"               // 内存泄漏相关
       const val BuglyVersion = "4.1.9.2"                   // bugly异常上报
       const val AndroidAop = "1.3.1"                       //aop相关
       const val AndroidKspVersion = "1.8.10-1.0.9"         //ksp版本

       const val ExoPlayerVersion = "2.17.0"                //视频播放器版本
       const val lottileVersion =  "2.8.0"                  //lottie 动画版本
       const val slf4jVersion = "1.7.21"                    //slf4j版本

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
        const val TestExtJunit = "androidx.test.ext:junit:${Version.TEST_JUNIT_VERSION}"
        const val TestEspresso = "androidx.test.espresso:espresso-core:${Version.TEST_ESPRESSO_VERSION}"
        //AppCompat
        const val AppCompat = "androidx.appcompat:appcompat:${Version.APPCOMPAT_VERSION}"
        //CoreKts
        const val CoreKts = "androidx.core:core-ktx:${Version.CORE_KTX_VERSION}"
        //约束布局
        const val ConstraintLayout = "androidx.constraintlayout:constraintlayout:${Version.CONSTRAINTLAYOUT_VERSION}"
        //ActivityKtx
        const val ActivityKtx = "androidx.activity:activity-ktx:${Version.ACTIVITY_KTX_VERSION}"
        //FragmentKtx
        const val FragmentKtx = "androidx.fragment:fragment-ktx:${Version.FRAGMENT_KTX_VERSION}"
        //MultiDex
        const val MultiDex = "androidx.multidex:multidex:${Version.MULTIDEX_VERSION}"


    }


    /**
     * Android相关依赖
     *
     * @author Knight
     * @since 14/12/21 15:48 PM
     */
    object Android {
        const val Junit = "junit:junit:${Version.JUNIT_VERSION}"
        const val Material = "com.google.android.material:material:${Version.MATERIAL_VERSION}"
        //flexLayout
        const val FlexLayout = "com.google.android:flexbox:${Version.FLEX_LAYOUT_VERSION}"
        //exoPlayer
        const val ExoPlayer = "com.google.android.exoplayer:exoplayer:${Version.ExoPlayerVersion}"
        const val ExoPlayerCore = "com.google.android.exoplayer:exoplayer-core:${Version.ExoPlayerVersion}"
        const val ExoPlayerDash = "com.google.android.exoplayer:exoplayer-dash:${Version.ExoPlayerVersion}"
        const val ExoPlayerHls = "com.google.android.exoplayer:exoplayer-hls:${Version.ExoPlayerVersion}" //如果要播放 m3u8 ，添加exoplayer-hls
        const val ExoPlayerUi = "com.google.android.exoplayer:exoplayer-ui:${Version.ExoPlayerVersion}"
        const val ExoPlayerRtmp = "com.google.android.exoplayer:extension-rtmp:${Version.ExoPlayerVersion}"
    }

    /**
     * Kotlin相关依赖
     *
     * @author Knight
     * @since 14/12/21 15:57 PM
     */
    object Kotlin {
        const val Kotlin = "org.jetbrains.kotlin:kotlin-stdlib:${Version.KOTLIN_VERSION}"
        const val CoroutinesCore =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.COROUTINES_VERSION}"
        const val CoroutinesAndroid =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES_VERSION}"
    }

    /**
     * JetPack相关依赖
     * @author Knight
     * @since 14/12/21 15:54 PM
     */
    object JetPack {
        const val ViewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.LIFECYCLE}"
        const val ViewModelSavedState =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.LIFECYCLE}"
        const val LiveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.LIFECYCLE}"
        const val Lifecycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.LIFECYCLE}"
        const val LifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Version.LIFECYCLE_EXTENSION}"
        const val CameraCore = "androidx.camera:camera-core:${Version.CAMERAX_VERSION}"
        const val CameraCamera2 = "androidx.camera:camera-camera2:${Version.CAMERAX_VERSION}"
        const val CameraLifecycle = "androidx.camera:camera-lifecycle:${Version.CAMERAX_VERSION}"
        const val CameraView = "androidx.camera:camera-view:${Version.CAMERA_VIEW_VERSION}"
        const val Zxing = "com.google.zxing:core:${Version.ZXING_VERSION}"
        const val LifecycleCompilerAPT =
            "androidx.lifecycle:lifecycle-compiler:${Version.LIFECYCLE}"
        const val HiltCore = "com.google.dagger:hilt-android:${Version.HILT}"
        const val HiltApt = "com.google.dagger:hilt-compiler:${Version.HILT}"
        const val HiltAndroidx = "androidx.hilt:hilt-compiler:${Version.HILT_ANDROIDX}"
        const val RoomTime = "androidx.room:room-runtime:${Version.ROOM_VERSION}"
        const val RoomCompiler = "androidx.room:room-compiler:${Version.ROOM_VERSION}"
        const val RoomKtx = "androidx.room:room-ktx:${Version.ROOM_VERSION}"
    }

    /**
     * GitHub及其他相关依赖
     *
     * @author Knight
     * @since 14/12/21 19:07 PM
     */
    object GitHub {
        //GoRouter https://github.com/wyjsonGo/GoRouter
        const val GoRouter = "com.github.wyjsonGo.GoRouter:GoRouter-Api:${Version.GO_ROUTER}"
        const val GoRouteCompiler = "com.github.wyjsonGo.GoRouter:GoRouter-Compiler:${Version.GO_ROUTER_COMPILER}"
        //google https://github.com/google/auto
        const val AutoService = "com.google.auto.service:auto-service:${Version.AUTO_SERVICE}"
        const val AutoServiceAnnotations =
            "com.google.auto.service:auto-service-annotations:${Version.AUTO_SERVICE}"
        //okhttp https://github.com/square/okhttp
        const val OkHttp = "com.squareup.okhttp3:okhttp:${Version.OKHTTP}"
        //retrofit https://github.com/square/retrofit
        const val Retrofit = "com.squareup.retrofit2:retrofit:${Version.RETROFIT}"

        const val RetrofitConverterGson =
            "com.squareup.retrofit2:converter-gson:${Version.RETROFIT_FIT_CONVERTER_GSON}"
        //Gson https://github.com/google/gson
        const val Gson = "com.google.code.gson:gson:${Version.GSON}"
        const val OkHttpInterceptorLogging = "com.squareup.okhttp3:logging-interceptor:${Version.OKHTTP_INTERCEPTOR_LOGGING}"
        //EventBus https://github.com/greenrobot/EventBus
        const val EventBus = "org.greenrobot:eventbus:${Version.EVENT_BUS}"
        const val EventBusAPT = "org.greenrobot:eventbus-annotation-processor:${Version.EVENT_BUS}"
        //屏幕适配 https://github.com/JessYanCoding/AndroidAutoSize
        const val AutoSize = "me.jessyan:autosize:${Version.AUTO_SIZE}"
        //底部导航栏 https://github.com/CListery/BottomNavigationEx
        const val bottomNavigate = "io.github.clistery:bottomnavigationex-ex:${Version.BOTTOM_NAVIGATION}"
        const val bottomNavigateEx = "io.github.clistery:bottomnavigationex-16x:${Version.BOTTOM_NAVIGATION}"
        //MMKV https://github.com/Tencent/MMKV
        const val MMKV = "com.tencent:mmkv:${Version.MM_KV}"
        //滑动切换 https://github.com/hackware1993/MagicIndicator
        const val MagicIndicator = "com.github.hackware1993:MagicIndicator:${Version.MAGIC_INDICATOR}"
        //网页加载库 https://github.com/Justson/AgentWeb
        const val AgentWeb = "com.github.Justson.AgentWeb:agentweb-core:${Version.AGENT}"
        //预览大图插件 https://github.com/panpf/sketch
        const val SketchPhoto = "io.github.panpf.sketch:sketch:${Version.SKETCH}"
        //全局加载视图 https://github.com/KingJA/LoadSir
        const val LoadSir = "com.kingja.loadsir:loadsir:${Version.LOAD_SIR}"
        //Cookie管理框架 https://github.com/franmontiel/PersistentCookieJar https://github.com/gotev/android-cookie-store
        //图像加载库 https://github.com/bumptech/glide
        const val Glide = "com.github.bumptech.glide:glide:${Version.GLIDE}"
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
        //aop相关 https://github.com/FlyJingFish/AndroidAOP
        const val AndroidAop = "io.github.FlyJingFish.AndroidAop:android-aop-core:${Version.AndroidAop}"
        const val AndroidAopAnnotation = "io.github.FlyJingFish.AndroidAop:android-aop-annotation:${Version.AndroidAop}"
        const val AndroidKsp = "io.github.FlyJingFish.AndroidAop:android-aop-ksp:${Version.AndroidAop}"
    }

    /**
     * 第三方sdk
     *
     */
    object OtherUtils {
        //bugly https://bugly.qq.com/docs/release-notes/release-android-bugly
        const val Bugly = "com.tencent.bugly:crashreport:${Version.BuglyVersion}"
        const val Lottle = "com.airbnb.android:lottie:${Version.lottileVersion}"
        const val slf4j = "org.slf4j:slf4j-android:${Version.slf4jVersion}"
    }



}