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
- 基于Android Studio Ladybug
- Android Gradle Plugin 8.9
- Android Gradle插件8.0.0

打包方式如下图：
![wanandroid_release_apk.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_release_apk.png)

打包成功后会在`app\build\outputs\apk\pro\release`目录下有`wanandroid_vxxx_release_xxx.apk`形式的release安装包,如下

![wanandroid_app_path.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_app_path.png)

**注意：**
升级到1.2.1版本后用resguardRelease打包命令导致首页收藏按钮动画异常，所以采取普通的assembleRelease,如下

![wanandroid_normal_release.png](https://gitee.com/MengSuiXinSuoYuan/wanandroid_server/raw/master/wanandroid_picture/wanandroid_normal_release.png)





## 更新日志
### 2.1.2(10)
- wanandroid 第十版发布
- 优化权限库
- 修复一些问题
- 解决调试模式定位功能不生效问题
- 发布时间：2025/06/09

### 2.1.1(9)
-  wanandroid 第九版发布
-  修复一些问题
-  发布时间：2025/05/13

### 2.1.0(8)
-  wanandroid 第八版发布
-  新增热搜功能
-  新增天气功能
-  新增每天新闻
-  修复一些问题
-  发布时间：2025/05/07


### 2.0.0(7)
-  wanandroid 第七版发布
-  适配Android15
-  新增开眼功能
-  更新优化权限库
-  更新优化Toast功能
-  发布时间：2025/02/14

### 1.2.0(6)
-  wanandroid 第六版发布
-  规范代码命名
-  新增我的-推荐视频模仿抖音划一划功能  
-  修改一些问题
-  发布时间：2024/04/15


### 1.1.0(5)
-  wanandroid 第五版发布
-  AGP 版本适配到8.0
-  Android Plugin for Gradle 版本升级到8.0.0
-  权限库版本，路由版本库，kotlin版本改造升级
-  适配Android14
-  修改一些问题
-  发布时间：2024/01/11

### 1.0.3(4)
-  wanandroid 第四版发布
-  AGP Gradle版本适配到7.2
-  Android Plugin for Gradle 版本升级到7.0.2
-  网络请求库版本和日志拦截器同时升级到4.11.0
-  修改一些问题
-  发布时间：2023/08/18


### 1.0.2(3)
-  wanandroid 第三版发布
-  新增关于功能(更新日志，接入合作方目录，检查更新)
-  发布时间：2022/09/02

### 1.0.1(2)
-  wanandroid 第二版发布
-  新增课程工具模块
-  发布时间：2022/06/02

### 1.0.0(1)
-  wanandroid 第一版发布
-  发布时间：2022/06/01




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


