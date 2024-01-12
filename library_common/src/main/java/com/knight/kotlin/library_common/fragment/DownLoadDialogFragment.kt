package com.knight.kotlin.library_common.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Gravity
import android.view.KeyEvent
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_common.databinding.DownloadDialogBinding
import com.knight.kotlin.library_common.util.AppUtils
import com.knight.kotlin.library_common.util.DownLoadManagerUtils.downloadApk
import com.knight.kotlin.library_common.util.FormetUtils
import com.knight.kotlin.library_common.util.ThreadPoolUtils
import java.io.File


/**
 * Author:Knight
 * Time:2022/1/12 9:56
 * Description:DownLoadDialogFragment
 */
class DownLoadDialogFragment : BaseDialogFragment<DownloadDialogBinding,EmptyViewModel>() {

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



    @NonNull
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        val dialog: Dialog = super.onCreateDialog(savedInstanceState)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnKeyListener(DialogInterface.OnKeyListener { dialog, keyCode, event ->
            keyCode == KeyEvent.KEYCODE_BACK
        })
        return dialog
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