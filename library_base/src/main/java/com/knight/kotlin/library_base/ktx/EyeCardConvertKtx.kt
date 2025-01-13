package com.knight.kotlin.library_base.ktx

import com.knight.kotlin.library_base.config.EyeScroll
import com.knight.kotlin.library_base.entity.EyeCardEntity
import com.knight.kotlin.library_base.entity.EyeHeaderItemCard
import com.knight.kotlin.library_base.entity.EyeItemCard
import com.knight.kotlin.library_base.entity.EyeMetroItemCard
import com.knight.kotlin.library_base.entity.EyeSlideItemCard

/**
 * @Description
 * @Author knight
 * @Time 2025/1/13 22:44
 *
 */

fun List<EyeCardEntity>.toItemCard(): List<EyeItemCard> {
    return filter { it.card_data?.body?.api_request == null }.flatMap {
        val list = mutableListOf<EyeItemCard>()
        if (!it.card_data?.header?.left.isNullOrEmpty()) {
            list.add(EyeHeaderItemCard(left = it.card_data?.header!!))
        }
        if (it.interaction?.scroll == EyeScroll.HORIZONTAL) {
            list.add(
                EyeSlideItemCard(
                    it.card_data?.body?.metro_list?.get(0)?.style?.tpl_label ?: "",
                    it.card_data?.body?.metro_list ?: listOf()
                )
            )
        } else {
            list.addAll(it.card_data?.body?.metro_list?.mapIndexed { index, metroCard ->
                EyeMetroItemCard(
                    metroCard.style?.tpl_label ?: "", metroCard, index
                )
            } ?: listOf())
        }
        list.addAll(it.card_data?.footer?.left?.mapIndexed { index, metroCard ->
            EyeMetroItemCard(
                metroCard.style?.tpl_label ?: "", metroCard, index
            )
        } ?: listOf())
        list
    }
}