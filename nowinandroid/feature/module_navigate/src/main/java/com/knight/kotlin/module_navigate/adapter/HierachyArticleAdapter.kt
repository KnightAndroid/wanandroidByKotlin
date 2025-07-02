package com.knight.kotlin.module_navigate.adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.core.library_base.databinding.BaseArticleItemBinding
import com.core.library_base.databinding.BaseTextItemBinding
import com.core.library_base.ktx.toHtml
import com.knight.kotlin.library_base.utils.CacheUtils
import com.knight.kotlin.library_base.config.Appconfig
import com.knight.kotlin.library_base.config.EyeTypeConstants
import com.knight.kotlin.library_util.StringUtils
import com.knight.kotlin.library_util.image.ImageLoader
import com.knight.kotlin.module_navigate.entity.HierachyTabArticleEntity

/**
 * Author:Knight
 * Time:2022/5/6 16:55
 * Description:HierachyArticleAdapter
 */
class HierachyArticleAdapter(data:List<HierachyTabArticleEntity>):
    BaseMultiItemAdapter<HierachyTabArticleEntity>(data) {



    // 标题 的 viewholder
    class HierachyTextVH(val viewBinding: BaseTextItemBinding) : RecyclerView.ViewHolder(viewBinding.root)

    // 正文视频 的 viewholder
    class HierachyPictureVH(val viewBinding: BaseArticleItemBinding) : RecyclerView.ViewHolder(viewBinding.root)


    // 在 init 初始化的时候，添加多类型
    init {
        addItemType(EyeTypeConstants.TEXT_TYPE, object : OnMultiItemAdapterListener<HierachyTabArticleEntity, HierachyTextVH> { // 类型 1
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): HierachyTextVH {
                // 创建 viewholder
                val viewBinding = BaseTextItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return HierachyTextVH(viewBinding)
            }

            override fun onBind(holder: HierachyTextVH, position: Int, item: HierachyTabArticleEntity?) {
                // 绑定 item 数据
                val binding = BaseTextItemBinding.bind(holder.itemView)
                item?.run {
                    //作者
                    if (!TextUtils.isEmpty(author)) {
                        binding.baseItemArticleAuthor.setText(StringUtils.getStyle(context,author, Appconfig.search_keyword))
                    } else {
                        binding.baseItemArticleAuthor.setText(StringUtils.getStyle(context,shareUser, Appconfig.search_keyword))
                    }

                    //一级分类
                    if (!TextUtils.isEmpty(superChapterName) || !TextUtils.isEmpty(chapterName)) {
                        val gradientDrawable = GradientDrawable()
                        gradientDrawable.shape = GradientDrawable.RECTANGLE
                        gradientDrawable.setStroke(2, CacheUtils.getThemeColor())
                        binding.baseTvArticleSuperchaptername.visibility = View.VISIBLE
                        if (!TextUtils.isEmpty(superChapterName)) {
                            if (!TextUtils.isEmpty(chapterName)) {
                                binding.baseTvArticleSuperchaptername.setText(superChapterName + "/" + chapterName)
                            } else {
                                binding.baseTvArticleSuperchaptername.setText(superChapterName)
                            }
                        } else {
                            if (!TextUtils.isEmpty(chapterName)) {
                                binding.baseTvArticleSuperchaptername.setText(chapterName)
                            } else {
                                binding.baseTvArticleSuperchaptername.setText("")
                            }
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            binding.baseTvArticleSuperchaptername
                                .setBackground(gradientDrawable)
                        } else {
                            binding.baseTvArticleSuperchaptername
                                .setBackgroundDrawable(gradientDrawable)
                        }
                    } else {
                        binding.baseTvArticleSuperchaptername.visibility = View.GONE
                    }
                    //时间赋值
                    if (!TextUtils.isEmpty(niceDate)) {
                        binding.baseItemArticledate.setText(niceDate)
                    } else {
                        binding.baseItemArticledate.setText(niceShareDate)
                    }
                    //标题
                    binding.baseTvArticletitle.setText(StringUtils.getStyle(context, title.toHtml().toString(), Appconfig.search_keyword))
                    //是否收藏
                    if (collect) {
                        binding.baseIconCollect.setBackgroundResource( com.core.library_base.R.drawable.base_icon_collect)
                    } else {
                        binding.baseIconCollect.setBackgroundResource(com.core.library_base.R.drawable.base_icon_nocollect)
                    }

                }
            }
        }).addItemType(EyeTypeConstants.IMAGE_TYPE, object : OnMultiItemAdapterListener<HierachyTabArticleEntity, HierachyPictureVH> { // 类型 2
            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int):HierachyPictureVH {
                // 创建 viewholder
                val viewBinding = BaseArticleItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return HierachyPictureVH(viewBinding)
            }

            override fun onBind(holder: HierachyPictureVH, position: Int, item: HierachyTabArticleEntity?) {
                // 绑定 item 数据
                val binding = BaseArticleItemBinding.bind(holder.itemView)
                item?.run {
                    //项目图片
                    ImageLoader.loadStringPhoto(context,envelopePic,binding.baseItemImageview)

                    //作者
                    if (!TextUtils.isEmpty(author)) {
                        binding.baseItemTvAuthor.setText(StringUtils.getStyle(context,author, Appconfig.search_keyword))
                    } else {
                        binding.baseItemTvAuthor.setText(StringUtils.getStyle(context,shareUser, Appconfig.search_keyword))
                    }
                    //时间赋值
                    if (!TextUtils.isEmpty(niceDate)) {
                        binding.baseItemTvTime.setText(niceDate)
                    } else {
                        binding.baseItemTvTime.setText(niceShareDate)
                    }
                    //标题
                    binding.baseTvTitle.setText(StringUtils.getStyle(context,title.toHtml().toString(), Appconfig.search_keyword))

                    //描述
                    if (!TextUtils.isEmpty(desc)) {
                        binding.baseTvProjectDesc.visibility = View.VISIBLE
                        binding.baseTvProjectDesc.setText(StringUtils.getStyle(context,desc.toHtml().toString(),
                            Appconfig.search_keyword))
                    } else {
                        binding.baseTvProjectDesc.visibility = View.GONE
                    }

                    //分类
                    if (!TextUtils.isEmpty(superChapterName)) {
                        binding.baseTvSuperchapter.visibility = View.VISIBLE
                        binding.baseTvSuperchapter.setText(superChapterName)
                    } else {
                        binding.baseTvSuperchapter.visibility = View.GONE
                    }

                    //是否收藏
                    if (collect) {
                        binding.baseArticleCollect.setBackgroundResource(com.core.library_base.R.drawable.base_icon_collect)
                    } else {
                        binding.baseArticleCollect.setBackgroundResource(com.core.library_base.R.drawable.base_icon_nocollect)
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