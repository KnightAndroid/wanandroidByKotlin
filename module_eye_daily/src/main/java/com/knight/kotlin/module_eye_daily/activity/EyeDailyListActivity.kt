package com.knight.kotlin.module_eye_daily.activity

import androidx.core.content.ContextCompat
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_eye_daily.R
import com.knight.kotlin.module_eye_daily.databinding.EyeDailyListActivityBinding
import com.knight.kotlin.module_eye_daily.entity.EyeDailyItemEntity
import com.knight.kotlin.module_eye_daily.vm.EyeDailyListVm
import com.wyjson.router.annotation.Route
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2024/4/28 15:54
 * Description:EyeDailyListActivity
 */


@AndroidEntryPoint
@Route(path = RouteActivity.EyeDaily.DailyListActivity)
class EyeDailyListActivity:BaseActivity<EyeDailyListActivityBinding,EyeDailyListVm>() {

    private val TEXT_HEADER_TYPE = "textCard"
    private var mNextPageUrl: String? = null
//    //头部广告View
//    private val bannerHeadView: View by lazy {
//        LayoutInflater.from(this).inflate(R.layout.eye_daily_list_head, null)
//    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
          mViewModel.getDailyBanner().observerKt{
              //去除标识为文本卡片的
              it.itemList.removeAll{
                  it.type == TEXT_HEADER_TYPE
              }
              setDailyBanner(it.itemList)
          }
    }

    override fun reLoadData() {

    }

    override fun EyeDailyListActivityBinding.initView() {

    }


    /**
     * 设置广告数据
     * @param data
     */
    private fun setDailyBanner(data:MutableList<EyeDailyItemEntity>) {
         mBinding.eyeDailyBanner.apply {
             setAdapter(object: BannerImageAdapter<EyeDailyItemEntity>(data) {
                 override fun onBindView(
                     holder: BannerImageHolder,
                     data: EyeDailyItemEntity,
                     position: Int,
                     size: Int
                 ) {
                     data.data.content.data.cover?.let {
                         ImageLoader.loadStringPhoto(
                             this@EyeDailyListActivity,
                             it.feed,
                             holder.imageView
                         )
                     }
                 }

             })
             setIndicator(mBinding.indicator, false)
             indicator.indicatorView.setBackgroundColor(ContextCompat.getColor(this@EyeDailyListActivity,R.color.eye_daily_banner_indicator_bg))

         }

    }
}