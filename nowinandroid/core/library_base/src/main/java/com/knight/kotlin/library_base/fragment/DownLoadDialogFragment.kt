package com.knight.kotlin.library_base.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Gravity
import com.core.library_base.vm.EmptyViewModel
import com.core.library_common.util.FormetUtils
import com.knight.kotlin.library_base.databinding.DownloadDialogBinding
import com.knight.kotlin.library_base.utils.AppUtils
import com.knight.kotlin.library_base.utils.DownLoadManagerUtils.downloadApk
import com.knight.kotlin.library_base.utils.ThreadPoolUtils
import java.io.File


/**
 * Author:Knight
 * Time:2022/1/12 9:56
 * Description:DownLoadDialogFragment
 */
class DownLoadDialogFragment : BaseDialogFragment<DownloadDialogBinding, EmptyViewModel>() {

    private var downLoadLink:String? = null
    private var apkFile: File? = null
    private val successDownLoadApk = 1

    companion object {
        fun newInstance(downLoadLink: String?): DownLoadDialogFragment {
            val downLoadDialogFragment = DownLoadDialogFragment()
            val args = Bundle()
            args.putString("downLoadLink", downLoadLink)
            downLoadDialogFragment.arguments = args
            return downLoadDialogFragment
        }
    }




    override fun cancelOnTouchOutSide(): Boolean {
        return false
    }

    override fun getGravity() = Gravity.CENTER

    private val mHandler: Handler = object : Handler(Looper.myLooper() ?: Looper.getMainLooper()) {
        @SuppressLint("SetTextI18n")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what === successDownLoadApk) {
                //下载成功
                dismiss()
                activity?.let { AppUtils.installApk(apkFile, it) }
            } else if (msg.what === 2) {
                mBinding.tvDownloadRate.text =
                    (msg.arg2.toFloat() / msg.arg1.toFloat() * 100).toInt().toString() + "%"
                mBinding.tvSpeed.text = FormetUtils.formetFileSize(msg.arg2.toLong()) + "/" + FormetUtils.formetFileSize(msg.arg1.toLong())
            }
        }
    }

    inner class DownloadApkTask : Runnable {
        override fun run() {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                apkFile = activity?.let {
                    downloadApk(
                        downLoadLink,
                        mBinding.pbDownload,
                        mHandler,
                        it
                    )
                }
                val msg = Message()
                msg.what = successDownLoadApk
                mHandler.sendMessage(msg)
            }
        }
    }

    override fun DownloadDialogBinding.initView() {
        downLoadLink = arguments?.getString("downLoadLink")
        ThreadPoolUtils.execute(DownloadApkTask())
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

}