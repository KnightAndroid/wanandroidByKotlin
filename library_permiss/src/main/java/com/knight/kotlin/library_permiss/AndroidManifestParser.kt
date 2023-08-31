package com.knight.kotlin.library_permiss

import android.content.Context
import android.content.res.XmlResourceParser
import android.text.TextUtils
import com.knight.kotlin.library_permiss.AndroidManifestInfo.UsesSdkInfo
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException


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

    private val TAG_APPLICATION = "application"
    private val TAG_ACTIVITY = "activity"
    private val TAG_ACTIVITY_ALIAS = "activity-alias"
    private val TAG_SERVICE = "service"

    private val ATTR_PACKAGE = "package"
    private val ATTR_NAME = "name"
    private val ATTR_MAX_SDK_VERSION = "maxSdkVersion"
    private val ATTR_MIN_SDK_VERSION = "minSdkVersion"
    private val ATTR_USES_PERMISSION_FLAGS = "usesPermissionFlags"
    private val ATTR_REQUEST_LEGACY_EXTERNAL_STORAGE = "requestLegacyExternalStorage"
    private val ATTR_SUPPORTS_PICTURE_IN_PICTURE = "supportsPictureInPicture"
    private val ATTR_PERMISSION = "permission"

    /**
     * 解析 apk 包中的清单文件
     *
     * @param context          上下文
     * @param apkCookie        要解析 apk 的 cookie
     */

    @Throws(IOException::class, XmlPullParserException::class)
    fun parseAndroidManifest(context: Context, apkCookie: Int): AndroidManifestInfo? {
        val manifestInfo = AndroidManifestInfo()
        val parser = context.assets.openXmlResourceParser(apkCookie, ANDROID_MANIFEST_FILE_NAME)
        do {
            // 当前节点必须为标签头部
            if (parser.eventType != XmlResourceParser.START_TAG) {
                continue
            }
            val tagName = parser.name
            if (TextUtils.equals(TAG_MANIFEST, tagName)) {
                manifestInfo.packageName = parser.getAttributeValue(null, ATTR_PACKAGE)
            }
            if (TextUtils.equals(TAG_USES_SDK, tagName)) {
                manifestInfo.usesSdkInfo = parseUsesSdkFromXml(parser)
            }
            if (TextUtils.equals(TAG_USES_PERMISSION, tagName) ||
                TextUtils.equals(TAG_USES_PERMISSION_SDK_23, tagName) ||
                TextUtils.equals(TAG_USES_PERMISSION_SDK_M, tagName)
            ) {
                val permissionInfo = parsePermissionFromXml(parser)
                manifestInfo.permissionInfoList.add(permissionInfo)
            }
            if (TextUtils.equals(TAG_APPLICATION, tagName)) {
                manifestInfo.applicationInfo = parseApplicationFromXml(parser)
            }
            if (TextUtils.equals(TAG_ACTIVITY, tagName) ||
                TextUtils.equals(TAG_ACTIVITY_ALIAS, tagName)
            ) {
                val activityInfo = parseActivityFromXml(parser)
                manifestInfo.activityInfoList.add(activityInfo)
            }
            if (TextUtils.equals(TAG_SERVICE, tagName)) {
                val serviceInfo = parseServerFromXml(parser)
                manifestInfo.serviceInfoList.add(serviceInfo)
            }
        } while (parser.next() != XmlResourceParser.END_DOCUMENT)
        parser.close()
        return manifestInfo
    }

    private fun parseUsesSdkFromXml(parser: XmlResourceParser): UsesSdkInfo? {
        val usesSdkInfo = UsesSdkInfo()
        usesSdkInfo.minSdkVersion = parser.getAttributeIntValue(
            ANDROID_NAMESPACE_URI,
            ATTR_MIN_SDK_VERSION, 0
        )
        return usesSdkInfo
    }

    private fun parsePermissionFromXml(parser: XmlResourceParser): AndroidManifestInfo.PermissionInfo {
        val permissionInfo = AndroidManifestInfo.PermissionInfo()
        permissionInfo.name = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        permissionInfo.maxSdkVersion = parser.getAttributeIntValue(
            ANDROID_NAMESPACE_URI,
            ATTR_MAX_SDK_VERSION, Int.MAX_VALUE
        )
        permissionInfo.usesPermissionFlags = parser.getAttributeIntValue(
            ANDROID_NAMESPACE_URI,
            ATTR_USES_PERMISSION_FLAGS, 0
        )
        return permissionInfo
    }

    private fun parseApplicationFromXml(parser: XmlResourceParser): AndroidManifestInfo.ApplicationInfo? {
        val applicationInfo = AndroidManifestInfo.ApplicationInfo()
        applicationInfo.name = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        applicationInfo.requestLegacyExternalStorage = parser.getAttributeBooleanValue(
            ANDROID_NAMESPACE_URI, ATTR_REQUEST_LEGACY_EXTERNAL_STORAGE, false
        )
        return applicationInfo
    }

    private fun parseActivityFromXml(parser: XmlResourceParser): AndroidManifestInfo.ActivityInfo {
        val activityInfo = AndroidManifestInfo.ActivityInfo()
        activityInfo.name = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        activityInfo.supportsPictureInPicture = parser.getAttributeBooleanValue(
            ANDROID_NAMESPACE_URI, ATTR_SUPPORTS_PICTURE_IN_PICTURE, false
        )
        return activityInfo
    }

    private fun parseServerFromXml(parser: XmlResourceParser): AndroidManifestInfo.ServiceInfo {
        val serviceInfo = AndroidManifestInfo.ServiceInfo()
        serviceInfo.name = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_NAME)
        serviceInfo.permission = parser.getAttributeValue(ANDROID_NAMESPACE_URI, ATTR_PERMISSION)
        return serviceInfo
    }
}