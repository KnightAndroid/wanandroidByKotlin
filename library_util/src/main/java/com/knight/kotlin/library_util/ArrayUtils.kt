package com.knight.kotlin.library_util

import com.knight.kotlin.library_util.ObjectUtils.isEquals
import com.knight.kotlin.library_util.ObjectUtils.transformIntArray
import com.knight.kotlin.library_util.ObjectUtils.transformLongArray

/**
 * Author:Knight
 * Time:2021/12/29 15:02
 * Description:ArrayUtils
 */
object ArrayUtils {
    /**
     * is null or its length is 0
     *
     * @param <V>
     * @param sourceArray
     * @return
    </V> */
    fun <V> isEmpty(sourceArray: Array<V>?): Boolean {
        return sourceArray == null || sourceArray.size == 0
    }

    /**
     * get last element of the target element, before the first one that match the target element front to back
     *
     *  * if array is empty, return defaultValue
     *  * if target element is not exist in array, return defaultValue
     *  * if target element exist in array and its index is not 0, return the last element
     *  * if target element exist in array and its index is 0, return the last one in array if isCircle is true, else
     * return defaultValue
     *
     *
     * @param <V>
     * @param sourceArray
     * @param value value of target element
     * @param defaultValue default return value
     * @param isCircle whether is circle
     * @return
    </V> */
    fun <V> getLast(sourceArray: Array<V>?, value: V, defaultValue: V?, isCircle: Boolean): V? {
        if (isEmpty(sourceArray)) {
            return defaultValue
        }
        var currentPosition = -1
        for (i in sourceArray!!.indices) {
            if (isEquals(value, sourceArray[i])) {
                currentPosition = i
                break
            }
        }
        if (currentPosition == -1) {
            return defaultValue
        }
        return if (currentPosition == 0) {
            if (isCircle) sourceArray[sourceArray.size - 1] else defaultValue
        } else sourceArray[currentPosition - 1]
    }

    /**
     * get next element of the target element, after the first one that match the target element front to back
     *
     *  * if array is empty, return defaultValue
     *  * if target element is not exist in array, return defaultValue
     *  * if target element exist in array and not the last one in array, return the next element
     *  * if target element exist in array and the last one in array, return the first one in array if isCircle is
     * true, else return defaultValue
     *
     *
     * @param <V>
     * @param sourceArray
     * @param value value of target element
     * @param defaultValue default return value
     * @param isCircle whether is circle
     * @return
    </V> */
    fun <V> getNext(sourceArray: Array<V>?, value: V, defaultValue: V?, isCircle: Boolean): V? {
        if (isEmpty(sourceArray)) {
            return defaultValue
        }
        var currentPosition = -1
        for (i in sourceArray!!.indices) {
            if (isEquals(value, sourceArray[i])) {
                currentPosition = i
                break
            }
        }
        if (currentPosition == -1) {
            return defaultValue
        }
        return if (currentPosition == sourceArray.size - 1) {
            if (isCircle) sourceArray[0] else defaultValue
        } else sourceArray[currentPosition + 1]
    }

    /**
     */
    fun <V> getLast(sourceArray: Array<V>?, value: V, isCircle: Boolean): V? {
        return getLast(sourceArray, value, null, isCircle)
    }

    /**
     */
    fun <V> getNext(sourceArray: Array<V>?, value: V, isCircle: Boolean): V? {
        return getNext(sourceArray, value, null, isCircle)
    }

    /**
     */
    fun getLast(sourceArray: LongArray, value: Long, defaultValue: Long, isCircle: Boolean): Long? {
        require(sourceArray.size != 0) { "The length of source array must be greater than 0." }
        val array = transformLongArray(sourceArray)
        return getLast(array, value, defaultValue, isCircle)
    }

    /**
     */
    fun getNext(sourceArray: LongArray, value: Long, defaultValue: Long, isCircle: Boolean): Long? {
        require(sourceArray.size != 0) { "The length of source array must be greater than 0." }
        val array = transformLongArray(sourceArray)
        return getNext(array, value, defaultValue, isCircle)
    }

    /**
     */
    fun getLast(sourceArray: IntArray, value: Int, defaultValue: Int, isCircle: Boolean): Int? {
        require(sourceArray.size != 0) { "The length of source array must be greater than 0." }
        val array = transformIntArray(sourceArray)
        return getLast(array, value, defaultValue, isCircle)
    }

    /**
     */
    fun getNext(sourceArray: IntArray, value: Int, defaultValue: Int, isCircle: Boolean): Int? {
        require(sourceArray.size != 0) { "The length of source array must be greater than 0." }
        val array = transformIntArray(sourceArray)
        return getNext(array, value, defaultValue, isCircle)
    }

}