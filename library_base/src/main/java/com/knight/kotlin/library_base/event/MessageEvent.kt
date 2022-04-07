package com.knight.kotlin.library_base.event

import android.os.Bundle
import android.os.Parcelable

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

    fun put(value: java.io.Serializable): MessageEvent {


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

    fun put(key: String, value: java.io.Serializable): MessageEvent {


        bundle.putSerializable(key, value)
        return this
    }

    fun put(key: String, value: Parcelable): MessageEvent {


        bundle.putParcelable(key, value)
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

    fun <T : java.io.Serializable> getSerializable(): java.io.Serializable {

        return bundle.getSerializable(KEY_SERIALIZABLE) as T
    }

    fun <T : Parcelable> getParcelable(): T? {


        return bundle.getParcelable<T>(KEY_PARCELABLE)
    }



    fun getInt(key: String): Int {


        return bundle.getInt(key)
    }

    fun getString(key: String): String? {


        return bundle.getString(key)
    }

    fun getBoolean(key: String): Boolean {


        return bundle.getBoolean(key)
    }

    fun <T : java.io.Serializable> getSerializable(key: String): java.io.Serializable{


        return bundle.getSerializable(key) as T
    }

    fun <T : Parcelable> getParcelable(key: String): T? {


        return bundle.getParcelable<T>(key)
    }

    fun <T : Parcelable> getParcelableArrayList(): ArrayList<T>? {
        return bundle.getParcelableArrayList(KEY_PARCELABLE_LIST)
    }

    enum class MessageType {
        //登录成功
        LoginSuccess,
        //退出登录
        LogoutSuccess
    }

}
