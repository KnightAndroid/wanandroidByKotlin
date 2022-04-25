package com.knight.kotlin.module_home.activity

import android.text.TextUtils
import android.view.View
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.common.reflect.TypeToken
import com.knight.kotlin.library_aop.clickintercept.SingleClick
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.event.MessageEvent
import com.knight.kotlin.library_base.ktx.ActivityMessenger
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.EventBusUtils
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_util.toast.ToastUtils
import com.knight.kotlin.library_widget.flowlayout.OnTagClickListener
import com.knight.kotlin.library_widget.flowlayout.TagInfo
import com.knight.kotlin.library_widget.ktx.setItemChildClickListener
import com.knight.kotlin.library_widget.ktx.setItemClickListener
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.adapter.KnowLedgeAdapter
import com.knight.kotlin.module_home.databinding.HomeLabelActivityBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random

/**
 * Author:Knight
 * Time:2022/4/24 17:07
 * Description:KnowLedgeLabelActivity
 */

@AndroidEntryPoint
@Route(path = RouteActivity.Home.HomeKnowLedgeLabelActivity)
class KnowLedgeLabelActivity : BaseActivity<HomeLabelActivityBinding, EmptyViewModel>() {
    override val mViewModel: EmptyViewModel by viewModels()


    @JvmField
    @Autowired(name = "data")
    var data: ArrayList<String>? = null

    /**
     *
     * 固定标签
     */
    val fixDataList = mutableListOf<String>()

    val mDefaultList = mutableListOf<String>()

    private val myTagInfos = mutableListOf<TagInfo>()

    private var mMoreKnowLedgeList: List<TagInfo> = CacheUtils.getDataInfo(
        "moreknowledgeLabel",
        object : TypeToken<List<TagInfo>>() {}.type
    )

    private val mMoreKnowLedgeAdapter: KnowLedgeAdapter by lazy { KnowLedgeAdapter(arrayListOf()) }
    private var isEdit = false
    override fun setThemeColor(isDarkMode: Boolean) {
        mBinding.homeLabelEdit.setTextColor(themeColor)
        mBinding.homeIvAddlabel.setColorFilter(themeColor)
        mBinding.homeKnowledgetTag.setDefaultTextColor(themeColor)
        mBinding.homeKnowledgetTag.setFixViewEditingTextColor(themeColor)
        mBinding.homeKnowledgetTag.setSelectTextColor(themeColor)
    }

    override fun HomeLabelActivityBinding.initView() {
        homeIncludeTitle.baseIvBack.setOnClick { finish() }
        homeIncludeTitle.baseTvTitle.setText(R.string.home_knowledge_label)
        homeTvMylabel.text = getString(R.string.home_knowledge_label) + "(" + data?.size + "/10)"
        fixDataList.add(data?.get(0) ?: "")
        setOnClickListener(homeLabelEdit, homeIvAddlabel)
        //把首个标签移除,其他就是可编辑标签
        data?.let {
            for (i in it.indices) {
                if (i != 0) {
                    mDefaultList.add(it.get(i))
                }
            }
        }

        homeKnowledgetTag.setSelectTagId("-1")
        homeKnowledgetTag.setOnTagClickListener(object : OnTagClickListener {
            override fun onTagClick(tagInfo: TagInfo?) {

            }

            override fun onTagDelete(tagInfo: TagInfo?) {
                //删除标签 回调这里
                tagInfo?.let {
                    mMoreKnowLedgeAdapter.data.add(it)
                    mMoreKnowLedgeAdapter.notifyItemInserted(mMoreKnowLedgeAdapter.data.size - 1)
                    homeTvMylabel.setText(
                        getString(R.string.home_knowledge_label) + "(" + homeKnowledgetTag.getTagInfos()
                            .size + "/10)"
                    )
                    homeTvMorelabel.setText(
                        getString(R.string.home_more_knowledge) + "(" + mMoreKnowLedgeAdapter.data
                            .size + "/10)"
                    )
                }

            }
        })
        initData()


    }

    private fun initData() {
        myTagInfos.addAll(addTags("fix", fixDataList, TagInfo.TYPE_TAG_SERVICE))
        myTagInfos.addAll(addTags("default", mDefaultList, TagInfo.TYPE_TAG_USER))
        mBinding.homeKnowledgetTag.setTags(myTagInfos)

        val mManager = FlexboxLayoutManager(this)
        mManager.flexDirection = FlexDirection.ROW
        //左对齐
        mManager.justifyContent = JustifyContent.FLEX_START
        mManager.alignItems = AlignItems.CENTER
        mBinding.homeMoreknowledgeRv.setLayoutManager(mManager)



        if (mMoreKnowLedgeList.isNullOrEmpty()) {
            mMoreKnowLedgeList = mutableListOf()
        }

        mBinding.homeMoreknowledgeRv.adapter = mMoreKnowLedgeAdapter
        mMoreKnowLedgeAdapter.setNewInstance(mMoreKnowLedgeList.toMutableList())
        mBinding.homeTvMorelabel.setText(
            getString(R.string.home_more_knowledge) + "(" + mMoreKnowLedgeAdapter.data
                .size + "/10)"
        )
        initListener()
    }

    private fun initListener() {
        mMoreKnowLedgeAdapter.run {
            setItemClickListener { adapter, view, position ->
                if (isEdit) {
                    for (i in 0 until mBinding.homeKnowledgetTag.getTagInfos().size) {
                        if (mMoreKnowLedgeAdapter.data.get(position).tagName.equals(
                                mBinding.homeKnowledgetTag.getTagInfos().get(i).tagName
                            )
                        ) {
                            toast(R.string.home_same_label_tip)

                        }
                    }
                    mBinding.homeKnowledgetTag.addTag(
                        mMoreKnowLedgeAdapter.data.get(position),
                        isEdit
                    )
                    mBinding.homeTvMylabel.setText(
                        getString(R.string.home_knowledge_label) + "(" + mBinding.homeKnowledgetTag.getTagInfos()
                            .size + "/10)"
                    )
                    mMoreKnowLedgeAdapter.data.removeAt(position)
                    mMoreKnowLedgeAdapter.notifyItemRemoved(position)
                    mBinding.homeTvMorelabel.setText(
                        getString(R.string.home_more_knowledge) + "(" + mMoreKnowLedgeAdapter.data
                            .size + "/10)"
                    )

                }

            }
            addChildClickViewIds(R.id.home_iv_moreknowledge_delete)
            setItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.home_iv_moreknowledge_delete -> {

                        //删除
                        mMoreKnowLedgeAdapter.data.removeAt(position)
                        mMoreKnowLedgeAdapter.notifyItemRemoved(position)
                        mBinding.homeTvMorelabel.setText(
                            getString(R.string.home_more_knowledge) + "(" + mMoreKnowLedgeAdapter.data.size + "/10)"
                        )
                    }
                }
            }

        }
    }


    fun addTags(tagId: String, dataList: List<String>, type: Int): List<TagInfo> {
        val list: MutableList<TagInfo> = java.util.ArrayList()
        var tagInfo: TagInfo
        var name: String?
        val labelSize = dataList.size
        if (dataList != null && labelSize > 0) {
            for (i in 0 until labelSize) {
                name = dataList[i]
                tagInfo = TagInfo()
                tagInfo.type = type
                tagInfo.tagName = name
                tagInfo.tagId = tagId + i
                list.add(tagInfo)
            }
        }
        return list
    }

    private fun initTagDrag() {
        mBinding.homeKnowledgetTag.enableDragAndDrop()
        mBinding.homeKnowledgetTag.setIsEdit(true)
    }

    private fun initTagDefault() {
        mBinding.homeKnowledgetTag.setDefault()
        mBinding.homeKnowledgetTag.setIsEdit(false)
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    @SingleClick
    override fun onClick(v: View) {
        when (v) {
            mBinding.homeLabelEdit -> {
                if (mBinding.homeLabelEdit.getText().toString()
                        .equals(getString(R.string.home_edit))
                ) {
                    isEdit = true
                    mBinding.homeLabelEdit.setText(R.string.home_save)
                    initTagDrag()
                    mMoreKnowLedgeAdapter.setIsEdit(true)
                } else {
                    if (mBinding.homeKnowledgetTag.getTagInfos().size > 10) {
                        ToastUtils.show(getString(R.string.home_moreenough_tips, 10))
                    } else {
                        isEdit = false
                        mBinding.homeLabelEdit.setText(R.string.home_edit)
                        mMoreKnowLedgeAdapter.setIsEdit(false)
                        //保存到mmkv
                        val mKnowledgeLabelList = ArrayList<String>()
                        for (i in 0 until mBinding.homeKnowledgetTag.getTagInfos().size) {
                            mKnowledgeLabelList.add(
                                mBinding.homeKnowledgetTag.getTagInfos().get(i).tagName
                            )
                        }
                        //保存我的标签
                        CacheUtils.saveDataInfo("knowledgeLabel", mKnowledgeLabelList)
                        //保存更多标签
                        CacheUtils.saveDataInfo("moreknowledgeLabel", mMoreKnowLedgeAdapter.data)
                        initTagDefault()
                        EventBusUtils.postEvent(
                            MessageEvent(MessageEvent.MessageType.ChangeLabel).putStringList(
                                mKnowledgeLabelList
                            )
                        )
                    }
                }
            }
            mBinding.homeIvAddlabel -> {
//                startActivity(Intent(this,AddKnowLedgeLabelActivity::class.java))
                if (mMoreKnowLedgeAdapter.data.size < 10) {
                    ActivityMessenger.startActivityForResult<AddKnowLedgeLabelActivity>(this@KnowLedgeLabelActivity) {
                        if (it != null) {
                            //未成功处理，即（ResultCode != RESULT_OK）
                            //处理成功，这里可以操作返回的intent
                            it.extras?.let {
                                val result: String = it.getString("label_data") ?: ""
                                if (!TextUtils.isEmpty(result)) {
                                    for (i in 0 until mMoreKnowLedgeAdapter.data.size) {
                                        if (result == mMoreKnowLedgeAdapter.data.get(i).tagName) {
                                            ToastUtils.show(R.string.home_same_label_tip)
                                            return@let
                                        }
                                    }
                                    val tagInfo = TagInfo()
                                    tagInfo.type = TagInfo.TYPE_TAG_USER
                                    tagInfo.tagName = result
                                    tagInfo.tagId = "moreknowledgeLabel" + Random(1).nextInt(100)
                                    mMoreKnowLedgeAdapter.data.add(tagInfo)
                                    mMoreKnowLedgeAdapter.notifyItemInserted(
                                        mMoreKnowLedgeAdapter.data.size - 1
                                    )
                                    mBinding.homeTvMorelabel.setText(
                                        getString(R.string.home_more_knowledge) + "(" + mMoreKnowLedgeAdapter.data.size + "/10)"
                                    )
                                }
                            }
                        }
                    }
                } else {
                    toast(getString(R.string.home_moreenough_tips, 10))
                }
            }
        }
    }
}