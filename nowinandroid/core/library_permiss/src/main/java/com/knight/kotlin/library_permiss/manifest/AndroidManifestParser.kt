package com.knight.kotlin.library_permiss.manifest

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.AssetManager
import android.content.res.XmlResourceParser
import com.knight.kotlin.library_permiss.manifest.node.ActivityManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.ApplicationManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.BroadcastReceiverManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.IntentFilterManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.MetaDataManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.PermissionManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.ServiceManifestInfo
import com.knight.kotlin.library_permiss.manifest.node.UsesSdkManifestInfo
import com.knight.kotlin.library_permiss.tools.PermissionUtils
import com.knight.kotlin.library_permiss.tools.PermissionVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.getCurrentVersion
import com.knight.kotlin.library_permiss.tools.PermissionVersion.getTargetVersion
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method


/**
 * Author:Knight
 * Time:2023/8/29 16:37
 * Description:AndroidManifestParser  清单文件解析器
 */
object AndroidManifestParser {
    /** 清单文件的文件名称  */
    const val ANDROID_MANIFEST_FILE_NAME: String = "AndroidManifest.xml"

    /** Android 的命名空间  */
    const val ANDROID_NAMESPACE_URI: String = "http://schemas.android.com/apk/res/android"
    
    const val TAG_MANIFEST: String = "manifest"
    const val TAG_USES_SDK: String = "uses-sdk"
    const val TAG_USES_PERMISSION: String = "uses-permission"
    const val TAG_USES_PERMISSION_SDK_23: String = "uses-permission-sdk-23"
    const val TAG_USES_PERMISSION_SDK_M: String = "uses-permission-sdk-m"
    const val TAG_QUERIES: String = "queries"
    const val TAG_APPLICATION: String = "application"
    const val TAG_ACTIVITY: String = "activity"
    const val TAG_ACTIVITY_ALIAS: String = "activity-alias"
    const val TAG_SERVICE: String = "service"
    const val TAG_RECEIVER: String = "receiver"
    const val TAG_INTENT_FILTER: String = "intent-filter"
    const val TAG_ACTION: String = "action"
    const val TAG_CATEGORY: String = "category"
    const val TAG_META_DATA: String = "meta-data"
    const val ATTR_PACKAGE: String = "package"
    const val ATTR_NAME: String = "name"
    const val ATTR_VALUE: String = "value"
    const val ATTR_RESOURCE: String = "resource"
    const val ATTR_MAX_SDK_VERSION: String = "maxSdkVersion"
    const val ATTR_MIN_SDK_VERSION: String = "minSdkVersion"
    const val ATTR_USES_PERMISSION_FLAGS: String = "usesPermissionFlags"
    const val ATTR_REQUEST_LEGACY_EXTERNAL_STORAGE: String = "requestLegacyExternalStorage"
    const val ATTR_SUPPORTS_PICTURE_IN_PICTURE: String = "supportsPictureInPicture"
    const val ATTR_PERMISSION: String = "permission"

    /**
     * 获取当前应用的清单文件信息
     */
    
    fun getAndroidManifestInfo(context: Context): AndroidManifestInfo? {
        val apkPathCookie = findApkPathCookie(context, context.applicationInfo.sourceDir)
        // 如果 cookie 为 0，证明获取失败
        if (apkPathCookie == 0) {
            return null
        }

        var manifestInfo: AndroidManifestInfo? = null
        try {
            manifestInfo = parseAndroidManifest(context, apkPathCookie)
            // 如果读取到的包名和当前应用的包名不是同一个的话，证明这个清单文件的内容不是当前应用的
            // 具体案例：https://github.com/getActivity/XXPermissions/issues/102
            if (!PermissionUtils.reverseEqualsString(context.packageName, manifestInfo.packageName)) {
                return null
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: XmlPullParserException) {
            e.printStackTrace()
        }

        return manifestInfo
    }

    /**
     * 获取当前应用 Apk 在 AssetManager 中的 Cookie，如果获取失败，则为 0
     */
    @SuppressLint("PrivateApi")
    fun findApkPathCookie( context: Context,  apkPath: String?): Int {
        val assets = context.assets
        var cookie: Int

        try {
            if (getTargetVersion(context) >= PermissionVersion.ANDROID_9 && getCurrentVersion() >= PermissionVersion.ANDROID_9 && getCurrentVersion() < PermissionVersion.ANDROID_11) {
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
                    "findCookieForPath",
                    arrayOf(String::class.java)
                ) as? Method

                findCookieForPathMethod?.apply {
                    isAccessible = true
                    val cookie = invoke(context.assets, apkPath) as? Int
                    if (cookie != null) {
                        return cookie
                    }
                }
            }

            val addAssetPathMethod = assets.javaClass.getDeclaredMethod("addAssetPath", String::class.java)
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

                when {
                    PermissionUtils.equalsString(TAG_MANIFEST, tagName) -> {
                        manifestInfo.packageName = parsePackageFromXml(parser)
                    }

                    PermissionUtils.equalsString(TAG_USES_SDK, tagName) -> {
                        manifestInfo.usesSdkInfo = parseUsesSdkFromXml(parser)
                    }

                    PermissionUtils.equalsString(TAG_USES_PERMISSION, tagName) ||
                            PermissionUtils.equalsString(TAG_USES_PERMISSION_SDK_23, tagName) ||
                            PermissionUtils.equalsString(TAG_USES_PERMISSION_SDK_M, tagName) -> {
                        manifestInfo.permissionInfoList.add(parsePermissionFromXml(parser))
                    }

                    PermissionUtils.equalsString(TAG_QUERIES, tagName) -> {
                        manifestInfo.queriesPackageList.add(parsePackageFromXml(parser))
                    }

                    PermissionUtils.equalsString(TAG_APPLICATION, tagName) -> {
                        manifestInfo.applicationInfo = parseApplicationFromXml(parser)
                    }

                    PermissionUtils.equalsString(TAG_ACTIVITY, tagName) ||
                            PermissionUtils.equalsString(TAG_ACTIVITY_ALIAS, tagName) -> {
                        manifestInfo.activityInfoList.add(parseActivityFromXml(parser))
                    }

                    PermissionUtils.equalsString(TAG_SERVICE, tagName) -> {
                        manifestInfo.serviceInfoList.add(parseServerFromXml(parser))
                    }

                    PermissionUtils.equalsString(TAG_RECEIVER, tagName) -> {
                        manifestInfo.receiverInfoList.add(parseBroadcastReceiverFromXml(parser))
                    }

                    PermissionUtils.equalsString(TAG_META_DATA, tagName) &&
                            manifestInfo.applicationInfo != null -> {
                        if (manifestInfo.applicationInfo?.metaDataInfoList == null) {
                            manifestInfo.applicationInfo?.metaDataInfoList = mutableListOf()
                        }
                        manifestInfo.applicationInfo?.metaDataInfoList?.add(parseMetaDataFromXml(parser))
                    }
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
        val usesSdkInfo = UsesSdkManifestInfo()
        usesSdkInfo.minSdkVersion = parser.getAttributeIntValue(
            ANDROID_NAMESPACE_URI,
            ATTR_MIN_SDK_VERSION, 0
        )
        return usesSdkInfo
    }

    
    private fun parsePermissionFromXml( parser: XmlResourceParser): PermissionManifestInfo {
        val permissionInfo = PermissionManifestInfo()
        permissionInfo.name = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        permissionInfo.maxSdkVersion = parser.getAttributeIntValue(
            ANDROID_NAMESPACE_URI,
            ATTR_MAX_SDK_VERSION, PermissionManifestInfo.DEFAULT_MAX_SDK_VERSION
        )
        permissionInfo.usesPermissionFlags = parser.getAttributeIntValue(
            ANDROID_NAMESPACE_URI,
            ATTR_USES_PERMISSION_FLAGS, 0
        )
        return permissionInfo
    }

    
    private fun parseApplicationFromXml( parser: XmlResourceParser): ApplicationManifestInfo {
        val applicationInfo = ApplicationManifestInfo()
        val applicationClassName = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        applicationInfo.name = applicationClassName ?: ""
        applicationInfo.requestLegacyExternalStorage = parser.getAttributeBooleanValue(
            ANDROID_NAMESPACE_URI, ATTR_REQUEST_LEGACY_EXTERNAL_STORAGE, false
        )
        return applicationInfo
    }

    
    @Throws(IOException::class, XmlPullParserException::class)
    private fun parseActivityFromXml( parser: XmlResourceParser): ActivityManifestInfo {
        val activityInfo = ActivityManifestInfo()
        val activityClassName = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        activityInfo.name = activityClassName ?: ""
        activityInfo.supportsPictureInPicture = parser.getAttributeBooleanValue(
            ANDROID_NAMESPACE_URI, ATTR_SUPPORTS_PICTURE_IN_PICTURE, false
        )

        while (true) {
            val nextTagType = parser.next()
            val tagName = parser.name
            if (nextTagType == XmlResourceParser.END_TAG &&
                (PermissionUtils.equalsString(TAG_ACTIVITY, tagName) ||
                        PermissionUtils.equalsString(TAG_ACTIVITY_ALIAS, tagName))
            ) {
                break
            }

            if (nextTagType == XmlResourceParser.START_TAG && PermissionUtils.equalsString(TAG_INTENT_FILTER, tagName)) {
                if (activityInfo.intentFilterInfoList == null) {
                    activityInfo.intentFilterInfoList = ArrayList()
                }
                activityInfo.intentFilterInfoList.add(parseIntentFilterFromXml(parser))
            } else if (nextTagType == XmlResourceParser.START_TAG && PermissionUtils.equalsString(TAG_META_DATA, tagName)) {
                if (activityInfo.metaDataInfoList == null) {
                    activityInfo.metaDataInfoList = ArrayList()
                }
                activityInfo.metaDataInfoList.add(parseMetaDataFromXml(parser))
            }
        }

        return activityInfo
    }

    
    @Throws(IOException::class, XmlPullParserException::class)
    private fun parseServerFromXml( parser: XmlResourceParser): ServiceManifestInfo {
        val serviceInfo = ServiceManifestInfo()
        val serviceClassName = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        serviceInfo.name = serviceClassName ?: ""
        serviceInfo.permission = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_PERMISSION)

        while (true) {
            val nextTagType = parser.next()
            val tagName = parser.name
            if (nextTagType == XmlResourceParser.END_TAG && PermissionUtils.equalsString(TAG_SERVICE, tagName)) {
                break
            }

            if (nextTagType == XmlResourceParser.START_TAG && PermissionUtils.equalsString(TAG_INTENT_FILTER, tagName)) {
                if (serviceInfo.intentFilterInfoList == null) {
                    serviceInfo.intentFilterInfoList = ArrayList()
                }
                serviceInfo.intentFilterInfoList.add(parseIntentFilterFromXml(parser))
            } else if (nextTagType == XmlResourceParser.START_TAG && PermissionUtils.equalsString(TAG_META_DATA, tagName)) {
                if (serviceInfo.metaDataInfoList == null) {
                    serviceInfo.metaDataInfoList = ArrayList()
                }
                serviceInfo.metaDataInfoList.add(parseMetaDataFromXml(parser))
            }
        }

        return serviceInfo
    }

    
    @Throws(IOException::class, XmlPullParserException::class)
    private fun parseBroadcastReceiverFromXml( parser: XmlResourceParser): BroadcastReceiverManifestInfo {
        val receiverInfo = BroadcastReceiverManifestInfo()
        val broadcastReceiverClassName = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        receiverInfo.name = broadcastReceiverClassName ?: ""
        receiverInfo.permission = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_PERMISSION)

        while (true) {
            val nextTagType = parser.next()
            val tagName = parser.name
            if (nextTagType == XmlResourceParser.END_TAG && PermissionUtils.equalsString(TAG_RECEIVER, tagName)) {
                break
            }

            if (nextTagType == XmlResourceParser.START_TAG && PermissionUtils.equalsString(TAG_INTENT_FILTER, tagName)) {
                if (receiverInfo.intentFilterInfoList == null) {
                    receiverInfo.intentFilterInfoList = ArrayList()
                }
                receiverInfo.intentFilterInfoList.add(parseIntentFilterFromXml(parser))
            } else if (nextTagType == XmlResourceParser.START_TAG && PermissionUtils.equalsString(TAG_META_DATA, tagName)) {
                if (receiverInfo.metaDataInfoList == null) {
                    receiverInfo.metaDataInfoList = ArrayList()
                }
                receiverInfo.metaDataInfoList.add(parseMetaDataFromXml(parser))
            }
        }

        return receiverInfo
    }

    
    @Throws(IOException::class, XmlPullParserException::class)
    private fun parseIntentFilterFromXml( parser: XmlResourceParser): IntentFilterManifestInfo {
        val intentFilterInfo = IntentFilterManifestInfo()
        while (true) {
            val nextTagType = parser.next()
            val tagName = parser.name
            if (nextTagType == XmlResourceParser.END_TAG && PermissionUtils.equalsString(TAG_INTENT_FILTER, tagName)) {
                break
            }

            if (nextTagType != XmlResourceParser.START_TAG) {
                continue
            }

            if (PermissionUtils.equalsString(TAG_ACTION, tagName)) {
                intentFilterInfo.actionList.add(parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME))
            } else if (PermissionUtils.equalsString(TAG_CATEGORY, tagName)) {
                intentFilterInfo.categoryList.add(parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME))
            }
        }
        return intentFilterInfo
    }

    
    @Throws(IOException::class, XmlPullParserException::class)
    private fun parseMetaDataFromXml( parser: XmlResourceParser): MetaDataManifestInfo {
        val metaDataInfo = MetaDataManifestInfo()
        metaDataInfo.name = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        metaDataInfo.value = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_VALUE)
        metaDataInfo.resource = parser.getAttributeResourceValue(ANDROID_NAMESPACE_URI, ATTR_RESOURCE, 0)
        return metaDataInfo
    }
}