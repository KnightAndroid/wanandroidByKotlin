## WanAndroid
这是一个不断迭代，功能全面，简单易用的wanandroid APP

**Java MVP版：**~~[https://github.com/KnightAndroid/wanandroid](https://github.com/KnightAndroid/wanandroid)~~ 不再维护

## 前言

本APP是基于鸿洋开放的API来实现，项目采用Kotlin语言，MVVM代码模式，基于组件化 + 协程 + Jetpack + ARouter + Hilt + Retrofit等开源框架实现，本项目会一直持续维护迭代。

如果你也觉得好用，欢迎提供建议和想法(issues)，谢谢。


## 代码模式
![代码模式架构图.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/MVVM%E4%BB%A3%E7%A0%81%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

## 项目架构
![项目架构图.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

## 项目模块依赖图
![项目模块依赖图](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid%E6%A8%A1%E5%9D%97%E4%BE%9D%E8%B5%96%E5%9B%BE.png)

**项目目录简单说明：**

* **最外层**`core`**目录**：为**所有App(暂时只有wanandroid这个App)**都可以使用的代码及资源，**内部模块**被**所有App**内的`core`**模块**依赖。
* **最外层**`nowinandroid`**目录**：为 **wanandroidApp自己独有（特有）** 的相关代码及资源。
  * `core`**模块**：依赖**最外层**`core`**目录**内的模块，**反之不行**。
  * `app`**模块**：是wanandroidApp项目的app模块
  * `feature_xxx`**模块**：是wanandroidApp某个功能模块，直接依赖`nowinandroid`下的`core`模块





## 主要功能

- 首页、广场、导航、项目、公众号、我的、课程、推荐视频、开眼浏览图片视频等模块
- 登录注册
- 搜索功能：热门查询，搜索历史
- 收藏：文章添加收藏，取消收藏
- 扫一扫
- 积分排行榜
- 查看自己分享文章
- 他人等级以及所分享文章
- 用户主页
- 文章阅读历史
- 错误文章链接反馈
- 每日推荐
- 公众号搜索
- 消息管理
- 分类标签
- 深色模式切换
- 护眼模式切换
- 主题色切换
- 多语言切换
- 字体大小
- 指纹解锁
- 横屏适配
- 手势密码解锁
- 内存泄漏检测
- 推荐视频
- 自动夜间模式切换
- 开眼功能浏览图片和视频等
- 天气功能，日月升落图，降雨量，温度折线图等
- 热搜新闻功能



## 截图展示
### 普通模式
| ![1.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_home.jpg) | ![2.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_square.jpg) | ![3.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_project.jpg) |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![4.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_navigate.jpg) | ![5.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_cardblur.jpg) | ![6.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_othershare.jpg) |
| ![7.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_scan.jpg) | ![8.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_search.jpg) | ![9.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_secondfloor.jpg) |
| ![10.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_everydaypush.jpg) | ![11.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_historyrecord.jpg) | ![12.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_updateapp.jpg) |
| ![11.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_knowledgeLabel.jpg) | ![12.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_message_remind.jpg) | ![13.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_messagecenter.jpg) |
| ![14.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/set_changesize.png) | ![wanandroid_biometric_finger.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_biometric_finger.jpg) |![wanandroid_gesturelock.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_gesturelock.jpg) |
| ![15.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/home_feedback_article.png) |![wanandroid_shortcuts.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_shortcuts.jpg) |![wanandroid_home_eye_category.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_home_eye_category.jpg) |
| ![wanandroid_home_weather_detail.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_home_weather_detail.jpg) |![wanandroid_home_select_city.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_home_select_city.jpg)|![home_wanandroid_realtime.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/home_wanandroid_realtime.png)

### 深色模式
| ![dark_01.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/home_dark_01.jpg) | ![dark_02.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/square_dark_02.jpg) | ![dark_03.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/navigate_dark_03.jpg) |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![dark_04.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/mine_dark_04.jpg) |

### 护眼模式
| ![eyecare_01.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/eyecare_01.jpg) | ![eyecare_02.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/eyecare_02.jpg) |
| ------------------------------------------------------------ | ------------------------------------------------------------ |

### 主题切换
| ![theme_01.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/theme_01.jpg) | ![theme_02.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/theme_02.jpg) | ![theme_03.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/theme_03.jpg) |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![theme_04.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/theme_04.jpg) | ![theme_05.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/theme_05.jpg) |

### 多语言
| ![english_01.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/english_01.jpg) | ![english_02.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/english_02.jpg) | ![english_03.jpg](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/english_03.jpg) |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |

### 横屏模式
| ![landscape_home.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_landscape_home.png) | ![landscape_project.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_landscape_project.png) | ![landscape_set.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_landscape_set.png) |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![landscape_square.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_landscape_square.png) | ![landscape_wechat.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_landscape_wechat.png) |

## 主要GIF展示
| ![1.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_01.gif)          | ![2.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_02.gif) | ![3.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_03.gif) |
|------------------------------------------------------------------------------------------------------------| ------------------------------------------------------------ | ------------------------------------------------------------ |
| ![4.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_04.gif)          |![5.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_05.gif) | ![6.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_06.gif) |
| ![7.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_07.gif)          |![8.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_08.gif) |![9.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_09.gif) |
| ![10.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_10.gif)         |![11.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_skeleton.gif)|![12.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_icon_animate.gif)|
| ![13.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/recommend_video.gif) |![14.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_up_data_eye.gif) | ![15.gif](https://github.com/KnightAndroid/wanandroid_server/blob/main/wanandroid_gif/home_two_level.gif)
## 主要开源框架

- [GoRouter](https://github.com/wyjsonGo/GoRouter)
- [AndroidAutoSize](https://github.com/JessYanCoding/AndroidAutoSize)
- [LoadSir](https://github.com/KingJA/LoadSir)
- [SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)
- [MagicIndicator](https://github.com/hackware1993/MagicIndicator)
- [FlexboxLayout](https://github.com/google/flexbox-layout)
- [EasyHttp](https://github.com/getActivity/EasyHttp)
- [okhttp](https://github.com/square/okhttp)
- [PersistentCookieJar](https://github.com/franmontiel/PersistentCookieJar)
- [XXPermissions](https://github.com/getActivity/XXPermissions)
- [glide](https://github.com/bumptech/glide)
- [MMKV](https://github.com/greenrobot/EventBus)
- [eventbus](https://github.com/greenrobot/EventBus)
- [BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
- [SwipeRecyclerView](https://github.com/yanzhenjie/SwipeRecyclerView)
- [AgentWeb](https://github.com/Justson/AgentWeb)
- [LeakCanary](https://github.com/square/leakcanary)

## 本项目开发环境
- 基于Android Studio Meerkat Feature Drop | 2024.3.2 Patch 1
- Android Gradle Plugin 8.9
- Android Gradle插件8.0.0
- Gradle JDK 17

打包方式如下图：
![wanandroid_release_apk.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_release_apk.png)

打包成功后会在`app\build\outputs\apk\pro\release`目录下有`wanandroid_vxxx_release_xxx.apk`形式的release安装包,如下

![wanandroid_app_path.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_app_path.png)

**注意：**
升级到1.2.1版本后用resguardRelease打包命令导致首页收藏按钮动画异常，所以采取普通的assembleRelease,如下

![wanandroid_normal_release.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_normal_release.png)

若不能编译，打开AS**Settings** -- **Build,Execution,Deployment** -- **Build Tools** -- **Gradle** -- **Gradle JDK** 设置JDK 17


## 更新日志

- [更新日志](UPDATE_LOG.md)



## 本人联系
-  邮箱：15015706912@163.com

## 本APP下载链接

-  二维码下载

| ![wx_qrcode](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_kotlin_download.png) |
|---|

-  链接下载 [APP下载链接](https://www.pgyer.com/uf4k)

## Thanks
- 感谢鸿洋大大开放的[API](https://github.com/hongyangAndroid/wanandroid)
- [https://github.com/breezy-weather/breezy-weather](https://github.com/breezy-weather/breezy-weather)
- [https://github.com/zrq1060/architecture-android](https://github.com/zrq1060/architecture-android)
- [nowinandroid](https://github.com/android/nowinandroid)
- 感谢开眼的API

## 非商业用途声明

本项目中的代码、文档和相关资源仅限用于个人学习、学术研究或其他非商业用途。

## License

Copyright 2020 knight

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.


