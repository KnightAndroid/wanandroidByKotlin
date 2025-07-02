package com.knight.kotlin.module_eye_discover.adapter

import android.app.Activity
import android.content.Context
import android.graphics.Outline
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.library_base.entity.EyeTag
import com.core.library_base.ktx.setOnClick
import com.core.library_base.util.dp2px

import com.knight.kotlin.library_share.ShareDialog
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_eye_discover.R
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSpecialTopicItemBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSpecialTopicTagItemBinding
import com.knight.kotlin.module_eye_discover.entity.EyeSpecialTopicItemModel
import com.knight.kotlin.module_eye_discover.okplaystd.OkPlayerRv
import com.knight.kotlin.module_eye_discover.okplaystd.ViewAttr
import com.knight.kotlin.module_eye_discover.utils.AutoPlayUtils


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/20 15:33
 * @descript:专题详细页面 适配器
 */
class EyeDiscoverSpecialTopicDetailAdapter(private val mActivity: FragmentActivity, val mVideoClick: OnVideoClick) : BaseQuickAdapter<EyeSpecialTopicItemModel,EyeDiscoverSpecialTopicDetailAdapter.VH>() {

    class VH(
        parent: ViewGroup,
        val binding: EyeDiscoverSpecialTopicItemBinding = EyeDiscoverSpecialTopicItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ),
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int, item: EyeSpecialTopicItemModel?) {
        val binding = DataBindingUtil.getBinding<EyeDiscoverSpecialTopicItemBinding>(holder.itemView)
        item?.run {
            binding?.model = this
            binding?.activity = mActivity
            val container = binding!!.surfaceContainer
            binding.tvShare.setOnClick {
                ShareDialog.newInstance(data.content.data.title,data.content.data.description,data.content.data.cover!!.feed).showAllowingStateLoss(mActivity.supportFragmentManager, "dialog_share")
            }
            dealJzvdStdRv(container, this)
        }

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }

    private fun dealJzvdStdRv(container: FrameLayout, item: EyeSpecialTopicItemModel) {
        container.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, 4.dp2px().toFloat())
            }
        }
        container.clipToOutline = true
        val jzvdStdRv: OkPlayerRv
        //从播放详情页面回到列表后，需要先从详情渲染控件中移除再添加到Item布局中
        if (OkPlayerRv.getPlayer() != null && AutoPlayUtils.positionInList == items.indexOf(item)) {
            val parent = OkPlayerRv.getPlayer()?.parent
            if (parent != null) {
                (parent as ViewGroup).removeView(OkPlayerRv.getPlayer())
            }
            container.removeAllViews()
            container.addView(
                OkPlayerRv.getPlayer(), FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            jzvdStdRv = OkPlayerRv.getPlayer() as OkPlayerRv
        } else {
            //当Item中未添加JzvdStdRv时，此时需要调用addView进行添加
            if (container.childCount == 0) {
                jzvdStdRv = OkPlayerRv(container.context)
                container.addView(
                    jzvdStdRv,
                    FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                )
            } else {//当已经添加过了，则直接取出第一个子控件即可，达到复用的效果
                jzvdStdRv = container.getChildAt(0) as OkPlayerRv
            }
            //设置视频播放资源以及封面图
            jzvdStdRv.setUp(item.data.content.data.playUrl, "")
            ImageLoader.getInstance().loadStringPhoto(context,item.data.content.data.cover!!.feed,jzvdStdRv.posterImageView)
//            jzvdStdRv.posterImageView.load(item.data.content.data.cover!!.feed)
        }
        jzvdStdRv.id = R.id.eye_discover_jzvdplayer
        jzvdStdRv.isAtList = true
        jzvdStdRv.setClickUi(object : OkPlayerRv.ClickUi {
            override fun onClickUiToggle() {
                //点击视频播放/暂停时保存当前的播放位置
                AutoPlayUtils.positionInList = items.indexOf(item)
                //修改当前播放器是否列表播放的标记
                jzvdStdRv.isAtList = false
                //记录当前播放控件的坐标以及宽高

                val location = IntArray(2)
                jzvdStdRv.getLocationInWindow(location)
                val attr = ViewAttr(location[0],location[1],jzvdStdRv.measuredWidth,jzvdStdRv.measuredHeight)
                mVideoClick.videoClick(
                    jzvdStdRv, attr, items.indexOf(
                        item
                    )
                )
                jzvdStdRv.setClickUi(null)
            }

            override fun onClickStart() {
                //视频开始播放时记录当前的播放位置
                AutoPlayUtils.positionInList = items.indexOf(item)
            }
        })
    }

    interface OnVideoClick {
        fun videoClick(focusView: ViewGroup, viewAttr: ViewAttr, position: Int)
    }

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["tagList", "activity"])
        fun LinearLayout.setTagData(tagList: List<EyeTag>, activity: Activity) {
            removeAllViews()
            var tags = tagList
            if (tags.size > 3) {
                tags = tags.subList(0, 3)
            }
            tags.forEach { tag ->
                val binding =
                    EyeDiscoverSpecialTopicTagItemBinding.inflate(activity.layoutInflater, this, false)
                binding.name = tag.name
                addView(binding.root)
            }
        }
    }
}