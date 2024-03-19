package com.knight.kotlin.module_video.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.library_widget.BaseBottomSheetDialog
import com.knight.kotlin.module_video.adapter.VideoCommentAdapter
import com.knight.kotlin.module_video.databinding.VideoDialogCommentBinding
import com.knight.kotlin.module_video.vm.VideoVm
import dagger.hilt.android.AndroidEntryPoint

/**
 * Author:Knight
 * Time:2024/3/19 15:47
 * Description:VideoCommentDialog
 */
@AndroidEntryPoint
class VideoCommentDialog(val jokeId:Long) :  BaseBottomSheetDialog() {

    private val videoVm by lazy{ ViewModelProvider(this)[VideoVm::class.java] }
   //private lateinit var videoVm: VideoVm
    //视频列表适配器
    private val mVideoCommentAdapter: VideoCommentAdapter by lazy { VideoCommentAdapter(mutableListOf()) }

    //private  val jokeId = jokeId

    private lateinit var binding: VideoDialogCommentBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = VideoDialogCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        binding.recyclerView.init(LinearLayoutManager(activity),mVideoCommentAdapter,false)
       // videoVm = ViewModelProvider(this)[VideoVm::class.java]
        loadData()

    }

    private fun loadData() {
        videoVm.getVideoCommentList(jokeId,1,successCallBack ={
             mVideoCommentAdapter.setNewInstance(it.comments)

        },failureCallBack = {
            toast(it ?: getString(com.knight.kotlin.library_base.R.string.base_request_failure))
        })
    }

    protected override val height: Int
        protected get() = resources.displayMetrics.heightPixels - 600


}