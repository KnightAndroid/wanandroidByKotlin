package com.knight.kotlin.module_mine.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.config.EyeTypeConstants
import com.knight.kotlin.library_base.databinding.BaseArticleItemBinding
import com.knight.kotlin.library_base.databinding.BaseTextItemBinding
import com.knight.kotlin.library_base.ktx.toHtml
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_mine.R
import com.knight.kotlin.module_mine.entity.MyCollectArticleEntity

/**
 * Author:Knight
 * Time:2022/5/13 10:51
 * Description:MyCollectArticleAdapter
 */
class MyCollectArticleAdapter(data:List<MyCollectArticleEntity>) :
    BaseMultiItemAdapter<MyCollectArticleEntity>(data) {


    // 标题 的 viewholder
    class MineArticleTextVH(val viewBinding: BaseTextItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    // 正文视频 的 viewholder
    class MineArticleImageVH(val viewBinding: BaseArticleItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    // 在 init 初始化的时候，添加多类型
    init {
        addItemType(EyeTypeConstants.TEXT_TYPE, object : OnMultiItemAdapterListener<MyCollectArticleEntity,MineArticleTextVH> { // 类型 1
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): MineArticleTextVH {
                // 创建 viewholder
                val viewBinding = BaseTextItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return MineArticleTextVH(viewBinding)
            }

            override fun onBind(holder: MineArticleTextVH, position: Int, item: MyCollectArticleEntity?) {
                // 绑定 item 数据
                val binding = BaseTextItemBinding.bind(holder.itemView)
                item?.run {
                    //作者
                    if (author.isNullOrEmpty()) {
                        binding.baseItemArticleAuthor.setText("不详")
                    } else {
                        binding.baseItemArticleAuthor.setText(author)
                    }

                    //一级分类
                    if (!TextUtils.isEmpty(item.chapterName)) {
                        binding.baseTvArticleSuperchaptername.visibility = View.VISIBLE
                        binding.baseTvArticleSuperchaptername.setText(chapterName)
                        binding.baseTvArticleSuperchaptername.setTextColor(CacheUtils.getThemeColor())
                        val gradientDrawable = GradientDrawable()
                        gradientDrawable.shape = GradientDrawable.RECTANGLE
                        gradientDrawable.setStroke(2,CacheUtils.getThemeColor())
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            binding.baseTvArticleSuperchaptername.background = gradientDrawable
                        } else {
                            binding.baseTvArticleSuperchaptername.setBackgroundDrawable(gradientDrawable)
                        }
                    } else {
                        binding.baseTvArticleSuperchaptername.visibility = View.GONE
                    }

                    if (!TextUtils.isEmpty(niceDate)) {
                        binding.baseItemArticledate.setText(niceDate)
                    } else {
                        binding.baseItemArticledate.visibility = View.GONE
                    }
                    //标题
                    binding.baseTvArticletitle.setText(title.toHtml())
                    //是否收藏
                    binding.baseIconCollect.setBackgroundResource(R.drawable.mine_icon_delete)
                }



            }
        }).addItemType(EyeTypeConstants.IMAGE_TYPE, object : OnMultiItemAdapterListener<MyCollectArticleEntity, MineArticleImageVH> { // 类型 2
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int):MineArticleImageVH {
                // 创建 viewholder
                val viewBinding = BaseArticleItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return MineArticleImageVH(viewBinding)
            }

            override fun onBind(holder: MineArticleImageVH, position: Int, item: MyCollectArticleEntity?) {
                // 绑定 item 数据
                val binding = BaseArticleItemBinding.bind(holder.itemView)
                item?.run {
                    //项目图片
                    ImageLoader.loadStringPhoto(context,envelopePic,binding.baseItemImageview)
                    //作者
                    if (author.isNullOrEmpty()) {
                        binding.baseItemTvAuthor.setText("不详")
                    } else {
                        binding.baseItemTvAuthor.setText(author)
                    }

                    //时间赋值
                    if (!TextUtils.isEmpty(niceDate)) {
                        binding.baseItemTvTime.visibility = View.VISIBLE
                        binding.baseItemTvTime.setText(niceDate)
                    } else {
                        binding.baseItemTvTime.visibility = View.GONE
                    }
                    //标题
                    binding.baseTvTitle.setText(title.toHtml())

                    //描述
                    if (!TextUtils.isEmpty(desc)) {
                        binding.baseTvProjectDesc.visibility = View.VISIBLE
                        binding.baseTvProjectDesc.setText(desc.toHtml())
                    } else {
                        binding.baseTvProjectDesc.visibility = View.GONE
                    }

                    //分类
                    if (!TextUtils.isEmpty(chapterName)) {
                        binding.baseTvSuperchapter.visibility = View.VISIBLE
                        binding.baseTvSuperchapter.setText(chapterName)
                    } else {
                        binding.baseTvSuperchapter.visibility = View.GONE
                    }
                }
            }

            override fun isFullSpanItem(itemType: Int): Boolean {
                // 使用GridLayoutManager时，此类型的 item 是否是满跨度
                return true
            }

        }).onItemViewType { position, list -> // 根据数据，返回对应的 ItemViewType

           if (TextUtils.isEmpty(list[position].envelopePic)) {
                Appconfig.ARTICLE_TEXT_TYPE
            } else Appconfig.ARTICLE_PICTURE_TYPE
        }
    }


}