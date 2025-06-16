package com.knight.kotlin.library_util

/**
 * Author:Knight
 * Time:2021/12/29 14:38
 * Description:ObjectUtils
 */
object ObjectUtils {
    /**
     * compare two object
     *
     * @param actual
     * @param expected
     * @return
     *  * if both are null, return true
     *  * return actual.[Object.equals]
     *
     */
    fun isEquals(actual: Any?, expected: Any?): Boolean {
        return actual === expected || if (actual == null) expected == null else actual == expected
    }

    /**
     * convert long array to Long array
     *
     * @param source
     * @return
     */
    fun transformLongArray(source: LongArray): Array<Long?>? {
        val destin = arrayOfNulls<Long>(source.size)
        for (i in source.indices) {
            destin[i] = source[i]
        }
        return destin
    }

    /**
     * convert Long array to long array
     *
     * @param source
     * @return
     */
    fun transformLongArray(source: Array<Long>): LongArray? {
        val destin = LongArray(source.size)
        for (i in source.indices) {
            destin[i] = source[i]
        }
        return destin
    }

    /**
     * convert int array to Integer array
     *
     * @param source
     * @return
     */
    fun transformIntArray(source: IntArray): Array<Int?>? {
        val destin = arrayOfNulls<Int>(source.size)
        for (i in source.indices) {
            destin[i] = source[i]
        }
        return destin
    }

    /**
     * convert Integer array to int array
     *
     * @param source
     * @return
     */
    fun transformIntArray(source: Array<Int>): IntArray? {
        val destin = IntArray(source.size)
        for (i in source.indices) {
            destin[i] = source[i]
        }
        return destin
    }


}