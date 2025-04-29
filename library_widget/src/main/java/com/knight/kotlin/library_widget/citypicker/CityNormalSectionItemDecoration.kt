package com.knight.kotlin.library_widget.citypicker

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_widget.GroupCityListBean


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/18 9:53
 * @descript:普通城市分割线
 */
class CityNormalSectionItemDecoration(data: List<GroupCityListBean>,isLocalData: Boolean) :
    RecyclerView.ItemDecoration() {

    private lateinit var mData: MutableList<GroupCityListBean>

    private val mBgPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = if(CacheUtils.getNormalDark())  Color.parseColor("#EEEEEF") else Color.parseColor("#EDEDED")
    }

    private val mTextPaint: TextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 14.dp2px().toFloat()
        color = if(CacheUtils.getNormalDark()) Color.parseColor("#D3D3D3") else Color.parseColor("#999999")
    }

    private val mBounds: Rect = Rect()
    private val mSectionHeight: Int = 32.dp2px()
    private val mTextHeight: Int

    init {
        mData = mutableListOf()
        if (isLocalData) {
            val localData = GroupCityListBean("历史", listOf())
            mData.add(localData)
        }
        mData.addAll(data)
        // 只计算一次文字高度
        mTextPaint.getTextBounds("A", 0, 1, mBounds)
        mTextHeight = mBounds.height()

    }

    fun setData(data: MutableList<GroupCityListBean>) {
        this.mData = data
    }

    fun getData() :MutableList<GroupCityListBean>{
        return mData
    }

    private fun isFirstInGroup(position: Int): Boolean {
        if (position == 0) return true
        val currentGroup = mData.getOrNull(position)?.group
        val prevGroup = mData.getOrNull(position - 1)?.group
        return currentGroup != null && currentGroup != prevGroup
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (position in mData.indices && isFirstInGroup(position)) {
            outRect.set(0, mSectionHeight, 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val position = params.viewLayoutPosition

            if (position in mData.indices && isFirstInGroup(position)) {
                val group = mData[position].group
                drawSection(c, left, right, child, params, group)
            }
        }
    }

    private fun drawSection(
        c: Canvas,
        left: Int,
        right: Int,
        child: View,
        params: RecyclerView.LayoutParams,
        group: String
    ) {
        val top = child.top - params.topMargin
        val bottom = top - mSectionHeight

        // 背景
        c.drawRect(left.toFloat(), bottom.toFloat(), right.toFloat(), top.toFloat(), mBgPaint)

        // 文本
        val textY = bottom + (mSectionHeight - mTextHeight) / 2 + mTextHeight
        c.drawText(group, child.paddingLeft.toFloat(), textY.toFloat(), mTextPaint)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val pos = (parent.layoutManager as? LinearLayoutManager)?.findFirstVisibleItemPosition() ?: return
        if (mData.isEmpty() || pos !in mData.indices) return

        val group = mData[pos].group
        val child = parent.findViewHolderForLayoutPosition(pos)?.itemView ?: return

        var translate = 0f
        if ((pos + 1) < mData.size) {
            val nextGroup = mData[pos + 1].group
            if (group != nextGroup) {
                if (child.height + child.top < mSectionHeight) {
                    translate = (child.height + child.top - mSectionHeight).toFloat()
                    c.save()
                    c.translate(0f, translate)
                }
            }
        }

        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val top = parent.paddingTop

        // 背景
        c.drawRect(
            left.toFloat(),
            top.toFloat(),
            right.toFloat(),
            (top + mSectionHeight).toFloat(),
            mBgPaint
        )

        // 文本
        val textY = top + (mSectionHeight - mTextHeight) / 2 + mTextHeight
        c.drawText(group, child.paddingLeft.toFloat(), textY.toFloat(), mTextPaint)

        if (translate != 0f) c.restore()
    }
}