package github.leavesczy.robustwebview.loading

import android.content.Context
import android.view.View


/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 15:24
 * @descript:
 */
interface LoadingViewConfig {

    fun isShowLoading(): Boolean
    fun getLoadingView(context: Context): View?
    fun hideLoading()
    fun showLoading(context: Context?)
    fun setProgress(progress: Int){

    }
    fun onDestroy()
}