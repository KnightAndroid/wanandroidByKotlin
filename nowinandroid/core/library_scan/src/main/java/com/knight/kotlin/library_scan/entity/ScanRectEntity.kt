package com.knight.kotlin.library_scan.entity

import java.io.Serializable

/**
 * Author:Knight
 * Time:2022/2/14 11:26
 * Description:ScanRectEntity
 */
class ScanRectEntity(left: Int, top: Int, right: Int, bottom: Int) :
    Serializable {
    private var left: Int
    private var top: Int
    private var right: Int
    private var bottom: Int
    fun getLeft(): Int {
        return left
    }

    fun setLeft(left: Int) {
        this.left = left
    }

    fun getTop(): Int {
        return top
    }

    fun setTop(top: Int) {
        this.top = top
    }

    fun getRight(): Int {
        return right
    }

    fun setRight(right: Int) {
        this.right = right
    }

    fun getBottom(): Int {
        return bottom
    }

    fun setBottom(bottom: Int) {
        this.bottom = bottom
    }

    companion object {
        private const val serialVersionUID = 1L
    }

    init {
        this.left = left
        this.top = top
        this.right = right
        this.bottom = bottom
    }
}
