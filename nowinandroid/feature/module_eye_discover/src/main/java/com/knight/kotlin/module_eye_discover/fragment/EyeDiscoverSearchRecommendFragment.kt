package com.knight.kotlin.module_eye_discover.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import com.core.library_base.fragment.BaseFragment
import com.core.library_base.ktx.init
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteFragment
import com.core.library_base.util.dp2px
import com.google.android.material.chip.Chip
import com.google.android.material.shape.ShapeAppearanceModel

import com.knight.kotlin.module_eye_discover.activity.EyeDiscoverActivity
import com.knight.kotlin.module_eye_discover.adapter.EyeDiscoverSearchRecommendVideoAdapter
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverRecommendFragmentBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverRecommendSearchHotItemBinding
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverSearchRecommendVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/10 17:20
 * @descript:搜索推荐页面
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Discover.DiscoverRecommendFragment)
class EyeDiscoverSearchRecommendFragment : BaseFragment<EyeDiscoverRecommendFragmentBinding, EyeDiscoverSearchRecommendVm>() {


    //发现适配器
    private val mDiscoverSearchRecommendVideoAdapter: EyeDiscoverSearchRecommendVideoAdapter by lazy { EyeDiscoverSearchRecommendVideoAdapter() }




    private lateinit var mHeaderBinding: EyeDiscoverRecommendSearchHotItemBinding






    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
         mViewModel.getHotQueries().observerKt {
             if (mBinding.rvDiscoverRecommendVideo.headerCount == 0) {
                 if (!::mHeaderBinding.isInitialized) {
                      mHeaderBinding = EyeDiscoverRecommendSearchHotItemBinding.inflate(LayoutInflater.from(activity))
                 }
                 mBinding.rvDiscoverRecommendVideo.addHeaderView(mHeaderBinding.root)
                 for (key in it.result?.item_list!!) {
                    // val chip = Chip(context)

                     val materialContext = ContextThemeWrapper(context, com.google.android.material.R.style.Theme_MaterialComponents_DayNight)

                     val chip = Chip(materialContext)
                     val shapeAppearanceModel = ShapeAppearanceModel.Builder()
                         .setAllCornerSizes(12f) // 设置所有角的圆角半径（单位：像素）
                         .build()
                     chip.shapeAppearanceModel = shapeAppearanceModel
                     // 设置边线颜色和宽度
                     chip.setChipStrokeColor(ColorStateList.valueOf(Color.BLACK)) // 边线颜色
                     chip.setChipStrokeWidth(2f) // 边线宽度（单位：像素）
                     chip.setText(key)
                     chip.setOnClick {
                         (activity as EyeDiscoverActivity)?.let{
                             it.showResultFragment(chip.text.toString())
                         }

                     }
                     mHeaderBinding.chipGroup.chipSpacingVertical = 0
                     mHeaderBinding.chipGroup.setPadding(16.dp2px(), 0, 16.dp2px(), 8.dp2px())
                     mHeaderBinding.chipGroup.addView(chip)
                 }
             }

         }

        mViewModel.getRecommendCardList().observerKt {
          //  toast(it?.list?.get(0)?.card_data?.body?.metro_list?.get(0)?.type ?: "qqwq")
           // LogUtils.d(it.list?.size.toString()+"fv")


            mDiscoverSearchRecommendVideoAdapter.submitList(it?.list?.get(0)?.card_data?.body?.metro_list)
        }

    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverRecommendFragmentBinding.initView() {

        rvDiscoverRecommendVideo.init(
            LinearLayoutManager(activity),
            mDiscoverSearchRecommendVideoAdapter,
            true
        )

    }



    /**
     * 头部View
     */
    private fun getHeadView(): View {
        return EyeDiscoverRecommendSearchHotItemBinding.inflate(LayoutInflater.from(activity)).root

    }
}