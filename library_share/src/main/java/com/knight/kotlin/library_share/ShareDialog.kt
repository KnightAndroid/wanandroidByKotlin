package com.knight.kotlin.library_share

import android.graphics.Bitmap
import android.view.Gravity
import androidx.lifecycle.lifecycleScope
import com.knight.kotlin.library_base.entity.EyeData
import com.knight.kotlin.library_base.fragment.BaseDialogFragment
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_share.databinding.ShareDialogBinding
import com.knight.kotlin.library_util.FileUtils
import com.knight.kotlin.library_util.ShareSdkUtils
import com.knight.kotlin.library_util.bitmap.BitmapUtils
import com.knight.kotlin.library_util.bitmap.saveToAlbum
import com.knight.kotlin.library_util.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/22 15:49
 * @descript:分享弹窗
 */
class ShareDialog : BaseDialogFragment<ShareDialogBinding, EmptyViewModel>() {

    private  var data: EyeData? =null
    companion object {
        fun newInstance(data: EyeData): ShareDialog {
            val downLoadDialogFragment = ShareDialog()
//            val args = Bundle()
//            args.putParcelable("data",data)
//            downLoadDialogFragment.arguments = args
            return downLoadDialogFragment
        }
    }

    override fun getGravity(): Int = Gravity.CENTER
    override fun cancelOnTouchOutSide(): Boolean {
        return true
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun ShareDialogBinding.initView() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            data = arguments?.getParcelable("data",EyeData::class.java)
//        } else {
//            data = arguments?.getParcelable("data")
//        }
//        data?.run {
//            tvShareTitle.text = title
//            tvShareDesc.text = description
//            ImageLoader.getInstance().loadStringPhoto(requireActivity(),cover!!.feed,ivPhoto)
//        }


        tvSaveLocal.setOnClick {
//            val bitmapList = mutableListOf<Bitmap>()
//            val bitmapQrcode = BitmapUtils.getViewBitmap(rlQrcode)
//            val bitmapPhoto = BitmapUtils.getViewBitmap(ivPhoto)
//            val bitmapDesc = BitmapUtils.getViewBitmap(rlShareDesc)
//            bitmapList.add(bitmapQrcode)
//            bitmapList.add(bitmapPhoto)
//            bitmapList.add(bitmapDesc)
//            val saveBitmap = BitmapUtils.compressImage(BitmapUtils.puzzleBitmap(bitmapList))
//            saveBitmap?.run {
//                val uri = saveToAlbum(requireActivity(), FileUtils.getDateName("wanandroid") + ".jpg",null)
//                uri?.run {
//                    toast(getString(R.string.share_save_photo_success))
//                } ?: run{
//                    toast(getString(R.string.share_save_photo_failure))
//                }
//            }




            // 异步处理
            lifecycleScope.launch(Dispatchers.IO) {
                val bitmapQrcode = BitmapUtils.getViewBitmap(rlQrcode)
                val bitmapPhoto = BitmapUtils.getViewBitmap(ivPhoto)
                val bitmapDesc = BitmapUtils.getViewBitmap(rlShareDesc)

                val bitmapList = listOfNotNull(bitmapQrcode, bitmapPhoto, bitmapDesc) // 使用 listOfNotNull 避免 null

                val puzzleBitmap = BitmapUtils.puzzleBitmap(bitmapList)
                val saveBitmap = puzzleBitmap?.let { BitmapUtils.compressImage(it) }

                // 切换回主线程更新 UI
                withContext(Dispatchers.Main) {
                    saveBitmap?.run {
                        val uri = saveToAlbum(requireActivity(), FileUtils.getDateName("wanandroid") + ".jpg", null)
                        uri?.run {
                            toast(getString(R.string.share_save_photo_success))
                        } ?: run {
                            toast(getString(R.string.share_save_photo_failure))
                        }
                        // 及时释放 Bitmap
                        recycleSafely(this)
                    } ?: run {
                        toast(getString(R.string.share_save_photo_failure))
                    }
                    // 及时释放中间 Bitmap
                    recycleSafely(puzzleBitmap)
                    recycleSafely(bitmapQrcode)
                    recycleSafely(bitmapPhoto)
                    recycleSafely(bitmapDesc)
                }
            }

        }

        tvWechat.setOnClick {
            data?.run {
                ShareSdkUtils.shareWxText(title,description)
            }

        }

    }



    fun recycleSafely(bitmap: Bitmap?) {
        if (bitmap != null && !bitmap.isRecycled) {
            bitmap.recycle()
        }
    }

}