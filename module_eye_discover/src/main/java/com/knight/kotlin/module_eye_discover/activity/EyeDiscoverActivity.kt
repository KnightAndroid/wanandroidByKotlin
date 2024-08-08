package com.knight.kotlin.module_eye_discover.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.knight.kotlin.library_base.activity.BaseActivity
import com.knight.kotlin.library_base.ktx.init
import com.knight.kotlin.library_base.route.RouteActivity
import com.knight.kotlin.module_eye_discover.adapter.EyeDiscoverAdapter
import com.knight.kotlin.module_eye_discover.databinding.EyeDiscoverActivityBinding
import com.knight.kotlin.module_eye_discover.vm.EyeDiscoverVm
import com.wyjson.router.annotation.Route
import dagger.hilt.android.AndroidEntryPoint


/**
 * @author created by luguian
 * @organize
 * @Date 2024/8/5 17:53
 * @descript:开眼发现
 */
@AndroidEntryPoint
@Route(path = RouteActivity.EyeDiscover.EyeDiscoverActivity)
class EyeDiscoverActivity : BaseActivity<EyeDiscoverActivityBinding, EyeDiscoverVm>(){



    val sd = "{\"type\":\"videoSmallCard\",\"data\":{\"dataType\":\"VideoBeanForClient\",\"id\":196406,\"title\":\"Vlog：17位名人重启2020\",\"description\":\"我们邀请17位名人和来自全球20个国家的20位插画艺术家，共同发起了一项特别计划：“重启2020”。 \\n今年对于全世界来说，最大的心愿就是能按下重启键，一切重新开始。6月8日，让我们一起走入那个原本应该有的2020年。 \u200B\",\"library\":\"DEFAULT\",\"tags\":[{\"id\":678,\"name\":\"明星\",\"actionUrl\":\"eyepetizer://tag/678/?title=%E6%98%8E%E6%98%9F\",\"adTrack\":null,\"desc\":null,\"bgPicture\":\"http://img.kaiyanapp.com/1ba2b63b0e51c55c0a488f2c206c7770.jpeg?imageMogr2/quality/60/format/jpg\",\"headerImage\":\"http://img.kaiyanapp.com/45f5ee90bc22990d5bab85b9287b7c09.jpeg?imageMogr2/quality/60/format/jpg\",\"tagRecType\":\"NORMAL\",\"childTagList\":null,\"childTagIdList\":null,\"haveReward\":false,\"ifNewest\":false,\"newestEndTime\":null,\"communityIndex\":0},{\"id\":2,\"name\":\"创意\",\"actionUrl\":\"eyepetizer://tag/2/?title=%E5%88%9B%E6%84%8F\",\"adTrack\":null,\"desc\":\"技术与审美结合，探索视觉的无限可能\",\"bgPicture\":\"http://img.kaiyanapp.com/1b457058cf2b317304ff9f70543c040d.png?imageMogr2/quality/60/format/jpg\",\"headerImage\":\"http://img.kaiyanapp.com/fdefdb34cbe3d2ac9964d306febe9025.jpeg?imageMogr2/quality/100\",\"tagRecType\":\"NORMAL\",\"childTagList\":null,\"childTagIdList\":null,\"haveReward\":false,\"ifNewest\":false,\"newestEndTime\":null,\"communityIndex\":0}],\"consumption\":{\"collectionCount\":2581,\"shareCount\":1670,\"replyCount\":33,\"realCollectionCount\":3258},\"resourceType\":\"video\",\"slogan\":null,\"provider\":{\"name\":\"PGC\",\"alias\":\"PGC\",\"icon\":\"\"},\"category\":\"记录\",\"author\":{\"id\":398,\"icon\":\"http://ali-img.kaiyanapp.com/f4f2b2205861a02174d3b15eaa00214e.png?imageMogr2/quality/60/format/jpg\",\"name\":\"新世相\",\"description\":\"我们终将改变潮水的方向。\",\"link\":\"\",\"latestReleaseTime\":1721384209000,\"videoNum\":271,\"adTrack\":null,\"follow\":{\"itemType\":\"author\",\"itemId\":398,\"followed\":false},\"shield\":{\"itemType\":\"author\",\"itemId\":398,\"shielded\":false},\"approvedNotReadyVideoCount\":0,\"ifPgc\":true,\"recSort\":0,\"expert\":false},\"cover\":{\"feed\":\"http://ali-img.kaiyanapp.com/ac2715a02bfa47f9aeeaaccb81baec93.png?imageMogr2/quality/60/format/jpg\",\"detail\":\"http://ali-img.kaiyanapp.com/ac2715a02bfa47f9aeeaaccb81baec93.png?imageMogr2/quality/60/format/jpg\",\"blurred\":\"http://ali-img.kaiyanapp.com/6bf79aca08c15e30b391426fa0927bcf.jpeg?imageMogr2/quality/60/format/jpg\",\"sharing\":null,\"homepage\":null},\"playUrl\":\"http://baobab.kaiyanapp.com/api/v1/playUrl?vid=196406&resourceType=video&editionType=default&source=aliyun&playUrlType=url_oss&udid=fa53872206ed42e3857755c2756ab683f22d64a\",\"thumbPlayUrl\":null,\"duration\":234,\"webUrl\":{\"raw\":\"http://www.eyepetizer.net/detail.html?vid=196406\",\"forWeibo\":\"http://www.eyepetizer.net/detail.html?vid=196406&resourceType=video&utm_campaign=routine&utm_medium=share&utm_source=weibo&uid=0\"},\"releaseTime\":1591607114000,\"playInfo\":[],\"campaign\":null,\"waterMarks\":null,\"ad\":false,\"adTrack\":[],\"type\":\"NORMAL\",\"titlePgc\":\"Vlog：17位名人重启2020\",\"descriptionPgc\":\"我们邀请17位名人和来自全球20个国家的20位插画艺术家，共同发起了一项特别计划：“重启2020”。 \\n今年对于全世界来说，最大的心愿就是能按下重启键，一切重新开始。6月8日，让我们一起走入那个原本应该有的2020年。 \u200B\",\"remark\":\"\",\"ifLimitVideo\":false,\"searchWeight\":0,\"brandWebsiteInfo\":null,\"videoPosterBean\":{\"scale\":0.725,\"url\":\"http://eyepetizer-videos.oss-cn-beijing.aliyuncs.com/video_poster_share/3bcb23fe3d5575e226a847ab6a9b1a44.mp4\",\"fileSizeStr\":\"2.34MB\"},\"idx\":0,\"shareAdTrack\":null,\"favoriteAdTrack\":null,\"webAdTrack\":null,\"date\":1591607114000,\"promotion\":null,\"label\":null,\"labelList\":[],\"descriptionEditor\":\"\",\"collected\":false,\"reallyCollected\":false,\"played\":false,\"subtitles\":[],\"lastViewTime\":null,\"playlists\":null,\"src\":null,\"recallSource\":null,\"recall_source\":null},\"trackingData\":null,\"tag\":null,\"id\":0,\"adIndex\":-1}"
    //发现适配器
    private val mEyeDiscoverAdapter: EyeDiscoverAdapter by lazy { EyeDiscoverAdapter(
        this,mutableListOf()) }

    override fun setThemeColor(isDarkMode: Boolean) {

    }

    override fun initObserver() {

    }

    override fun initRequestData() {
       // val sdsdf  : EyeDiscoverVideoSmallCardEntity = fromJson(sd)
       // LogUtils.d("sds"+sdsdf.type)
        mViewModel.getDiscoverData().observerKt {
            mEyeDiscoverAdapter.submitList(it)
        }
    }

    override fun reLoadData() {

    }

    override fun EyeDiscoverActivityBinding.initView() {
        rvDiscoverList.init(
            LinearLayoutManager(this@EyeDiscoverActivity),
            mEyeDiscoverAdapter,
            true
        )
    }
}