package com.knight.kotlin.module_eye_discover.fragment

import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.fragment.BaseFragment
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_eye_discover.adapter.EyeDIscoverSearchRecommendAdapter
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverRecommendFragmentBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverRecommendSearchHotItemBinding
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverSearchRecommendVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/10 17:20
 * @descript:
 */
@AndroidEntryPoint
@Route(path = RouteFragment.Discover.DiscoverRecommendFragment)
class EyeDiscoverSearchRecommendFragment : BaseFragment<EyeDiscoverRecommendFragmentBinding,EyeDiscoverSearchRecommendVm>() {


    //发现适配器
    private val mDiscoverSearchRecommendAdapter: EyeDIscoverSearchRecommendAdapter by lazy { EyeDIscoverSearchRecommendAdapter() }

    val testList = mutableListOf<String>()


    private lateinit var mHeaderBinding: EyeDiscoverRecommendSearchHotItemBinding






    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
//         mViewModel.getHotQueries().observerKt {
//            // toast(it.result?.item_list.toString())
//             mDiscoverSearchRecommendAdapter.submitList(testList)
//             if (mBinding.rvDiscoverRecommendVideo.headerCount == 0) {
//                 if (!::mHeaderBinding.isInitialized) {
//                      mHeaderBinding = EyeDiscoverRecommendSearchHotItemBinding.inflate(LayoutInflater.from(activity))
//                 }
//                 mBinding.rvDiscoverRecommendVideo.addHeaderView(mHeaderBinding.root)
//                 for (key in it.result?.item_list!!) {
//                    // val chip = Chip(context)
//                     val chip = Chip(ContextThemeWrapper(context, com.google.android.material.R.style.Widget_Material3_Chip_Assist))
//                     val shapeAppearanceModel = ShapeAppearanceModel.Builder()
//                         .setAllCornerSizes(12f) // 设置所有角的圆角半径（单位：像素）
//                         .build()
//                     chip.shapeAppearanceModel = shapeAppearanceModel
//                     // 设置边线颜色和宽度
//                     chip.setChipStrokeColor(ColorStateList.valueOf(Color.BLACK)) // 边线颜色
//                     chip.setChipStrokeWidth(2f) // 边线宽度（单位：像素）
//                     chip.setText(key)
//                     mHeaderBinding.chipGroup.chipSpacingVertical = 0
//                     mHeaderBinding.chipGroup.setPadding(16.dp2px(), 0, 16.dp2px(), 8.dp2px())
//                     mHeaderBinding.chipGroup.addView(chip)
//                 }
//             }
//
//         }

        mViewModel.getRecommendCardList().observerKt {
            toast(it?.list?.get(0)?.card_data?.body?.metro_list?.get(0)?.type ?: "qqwq")
           // LogUtils.d(it.list?.size.toString()+"fv")
        }

    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverRecommendFragmentBinding.initView() {
        testList.add("1111111")
        testList.add("2222222")
        rvDiscoverRecommendVideo.init(
            LinearLayoutManager(activity),
            mDiscoverSearchRecommendAdapter,
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