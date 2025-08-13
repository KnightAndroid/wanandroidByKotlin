package com.knight.kotlin.module_constellate.dialog

import android.content.res.TypedArray
import android.os.Bundle
import android.view.Gravity
import androidx.recyclerview.widget.GridLayoutManager
import com.core.library_base.util.GsonUtils
import com.core.library_base.vm.EmptyViewModel
import com.core.library_common.util.dp2px
import com.google.gson.reflect.TypeToken
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.ktx.getScreenHeight
import com.knight.kotlin.library_base.ktx.getScreenWidth
import com.knight.kotlin.library_database.entity.EveryDayPushEntity
import com.knight.kotlin.library_util.JsonUtils
import com.knight.kotlin.library_widget.CustomGridItemDecoration
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_constellate.R
import com.knight.kotlin.module_constellate.adapter.ConstellateBottomTypeAdapter
import com.knight.kotlin.module_constellate.adapter.ConstellateTypeAdapter
import com.knight.kotlin.module_constellate.databinding.ConstellateSelectDialogBinding
import com.knight.kotlin.module_constellate.entity.ConstellateTypeEntity
import dagger.hilt.android.AndroidEntryPoint

/**
 * @Description
 * @Author knight
 * @Time 2025/8/6 21:48
 *
 */
@AndroidEntryPoint
class ConstellateSelectDialog :BaseDialogFragment<ConstellateSelectDialogBinding,EmptyViewModel>(){

    private val mConstellateBottomTypeAdapter: ConstellateBottomTypeAdapter by lazy { ConstellateBottomTypeAdapter() }

    private lateinit var typeArrayIcons: TypedArray

    private lateinit var selectConstellateName:String

    companion object {
        fun newInstance(selectConstellateName:String) : ConstellateSelectDialog {
            val mConstellateSelectDialog = ConstellateSelectDialog()
            val args = Bundle()
            args.putString("selectConstellateName",selectConstellateName)
            mConstellateSelectDialog.arguments = args
            return mConstellateSelectDialog
        }
    }

    override fun ConstellateSelectDialogBinding.initView() {
        selectConstellateName = arguments?.getString("selectConstellateName") ?: ""
        rvConstellateBottomType.init(GridLayoutManager(requireActivity(), 3), mConstellateBottomTypeAdapter, true)
        rvConstellateBottomType.post {
            setupConstellateBottomGrid()
        }
    }

    override fun getGravity() = Gravity.BOTTOM

    override fun cancelOnTouchOutSide() = true

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }


    /**
     *
     * 设置适配器宽高和间隔
     */
    private fun setupConstellateBottomGrid() {
        val itemSize = (getScreenWidth()) / 3
       // val verticalSpacing = (getScreenHeight() - mBinding.constellateMainToolbar.root.height  -  itemSize * 4) / 5  // 4 行，3 个间隔
//        mBinding.rvConstellateBottomType.addItemDecoration(
//            CustomGridItemDecoration(
//                spanCount = 3,
//                horizontalSpacing = 0.dp2px(),
//                verticalSpacing = 0,
//                totalRows = 4
//            )
//        )


        val constellateType = object : TypeToken<List<ConstellateTypeEntity>>() {}.type
        val jsonData: String = JsonUtils.getJson(requireActivity(), "constellatetype.json")
        val mDataList: MutableList<ConstellateTypeEntity> = GsonUtils.getList(jsonData, constellateType)
        typeArrayIcons = resources.obtainTypedArray(R.array.constellate_type)
        mConstellateBottomTypeAdapter.setTypedArray(typeArrayIcons)
        mConstellateBottomTypeAdapter.setItemSize(itemSize)
        mConstellateBottomTypeAdapter.setSelectConstellateName(selectConstellateName)
        mConstellateBottomTypeAdapter.submitList(mDataList)

    }
}