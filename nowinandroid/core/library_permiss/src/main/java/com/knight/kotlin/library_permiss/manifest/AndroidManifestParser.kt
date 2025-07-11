package com.knight.kotlin.library_permiss.manifest

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.content.res.XmlResourceParser
import com.knight.kotlin.library_permiss.manifest.node.ActivityManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.ApplicationManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.BroadcastReceiverManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.ServiceManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.UsesSdkManifestInfo
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


/**
 * Author:Knight
 * Time:2023/8/29 16:37
 * Description:AndroidManifestParser
 */
object AndroidManifestParser {
    /** 清单文件的文件名称  */
    private val ANDROID_MANIFEST_FILE_NAME = "AndroidManifest.xml"

    /** Android 的命名空间  */
    private val ANDROID_NAMESPACE_URI = "http://schemas.android.com/apk/res/android"

    private val TAG_MANIFEST = "manifest"

    private val TAG_USES_SDK = "uses-sdk"
    private val TAG_USES_PERMISSION = "uses-permission"
    private val TAG_USES_PERMISSION_SDK_23 = "uses-permission-sdk-23"
    private val TAG_USES_PERMISSION_SDK_M = "uses-permission-sdk-m"

    private val TAG_QUERIES: String = "queries"
    private val TAG_APPLICATION = "application"
    private val TAG_ACTIVITY = "activity"
    private val TAG_ACTIVITY_ALIAS = "activity-alias"
    private val TAG_SERVICE = "service"
    private val TAG_RECEIVER: String = "receiver"
    private val ATTR_PACKAGE = "package"
    private val ATTR_NAME = "name"
    private val ATTR_MAX_SDK_VERSION = "maxSdkVersion"
    private val ATTR_MIN_SDK_VERSION = "minSdkVersion"
    private val ATTR_USES_PERMISSION_FLAGS = "usesPermissionFlags"
    private val ATTR_REQUEST_LEGACY_EXTERNAL_STORAGE = "requestLegacyExternalStorage"
    private val ATTR_SUPPORTS_PICTURE_IN_PICTURE = "supportsPictureInPicture"
    private val ATTR_PERMISSION = "permission"

    /**
     * 获取当前应用的清单文件信息
     */
    
    fun getAndroidManifestInfo(context: Context): AndroidManifestInfo? {
        val apkPathCookie = findApkPathCookie(context, context.applicationInfo.sourceDir)
        // 如果 cookie 为 0，证明获取失败
        if (apkPathCookie == 0) {
            return null
        }

        var androidManifestInfo: AndroidManifestInfo? = null
        try {
            androidManifestInfo = parseAndroidManifest(context, apkPathCookie)
            // 如果读取到的包名和当前应用的包名不是同一个的话，证明这个清单文件的内容不是当前应用的
            // 具体案例：https://github.com/getActivity/XXPermissions/issues/102
            if (!PermissionUtils.reverseEqualsString(
                    context.packageName,
                    androidManifestInfo.packageName
                )
            ) {
                return null
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        }

        return androidManifestInfo
    }

    /**
     * 获取当前应用 Apk 在 AssetManager 中的 Cookie，如果获取失败，则为 0
     */
    @SuppressLint("PrivateApi")
    fun findApkPathCookie( context: Context,  apkPath: String?): Int {
        val assets = context.assets
        var cookie: Int

        try {
            if (PermissionVersion.getTargetVersion(context) >= PermissionVersion.ANDROID_9 && PermissionVersion.getCurrentVersion() >= PermissionVersion.ANDROID_9 && PermissionVersion.getCurrentVersion() < PermissionVersion.ANDROID_11) {

                // 反射套娃操作：实测这种方式只在 Android 9.0 和 Android 10.0 有效果，在 Android 11 上面就失效了
                val classArrayClass = java.lang.reflect.Array.newInstance(Class::class.java, 0).javaClass
                val metaGetDeclaredMethod: Method = Class::class.java.getDeclaredMethod(
                    "getDeclaredMethod",
                    String::class.java,
                    classArrayClass
                )
                metaGetDeclaredMethod.isAccessible = true
                // 注意 AssetManager.findCookieForPath 是 Android 9.0（API 28）的时候才添加的方法
                // 而 Android 9.0 用的是 AssetManager.addAssetPath 来获取 cookie
                // 具体可以参考 PackageParser.parseBaseApk 方法源码的实现
                val findCookieForPathMethod = metaGetDeclaredMethod.invoke(
                    AssetManager::class.java,
                    "findCookieForPath", arrayOf<Class<*>>(String::class.java)
                ) as Method
                if (findCookieForPathMethod != null) {
                    findCookieForPathMethod.isAccessible = true
                    cookie = findCookieForPathMethod.invoke(context.assets, apkPath) as Int
                    if (cookie != null) {
                        return cookie
                    }
                }
            }

            val addAssetPathMethod = assets.javaClass.getDeclaredMethod(
                "addAssetPath",
                String::class.java
            )
            cookie = addAssetPathMethod.invoke(assets, apkPath) as Int
            if (cookie != null) {
                return cookie
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        // 获取失败直接返回 0
        // 为什么不直接返回 Integer，而是返回 int 类型？
        // 去看看 AssetManager.findCookieForPath 获取失败会返回什么就知道了
        return 0
    }

    /**
     * 解析 apk 包中的清单文件
     *
     * @param context          上下文
     * @param apkCookie        要解析 apk 的 cookie
     */
    
    @Throws(IOException::class, XmlPullParserException::class)
    fun parseAndroidManifest(context: Context, apkCookie: Int): AndroidManifestInfo {
        val manifestInfo = AndroidManifestInfo()

        context.assets.openXmlResourceParser(apkCookie, ANDROID_MANIFEST_FILE_NAME).use { parser ->

            do {
                // 当前节点必须为标签头部
                if (parser.eventType != XmlResourceParser.START_TAG) {
                    continue
                }

                val tagName = parser.name

                if (PermissionUtils.equalsString(TAG_MANIFEST, tagName)) {
                    manifestInfo.packageName = parsePackageFromXml(parser)
                }

                if (PermissionUtils.equalsString(TAG_USES_SDK, tagName)) {
                    manifestInfo.usesSdkManifestInfo = parseUsesSdkFromXml(parser)
                }

                if (PermissionUtils.equalsString(TAG_USES_PERMISSION, tagName) ||
                    PermissionUtils.equalsString(TAG_USES_PERMISSION_SDK_23, tagName) ||
                    PermissionUtils.equalsString(TAG_USES_PERMISSION_SDK_M, tagName)
                ) {
                    manifestInfo.permissionManifestInfoList.add(parsePermissionFromXml(parser))
                }

                if (PermissionUtils.equalsString(TAG_QUERIES, tagName)) {
                    manifestInfo.queriesPackageList.add(parsePackageFromXml(parser))
                }

                if (PermissionUtils.equalsString(TAG_APPLICATION, tagName)) {
                    manifestInfo.applicationManifestInfo = parseApplicationFromXml(parser)
                }

                if (PermissionUtils.equalsString(TAG_ACTIVITY, tagName) ||
                    PermissionUtils.equalsString(TAG_ACTIVITY_ALIAS, tagName)
                ) {
                    manifestInfo.activityManifestInfoList.add(parseActivityFromXml(parser))
                }

                if (PermissionUtils.equalsString(TAG_SERVICE, tagName)) {
                    manifestInfo.serviceManifestInfoList.add(parseServerFromXml(parser))
                }

                if (PermissionUtils.equalsString(TAG_RECEIVER, tagName)) {
                    manifestInfo.broadcastReceiverManifestInfoList.add(parseBroadcastReceiverFromXml(parser))
                }

            } while (parser.next() != XmlResourceParser.END_DOCUMENT)
        }

        return manifestInfo
    }
    
    private fun parsePackageFromXml( parser: XmlResourceParser): String {
        val packageName = parser.getAttributeValue(null, ATTR_PACKAGE)
        return packageName ?: ""
    }

    
    private fun parseUsesSdkFromXml( parser: XmlResourceParser): UsesSdkManifestInfo {
        val usesSdkManifestInfo = UsesSdkManifestInfo()
        usesSdkManifestInfo.minSdkVersion = parser.getAttributeIntValue(
            ANDROID_NAMESPACE_URI,
            ATTR_MIN_SDK_VERSION, 0
        )
        return usesSdkManifestInfo
    }

    
    private fun parsePermissionFromXml( parser: XmlResourceParser): PermissionManifestInfo {
        val permissionManifestInfo = PermissionManifestInfo()
        permissionManifestInfo.name = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        permissionManifestInfo.maxSdkVersion = parser.getAttributeIntValue(
            ANDROID_NAMESPACE_URI,
            ATTR_MAX_SDK_VERSION, Int.MAX_VALUE
        )
        permissionManifestInfo.usesPermissionFlags = parser.getAttributeIntValue(
            ANDROID_NAMESPACE_URI,
            ATTR_USES_PERMISSION_FLAGS, 0
        )
        return permissionManifestInfo
    }

    
    private fun parseApplicationFromXml( parser: XmlResourceParser): ApplicationManifestInfo {
        val applicationManifestInfo = ApplicationManifestInfo()
        val applicationClassName = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        applicationManifestInfo.name = applicationClassName ?: ""
        applicationManifestInfo.requestLegacyExternalStorage = parser.getAttributeBooleanValue(
            ANDROID_NAMESPACE_URI, ATTR_REQUEST_LEGACY_EXTERNAL_STORAGE, false
        )
        return applicationManifestInfo
    }

    
    private fun parseActivityFromXml( parser: XmlResourceParser): ActivityManifestInfo {
        val activityManifestInfo = ActivityManifestInfo()
        val activityClassName = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        activityManifestInfo.name = activityClassName ?: ""
        activityManifestInfo.supportsPictureInPicture = parser.getAttributeBooleanValue(
            ANDROID_NAMESPACE_URI, ATTR_SUPPORTS_PICTURE_IN_PICTURE, false
        )
        return activityManifestInfo
    }

    
    private fun parseServerFromXml( parser: XmlResourceParser): ServiceManifestInfo {
        val serviceManifestInfo = ServiceManifestInfo()
        val serviceClassName = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        serviceManifestInfo.name = serviceClassName ?: ""
        serviceManifestInfo.permission =
            parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_PERMISSION)
        return serviceManifestInfo
    }

    
    private fun parseBroadcastReceiverFromXml( parser: XmlResourceParser): BroadcastReceiverManifestInfo {
        val broadcastReceiverManifestInfo = BroadcastReceiverManifestInfo()
        val broadcastReceiverClassName = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        broadcastReceiverManifestInfo.name = broadcastReceiverClassName ?: ""
        broadcastReceiverManifestInfo.permission =
            parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_PERMISSION)
        return broadcastReceiverManifestInfo
    }
}