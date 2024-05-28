package com.tubewiki.mine.view

import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelFragment
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.jmbon.middleware.adapter.ImageCommonBannerAdapter
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.bury.BuryHelper
import com.jmbon.middleware.bury.event.ClickEventEnum
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.tubewiki.mine.databinding.FragmentMineBinding
import com.tubewiki.mine.view.model.MineFragmentViewModel
import com.youth.banner.indicator.CircleIndicator
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 我的首页
 */
@Route(path = "/mine/main/fragment")
class MineFragment : ViewModelFragment<MineFragmentViewModel, FragmentMineBinding>() {
    private val statusHeight by lazy { StatusBarCompat.getStatusBarHeight(view?.context) }

    private val bannerAdapter by lazy {
        ImageCommonBannerAdapter {
            BuryHelper.addEvent(ClickEventEnum.EVENT_CLICK_MINE_BANNER.value)
        }
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        EventBus.getDefault().register(this)
    }

    override fun onDestroyView() {
        EventBus.getDefault().unregister(this)
        super.onDestroyView()
    }

    override fun initView(view: View) {
        binding.apply {
//            tvName.text = Constant.userInfo.userName
//            ivAvatar.loadCircle(Constant.userInfo.avatarFile)
//            ivBg.load(Constant.userInfo.background)

            rlSessionLog.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/conversationRecord").navigation()
            })
            rlSetting.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/setting/activity").navigation()
            })
            rlCollect.setOnSingleClickListener({
                ARouter.getInstance().build("/mine/setting/collect").navigation()
            })

//            jbEdit.setOnSingleClickListener({
//                ARouter.getInstance().build("/mine/edit/info").navigation()
//            })

            banner.apply {
                indicator = CircleIndicator(context)
                setAdapter(bannerAdapter, true)
            }
        }

    }

    override fun initViewModel() {
        super.initViewModel()
    }


    override fun onResume() {
        super.onResume()

        binding.apply {
            tvName.text = Constant.userInfo.userName
            ivAvatar.loadCircle(Constant.userInfo.avatarFile)
        }

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: UserLoginEvent) {

    }

}