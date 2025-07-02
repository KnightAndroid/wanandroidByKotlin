package com.knight.kotlin.module_home.activity


import androidx.annotation.OptIn
import androidx.fragment.app.FragmentTransaction
import androidx.media3.common.util.UnstableApi
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.route.RouteActivity
import com.core.library_base.vm.EmptyViewModel
import com.knight.kotlin.library_util.Mp3PlayerUtils
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.databinding.HomeActivityBinding
import com.knight.kotlin.module_home.fragment.HomeFragment
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.Home.HomeAloneMainActivity)
class HomeActivity : BaseActivity<HomeActivityBinding, EmptyViewModel>() {



    private val mHomeFragment by lazy { HomeFragment() }
    override fun setThemeColor(isDarkMode: Boolean) {

    }



    override fun initObserver() {

    }

    override fun initRequestData() {

    }



    override fun reLoadData() {

    }

    override fun HomeActivityBinding.initView() {
        //  val fragment: MyFragment = MyFragment()
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.home_ll, mHomeFragment)
        transaction.commit()
    }


    @OptIn(UnstableApi::class)
    override fun onDestroy() {
        super.onDestroy()
        //释放资源
        Mp3PlayerUtils.getInstance().release()
    }
}