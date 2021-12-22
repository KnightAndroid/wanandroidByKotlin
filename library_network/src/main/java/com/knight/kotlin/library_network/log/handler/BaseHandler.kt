package com.knight.kotlin.library_network.log.handler

import com.knight.kotlin.library_network.log.bean.JSONConfig

/**
 * Author:Knight
 * Time:2021/12/22 10:39
 * Description:BaseHandler
 */
abstract class BaseHandler {

    // 责任链的下一个节点，即处理者
    private var nextHandler: BaseHandler? = null

    // 捕获具体请求并进行处理，或是将请求传递到责任链的下一级别
    fun handleObject(obj: Any, jsonConfig: JSONConfig) {

        if (obj == null) {
            return
        }

        if (!handle(obj,jsonConfig)) {
            // 当前处理者不能胜任，则传递至责任链的下一节点
            this.nextHandler?.handleObject(obj,jsonConfig)
        }
    }

    // 设置责任链中的下一个处理者
    fun setNextHandler(nextHandler: BaseHandler) {
        this.nextHandler = nextHandler
    }

    // 定义链中每个处理者具体的处理方式
    protected abstract fun handle(obj: Any, jsonConfig: JSONConfig): Boolean
}