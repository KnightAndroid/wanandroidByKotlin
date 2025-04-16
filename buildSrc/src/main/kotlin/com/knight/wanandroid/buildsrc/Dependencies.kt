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
       const val KOTLIN_GRADLE_PLUGIN_VERSION = "2.0.21"
       //kotlin Serialization序列化（KOTLIN_GRADLE_PLUGIN_VERSION后续要和这个版本统一）
       const val KOTLIN_SERIALIZATION = "1.8.10"
       //AndroidX----------------------------------------------
       const val APPCOMPAT_VERSION = "1.7.0"
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
       const val MATERIAL_VERSION = "1.12.0"
       //智能排版
       const val FLEX_LAYOUT_VERSION = "3.0.0"


       //kotlin相关-----------------------------------------------
       const val KOTLIN_VERSION = "2.0.21"
       //协程
       const val COROUTINES_VERSION = "1.7.0"

       //JetPack--------------------------------------------------
       //Lifecycle相关（ViewModel & LiveData & Lifecycle）
       const val LIFECYCLE = "2.5.1"
       const val LIFECYCLE_EXTENSION = "2.2.0"
       //DI框架-Hilt
       const val HILT = "2.51.1"
       const val HILT_ANDROIDX = "1.0.0"
       //数据库Room
       const val ROOM_VERSION = "2.6.0"
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
       const val MMKV = "1.2.11"                           // 腾讯 MMKV 替代SP
       const val AUTO_SERVICE = "1.0"                       // 自动生成SPI暴露服务文件
       const val EVENT_BUS = "3.3.1"                        // 事件总线
       const val AUTO_SIZE = "v1.2.1"                        // 屏幕适配
       const val BOTTOM_NAVIGATION = "2.2.0"                // 底部导航栏
       const val MAGIC_INDICATOR = "1.7.0"                  // 指示器
       const val AGENT = "v5.0.0-alpha.1-androidx"         // Android WebView
       const val SKETCH = "2.7.1"                          // 预览大图
       const val LOAD_SIR = "1.3.8"                         // 全局视图加载
       const val COOKIE = "v1.0.0"                         // Cookie管理
       const val GLIDE = "4.11.0"                          // Glide图像版本
       const val SMART_REFRESH = "2.1.0"                    // 智能刷新库
       const val BASE_ADAPTER = "4.1.4"                     // Base适配器
       const val RECYCLEVIEW_VERSION = "1.3.2"              // recycleview
       const val BANNER_VERSION = "2.2.2"                   // Banner
       const val SHIMMER_LAYOUT_VERSION = "2.1.0"            // shimmerLayout
       //第三方其他相关------------------------------------------------------
       const val LEAKCANARY_VERSION = "2.8.1"               // 内存泄漏相关
       const val BUG_LY = "4.1.9.2"                   // bugly异常上报
       const val ANDROID_AOP = "2.4.8"                       //aop相关
       const val ANDROID_KSP_VERSION = "2.0.21-1.0.25"         //ksp版本  https://github.com/google/ksp/releases?page=1

       const val EXOPLAYER = "2.17.0"                //视频播放器版本
       const val LOTTIE =  "2.8.0"                  //lottie 动画版本
       const val SLF4J = "1.7.21"                    //slf4j版本
       const val windowx = "1.3.0"                      //window 版本
       const val kotlinxSerializationJson = "1.7.3"   //kotlin序列化帮助类
       const val BaiduMapSdkVersion = "9.6.4" //地图定位
       const val BaiduNaviSdkVersion = "7.6.4" //导航版本
       const val Suncalc = "3.11" //日月日出日落月出月落 计算
    }
    /**
     * AndroidX相关依赖
     *
     * @author Knight
     * @since 14/14/21 14:48 PM
     */
    object Androidx {
        //单元测试
        const val ANDROID_JUNIT_RUNNER = "androidx.test.runner.AndroidJUnitRunner"
        const val TEST_EXT_JUNIT = "androidx.test.ext:junit:${Version.TEST_JUNIT_VERSION}"
        const val TEST_ESPRESSO = "androidx.test.espresso:espresso-core:${Version.TEST_ESPRESSO_VERSION}"
        //AppCompat
        const val APP_COMPAT = "androidx.appcompat:appcompat:${Version.APPCOMPAT_VERSION}"
        //CoreKts
        const val CORE_KTS = "androidx.core:core-ktx:${Version.CORE_KTX_VERSION}"
        //约束布局
        const val CONSTRAINTLAYOUT = "androidx.constraintlayout:constraintlayout:${Version.CONSTRAINTLAYOUT_VERSION}"
        //ActivityKtx
        const val ACTIVITY_KTX = "androidx.activity:activity-ktx:${Version.ACTIVITY_KTX_VERSION}"
        //FragmentKtx
        const val FRAGMENT_KTX = "androidx.fragment:fragment-ktx:${Version.FRAGMENT_KTX_VERSION}"
        //MultiDex
        const val MULTIDEX = "androidx.multidex:multidex:${Version.MULTIDEX_VERSION}"
        //windows
        const val WINDOWX = "androidx.window:window:${Version.windowx}"

    }


    /**
     * Android相关依赖
     *
     * @author Knight
     * @since 14/12/21 15:48 PM
     */
    object Android {
        const val JUNIT = "junit:junit:${Version.JUNIT_VERSION}"
        const val MATERIAL = "com.google.android.material:material:${Version.MATERIAL_VERSION}"
        //flexLayout
        const val FLEXLAYOUT = "com.google.android.flexbox:flexbox:${Version.FLEX_LAYOUT_VERSION}"
        //exoPlayer
        const val EXOPLAYER = "com.google.android.exoplayer:exoplayer:${Version.EXOPLAYER}"
        const val EXOPLAYER_CORE = "com.google.android.exoplayer:exoplayer-core:${Version.EXOPLAYER}"
        const val EXOPLAYER_DASH = "com.google.android.exoplayer:exoplayer-dash:${Version.EXOPLAYER}"
        const val EXOPLAYER_HLS = "com.google.android.exoplayer:exoplayer-hls:${Version.EXOPLAYER}" //如果要播放 m3u8 ，添加exoplayer-hls
        const val EXOPLAYER_UI = "com.google.android.exoplayer:exoplayer-ui:${Version.EXOPLAYER}"
        const val EXOPLAYER_RTMP = "com.google.android.exoplayer:extension-rtmp:${Version.EXOPLAYER}"
    }

    /**
     * Kotlin相关依赖
     *
     * @author Knight
     * @since 14/12/21 15:57 PM
     */
    object Kotlin {
        const val KOTLIN = "org.jetbrains.kotlin:kotlin-stdlib:${Version.KOTLIN_VERSION}"
        const val COROUTINES_CORE =
            "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Version.COROUTINES_VERSION}"
        const val COROUTINES_ANDROID =
            "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Version.COROUTINES_VERSION}"

        const val kotlinx_serialization_json =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:${Version.kotlinxSerializationJson}"
    }

    /**
     * JetPack相关依赖
     * @author Knight
     * @since 14/12/21 15:54 PM
     */
    object JetPack {
        const val VIEWMODEL = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Version.LIFECYCLE}"
        const val VIEWMODEL_SAVEDSTATE =
            "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Version.LIFECYCLE}"
        const val LIVEDATA = "androidx.lifecycle:lifecycle-livedata-ktx:${Version.LIFECYCLE}"
        const val LIFECYCLE = "androidx.lifecycle:lifecycle-runtime-ktx:${Version.LIFECYCLE}"
        const val LIFECYCLE_EXTENSIONS = "androidx.lifecycle:lifecycle-extensions:${Version.LIFECYCLE_EXTENSION}"
        const val CAMERA_CORE = "androidx.camera:camera-core:${Version.CAMERAX_VERSION}"
        const val CAMERA2 = "androidx.camera:camera-camera2:${Version.CAMERAX_VERSION}"
        const val CAMERA_LIFECYCLE = "androidx.camera:camera-lifecycle:${Version.CAMERAX_VERSION}"
        const val CAMERA_VIEW = "androidx.camera:camera-view:${Version.CAMERA_VIEW_VERSION}"
        const val ZXNG = "com.google.zxing:core:${Version.ZXING_VERSION}"
        const val LIFECYCLE_COMPILER_APT =
            "androidx.lifecycle:lifecycle-compiler:${Version.LIFECYCLE}"
        const val HILT_CORE = "com.google.dagger:hilt-android:${Version.HILT}"
        const val HILT_APT = "com.google.dagger:hilt-compiler:${Version.HILT}"
        const val HILT_ANDROIDX = "androidx.hilt:hilt-compiler:${Version.HILT_ANDROIDX}"
        const val ROOM_RUNTIME = "androidx.room:room-runtime:${Version.ROOM_VERSION}"
        const val ROOM_COMPILER = "androidx.room:room-compiler:${Version.ROOM_VERSION}"
        const val ROOM_KTX = "androidx.room:room-ktx:${Version.ROOM_VERSION}"
    }

    /**
     * GitHub及其他相关依赖
     *
     * @author Knight
     * @since 14/12/21 19:07 PM
     */
    object GitHub {
        //GoRouter https://github.com/wyjsonGo/GoRouter
        const val GO_ROUTER = "com.github.wyjsonGo.GoRouter:GoRouter-Api:${Version.GO_ROUTER}"
        const val GO_ROUTER_COMPILER = "com.github.wyjsonGo.GoRouter:GoRouter-Compiler:${Version.GO_ROUTER_COMPILER}"
        //google https://github.com/google/auto
        const val AUTO_SERVICE = "com.google.auto.service:auto-service:${Version.AUTO_SERVICE}"
        const val AUTO_SERVICE_ANNOTATIONS =
            "com.google.auto.service:auto-service-annotations:${Version.AUTO_SERVICE}"
        //okhttp https://github.com/square/okhttp
        const val OKHTTP = "com.squareup.okhttp3:okhttp:${Version.OKHTTP}"
        //retrofit https://github.com/square/retrofit
        const val RETROFIT = "com.squareup.retrofit2:retrofit:${Version.RETROFIT}"

        const val RETROFIT_CONVERTER_GSON =
            "com.squareup.retrofit2:converter-gson:${Version.RETROFIT_FIT_CONVERTER_GSON}"
        //Gson https://github.com/google/gson
        const val GSON = "com.google.code.gson:gson:${Version.GSON}"
        const val OKHTTP_INTERCEPTOR_LOGGING = "com.squareup.okhttp3:logging-interceptor:${Version.OKHTTP_INTERCEPTOR_LOGGING}"
        //EventBus https://github.com/greenrobot/EventBus
        const val EVENTBUS = "org.greenrobot:eventbus:${Version.EVENT_BUS}"
        const val EVENTBUS_APT = "org.greenrobot:eventbus-annotation-processor:${Version.EVENT_BUS}"
        //屏幕适配 https://github.com/JessYanCoding/AndroidAutoSize
        const val AUTO_SIZE = "com.github.JessYanCoding:AndroidAutoSize:${Version.AUTO_SIZE}"
        //底部导航栏 https://github.com/CListery/BottomNavigationEx
        const val BOTTOM_NAVIGATE = "io.github.clistery:bottomnavigationex-ex:${Version.BOTTOM_NAVIGATION}"
        const val BOTTOM_NAVIGATE_X = "io.github.clistery:bottomnavigationex-110x:${Version.BOTTOM_NAVIGATION}"
        //MMKV https://github.com/Tencent/MMKV
        const val MMKV = "com.tencent:mmkv:${Version.MMKV}"
        //滑动切换 https://github.com/hackware1993/MagicIndicator
        const val MAGIC_INDICATOR = "com.github.hackware1993:MagicIndicator:${Version.MAGIC_INDICATOR}"
        //网页加载库 https://github.com/Justson/AgentWeb
        const val AGENT_WEB = "com.github.Justson.AgentWeb:agentweb-core:${Version.AGENT}"
        //预览大图插件 https://github.com/panpf/sketch
        const val SKETCH_PHOTO = "io.github.panpf.sketch:sketch:${Version.SKETCH}"
        //全局加载视图 https://github.com/KingJA/LoadSir
        const val LOAD_SIR = "com.kingja.loadsir:loadsir:${Version.LOAD_SIR}"
        //Cookie管理框架 https://github.com/franmontiel/PersistentCookieJar https://github.com/gotev/android-cookie-store
        //图像加载库 https://github.com/bumptech/glide
        const val GLIDE = "com.github.bumptech.glide:glide:${Version.GLIDE}"
        //智能刷新库 核心依赖 https://github.com/scwang90/SmartRefreshLayout
        const val SMART_KERNEL = "io.github.scwang90:refresh-layout-kernel:${Version.SMART_REFRESH}"
        //智能刷新库 经典刷新头
        const val SMART_HEADER = "io.github.scwang90:refresh-header-classics:${Version.SMART_REFRESH}"
        //智能刷新库 经典加载
        const val SMART_FOOTER = "io.github.scwang90:refresh-footer-classics:${Version.SMART_REFRESH}"
        //智能刷新库 二级刷新头
        const val SMART_HEADER_TWO_LEVEL = "io.github.scwang90:refresh-header-two-level:${Version.SMART_REFRESH}"
        //BaseAdapter https://github.com/CymChad/BaseRecyclerViewAdapterHelper
        const val BASE_ADAPTER_HELPER = "io.github.cymchad:BaseRecyclerViewAdapterHelper4:${Version.BASE_ADAPTER}"
        //SwipeRecycleview https://github.com/yanzhenjie/SwipeRecyclerView
        const val SWIPE_RECYCLERVIEW = "com.yanzhenjie.recyclerview:x:${Version.RECYCLEVIEW_VERSION}"
        //Banner https://github.com/youth5201314/banner
        const val BANNER = "io.github.youth5201314:banner:${Version.BANNER_VERSION}"
        //骨架屏 https://github.com/team-supercharge/ShimmerLayout
        const val SHIMMER_LAYOUT = "io.supercharge:shimmerlayout:${Version.SHIMMER_LAYOUT_VERSION}"
        //内存泄漏 https://github.com/square/leakcanary
        const val LEAK_CANARY = "com.squareup.leakcanary:leakcanary-android:${Version.LEAKCANARY_VERSION}"
        //aop相关 https://github.com/FlyJingFish/AndroidAOP
        const val ANDROID_AOP = "io.github.FlyJingFish.AndroidAop:android-aop-core:${Version.ANDROID_AOP}"
        //👇非必须项 👈 这个包提供了一些常见的注解切面
        const val ANDROID_EXTRA_AOP =  "io.github.FlyJingFish.AndroidAop:android-aop-extra:${Version.ANDROID_AOP}"
        const val ANDROID_AOP_ANNOTATION = "io.github.FlyJingFish.AndroidAop:android-aop-annotation:${Version.ANDROID_AOP}"
        const val ANDROID_KSP = "io.github.FlyJingFish.AndroidAop:android-aop-ksp:${Version.ANDROID_AOP}"
    }

    /**
     * 第三方sdk
     *
     */
    object OtherUtils {
        //bugly https://bugly.qq.com/docs/release-notes/release-android-bugly
        const val BUGLY = "com.tencent.bugly:crashreport:${Version.BUG_LY}"
        const val LOTTIE = "com.airbnb.android:lottie:${Version.LOTTIE}"
        const val SLF4J = "org.slf4j:slf4j-android:${Version.SLF4J}"
        const val BAIDULOCATION = "com.baidu.lbsyun:BaiduMapSDK_Location:${Version.BaiduMapSdkVersion}"
        const val BAIDUNAVI = "com.baidu.lbsyun:BaiduMapSDK_Map-AllNavi:${Version.BaiduNaviSdkVersion}"
        const val BAIDUSEARCH = "com.baidu.lbsyun:BaiduMapSDK_Search:${Version.BaiduNaviSdkVersion}"
        const val BAIDUUTIL = "com.baidu.lbsyun:BaiduMapSDK_Util:${Version.BaiduNaviSdkVersion}"
        const val SUNCALC = "org.shredzone.commons:commons-suncalc:${Version.Suncalc}"
    }



}