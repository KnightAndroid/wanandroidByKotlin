package com.knight.kotlin.module_home.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.api.HomeNewsApiService
import com.knight.kotlin.module_home.entity.ZaoBaoData
import javax.inject.Inject


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/11 15:37
 * @descript:
 */
class HomeNewsRepo @Inject constructor(): BaseRepository() {

    @Inject
    lateinit var mHomeNewsApiService: HomeNewsApiService







    /**
     *
     * 获取早报新闻
     */
    fun getNews() = request<ZaoBaoData>({
        mHomeNewsApiService.getNews().run {
            responseCodeExceptionHandler( code,msg)
            emit(this.data)
        }
    }) {
        it?.let { it1 -> toast(it1) }
    }





}