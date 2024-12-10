package com.knight.kotlin.module_eye_discover.activity

import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ENTER
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import com.google.android.material.search.SearchView
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.module_eye_discover.R
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverActivityBinding
import com.knight.kotlin.module_eye_discover.fragment.EyeDiscoverRecommendFragment
import com.knight.kotlin.module_eye_discover.fragment.EyeDiscoverScollListFragment
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

   val recommendFragment = EyeDiscoverRecommendFragment()
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
                switchFragment(recommendFragment)
            //    supportFragmentManager.beginTransaction().show(recommendFragment)
//                supportFragmentManager.showFragment<EyeDiscoverRecommendFragment>(
//                    R.id.search_view_container
//                )
            } else {
              //  supportFragmentManager.beginTransaction().show(recommendFragment)
                //supportFragmentManager.beginTransaction().hide(recommendFragment)
                switchFragment(scrollListFragment)
//                supportFragmentManager.beginTransaction().hide()
//                childFragmentManager.hide(R.id.search_view_container)
             //   childFragmentManager.hide(R.id.search_view_result_container)
            }
        }
    }




    fun switchFragment(newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()

        // 获取当前显示的 Fragment
        val currentFragment = supportFragmentManager.fragments.find { it.isVisible }

        if (currentFragment != null && currentFragment != newFragment) {
            transaction.hide(currentFragment) // 隐藏当前 Fragment
        }

        if (!newFragment.isAdded) {
            transaction.add(R.id.search_view_container, newFragment, newFragment.tag) // 添加新 Fragment
        } else {
            transaction.show(newFragment) // 显示已添加的 Fragment
        }

        transaction.commit()
    }








}