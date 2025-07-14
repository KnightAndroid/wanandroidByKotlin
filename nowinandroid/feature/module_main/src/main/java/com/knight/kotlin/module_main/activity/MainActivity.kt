package com.knight.kotlin.module_main.activity

import android.animation.ObjectAnimator
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import com.knight.kotlin.library_base.activity.BaseActivity
import com.core.library_base.annotation.EventBusRegister
import com.core.library_base.event.MessageEvent
import com.core.library_base.route.RouteActivity
import com.core.library_base.route.RouteFragment
import com.core.library_base.util.ActivityManagerUtils
import com.core.library_common.util.CacheUtils
import com.core.library_common.util.ColorUtils
import com.knight.kotlin.library_base.utils.StatusBarUtils
import com.knight.kotlin.library_util.ViewInitUtils
import com.knight.kotlin.library_util.toast
import com.knight.kotlin.module_main.R
import com.knight.kotlin.module_main.databinding.MainActivityBinding
import com.knight.kotlin.module_main.vm.MainViewModel
import com.wyjson.router.GoRouter
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@EventBusRegister
@AndroidEntryPoint
@Route(path = RouteActivity.Main.MainActivity)
class MainActivity : BaseActivity<MainActivityBinding, MainViewModel>() {
    private var mExitAppTime: Long = 0
    val fragments:MutableList<Fragment> = mutableListOf()
    private var outAnimator: ObjectAnimator? = null
    private var inAnimator: ObjectAnimator? = null
    override fun MainActivityBinding.initView() {
        initFragments()
        ViewInitUtils.setViewPager2Init(this@MainActivity,mainViewpager,fragments,
            isOffscreenPageLimit = true,
            isUserInputEnabled = false
        )
        btnNav.run {
            itemTextColor =  ColorUtils.createColorStateList(CacheUtils.getThemeColor(), ColorUtils.convertToColorInt("a6a6a6"))
            itemIconTintList = ColorUtils.createColorStateList(CacheUtils.getThemeColor(), ColorUtils.convertToColorInt("a6a6a6"))
           // setIconTintList(ColorUtils.createColorStateList(CacheUtils.getThemeColor(),ColorUtils.convertToColorInt("a6a6a6")))
            setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.homeFragment -> mainViewpager.setCurrentItem(0,false)
                    R.id.squareFragment -> mainViewpager.setCurrentItem(1,false)
                    R.id.projectFragment -> mainViewpager.setCurrentItem(2,false)
                    R.id.navigateFragment -> mainViewpager.setCurrentItem(3,false)
                    R.id.mineFragment -> mainViewpager.setCurrentItem(4,false)
                }
                true
            }

        }
    }

    override fun initObserver() {}

    override fun initRequestData() {}
    override fun reLoadData() {

    }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    private fun initFragments() {
        fragments.add(GoRouter.getInstance().build(RouteFragment.Home.HomeFragment).go()  as Fragment)
        fragments.add(GoRouter.getInstance().build(RouteFragment.Square.SquareFragment).go()  as Fragment)
        fragments.add(GoRouter.getInstance().build(RouteFragment.Project.ProjectFragment).go()  as Fragment)
        fragments.add(GoRouter.getInstance().build(RouteFragment.Navigate.NavigateHomeFragment).go()  as Fragment)
        fragments.add(GoRouter.getInstance().build(RouteFragment.Mine.MineFragment).go()  as Fragment)
    }


    override fun onKeyDown(keyCode:Int,event: KeyEvent):Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitAppTime <= 2000) {
                ActivityManagerUtils.getInstance()?.finishAllActivity()
                System.exit(0)
            } else {
                mExitAppTime = System.currentTimeMillis()
                toast(R.string.main_exit_tip)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        when (event.type) {
            MessageEvent.MessageType.RecreateMain -> {
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                if (fragments.get(0) != null) {
                    fragmentTransaction.remove(fragments.get(0))
                }

                if (fragments.get(1) != null) {
                    fragmentTransaction.remove(fragments.get(1))
                }

                if (fragments.get(2) != null) {
                    fragmentTransaction.remove(fragments.get(2))

                }

                if (fragments.get(3) != null) {
                    fragmentTransaction.remove(fragments.get(3))

                }
                fragmentTransaction.commitAllowingStateLoss()
                recreate()
            }

           MessageEvent.MessageType.EyeMode -> {
                openOrCloseEye(event.getBoolean())
           }
           MessageEvent.MessageType.OpenEyepetizer ->{
                if (event.getBoolean()) {

                    StatusBarUtils.transparentStatusBarWithFont(this,false)
                    bottomNavShowOrDimiss(true)
                } else {
                    StatusBarUtils.transparentStatusBarWithFont(this,true)
                    bottomNavShowOrDimiss(false)
                }

           }
            else -> {}
        }
    }

    /**
     * 动画显示隐藏
     *
     *
     * @param show
     */
    private fun bottomNavShowOrDimiss(show:Boolean) {
        if (show) { // 上滑显示
            outAnimator?.let {
                if (!it.isRunning && mBinding.btnNav.translationY <= 0) {
                    it.start()
                }
            } ?:run{
                outAnimator =
                    ObjectAnimator.ofFloat(mBinding.btnNav, "translationY", 0f, mBinding.btnNav.height.toFloat())
                outAnimator?.setDuration(200)
                outAnimator?.start()
            }


        } else  { // 下滑隐藏
            inAnimator?.let {
                if (!it.isRunning && mBinding.btnNav.translationY >= mBinding.btnNav.height) {
                    it.start()
                }
            } ?: run {
                inAnimator =
                    ObjectAnimator.ofFloat(mBinding.btnNav, "translationY", mBinding.btnNav.height.toFloat(), 0f)
                inAnimator?.setDuration(200)
                inAnimator?.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        fragments.clear()
    }
}