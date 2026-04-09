package com.knight.kotlin.module_realtime.dialog


import android.os.Build
import android.text.Html
import android.view.Gravity
import com.core.library_base.contact.EmptyContract
import com.core.library_base.vm.EmptyMviViewModel
import com.knight.kotlin.library_base.fragment.BaseMviDialogFragment
import com.knight.kotlin.module_realtime.databinding.RealtimeRankRuleDialogBinding
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2025/4/3 15:01
 * @descript:
 */
@AndroidEntryPoint
class RealTimeRankRuleFragment : BaseMviDialogFragment<
        RealtimeRankRuleDialogBinding,
        EmptyMviViewModel,
        EmptyContract.Event,
        EmptyContract.State,
        EmptyContract.Effect>() {

    override fun getGravity() = Gravity.BOTTOM

    override fun cancelOnTouchOutSide() = true
    override fun renderState(state: EmptyContract.State) {

    }

    override fun handleEffect(effect: EmptyContract.Effect) {

    }


    override fun RealtimeRankRuleDialogBinding.initView() {
        tvRealtimeRankRule.text = buildHtml().fromHtml()
    }

    override fun initObserver() {

    }

    override fun initRequestData() {

    }

    override fun reLoadData() {

    }

    /**
     * HTML 内容单独抽离，避免污染 UI 逻辑
     */
    private fun buildHtml(): String = """
        <b>一、榜单介绍</b><br><br>
        百度热搜民生榜是百度推出的反映社会新闻的榜单。榜单以数亿用户海量的真实数据为基础，通过专业的数据挖掘方法，计算各热点事件的热搜指数，综合反应热点事件在百度平台的热度。<br><br>
        
        <b>二、上榜规则</b><br><br>
        上榜的热点事件基于各事件在百度平台上的热度排序得出。<br><br>
        
        <b>三、更新规则</b><br><br>
        百度热搜民生榜每5分钟进行更新。<br><br>
        
        <b>四、计分规则</b><br><br>
        热搜指数结合搜索指数、资讯指数及各榜单领域特有数据，通过科学的分析与计算，得到的可反映热度变化的值。<br><br>
        
        1、搜索指数：搜索指数以用户在百度的搜索量为数据基础，以关键词为统计对象，将各个关键词在百度平台搜索中的搜索频次加权求和、指数化处理后得出。<br><br>
        
        2、资讯指数：资讯指数以百度智能分发和推荐内容数据为基础，将用户的阅读、评论、转发、点赞、不喜欢等行为的数量加权求和、指数化处理后得出。<br><br>
        
        3、互动指数：互动指数以用户对某具体事件的评论、转发、点赞量为基础，对互动行为数量加权求和、指数化处理后得出。<br><br>
        
        <b>五、反馈入口</b><br><br>
        百度热搜旨在建立权威、全面、热门、时效的各类关键词排行榜，欢迎广大用户积极反馈，您的建议是我们不断改进产品的不竭动力。<br><br>
        
        反馈邮箱 ext_baidu-top@baidu.com
    """.trimIndent()

    /**
     * Html 解析扩展（推荐全局复用）
     */
    private fun String.fromHtml() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            Html.fromHtml(this)
        }
}