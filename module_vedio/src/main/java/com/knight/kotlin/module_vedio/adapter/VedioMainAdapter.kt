package com.knight.kotlin.module_vedio.adapter

import android.util.Log
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_vedio.entity.VedioListEntity
import com.knight.library.cryption.AesUtils

/**
 * Author:Knight
 * Time:2024/2/26 14:43
 * Description:VedioMainAdapter
 */
class VedioMainAdapter(data:MutableList<VedioListEntity>) : BaseQuickAdapter<VedioListEntity,BaseViewHolder>(
    com.knight.kotlin.module_vedio.R.layout.vedio_main_item,data) {
    override fun convert(holder: BaseViewHolder, item: VedioListEntity) {
        item.run {
            ImageLoader.loadVedioFirstFrame(context,removePrefixToDecry(joke.videoUrl),holder.getView(
                com.knight.kotlin.module_vedio.R.id.iv_vedio),10)

        }

    }

    /**
     * 移除前缀并且解密
     * @param cipherUrl
     * @return
     */
    fun removePrefixToDecry(cipherUrl:String) :String {
        val  prefixToRemove = "ftp://" // 要移除的前缀
        if (cipherUrl.startsWith(prefixToRemove)) {
            val startIndex = prefixToRemove.length
            val result = cipherUrl.substring (startIndex)
            Log.d("sdsd",result)
            return AesUtils.decrypt(result) ?: ""
        } else {
            return cipherUrl
        }

    }
}