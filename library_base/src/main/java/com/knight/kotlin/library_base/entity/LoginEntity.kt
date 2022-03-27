package com.knight.kotlin.library_base.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.library_base.entity
 * @ClassName:      LoginEntity
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/24 3:03 下午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/24 3:03 下午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

@Parcelize
data class LoginEntity(
    val loginName:String,
    val loginPassword:String
): Parcelable