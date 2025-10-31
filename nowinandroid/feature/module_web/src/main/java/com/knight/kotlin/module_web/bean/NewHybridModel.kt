package com.knight.kotlin.module_web.bean

/**
 * author ：knight
 * createTime：2025/10/30
 * mail:15015706912@163.com
 * describe：
 */
data class NewHybridModel(val callId:String,val data:DataModel)
data class DataModel(val page:String,val data:DataSubModel)
data class DataSubModel(val url:String)