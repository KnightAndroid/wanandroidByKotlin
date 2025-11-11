package github.leavesczy.robustwebview.loading

import android.content.Context
import android.view.View
import com.knight.kotlin.module_web.widget.AppProgressLoadingView


/**
 * @author created by luguian
 * @organize
 * @Date 2025/11/7 15:30
 * @descript:
 */
class ProgressLoadingConfigImpl : LoadingViewConfig {
    private var mAppProgressLoadingView: AppProgressLoadingView? = null
    private var isShowLoading: Boolean = false
    override fun isShowLoading(): Boolean {
        return isShowLoading
    }

    override fun getLoadingView(context: Context): View? {
        initProgressLoadingView(context)
        isShowLoading = true
        return mAppProgressLoadingView

    }


    override fun hideLoading() {
        isShowLoading = false
        mAppProgressLoadingView?.visibility = View.GONE
    }

    override fun showLoading(context: Context?) {
        initProgressLoadingView(context)
        isShowLoading = true
        mAppProgressLoadingView?.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        mAppProgressLoadingView = null
    }

    private fun initProgressLoadingView(context: Context?) {
        if (mAppProgressLoadingView == null && context != null) {
            mAppProgressLoadingView = AppProgressLoadingView(context)
        }
    }
}