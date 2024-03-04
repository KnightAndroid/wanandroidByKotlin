package com.knight.kotlin.module_vedio.activity
import androidx.recyclerview.widget.GridLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.observeLiveData
import com.knight.kotlin.library_base.ktx.setOnClick
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.util.CacheUtils
import com.knight.kotlin.library_base.util.ColorUtils
import com.knight.kotlin.library_base.util.dp2px
import com.knight.kotlin.library_widget.SpacesItemDecoration
import com.knight.kotlin.library_widget.ktx.init
import com.knight.kotlin.module_vedio.adapter.VedioMainAdapter
import com.knight.kotlin.module_vedio.databinding.VedioMainActivityBinding
import com.knight.kotlin.module_vedio.entity.VedioListEntity
import com.knight.kotlin.module_vedio.utils.VedioCryUtils
import com.knight.kotlin.module_vedio.vm.VedioVm
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@Route(path = RouteActivity.Vedio.VedioMainActivity)
class VedioMainActivity : BaseActivity<VedioMainActivityBinding,VedioVm>(), OnRefreshListener,
    OnLoadMoreListener {

    //视频列表适配器
    private val mVedioMainAdapter:VedioMainAdapter by lazy { VedioMainAdapter(mutableListOf())}


    //由于部分视频播放不了 因此需要特别处理
    private val vedios = mutableListOf<VedioListEntity>()
    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {
        observeLiveData(mViewModel.vedios, ::getDouyinVedios)
    }

    override fun initRequestData() {
        mViewModel.getDouyinVedios()
    }

    override fun reLoadData() {

    }

    override fun VedioMainActivityBinding.initView() {
        includeVedioToolbar.baseTvTitle.text = getString(com.knight.kotlin.module_vedio.R.string.vedio_main_toolbar)
        includeVedioToolbar.baseIvBack.setOnClick {
            finish()
        }
        requestLoading(includeVedio.baseFreshlayout)
        includeVedio.baseFreshlayout.setOnRefreshListener(this@VedioMainActivity)
        includeVedio.baseFreshlayout.setOnLoadMoreListener(this@VedioMainActivity)
        includeVedio.baseBodyRv.init(
            GridLayoutManager(this@VedioMainActivity,
            2,
        ),mVedioMainAdapter,true)
        includeVedio.baseBodyRv.addItemDecoration(SpacesItemDecoration(10.dp2px()))
        vedioFloatBtn.backgroundTintList = ColorUtils.createColorStateList(CacheUtils.getThemeColor(), CacheUtils.getThemeColor())
    }


    private fun getDouyinVedios(data: MutableList<VedioListEntity>) {
        requestSuccess()
        vedios.addAll(data)
        mBinding.includeVedio.baseFreshlayout.finishLoadMore()
        mBinding.includeVedio.baseFreshlayout.finishRefresh()

        for (i in vedios.size - 1 downTo 0) {
            vedios[i].joke.videoUrl = VedioCryUtils.removePrefixToDecry( vedios[i].joke.videoUrl)
            if (vedios[i].joke.videoUrl.contains("flag=null")) {
                vedios.removeAt(i)
            }
        }
        if (vedios.size < 10) {
            mViewModel.getDouyinVedios()
        } else {
            mVedioMainAdapter.setNewInstance(vedios)
        }


    }

    override fun onRefresh(refreshLayout: RefreshLayout) {

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {

    }

}



