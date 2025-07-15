package com.knight.kotlin.module_mine.repo

import com.core.library_base.ktx.dimissLoadingDialog
import com.knight.kotlin.library_common.entity.UserInfoEntity
import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_mine.api.QuickLoginApiService
import javax.inject.Inject

/**
 *
 * @ProjectName:    wanandroid
 * @Package:        com.knight.kotlin.module_mine.repo
 * @ClassName:      QuickLoginRepo
 * @Description:    java类作用描述
 * @Author:         knight
 * @CreateDate:     2022/3/24 11:38 上午
 * @UpdateUser:     更新者
 * @UpdateDate:     2022/3/24 11:38 上午
 * @UpdateRemark:   更新说明
 * @Version:        1.0
 */

class QuickLoginRepo @Inject constructor() : BaseRepository() {
    @Inject
    lateinit var mQuickLoginApiService: QuickLoginApiService

    /**
     *
     * 登录
     */
    fun login(userName:String,passWord:String) = request<UserInfoEntity>({
        mQuickLoginApiService.login(userName,passWord).run {
            responseCodeExceptionHandler(code, msg)
            emit(data)
        }
    }){
        dimissLoadingDialog()
        it?.run {
            toast(it)
        }

    }
}