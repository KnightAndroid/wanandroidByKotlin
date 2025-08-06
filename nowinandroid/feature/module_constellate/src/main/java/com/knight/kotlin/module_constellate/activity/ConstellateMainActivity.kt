package com.knight.kotlin.module_constellate.activity

import android.content.res.TypedArray
import android.graphics.Color
import androidx.recyclerview.widget.GridLayoutManager
import com.core.library_base.ktx.setOnClick
import com.core.library_base.route.RouteActivity
import com.core.library_base.util.GsonUtils
import com.core.library_base.vm.EmptyViewModel
import com.core.library_common.util.dp2px
import com.google.gson.reflect.TypeToken
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.getScreenHeight
import com.knight.kotlin.library_base.ktx.getScreenWidth
import com.knight.kotlin.library_util.JsonUtils
import com.knight.kotlin.library_widget.CustomGridItemDecoration
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.library_widget.ktx.setSafeOnItemClickListener
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.adapter.ConstellateTypeAdapter
import com.knight.kotlin.module_constellate.databinding.ConstellateMainActivityBinding
import com.knight.kotlin.module_constellate.entity.ConstellateTypeEntity
import com.wyjson.router.GoRouter
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/6/18 14:24
 * @descript:星座主页面
 */

@AndroidEntryPoint
@Route(path = RouteActivity.Constellate.ConstellateMainActivity)
class ConstellateMainActivity : BaseActivity<ConstellateMainActivityBinding, EmptyViewModel>() {


    private val mConstellateTypeAdapter: ConstellateTypeAdapter by lazy { ConstellateTypeAdapter() }

    private lateinit var typeArrayIcons: TypedArray
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun ConstellateMainActivityBinding.initView() {
        mBinding.title = getString(R.string.constellate_fate_toolbar_title)
        constellateMainToolbar.baseIvBack.setBackgroundResource(com.core.library_base.R.drawable.base_right_whitearrow)
        constellateMainToolbar.baseTvTitle.setTextColor(Color.WHITE)
        constellateMainToolbar.baseIvBack.setOnClick {
            finish()
        }
        mBinding.rvConstellateType.init(GridLayoutManager(this@ConstellateMainActivity, 3), mConstellateTypeAdapter, true)
        constellateMainToolbar.root.post {
           setupConstellateGrid()
        }
        initAdapterListener()

    }


    /**
     *
     * 初始化适配器监听
     */
    private fun initAdapterListener() {
        mConstellateTypeAdapter.run {
            //Item点击事件
            setSafeOnItemClickListener {adapter,view,position ->

                GoRouter.getInstance().build(RouteActivity.Constellate.ConstellateFortuneActivity)
                    .withParcelable("constellate",adapter.getItem(position)).go()
            }
        }
    }

    /**
     *
     * 设置适配器宽高和间隔
     */
    private fun setupConstellateGrid() {
        val itemSize = (getScreenWidth()) / 3
        val verticalSpacing = (getScreenHeight() - mBinding.constellateMainToolbar.root.height  -  itemSize * 4) / 5  // 4 行，3 个间隔
        mBinding.rvConstellateType.addItemDecoration(
            CustomGridItemDecoration(
                spanCount = 3,
                horizontalSpacing = 0.dp2px(),
                verticalSpacing = verticalSpacing,
                totalRows = 4
            )
        )


        val constellateType = object : TypeToken<List<ConstellateTypeEntity>>() {}.type
        val jsonData: String = JsonUtils.getJson(this@ConstellateMainActivity, "constellatetype.json")
        val mDataList: MutableList<ConstellateTypeEntity> = GsonUtils.getList(jsonData, constellateType)
        typeArrayIcons = resources.obtainTypedArray(R.array.constellate_type)
        mConstellateTypeAdapter.setTypedArray(typeArrayIcons)
        mConstellateTypeAdapter.setItemSize(itemSize)
        mConstellateTypeAdapter.submitList(mDataList)
    }

    override fun onDestroy() {
        super.onDestroy()
        typeArrayIcons.recycle()
    }

}