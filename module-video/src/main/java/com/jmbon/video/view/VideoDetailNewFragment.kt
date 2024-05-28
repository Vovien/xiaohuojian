package com.jmbon.video.view

import android.util.SparseArray
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.resumed
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.bean.VideoDetail
import com.jmbon.middleware.bean.event.CommonEventHub
import com.jmbon.middleware.bean.event.FocusChangedEvent
import com.jmbon.middleware.bean.event.VideoCollectEvent
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.video.databinding.FragmentVideoDetailNewBinding
import com.jmbon.video.util.NetWatchdog
import com.jmbon.video.util.getAvailablePlayAddress
import com.jmbon.video.viewmodel.VideoDetailsViewModel
import kotlinx.coroutines.flow.onStart
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference
import java.util.UUID

@Route(path = "/video/details/new/fragment")
class VideoDetailNewFragment :
    ViewModelFragment<VideoDetailsViewModel, FragmentVideoDetailNewBinding>() {

    private val mListPlayerView by lazy {
        binding.listPlayerView
    }

    private val mNetWatchDog by lazy { NetWatchdog(context) }

    override fun beforeViewInit() {
        super.beforeViewInit()
        EventBus.getDefault().register(this)
    }

    override fun initView(view: View) {
        initListener()
        mNetWatchDog.startWatch()
    }

    private fun initListener() {
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
        mListPlayerView.setOnRefreshDataListener(MyOnRefreshListener(this))
    }

    override fun getData() {
        resumed {
            showLoading(whiteBackground = false)
        }
        pullVideoList(true)
    }

    private fun pullVideoList(isRefresh: Boolean) {
        resumed {
            viewModel.getAllVideoList().netCatch {
                "获取视频详情失败".showToast()
                dismissLoading()
                if (isRefresh) {
                    mListPlayerView.hideRefresh()
                }
            }.next {
                if (this.videoList.isNotNullEmpty()) {
                    val mSparseArray: SparseArray<String>
                    if (isRefresh) {
                        mListPlayerView.removeAllUrl()
                        mSparseArray = SparseArray()
                        mListPlayerView.setData(this.videoList)
                        //遍历资源,添加到列表播放器当中
                        val size = mSparseArray.size()
                        this.videoList.forEachIndexed { index, videoData ->
                            val randomUUID = UUID.randomUUID().toString()
                            mListPlayerView.addUrl(
                                videoData.getAvailablePlayAddress(),
                                randomUUID
                            )
                            mSparseArray.put(size + index, randomUUID)
                        }
                        dismissLoading()
                    } else {
                        mSparseArray = mListPlayerView.correlationTable
                        mListPlayerView.addMoreData(this.videoList)
                        val size = mSparseArray.size()
                        this.videoList.forEachIndexed { index, videoData ->
                            val randomUUID = UUID.randomUUID().toString()
                            mListPlayerView.addUrl(
                                videoData.getAvailablePlayAddress(),
                                randomUUID
                            )
                            mSparseArray.put(size + index, randomUUID)
                        }
                    }
                    mListPlayerView.correlationTable = mSparseArray
                }
            }
        }
    }

    override fun initViewModel() {
        super.initViewModel()
        ARouter.getInstance().inject(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun collectEvent(event: VideoCollectEvent) {
        mListPlayerView.collectEvent(event)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun commonEvent(event: CommonEventHub.VideoPageEvent) {
        if (event.index == 1) {
            if (isShowLoading()) {
                return
            }
            resumed {
                showLoading(whiteBackground = false)
            }
            pullVideoList(true)
        }
    }

    private class MyOnRefreshListener(videoDetailNewFragment: VideoDetailNewFragment) :
        AliyunListPlayerView.OnRefreshDataListener {
        private val weakReference: WeakReference<VideoDetailNewFragment>

        init {
            weakReference =
                WeakReference(
                    videoDetailNewFragment
                )
        }

        override fun onRefresh() {
            val videoDetailNewFragment: VideoDetailNewFragment? = weakReference.get()
            videoDetailNewFragment?.onRefresh()
        }

        override fun onLoadMore() {
            val videoDetailNewFragment: VideoDetailNewFragment? = weakReference.get()
            videoDetailNewFragment?.onLoadMore()
        }

        override fun onCollection(video: VideoDetail.VideoData) {
            val videoDetailNewFragment: VideoDetailNewFragment? = weakReference.get()
            videoDetailNewFragment?.collect(video)
        }

    }

    private fun collect(video: VideoDetail.VideoData) {
        resumed {
            if (video.collectStatus == 1) {
                viewModel.collect("add", videoId = video.videoId).next {
                    "收藏成功".showToast()
                }
            } else {
                viewModel.collect("del", videoId = video.videoId).next {
                    "取消收藏".showToast()
                }
            }
        }


    }


    private fun onRefresh() {
        pullVideoList(true)
    }

    private fun onLoadMore() {
        pullVideoList(false)
    }

    override fun onPause() {
        super.onPause()
        mListPlayerView.setOnBackground(true)
    }

    override fun onResume() {
        super.onResume()
        mListPlayerView.setOnBackground(false)
    }

    override fun onDestroyView() {
        mListPlayerView.destroy()
        mNetWatchDog.stopWatch()
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
        System.gc()
    }


}