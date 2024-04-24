package com.knight.kotlin.library_base.event

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.io.Serializable

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.library_base.event
 * @ClassName:      MessageEvent
 * @Description:    eventBus事件封装
 * @Author:         knight
 * @CreateDate:     2022/3/24 3:27 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/24 3:27 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

data class MessageEvent(var type: MessageType) {
    private val KEY_INT = "key_int"
    private val KEY_STRING = "key_string"
    private val KEY_BOOL = "key_bool"
    private val KEY_SERIALIZABLE = "key_serializable"
    private val KEY_PARCELABLE = "key_parcelable"
    private val KEY_PARCELABLE_LIST = "key_parcelable_list"
    private val KEY_STRING_LIST = "key_stringList"
    var bundle = Bundle()

    fun put(value: Int): MessageEvent {
        bundle.putInt(KEY_INT, value)
        return this
    }

    fun put(value: String): MessageEvent {
        bundle.putString(KEY_STRING, value)
        return this
    }

    fun put(value: Boolean): MessageEvent {
        bundle.putBoolean(KEY_BOOL, value)
        return this
    }

    fun put(value: Serializable): MessageEvent {
        bundle.putSerializable(KEY_SERIALIZABLE, value)
        return this
    }

    fun put(value: Parcelable): MessageEvent {
        bundle.putParcelable(KEY_PARCELABLE, value)
        return this
    }

    fun put(key: String, value: Int): MessageEvent {
        bundle.putInt(key, value)
        return this
    }

    fun put(key: String, value: String): MessageEvent {
        bundle.putString(key, value)
        return this
    }

    fun put(key: String, value: Boolean): MessageEvent {
        bundle.putBoolean(key, value)
        return this
    }

    fun put(key: String, value: Serializable): MessageEvent {
        bundle.putSerializable(key, value)
        return this
    }

    fun put(key: String, value: Parcelable): MessageEvent {
        bundle.putParcelable(key, value)
        return this
    }


    fun putStringList(value: ArrayList<String>):MessageEvent {
        bundle.putStringArrayList(KEY_STRING_LIST,value)
        return this
    }

    fun put(key: String,value:ArrayList<out Parcelable>) :MessageEvent {
        bundle.putParcelableArrayList(key,value)
        return this
    }

    //===============================================================

    fun getInt(): Int {
        return bundle.getInt(KEY_INT)
    }

    fun getString(): String? {


        return bundle.getString(KEY_STRING)
    }

    fun getBoolean(): Boolean {


        return bundle.getBoolean(KEY_BOOL)
    }

    fun <T : Serializable> getSerializable() : Serializable {
        return bundle.getSerializable(KEY_SERIALIZABLE) as T
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun <T : Serializable> getSerializable(clazz:Class<T>): T? {
        return bundle.getSerializable(KEY_SERIALIZABLE,clazz)
    }


    fun <T : Parcelable> getParcelable(): T? {
        return bundle.getParcelable(KEY_PARCELABLE)
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun <T : Parcelable> getParcelable(clazz:Class<T>): T? {
        return bundle.getParcelable(KEY_PARCELABLE,clazz)
    }

    fun getInt(key: String): Int {
        return bundle.getInt(key)
    }

    fun getString(key: String): String? {
        return bundle.getString(key)
    }

    fun getStringList():ArrayList<String>{
        return bundle.getStringArrayList(KEY_STRING_LIST) ?: ArrayList()
    }

    fun getBoolean(key: String): Boolean {
        return bundle.getBoolean(key)
    }

    fun <T : Serializable> getSerializable(key: String): Serializable{
        return bundle.getSerializable(key) as T
    }

    fun <T : Parcelable> getParcelable(key: String): T? {
        return bundle.getParcelable(key)
    }

    fun <T : Parcelable> getParcelableArrayList(): ArrayList<T>? {
        return bundle.getParcelableArrayList(KEY_PARCELABLE_LIST)
    }

    enum class MessageType {
        //登录成功
        LoginSuccess,
        //退出登录
        LogoutSuccess,
        //分享文章成功
        ShareArticleSuccess,
        //收藏成功
        CollectSuccess,
        //改变标签
        ChangeLabel,
        //状态栏着色
        ChangeStatusTheme,
        //护眼模式
        EyeMode,
        //页面重建
        RecreateMain
    }

}
