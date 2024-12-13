package com.knight.kotlin.module_eye_discover.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter4.BaseQuickAdapter
import com.knight.kotlin.module_eye_discover.adapter.EyeDiscoverCategoryDetailAdapter.VH
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverRecommendSearchVideoItemBinding


/**
 * @author created by luguian
 * @organize
 * @Date 2024/12/13 14:27
 * @descript:推荐搜索适配器
 */
class EyeDIscoverSearchRecommendAdapter : BaseQuickAdapter<String,EyeDIscoverSearchRecommendAdapter.VH>() {

  class VH(
      parent:ViewGroup,
      val binding:EyeDiscoverRecommendSearchVideoItemBinding = EyeDiscoverRecommendSearchVideoItemBinding.inflate(
          LayoutInflater.from(parent.context), parent, false
      ),
  ) : RecyclerView.ViewHolder(binding.root)




    override fun onBindViewHolder(holder: VH, position: Int, item: String?) {

    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): VH {
        return VH(parent)
    }
}