package com.knight.kotlin.library_widget.slidinglayout

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import androidx.core.view.ViewCompat
import kotlin.math.abs


/**
 * @author created by luguian
 * @organize
 * @Date 2025/2/19 14:30
 * @descript:
 */
class SlidingLayout @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    HorizontalScrollView(context, attrs, defStyleAttr) {
    //获取屏幕宽
    var screenWidth: Int = resources.displayMetrics.widthPixels
    var menuWidth: Int = screenWidth - 150

    //默认缩放比例
    var minScale: Float = 0.7f

    //默认透明度比例
    var minAlpha: Float = 0.3f

    private var menuView: ViewGroup? = null
    private var contentView: ViewGroup? = null

    //快速滑动处理
    private val gestureDetector: GestureDetector

    private var menuViewIsOpen = false

    private var isIntercept = false

    init {
        //        gestureDetector=new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
//            @Override
//            public boolean onDown(MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public void onShowPress(MotionEvent e) {
//
//            }
//
//            @Override
//            public boolean onSingleTapUp(MotionEvent e) {
//                return false;
//            }
//
//            @Override
//            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//                return false;
//            }
//
//            @Override
//            public void onLongPress(MotionEvent e) {
//
//            }
//
//            @Override
//            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//                return false;
//            }
//        });
        gestureDetector = GestureDetector(getContext(),
            object : SimpleOnGestureListener() {
                override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                    //快速滑动
                    if (menuViewIsOpen) {
                        //关闭menu 往左边快速滑动 velocityX 负数
                        if (velocityX < 0 && abs(velocityX.toDouble()) > abs(velocityY.toDouble())) {
                            closeMenu()
                            return true
                        }
                    } else {
                        //打开menu 往右快速滑动 velocityX 正数
                        if (velocityX > 0 && abs(velocityX.toDouble()) > abs(velocityY.toDouble())) {
                            openMenu()
                            return true
                        }
                    }

                    return super.onFling(e1, e2, velocityX, velocityY)
                }
            })
    }

    /*

    LayoutInflater -
        void rInflate(XmlPullParser parser, View parent, Context context,
                AttributeSet attrs, boolean finishInflate){

                    if (finishInflate) {
                        parent.onFinishInflate();
                    }
                }
    */
    //布局解析完毕 调用 是在onLayout之前调用
    override fun onFinishInflate() {
        super.onFinishInflate()
        //获取根部局LinearLayout
        val linearLayout = getChildAt(0) as ViewGroup

        if (linearLayout.childCount > 2) {
            throw RuntimeException("KgSlidingMenu的子布局最多2个")
        }

        // include_menu 屏幕宽度-右边菜单缩放后的宽度
        menuView = linearLayout.getChildAt(0) as ViewGroup
        val menuParams = menuView!!.layoutParams
        menuParams.width = menuWidth
        menuView!!.layoutParams = menuParams

        // include_content 宽度=屏幕宽度
        contentView = linearLayout.getChildAt(1) as ViewGroup
        val contentParams = contentView!!.layoutParams
        contentParams.width = screenWidth
        contentView!!.layoutParams = contentParams

        //此处无效 因为这个方法是在 onLayout之前执行
        // onLayout方法执行又重新摆放了
        //初始化显示include_content布局
        //scrollTo(width,0);//移动一个屏幕的距离 显示include_content
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        if (!menuViewIsOpen) {
            scrollTo(screenWidth, 0)
        }

    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        if (isIntercept == true) {
            return true
        }

        //当手指滑动速率 大于某个速率 就认为是快速滑动
        //如果执行了快速滑动 下面代表不执行 避免冲突导致快速滑动无效
        if (gestureDetector.onTouchEvent(ev)) {
            return gestureDetector.onTouchEvent(ev)
        }

        when (ev.action) {
            MotionEvent.ACTION_UP -> {
                //初始化现实content的状态 x坐标是 = menuWidth
                //手指抬起移动的距离 大于 menuWidth的一半 显示右边 content
                if (scrollX > screenWidth / 2) {
                    closeMenu()
                } else {
                    //显示左边 menu
                    openMenu()
                }

                //不执行 super.onTouchEvent(ev);
                return true
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        //重置 防止改变状态后拦截onTouch
        isIntercept = false

        if (menuViewIsOpen) {
            //菜单打开的时候 触摸右边 关闭
            if (ev.x > menuWidth) {
                closeMenu()
                //拦截右边菜单事件 子view不响应触摸事件
                // 返回true 拦截子 view touch事件 会 响应自身的 onTouch事件
                isIntercept = true
                return true
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    fun openMenu() {
        // smoothScrollTo(); 有移动动画 但是没有移动
        // 需要onTouchEvent return true;
        // ScrollTo(); 没有动画 但是可以直接移动
        //smoothScrollTo(0, 0)
        smoothScrollToWithDuration(0,500)
        menuViewIsOpen = true
    }

    fun closeMenu() {
        //smoothScrollTo(screenWidth, 0)
        smoothScrollToWithDuration(screenWidth,500)
        menuViewIsOpen = false
    }

    // 左边缩放+透明度 右边缩放
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        //        Log.i("TAG"," l "+l+" t "+t+" oldl "+oldl+" oldt "+oldt);
        // 向右滑动 l和oldl不断减少 计算缩放比
        val scale = l.toFloat() / menuWidth // 1-0
        // menu全部显示的时候 scale=0 右边缩放minScale + 0
        // 右边全部显示的时候 scale=1 右边不需要缩放 刚好1 minScale + (1-minScale)
        // 1 - 0.7
        val rightScale = minScale + scale * (1 - minScale)
        //设置右边缩放 默认是中心点缩放
        rightScaleAnim(rightScale)
        //设置左边缩放 和右边相反 0.7 - 1
        val leftScale = minScale + (1 - scale) * (1 - minScale)
        leftScaleAnim(leftScale)
        // 0.3 - 1
        val leftAlpha = minAlpha + (1 - scale) * (1 - minAlpha)
        leftAlphaAnim(leftAlpha)

        //退出按钮刚  开始在右边，划出时出字的变化
        ViewCompat.setTranslationX(menuView, 0.17f * l)
    }

    private fun leftAlphaAnim(leftAlpha: Float) {
        ViewCompat.setAlpha(menuView, leftAlpha)
    }

    private fun leftScaleAnim(leftScale: Float) {
        ViewCompat.setScaleX(menuView, leftScale)
        ViewCompat.setScaleY(menuView, leftScale)
    }

    private fun rightScaleAnim(rightScale: Float) {
        ViewCompat.setScaleX(contentView, rightScale)
        ViewCompat.setScaleY(contentView, rightScale)

        //        AnimatorSet animatorSet= new AnimatorSet();
//        ObjectAnimator scaleX = ObjectAnimator.ofFloat
//                (contentView, "scaleX", rightScale);
//        ObjectAnimator scaleY = ObjectAnimator.ofFloat
//                (contentView, "scaleY", rightScale);
        contentView!!.pivotX = 0f
        contentView!!.pivotY = (contentView!!.measuredHeight / 2).toFloat()

        //        animatorSet.play(scaleX).with(scaleY);
    }



    fun smoothScrollToWithDuration(targetX: Int, duration: Int) {
        // 获取当前滚动位置
        val startX = scrollX

        // 创建一个 ValueAnimator 来平滑滚动
        val animator = ValueAnimator.ofInt(startX, targetX)
        animator.duration = duration.toLong() // 设置动画的时长（单位：毫秒）

        // 每一帧更新滚动位置
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            scrollTo(value, 0)  // 更新水平滚动位置
        }

        // 启动动画
        animator.start()
    }
}