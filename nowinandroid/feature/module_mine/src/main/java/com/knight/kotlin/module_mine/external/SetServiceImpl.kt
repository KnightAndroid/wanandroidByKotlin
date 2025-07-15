package com.knight.kotlin.module_mine.external

import com.knight.kotlin.library_common.util.CacheUtils
import com.google.auto.service.AutoService
import com.knight.kotlin.module_set.external.MineExternalContact

/**
 * Author:Knight
 * Time:2023/5/6 11:30
 * Description:SetServiceImpl
 */
@AutoService(MineExternalContact::class)
class SetServiceImpl : MineExternalContact {
    override fun getUserRank(): String {
        return CacheUtils.getUserRank()
    }
}