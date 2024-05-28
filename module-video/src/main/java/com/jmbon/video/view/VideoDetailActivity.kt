package com.jmbon.video.view

import android.os.Bundle
import android.util.SparseArray
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.resumed
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.gyf.immersionbar.ImmersionBar
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.bean.event.FocusChangedEvent
import com.jmbon.middleware.bean.event.VideoCollectEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.video.databinding.ActivityVideoDetailBinding
import com.jmbon.video.util.NetWatchdog
import com.jmbon.video.util.getAvailablePlayAddress
import com.jmbon.video.viewmodel.VideoDetailsViewModel
import com.qmuiteam.qmui.kotlin.onClick
import org.greenrobot.eventbus.EventBus
import java.lang.ref.WeakReference
import java.util.UUID

@Route(path = "/video/details/activity")
class VideoDetailActivity : ViewModelActivity<VideoDetailsViewModel, ActivityVideoDetailBinding>() {

    @JvmField
    @Autowired(name = TagConstant.VIDEO_ID)
    var videoId: Int = 18151

    private val mListPlayerView by lazy {
        binding.listPlayerView
    }

    private val mNetWatchDog by lazy { NetWatchdog(this) }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ImmersionBar.with(this@VideoDetailActivity).transparentStatusBar()
            .statusBarDarkFont(true).init()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {
        initListener()
        val statusHeight =
            StatusBarCompat.getStatusBarHeight(this@VideoDetailActivity) + binding.titleBarLayout.paddingTop
        binding.titleBarLayout.setPadding(
            binding.titleBarLayout.paddingStart,
            statusHeight,
            binding.titleBarLayout.paddingEnd,
            binding.titleBarLayout.paddingBottom
        )

        binding.ivBack.onClick {
            finish()
        }
        //详情不用刷新
        mListPlayerView.setRefreshEnable(false)
        //详情不用去监听Event
        mListPlayerView.setIsEvent(false)
        mNetWatchDog.startWatch()
    }

    private fun initListener() {
        mListPlayerView.setOnRefreshDataListener(MyOnRefreshListener(this))
        mNetWatchDog.setNetChangeListener(object : NetWatchdog.NetChangeListener {
            override fun onWifiTo4G() {
                if (Constant.trafficTips.not()) {
                    "当前非wifi状态\n请注意流量消耗".showToast()
                    Constant.trafficTips = true
                }
            }

            override fun on4GToWifi() {
            }

            override fun onNetDisconnected() {
                "请检查网络连接状态".showToast()
            }

        })
    }

    private class MyOnRefreshListener(videoDetailNewFragment: VideoDetailActivity) :
        AliyunListPlayerView.OnRefreshDataListener {
        private val weakReference: WeakReference<VideoDetailActivity>

        init {
            weakReference =
                WeakReference(
                    videoDetailNewFragment
                )
        }

        override fun onRefresh() = Unit

        override fun onLoadMore() = Unit

        override fun onCollection(video: VideoDetail.VideoData) {
            val videoDetailActivity: VideoDetailActivity? = weakReference.get()
            videoDetailActivity?.collect(video)
        }
    }

    private fun collect(video: VideoDetail.VideoData) {
        resumed {
            if (video.collectStatus == 1) {
                viewModel.collect("add", videoId = video.videoId).next {
                    EventBus.getDefault().post(VideoCollectEvent(true, video.videoId))
                    "收藏成功".showToast()
                }
            } else {
                viewModel.collect("del", videoId = video.videoId).next {
                    EventBus.getDefault().post(VideoCollectEvent(false, video.videoId))
                    "取消收藏".showToast()
                }
            }
        }
    }


    override fun getData() {
        resumed {
            showLoading(false)
        }
        videoDetail()
    }

    private fun videoDetail() {
        resumed {
            viewModel.getVideoDetail(videoId).netCatch {
                "获取视频详情失败".showToast()
                dismissLoading()
            }.next {
                if (video != null) {
                    mListPlayerView.removeAllUrl()
                    val mSparseArray: SparseArray<String> = SparseArray()
                    mListPlayerView.setData(listOf(video!!))
                    //遍历资源,添加到列表播放器当中
                    val randomUUID = UUID.randomUUID().toString()
                    mListPlayerView.addUrl(
                        video!!.getAvailablePlayAddress(),
                        randomUUID
                    )
                    mSparseArray.put(0, randomUUID)
                    dismissLoading()
                    mListPlayerView.correlationTable = mSparseArray
                }

            }
        }
    }

    override fun onPause() {
        super.onPause()
        mListPlayerView.setOnBackground(true)
    }

    override fun onResume() {
        super.onResume()
        mListPlayerView.setOnBackground(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        mListPlayerView.destroy()
        mNetWatchDog.stopWatch()
        System.gc()
    }
}