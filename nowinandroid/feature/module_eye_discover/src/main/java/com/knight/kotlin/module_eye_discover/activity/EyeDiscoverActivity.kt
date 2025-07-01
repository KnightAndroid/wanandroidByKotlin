package com.knight.kotlin.module_eye_discover.activity

import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import com.core.library_base.route.RouteActivity
import com.google.android.material.search.SearchView
import com.core.library_base.activity.BaseActivity
import com.core.library_base.ktx.hide
import com.core.library_base.ktx.showFragment
import com.knight.kotlin.module_eye_discover.R
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverActivityBinding
import com.knight.kotlin.module_eye_discover.fragment.EyeDiscoverScollListFragment
import com.knight.kotlin.module_eye_discover.fragment.EyeDiscoverSearchRecommendFragment
import com.knight.kotlin.module_eye_discover.fragment.EyeDiscoverSearchResultFragment
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverScrollListVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/5 17:53
 * @descript:开眼发现首界面
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeDiscover.EyeDiscoverActivity)
class EyeDiscoverActivity : BaseActivity<EyeDiscoverActivityBinding, EyeDiscoverScrollListVm>(){


   val scrollListFragment = EyeDiscoverScollListFragment()



    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverActivityBinding.initView() {

        eyeDiscoverSearchBar.setBackgroundResource(R.drawable.eye_discover_bg_search_shape)
        eyeDiscoverSearchBar.navigationIcon = ContextCompat.getDrawable(this@EyeDiscoverActivity, com.knight.kotlin.library_base.R.drawable.base_iv_left_arrow)
        eyeDiscoverSearchBar.setNavigationOnClickListener {
            finish()
        }
        loadPage()
        setSearchListener()
    }

    /**
     *
     * 加载首页
     */
    private fun loadPage() {
        supportFragmentManager.beginTransaction().replace(R.id.eye_discover_container, scrollListFragment)
            .commitAllowingStateLoss()
    }


    private fun setSearchListener() {


        mBinding.eyeDiscoverSearchView
            .editText
            .setOnEditorActionListener { _, _, event: KeyEvent? ->
                if (event?.keyCode == KEYCODE_ENTER) {
                    mBinding.eyeDiscoverSearchView.clearFocusAndHideKeyboard()
                    //提交进行搜索
                    //uerySearchViewModel.submitSearchQuery(searchView.text.toString())

                    showResultFragment(mBinding.eyeDiscoverSearchView.text.toString())

                }
                false
            }

        mBinding.eyeDiscoverSearchView.editText.doAfterTextChanged {

            //querySearchViewModel.submitPreSearchQuery(it.toString())
        }

        val onBackPressedCallback: OnBackPressedCallback =
            object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    mBinding.eyeDiscoverSearchView.hide()
                }
            }
       onBackPressedDispatcher.addCallback(this@EyeDiscoverActivity, onBackPressedCallback)


        mBinding.eyeDiscoverSearchView.addTransitionListener { _, _, newState: SearchView.TransitionState ->
            onBackPressedCallback.isEnabled =
                newState == SearchView.TransitionState.SHOWN
            if (newState == SearchView.TransitionState.SHOWN) {
                supportFragmentManager.showFragment<EyeDiscoverSearchRecommendFragment>(R.id.search_view_container)
            } else {
                supportFragmentManager.hide(R.id.search_view_container)
                supportFragmentManager.hide(R.id.search_view_result_container)
            }
        }
    }




    fun showResultFragment(query:String) {
        supportFragmentManager.showFragment<EyeDiscoverSearchResultFragment>(
            R.id.search_view_result_container,
            args = bundleOf("query" to query)
        )

    }








}