package com.core.library_base.util

import android.util.ArrayMap
import java.util.ServiceLoader

/**
 * Author:Knight
 * Time:2023/5/6 14:44
 * Description:ServiceApiFactory
 */
class ServiceApiFactory {

    companion object {
        val INSTANCE = ServiceApiFactory()
        fun getInstance(): ServiceApiFactory {
            return INSTANCE
        }

    }

    private val loaderMap = ArrayMap<Class<*>, ServiceLoader<*>>()
    private val serviceMap = ArrayMap<Class<*>, Any>()


    @Suppress("UNCHECKED_CAST")
    fun <T> getService(clazz: Class<T>): T? {
        val o = serviceMap[clazz]
        if (o != null && isInterface(o.javaClass, clazz.name)) {
            return o as T
        }
        var serviceLoader = loaderMap[clazz]
        if (serviceLoader == null) {
            serviceLoader = ServiceLoader.load(clazz)
            loaderMap[clazz] = serviceLoader
        }
        if (serviceLoader != null && serviceLoader.iterator().hasNext()) {
            val next = serviceLoader.iterator().next() as T
            serviceMap[clazz] = next
            return next
        }
        return null
    }

    private fun isInterface(c: Class<*>, szInterface: String): Boolean {
        val face = c.interfaces
        for (aClass in face) {
            if (aClass.name == szInterface) {
                return true
            } else {
                val face1 = aClass.interfaces
                for (value in face1) {
                    if (value.name == szInterface) {
                        return true
                    } else if (isInterface(value, szInterface)) {
                        return true
                    }
                }
            }
        }
        return if (null != c.superclass) {
            isInterface(c.superclass, szInterface)
        } else false
    }

}