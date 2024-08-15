package com.knight.kotlin.module_eye_discover.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseMultiItemAdapter
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_util.startPageWithAnimate
import com.knight.kotlin.library_widget.RecyclerItemDecoration
import com.knight.kotlin.module_eye_discover.R
import com.knight.kotlin.module_eye_discover.constants.DiscoverItemType
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverBriefCardItemBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverCategoryItemBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSubTitleItemBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverSubjectItemBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverTopBannerItemBinding
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverVideoCardItemBinding
import com.knight.kotlin.module_eye_discover.entity.BaseEyeDiscoverEntity
import com.knight.kotlin.module_eye_discover.entity.EyeCategoryCardEntity
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverBriefCardEntity
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverTopBannerEntity
import com.knight.kotlin.module_eye_discover.entity.EyeDiscoverVideoSmallCardEntity
import com.knight.kotlin.module_eye_discover.entity.EyeSubTitleEntity
import com.knight.kotlin.module_eye_discover.entity.EyeSubjectCardEntity
import com.knight.kotlin.module_eye_discover.entity.SquareCard
import com.youth.banner.indicator.CircleIndicator


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/8 10:00
 * @descript:首页发现适配器
 */
class EyeDiscoverAdapter(activity : FragmentActivity, data:List<BaseEyeDiscoverEntity>) : BaseMultiItemAdapter<BaseEyeDiscoverEntity>(data) {

    //类型 1 广告 viewHolder
    class EyeDiscoverTopBannerVH(viewBinding : EyeDiscoverTopBannerItemBinding) : RecyclerView.ViewHolder(viewBinding.root)
    //类型 2 副标题 viewHolder
    class EyeDiscoverSubTitleVH(viewBinding : EyeDiscoverSubTitleItemBinding) : RecyclerView.ViewHolder(viewBinding.root)
    //类型 3 热门分类 viewHolder
    class EyeDiscoverCategoryVH(viewBinding: EyeDiscoverCategoryItemBinding) : RecyclerView.ViewHolder(viewBinding.root)
    //类型 4
    class EyeDiscoverSubjectVH(viewBinding:EyeDiscoverSubjectItemBinding) : RecyclerView.ViewHolder(viewBinding.root)
    //类型 5
    class EyeDiscoverVideoSmallVH(viewBinding:EyeDiscoverVideoCardItemBinding) : RecyclerView.ViewHolder(viewBinding.root)
    //类型 6
    class EyeDiscoverBriefVH(viewBinding : EyeDiscoverBriefCardItemBinding) :   RecyclerView.ViewHolder(viewBinding.root)
    init {
        addItemType(DiscoverItemType.TOPBANNERVIEW,object:OnMultiItemAdapterListener<BaseEyeDiscoverEntity,EyeDiscoverTopBannerVH>{
            override fun onBind(holder: EyeDiscoverTopBannerVH, position: Int, item: BaseEyeDiscoverEntity?) {
                // 绑定 item 数据
                val binding = DataBindingUtil.getBinding<EyeDiscoverTopBannerItemBinding>(holder.itemView)
                binding?.viewModel = (item as EyeDiscoverTopBannerEntity).data
                binding?.discoverTopBanner?.addBannerLifecycleObserver(activity)?.indicator = CircleIndicator(activity)
            }

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): EyeDiscoverTopBannerVH {
                //创建 viewholder
                val viewBinding = EyeDiscoverTopBannerItemBinding.inflate(LayoutInflater.from(context), parent, false)
                return EyeDiscoverTopBannerVH(viewBinding)
            }
        }).addItemType(DiscoverItemType.CATEGORYVIEW,object : OnMultiItemAdapterListener<BaseEyeDiscoverEntity,EyeDiscoverCategoryVH>{
            override fun onBind(holder: EyeDiscoverCategoryVH, position: Int, item: BaseEyeDiscoverEntity?) {
                val binding = DataBindingUtil.getBinding<EyeDiscoverCategoryItemBinding>(holder.itemView)
                val cardBean = item as EyeCategoryCardEntity
                binding?.run {
                    tvTitle.setText(cardBean.data.header.title)
                    tvActionTitle
                        .setText(cardBean.data.header.rightText)
                    val dataList: ArrayList<SquareCard> = ArrayList<SquareCard>()
                    for (i in 0 until cardBean.data.itemList.size) {
                        dataList.add(cardBean.data.itemList.get(i).data)
                    }
                    val adapter = EyeDiscoverSubCategoryAdapter()
                    adapter.run {
                        setOnItemClickListener { adapter, view, position ->
                            startPageWithAnimate(activity, RouteActivity.EyeDiscover.EyeCategoryDetailActivity,view,activity.getString(R.string.eye_discover_share_element_container),
                                "id" to dataList[position].id,
                                "name" to dataList[position].title.replace("#",""),
                                "headImage" to dataList[position].image)
                        }
                    }
                    binding.rvCategoryView.adapter = adapter
                    adapter.submitList(dataList)
                }

            }

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): EyeDiscoverCategoryVH {
                //创建 viewholder
                val viewBinding =  EyeDiscoverCategoryItemBinding.inflate(LayoutInflater.from(context), parent, false)

                viewBinding.rvCategoryView.setHasFixedSize(true)
                val gridLayoutManager =
                    GridLayoutManager(activity, 2)
                gridLayoutManager.orientation = RecyclerView.HORIZONTAL
                viewBinding.rvCategoryView.setLayoutManager(gridLayoutManager)
                viewBinding.rvCategoryView.addItemDecoration(
                    RecyclerItemDecoration(
                        0,
                        0, 5.dp2px(), 0
                    )
                )


                return EyeDiscoverCategoryVH(viewBinding)
            }

        })
            .addItemType(DiscoverItemType.SUBJECTVIEW,object : OnMultiItemAdapterListener<BaseEyeDiscoverEntity,EyeDiscoverSubjectVH>{
            override fun onBind(holder: EyeDiscoverSubjectVH, position: Int, item: BaseEyeDiscoverEntity?) {
                val binding = DataBindingUtil.getBinding<EyeDiscoverSubjectItemBinding>(holder.itemView)
                val cardBean = item as EyeSubjectCardEntity
                binding?.run {
                    tvTitle.setText(cardBean.data.header.title)
                    tvActionTitle
                        .setText(cardBean.data.header.rightText)
                    val dataList: ArrayList<SquareCard> = ArrayList<SquareCard>()
                    for (i in 0 until cardBean.data.itemList.size) {
                        dataList.add(cardBean.data.itemList.get(i).data)
                    }
                    val adapter = EyeDiscoverSubSubjectAdapter()
                    binding.rvSubjectView.adapter = adapter
                    adapter.submitList(dataList)
                }
            }

            override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): EyeDiscoverSubjectVH {
                //创建 viewholder
                val viewBinding =  EyeDiscoverSubjectItemBinding.inflate(LayoutInflater.from(context), parent, false)
                viewBinding.rvSubjectView.setHasFixedSize(true)
                val gridLayoutManager =
                    GridLayoutManager(activity, 2)
                viewBinding.rvSubjectView.setLayoutManager(gridLayoutManager)
                viewBinding.rvSubjectView.addItemDecoration(
                    RecyclerItemDecoration(
                        0,
                        0, 5.dp2px(), 5.dp2px()
                    )
                )
                return EyeDiscoverSubjectVH(viewBinding)
            }
        }).addItemType(DiscoverItemType.SUBTITLEVIEW,object : OnMultiItemAdapterListener<BaseEyeDiscoverEntity,EyeDiscoverSubTitleVH>{
                override fun onBind(holder: EyeDiscoverSubTitleVH, position: Int, item: BaseEyeDiscoverEntity?) {
                  val binding = DataBindingUtil.getBinding<EyeDiscoverSubTitleItemBinding>(holder.itemView)
                    binding?.viewModel = (item as EyeSubTitleEntity).data
                }

                override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): EyeDiscoverSubTitleVH {
                    //创建 viewholder
                    val viewBinding =  EyeDiscoverSubTitleItemBinding.inflate(LayoutInflater.from(context), parent, false)
                    return EyeDiscoverSubTitleVH(viewBinding)
                }
            }).addItemType(DiscoverItemType.VIDEOVIEW,object : OnMultiItemAdapterListener<BaseEyeDiscoverEntity,EyeDiscoverVideoSmallVH> {
                override fun onBind(holder: EyeDiscoverVideoSmallVH, position: Int, item: BaseEyeDiscoverEntity?) {
                    val binding = DataBindingUtil.getBinding<EyeDiscoverVideoCardItemBinding>(holder.itemView)
                    binding?.viewModel = (item as EyeDiscoverVideoSmallCardEntity).data
                }

                override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): EyeDiscoverVideoSmallVH {
                    //创建 viewholder
                    val viewBinding =  EyeDiscoverVideoCardItemBinding.inflate(LayoutInflater.from(context), parent, false)
                    return EyeDiscoverVideoSmallVH(viewBinding)
                }

            }).addItemType(DiscoverItemType.BRIEFVIEW,object : OnMultiItemAdapterListener<BaseEyeDiscoverEntity,EyeDiscoverBriefVH>{
                override fun onBind(holder: EyeDiscoverBriefVH, position: Int, item: BaseEyeDiscoverEntity?) {
                    val binding = DataBindingUtil.getBinding<EyeDiscoverBriefCardItemBinding>(holder.itemView)
                    binding?.viewModel = (item as EyeDiscoverBriefCardEntity).data
                }

                override fun onCreate(context: Context, parent: ViewGroup, viewType: Int): EyeDiscoverBriefVH {
                    //创建 viewholder
                    val viewBinding =  EyeDiscoverBriefCardItemBinding.inflate(LayoutInflater.from(context), parent, false)
                    return EyeDiscoverBriefVH(viewBinding)
                }
            }).onItemViewType { position, list -> // 根据数据，返回对应的 ItemViewType
            if (list[position] is EyeDiscoverTopBannerEntity) {
                DiscoverItemType.TOPBANNERVIEW
            } else if (list[position] is EyeCategoryCardEntity) {
                DiscoverItemType.CATEGORYVIEW
            } else if (list[position] is EyeSubjectCardEntity)  {
                DiscoverItemType.SUBJECTVIEW
            } else if (list[position] is EyeDiscoverVideoSmallCardEntity) {
                DiscoverItemType.VIDEOVIEW
            } else if (list[position] is EyeDiscoverBriefCardEntity) {
                DiscoverItemType.BRIEFVIEW
            } else {
                DiscoverItemType.SUBTITLEVIEW
            }

        }
    }
}