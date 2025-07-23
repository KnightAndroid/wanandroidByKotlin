package com.knight.kotlin.module_home.repo

import com.knight.kotlin.library_base.repository.BaseRepository
import com.knight.kotlin.library_network.model.responseCodeExceptionHandler
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_home.api.HomeNewsApiService
import com.knight.kotlin.module_home.entity.ZaoBaoAudioBean
import com.knight.kotlin.module_home.entity.ZaoBaoBean
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
    fun getNews(type:String) = request<ZaoBaoBean>({
        mHomeNewsApiService.getNews(type).run {
            responseCodeExceptionHandler(if ( code == "200") 0 else -1,msg)
            emit(this)
        }
    }) {
        it?.let { it1 -> toast(it1) }
    }



    /**
     *
     * 获取早报音频
     */
    fun getAudio() = request<ZaoBaoAudioBean>({
        mHomeNewsApiService.getAudio().run {
            responseCodeExceptionHandler(code,msg)
            if (data != null) {
                emit(data)
            }
        }
    }) {
        it?.let { it1 -> toast(it1) }
    }

}