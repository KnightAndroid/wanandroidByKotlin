package com.knight.kotlin.module_main.vm

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.knight.kotlin.library_base.route.RouteFragment
import com.knight.kotlin.library_base.vm.BaseViewModel
import com.knight.kotlin.module_main.repo.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Author:Knight
 * Time:2021/12/22 17:36
 * Description:MainViewModel
 * 首页VM层
 */
@HiltViewModel
class MainViewModel @Inject constructor(private val mRepository:MainRepository):BaseViewModel() {

    val fragments:MutableList<Fragment> = mutableListOf(
        ARouter.getInstance().build(RouteFragment.Home.HomeFragment).navigation()  as Fragment,
        ARouter.getInstance().build(RouteFragment.Square.SquareFragment).navigation()  as Fragment,
        ARouter.getInstance().build(RouteFragment.Project.ProjectFragment).navigation()  as Fragment,
        ARouter.getInstance().build(RouteFragment.Navigate.NavigateHomeFragment).navigation()  as Fragment,
        ARouter.getInstance().build(RouteFragment.Mine.MineFragment).navigation()  as Fragment
    )


}