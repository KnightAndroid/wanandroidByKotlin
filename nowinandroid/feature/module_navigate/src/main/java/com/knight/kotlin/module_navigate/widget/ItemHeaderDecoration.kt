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
import com.google.android.flexbox.FlexboxLayoutManager
import com.knight.kotlin.library_common.util.CacheUtils
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.entity.HierachyRightBeanEntity
import com.knight.kotlin.module_navigate.listener.CheckListener

/**
 * Author:Knight
 * Time:2022/5/5 17:38
 * Description:ItemHeaderDecoration
 */
class ItemHeaderDecoration(
    context: Context,
    datas: List<HierachyRightBeanEntity>
) : RecyclerView.ItemDecoration() {

    /**
     * Header高度
     *
     * 必须和 XML 高度一致
     */
    private val mTitleHeight = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        33f,
        context.resources.displayMetrics
    ).toInt()

    /**
     * title字体大小
     */
    private val titleFontSize = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        16f,
        context.resources.displayMetrics
    ).toInt()

    /**
     * 数据源
     */
    private var mDatas = datas

    /**
     * 左右联动监听
     */
    private var mCheckListener: CheckListener? = null

    private val paint = Paint()

    private val mInflater: LayoutInflater =
        LayoutInflater.from(context)

    /**
     * 缓存HeaderView
     */
    private val mTopTitleView: View by lazy {
        mInflater.inflate(
            R.layout.navigate_right_title_item,
            null,
            false
        )
    }

    private val mTvTitle: TextView by lazy {
        mTopTitleView.findViewById(R.id.navigate_right_tv_title)
    }

    private val mRightView: View by lazy {
        mTopTitleView.findViewById(R.id.navigate_right_view)
    }

    init {

        paint.textSize = titleFontSize.toFloat()

        paint.isAntiAlias = true
    }

    fun setCheckListener(
        checkListener: CheckListener
    ) {

        mCheckListener = checkListener
    }

    companion object {

        /**
         * 当前左侧选中的tag
         */
        var currentTag = "0"
    }

    fun setData(
        datas: List<HierachyRightBeanEntity>
    ): ItemHeaderDecoration {

        mDatas = datas

        return this
    }

    override fun onDrawOver(
        canvas: Canvas,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val layoutManager =
            parent.layoutManager
                    as? FlexboxLayoutManager
                ?: return

        val pos =
            layoutManager.findFirstVisibleItemPosition()

        if (pos < 0 || pos >= mDatas.size) {
            return
        }

        /**
         * 获取真正Header位置
         */
        val headerPosition =
            getHeaderPosition(pos)

        var tag = mDatas[headerPosition].tag

        val child =
            parent.findViewHolderForLayoutPosition(
                pos
            )?.itemView

        /**
         * Canvas是否需要平移
         */
        var isTranslate = false

        /**
         * Header顶替效果
         */
        if (pos + 1 < mDatas.size &&
            !TextUtils.equals(
                mDatas[headerPosition].tag,
                mDatas[pos + 1].tag
            )
        ) {

            if (mDatas[headerPosition].isTitle) {

                child?.let {

                    if (it.height + it.top < mTitleHeight) {

                        canvas.save()

                        isTranslate = true

                        val height =
                            it.height +
                                    it.top -
                                    mTitleHeight

                        canvas.translate(
                            0f,
                            height.toFloat()
                        )
                    }
                }
            }
        }

        /**
         * 绘制Header
         */
        drawHeader(
            parent,
            headerPosition,
            canvas
        )

        if (isTranslate) {

            canvas.restore()
        }

        /**
         * 左右联动
         */
        if (!TextUtils.equals(tag, currentTag)) {

            currentTag = tag

            mCheckListener?.check(
                tag.toInt(),
                false
            )
        }
    }

    /**
     * 获取当前Header位置
     *
     * 防止body item导致Header错乱
     */
    private fun getHeaderPosition(
        position: Int
    ): Int {

        var currentPosition = position

        while (currentPosition >= 0) {

            if (mDatas[currentPosition].isTitle) {

                return currentPosition
            }

            currentPosition--
        }

        return 0
    }

    /**
     * 绘制Header
     */
    @SuppressLint("ResourceAsColor")
    private fun drawHeader(
        parent: RecyclerView,
        pos: Int,
        canvas: Canvas
    ) {

        if (pos < 0 || pos >= mDatas.size) {
            return
        }

        /**
         * 设置标题
         */
        mTvTitle.text =
            mDatas[pos].titleName

        /**
         * 设置主题色
         */
        mRightView.setBackgroundColor(
            CacheUtils.getThemeColor()
        )

        /**
         * 测量宽高
         */
        val widthSpec: Int

        val heightSpec: Int

        var lp =
            mTopTitleView.layoutParams
                    as? RecyclerView.LayoutParams

        if (lp == null) {

            lp = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            mTopTitleView.layoutParams = lp
        }

        widthSpec = when (lp.width) {

            ViewGroup.LayoutParams.MATCH_PARENT -> {

                View.MeasureSpec.makeMeasureSpec(
                    parent.width -
                            parent.paddingLeft -
                            parent.paddingRight,
                    View.MeasureSpec.EXACTLY
                )
            }

            ViewGroup.LayoutParams.WRAP_CONTENT -> {

                View.MeasureSpec.makeMeasureSpec(
                    parent.width -
                            parent.paddingLeft -
                            parent.paddingRight,
                    View.MeasureSpec.AT_MOST
                )
            }

            else -> {

                View.MeasureSpec.makeMeasureSpec(
                    lp.width,
                    View.MeasureSpec.EXACTLY
                )
            }
        }

        heightSpec = when (lp.height) {

            ViewGroup.LayoutParams.MATCH_PARENT -> {

                View.MeasureSpec.makeMeasureSpec(
                    parent.height -
                            parent.paddingTop -
                            parent.paddingBottom,
                    View.MeasureSpec.EXACTLY
                )
            }

            ViewGroup.LayoutParams.WRAP_CONTENT -> {

                View.MeasureSpec.makeMeasureSpec(
                    mTitleHeight,
                    View.MeasureSpec.EXACTLY
                )
            }

            else -> {

                View.MeasureSpec.makeMeasureSpec(
                    mTitleHeight,
                    View.MeasureSpec.EXACTLY
                )
            }
        }

        /**
         * measure
         */
        mTopTitleView.measure(
            widthSpec,
            heightSpec
        )

        /**
         * layout
         */
        mTopTitleView.layout(
            parent.paddingLeft,
            parent.paddingTop,
            parent.paddingLeft +
                    mTopTitleView.measuredWidth,
            parent.paddingTop +
                    mTopTitleView.measuredHeight
        )

        /**
         * draw
         */
        mTopTitleView.draw(canvas)
    }
}