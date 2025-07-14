package com.knight.kotlin.module_navigate.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextUtils
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.core.library_common.util.CacheUtils
import com.google.android.flexbox.FlexboxLayoutManager
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.entity.HierachyRightBeanEntity
import com.knight.kotlin.module_navigate.listener.CheckListener

/**
 * Author:Knight
 * Time:2022/5/5 17:38
 * Description:ItemHeaderDecoration
 */
class ItemHeaderDecoration constructor(context: Context,datas:List<HierachyRightBeanEntity>) : RecyclerView.ItemDecoration() {


    private val mTitleHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
            30f,
            context.resources.displayMetrics
        ).toInt()
    private val titleFontSize =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, context.resources.displayMetrics)
            .toInt()
    private var mDatas = datas
    private var mCheckListener: CheckListener? = null
    private val paint = Paint()
    private val mInflater: LayoutInflater = LayoutInflater.from(context);

    /**
     * 标记当前左侧选中的position，因为有可能选中的item，右侧不能置顶，所以强制替换掉当前的tag
     *
     */


    init {
        paint.textSize = titleFontSize.toFloat()
        paint.isAntiAlias = true
    }

    fun setCheckListener(checkListener: CheckListener) {
        mCheckListener = checkListener
    }

    companion object {
        var currentTag = "0"
//        fun setCurrentTag(tag: String) {
//            currentTag = tag
//        }
    }

    fun setData(mDatas: List<HierachyRightBeanEntity>): ItemHeaderDecoration {
        this.mDatas = mDatas
        return this
    }

    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val manager = parent.layoutManager as FlexboxLayoutManager?
        val pos = (parent.layoutManager as FlexboxLayoutManager?)?.findFirstVisibleItemPosition() ?: 0
        var tag: String = mDatas[pos].tag
        val child = parent.findViewHolderForLayoutPosition(pos)?.itemView
        //canvas是否平移的标志
        var isTranslate = false
        if (!TextUtils.equals(mDatas[pos].tag, mDatas[pos + 1].tag)) {
            tag = mDatas[pos].tag
            if (mDatas[pos].isTitle) {
                //body 才平移
                child?.let {
                    if (it.height + it.top < mTitleHeight) {
                        canvas.save()
                        isTranslate = true
                        val height = it.height + it.top - mTitleHeight
                        canvas.translate(0f, height.toFloat())
                    }
                }

            }
        }
        drawHeader(parent, pos, canvas)
        if (isTranslate) {
            canvas.restore()
        }
        if (!TextUtils.equals(tag, currentTag)) {
            currentTag = tag
            val integer = Integer.valueOf(tag)
            mCheckListener?.let {
                it.check(integer, false)
            }
        }
    }

    /**
     * @param parent
     * @param pos
     */
    @SuppressLint("ResourceAsColor")
    private fun drawHeader(parent: RecyclerView, pos: Int, canvas: Canvas) {
        val topTitleView: View =
            mInflater.inflate(R.layout.navigate_right_title_item, parent, false)
        val tvTitle = topTitleView.findViewById<View>(R.id.navigate_right_tv_title) as TextView
        val hierachy_right_view = topTitleView.findViewById(R.id.navigate_right_view) as View
        tvTitle.setText(mDatas[pos].titleName)
        hierachy_right_view.setBackgroundColor(CacheUtils.getThemeColor())

        //绘制title开始
        val toDrawWidthSpec: Int
        val toDrawHeightSpec: Int
        var lp = topTitleView.layoutParams as RecyclerView.LayoutParams
        if (lp == null) {
            //这里是根据复杂布局layout的width height，new一个Lp
            lp = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            topTitleView.layoutParams = lp
        }
        topTitleView.layoutParams = lp
        toDrawWidthSpec = if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
            //如果是MATCH_PARENT，则用父控件能分配的最大宽度和EXACTLY构建MeasureSpec
            View.MeasureSpec.makeMeasureSpec(
                parent.width - parent.paddingLeft - parent.paddingRight,
                View.MeasureSpec.EXACTLY
            )
        } else if (lp.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            //如果是WRAP_CONTENT，则用父控件能分配的最大宽度和AT_MOST构建MeasureSpec
            View.MeasureSpec.makeMeasureSpec(
                parent.width - parent.paddingLeft - parent.paddingRight,
                View.MeasureSpec.AT_MOST
            )
        } else {
            //否则则是具体的宽度数值，则用这个宽度和EXACTLY构建MeasureSpec
            View.MeasureSpec.makeMeasureSpec(lp.width, View.MeasureSpec.EXACTLY)
        }
        //高度同理
        toDrawHeightSpec = if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
            View.MeasureSpec.makeMeasureSpec(
                parent.height - parent.paddingTop - parent.paddingBottom,
                View.MeasureSpec.EXACTLY
            )
        } else if (lp.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            View.MeasureSpec.makeMeasureSpec(
                parent.height - parent.paddingTop - parent.paddingBottom,
                View.MeasureSpec.AT_MOST
            )
        } else {
            View.MeasureSpec.makeMeasureSpec(mTitleHeight, View.MeasureSpec.EXACTLY)
        }
        //依次调用 measure,layout,draw方法，将复杂头部显示在屏幕上
        topTitleView.measure(toDrawWidthSpec, toDrawHeightSpec)
        topTitleView.layout(
            parent.paddingLeft,
            parent.paddingTop,
            parent.paddingLeft + topTitleView.measuredWidth,
            parent.paddingTop + topTitleView.measuredHeight
        )
        //Canvas默认在视图顶部，无需平移，直接绘制
        topTitleView.draw(canvas)
        //绘制title结束
    }





}