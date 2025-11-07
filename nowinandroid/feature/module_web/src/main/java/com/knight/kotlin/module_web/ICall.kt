
package com.knight.kotlin.module_web

import com.knight.kotlin.module_web.cache.WebResource


/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 14:43
 * @descript:
 */
interface ICall {
    fun call(): WebResource?
}