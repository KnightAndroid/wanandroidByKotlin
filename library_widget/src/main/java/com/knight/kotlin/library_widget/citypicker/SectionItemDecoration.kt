package com.knight.kotlin.library_widget.citypicker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.library_base.util.dp2px
import java.util.Locale
import java.util.regex.Matcher


/**
 * @Description
 * @Author knight
 * @Time 2025/4/17 22:38
 *
 */

class SectionItemDecoration(context: Context, data: List<CityBean>) :
    RecyclerView.ItemDecoration() {
    private var mData: List<CityBean>
    private val mBgPaint: Paint
    private val mTextPaint: TextPaint
    private val mBounds: Rect

    private val mSectionHeight: Int
    private val mBgColor: Int
    private val mTextColor: Int
    private val mTextSize: Int

    init {
        this.mData = data
        mBgColor = Color.parseColor("#EDEDED")
        mSectionHeight = 32.dp2px()
        mTextSize = 14.dp2px()
        mTextColor = Color.parseColor("#999999")

        mBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBgPaint.color = mBgColor

        mTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
        mTextPaint.textSize = mTextSize.toFloat()
        mTextPaint.color = mTextColor

        mBounds = Rect()
    }

    fun setData(data: List<CityBean>) {
        this.mData = data
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child
                .layoutParams as RecyclerView.LayoutParams
            val position = params.viewLayoutPosition
            if (mData != null && !mData!!.isEmpty() && position <= mData!!.size - 1 && position > -1) {
                if (position == 0) {
                    drawSection(c, left, right, child, params, position)
                } else {
                    if (null != mData!![position].city
                        && !mData!![position].city
                            .equals(mData!![position - 1].city)
                    ) {
                        drawSection(c, left, right, child, params, position)
                    }
                }
            }
        }
    }

    private fun drawSection(
        c: Canvas, left: Int, right: Int, child: View,
        params: RecyclerView.LayoutParams, position: Int
    ) {
        c.drawRect(
            left.toFloat(),
            (child.top - params.topMargin - mSectionHeight).toFloat(),
            right.toFloat(),
            (child.top - params.topMargin).toFloat(), mBgPaint
        )
        mTextPaint.getTextBounds(
            mData!![position].city,
            0,
            mData!![position].city.length,
            mBounds
        )
        c.drawText(
            mData!![position].city,
            child.paddingLeft.toFloat(),
            (child.top - params.topMargin - (mSectionHeight / 2 - mBounds.height() / 2)).toFloat(),
            mTextPaint
        )
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val pos = ((parent.layoutManager) as LinearLayoutManager).findFirstVisibleItemPosition()
        if (pos < 0) return
        if (mData == null || mData!!.isEmpty()) return
        val section: String = mData!![pos].city
        val child = parent.findViewHolderForLayoutPosition(pos)!!.itemView

        var flag = false
        if ((pos + 1) < mData!!.size) {
            if (null != section && section != mData!![pos + 1].city) {
                if (child.height + child.top < mSectionHeight) {
                    c.save()
                    flag = true
                    c.translate(0f, (child.height + child.top - mSectionHeight).toFloat())
                }
            }
        }
        c.drawRect(
            parent.paddingLeft.toFloat(),
            parent.paddingTop.toFloat(),
            (parent.right - parent.paddingRight).toFloat(),
            (parent.paddingTop + mSectionHeight).toFloat(), mBgPaint
        )
        mTextPaint.getTextBounds(section, 0, section.length, mBounds)
        c.drawText(
            section,
            child.paddingLeft.toFloat(),
            (parent.paddingTop + mSectionHeight - (mSectionHeight / 2 - mBounds.height() / 2)).toFloat(),
            mTextPaint
        )
        if (flag) c.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (mData != null && !mData!!.isEmpty() && position <= mData!!.size - 1 && position > -1) {
            if (position == 0) {
                outRect.set(0, mSectionHeight, 0, 0)
            } else {
                if (null != mData!![position].city
                    && !mData!![position].city.equals(mData!![position - 1].city)
                ) {
                    outRect.set(0, mSectionHeight, 0, 0)
                }
            }
        }
    }


//    /***
//     * 获取悬浮栏文本，（#、定位、热门 需要特殊处理）
//     * @return
//     */
//    fun getSection(city:String): String {
//        if (TextUtils.isEmpty(city)) {
//            return "#"
//        } else {
//
//            val transliterator = Transliterator.getInstance("Han-Latin/Names; Latin-ASCII; Any-Upper")
//            return transliterator.transliterate(input).replace(" ", "")
//
//
//            val c: String = pinyin.substring(0, 1)
//            val p: Pattern = Pattern.compile("[a-zA-Z]")
//            val m: Matcher = p.matcher(c)
//            return if (m.matches()) {
//                c.uppercase(Locale.getDefault())
//            } else if (TextUtils.equals(c, "定") || TextUtils.equals(c, "热")) pinyin
//            else "#"
//        }
//    }
}