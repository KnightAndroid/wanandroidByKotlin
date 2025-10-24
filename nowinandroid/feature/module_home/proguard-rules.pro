# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-dontwarn com.core.library_base.R$anim
-dontwarn com.core.library_base.R$drawable
-dontwarn com.core.library_base.R$id
-dontwarn com.core.library_base.R$layout
-dontwarn com.core.library_base.R$string

-dontwarn com.google.android.material.R$attr
-dontwarn com.google.android.material.R$style
-dontwarn com.knight.kotlin.library_widget.R$color
-dontwarn com.knight.kotlin.library_widget.R$drawable
-dontwarn com.knight.kotlin.library_widget.R$layout

-dontwarn com.core.library_base.annotation.EventBusRegister
-dontwarn com.core.library_base.databinding.BaseArticleItemBinding
-dontwarn com.core.library_base.databinding.BaseTextItemBinding
-dontwarn com.core.library_base.event.MessageEvent$MessageType
-dontwarn com.core.library_base.event.MessageEvent
-dontwarn com.core.library_base.ktx.CommonExtKt
-dontwarn com.core.library_base.ktx.GhostFragment
-dontwarn com.core.library_base.util.EventBusUtils
-dontwarn com.core.library_base.vm.BaseViewModel
-dontwarn com.core.library_common.util.ColorUtils
-dontwarn com.knight.kotlin.library_base.BaseApp
-dontwarn com.knight.kotlin.library_base.entity.BaiduCardBean
-dontwarn com.knight.kotlin.library_base.entity.BaiduCardDataBean
-dontwarn com.knight.kotlin.library_base.entity.BaiduContent
-dontwarn com.knight.kotlin.library_base.entity.BaiduErrorMessage
-dontwarn com.knight.kotlin.library_base.entity.BaiduTopRealTimeBean
-dontwarn com.knight.kotlin.library_base.entity.OfficialAccountEntity
-dontwarn com.knight.kotlin.library_base.entity.Rise
-dontwarn com.knight.kotlin.library_base.entity.SearchHotKeyEntity
-dontwarn com.knight.kotlin.library_base.entity.TodayWeatherDataBean
-dontwarn com.knight.kotlin.library_base.entity.WeatherAir
-dontwarn com.knight.kotlin.library_base.entity.WeatherEveryHour
-dontwarn com.knight.kotlin.library_base.entity.WeatherIndexItem
-dontwarn com.knight.kotlin.library_base.fragment.BaseDialogFragment
-dontwarn com.knight.kotlin.library_base.fragment.BaseFragment
-dontwarn com.knight.kotlin.library_base.ktx.CommonBaseExtKt
-dontwarn com.knight.kotlin.library_base.utils.ArouteUtils$Companion
-dontwarn com.knight.kotlin.library_base.utils.ArouteUtils
-dontwarn com.knight.kotlin.library_common.config.Appconfig
-dontwarn com.knight.kotlin.library_common.entity.AppThemeBean
-dontwarn com.knight.kotlin.library_common.enum.BackgroundAnimationMode
-dontwarn com.knight.kotlin.library_common.enum.PollutantIndex
-dontwarn com.knight.kotlin.library_common.util.LanguageFontSizeUtils
-dontwarn com.knight.kotlin.library_database.dao.PushArticlesDateDao
-dontwarn com.knight.kotlin.library_database.dao.SearchDayPushArticleDao
-dontwarn com.knight.kotlin.library_database.db.AppDataBase$Companion
-dontwarn com.knight.kotlin.library_database.entity.PushDateEntity
-dontwarn com.knight.kotlin.library_database.entity.SearchHistroyKeywordEntity
-dontwarn com.knight.kotlin.library_database.repository.HistroyKeywordsRepository
-dontwarn com.knight.kotlin.library_database.repository.PushArticlesDataRepository
-dontwarn com.knight.kotlin.library_database.repository.SearchCityRepository
-dontwarn com.knight.kotlin.library_network.model.ExceptionHandlerKt
-dontwarn com.knight.kotlin.library_permiss.OnPermissionCallback
-dontwarn com.knight.kotlin.library_util.DateUtils
-dontwarn com.knight.kotlin.library_util.StringUtils
-dontwarn com.knight.kotlin.library_util.UtilsKt
-dontwarn com.knight.kotlin.library_util.baidu.OnceLocationListener
-dontwarn com.knight.kotlin.library_util.image.ImageExtKt
-dontwarn com.knight.kotlin.library_util.image.ImageLoader$Companion
-dontwarn com.knight.kotlin.library_util.image.ImageLoader
-dontwarn com.knight.kotlin.library_util.toast.ToastUtils
-dontwarn com.knight.kotlin.library_widget.AlignTextView
-dontwarn com.knight.kotlin.library_widget.CardShinningView
-dontwarn com.knight.kotlin.library_widget.ClearEditText
-dontwarn com.knight.kotlin.library_widget.EasyFlipView$OnFlipAnimationListener
-dontwarn com.knight.kotlin.library_widget.EasyFlipView
-dontwarn com.knight.kotlin.library_widget.IncepterVerticalNestScrollView
-dontwarn com.knight.kotlin.library_widget.RoundProgress
-dontwarn com.knight.kotlin.library_widget.StickyScrollView
-dontwarn com.knight.kotlin.library_widget.ZzWeatherView
-dontwarn com.knight.kotlin.library_widget.circleimagebar.CircularMusicProgressBar
-dontwarn com.knight.kotlin.library_widget.citypicker.InnerListener
-dontwarn com.knight.kotlin.library_widget.citypicker.view.SideIndexBar$OnIndexTouchedChangedListener
-dontwarn com.knight.kotlin.library_widget.citypicker.view.SideIndexBar
-dontwarn com.knight.kotlin.library_widget.databinding.BaseIncludeToolbarBinding
-dontwarn com.knight.kotlin.library_widget.databinding.BaseLayoutRecycleviewBinding
-dontwarn com.knight.kotlin.library_widget.flowlayout.FlowLayout
-dontwarn com.knight.kotlin.library_widget.flowlayout.OnTagClickListener
-dontwarn com.knight.kotlin.library_widget.flowlayout.TagInfo
-dontwarn com.knight.kotlin.library_widget.pagetransformer.DragLayout$GotoDetailListener
-dontwarn com.knight.kotlin.library_widget.pagetransformer.DragLayout
-dontwarn com.knight.kotlin.library_widget.skeleton.SkeletonScreen
-dontwarn com.knight.kotlin.library_widget.slidinglayout.SlidingLayout$MenuStatusListener
-dontwarn com.knight.kotlin.library_widget.slidinglayout.SlidingLayout
-dontwarn com.knight.kotlin.library_widget.utils.WeatherPicUtil
-dontwarn com.knight.kotlin.library_widget.utils.WeatherUtils
-dontwarn com.knight.kotlin.library_widget.weatherview.ArcProgress$ArcProgressDrawStatus
-dontwarn com.knight.kotlin.library_widget.weatherview.ArcProgress
-dontwarn com.knight.kotlin.library_widget.weatherview.ScrollBarChartView
-dontwarn com.knight.kotlin.library_widget.weatherview.sunmoon.SunMoonView$SunMoonDrawStatus
-dontwarn com.knight.kotlin.library_widget.weatherview.sunmoon.SunMoonView
-dontwarn com.knight.library_biometric.listener.BiometricStatusCallback
-dontwarn com.knight.kotlin.library_widget.citypicker.CityListAdapter
-dontwarn com.knight.kotlin.library_util.Mp3PlayerUtils$Companion
-dontwarn com.knight.kotlin.library_util.Mp3PlayerUtils
-dontwarn com.knight.kotlin.library_widget.overlaymenu.FloatWindow
-dontwarn com.knight.kotlin.library_database.entity.CityBean




