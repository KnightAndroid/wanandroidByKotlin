package com.knight.kotlin.module_home.activity


import androidx.fragment.app.FragmentTransaction
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.library_base.vm.EmptyViewModel
import com.knight.kotlin.module_home.R
import com.knight.kotlin.module_home.databinding.HomeActivityBinding
import com.knight.kotlin.module_home.fragment.HomeFragment
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@Route(path = RouteActivity.Home.HomeAloneMainActivity)
class HomeActivity : BaseActivity<HomeActivityBinding,EmptyViewModel>() {



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
}