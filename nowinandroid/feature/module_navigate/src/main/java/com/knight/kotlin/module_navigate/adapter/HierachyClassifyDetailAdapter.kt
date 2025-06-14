package com.knight.kotlin.module_navigate.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.View
import android.widget.TextView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.module_navigate.R
import com.knight.kotlin.module_navigate.entity.HierachyRightBeanEntity
import com.knight.kotlin.module_navigate.holder.RvHolder
import com.knight.kotlin.module_navigate.listener.RvListener

/**
 * Author:Knight
 * Time:2022/5/5 16:35
 * Description:HierachyClassifyDetailAdapter
 */
class HierachyClassifyDetailAdapter : RvAdapter<HierachyRightBeanEntity>{


    @JvmOverloads
    constructor(context: Context,list: List<HierachyRightBeanEntity> , listener: RvListener)
            : super(context, list, listener) {

    }

    override fun getItemViewType(position: Int): Int {
        return if (mlist.get(position).isTitle) 0 else 1
    }


    override fun getLayoutId(viewType: Int): Int {
        return if (viewType == 0) R.layout.navigate_right_title_item else R.layout.navigate_classify_detail
    }

    override fun getHolder(view: View, viewType: Int): RvHolder<HierachyRightBeanEntity> {
        return ClassifyHolder(view,viewType,mListener)
    }


    inner class ClassifyHolder(itemView:View,type:Int,listener:RvListener) : RvHolder<HierachyRightBeanEntity>(itemView, listener){

        //内容
        private lateinit var navigate_tv_content: TextView
        //标题
        private lateinit var navigate_right_tv_title:TextView
        //标题右边颜色条
        private lateinit var navigate_right_view:View
        init {
            when(type) {
                0 -> {
                    navigate_right_tv_title = itemView.findViewById(R.id.navigate_right_tv_title)
                    navigate_right_view = itemView.findViewById(R.id.navigate_right_view)
                }
                1 ->{
                    navigate_tv_content = itemView.findViewById(R.id.navigate_tv_content)
                }
                else ->{}
            }

        }
        override fun bindHolder(t: HierachyRightBeanEntity, position: Int) {
            val itemViewType = this@HierachyClassifyDetailAdapter.getItemViewType(position)
            when (itemViewType) {
                0 ->{
                    navigate_right_tv_title.text = t.name
                    navigate_right_view.setBackgroundColor(CacheUtils.getThemeColor())
                }
                1->{
                    val lp = navigate_tv_content.layoutParams
                    if (lp is FlexboxLayoutManager.LayoutParams) {
                        val flexboxLp = navigate_tv_content.getLayoutParams() as FlexboxLayoutManager.LayoutParams
                        flexboxLp.flexGrow = 1.0f
                        flexboxLp.alignSelf = AlignItems.FLEX_END
                    }
                    navigate_tv_content.text = t.name
                    navigate_tv_content.setTextColor(CacheUtils.getThemeColor())
                    val gradientDrawable = GradientDrawable()
                    gradientDrawable.shape = GradientDrawable.RECTANGLE
                    gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
                    gradientDrawable.cornerRadius = 6.dp2px().toFloat()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        navigate_tv_content.setBackground(gradientDrawable)
                    } else {
                        navigate_tv_content.setBackgroundDrawable(gradientDrawable)
                    }
                }
                else ->{}
            }
        }

    }

}