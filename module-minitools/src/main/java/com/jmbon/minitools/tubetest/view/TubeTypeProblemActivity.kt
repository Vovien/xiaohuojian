package com.jmbon.minitools.tubetest.view

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.utils.*
import com.jmbon.minitools.R
import com.jmbon.minitools.databinding.ActivityTubeTypeProblemBinding
import com.jmbon.minitools.tubetest.util.ArouterUtil
import com.jmbon.minitools.tubetest.util.TubeArouterConstant
import com.jmbon.minitools.tubetest.util.TubeConstant
import com.jmbon.minitools.base.util.MiniMenuUtils
import com.jmbon.minitools.tubetest.viewmodel.PregnantTipsViewModel
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.lxj.xpopup.XPopup


@Route(path = TubeArouterConstant.TUBE_TYPE_PROBLEM, name = "试管类型问题页面")
class TubeTypeProblemActivity :
    ViewModelActivity<PregnantTipsViewModel, ActivityTubeTypeProblemBinding>() {
    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)

    }

    override fun initView(savedInstanceState: Bundle?) {
        titleBarView.setRightView(R.layout.layout_minitool_right)

        initListener()
    }

    private fun initListener() {


        titleBarView.rightCustomView.findViewById<View>(R.id.iv_more).setOnClickListener {
            MiniMenuUtils.showMenuDialog(
                this,
                TubeConstant.miniApp
            )
        }

        titleBarView.rightCustomView.findViewById<View>(R.id.iv_close).setOnClickListener {
            TubeConstant.lastActivity?.let {
                ActivityUtils.finishToActivity(it, true)
            }
        }


        binding.rlTubeType.setOnSingleClickListener({
            showTubeTypeDialog()
        })
        binding.rlTubeMethod.setOnSingleClickListener({
            showTubeMethodDialog()
        })



        binding.btnSure.setOnClickListener {

            TubeConstant.userInfoBean.tubetest_type = "${binding.tvType.tag as Int}"
            TubeConstant.userInfoBean.programme = "${binding.tvMethod.tag as Int}"

            ArouterUtil.toActivity(TubeArouterConstant.TUBE_TRY_PROBLEM)


        }
    }

    override fun initData() {
    }

    override fun getData() {


    }

    override fun onDestroy() {
        super.onDestroy()
    }


    fun checkChoiceComplete() {
        if (binding.tvType.text.isNotNullEmpty() && binding.tvMethod.text.isNotNullEmpty()) {
            binding.btnSure.isEnabled = true
        } else {
            binding.btnSure.isEnabled = false

        }
    }


    /**
     * 试管类型
     */
    private fun showTubeTypeDialog() {
        val listData = arrayListOf<MultiItemEntity>()

        TubeConstant.baseInfoBean.tubetestTypes.forEach {
            listData.add(
                CustomDialogTypeBean(
                    it.title,
                    2
                )
            )
        }


        listData.add(
            CustomDialogTypeBean(
                resources.getString(com.jmbon.widget.R.string.currency_cancle),
                4
            )
        )
        XPopup.Builder(this)
            .isDestroyOnDismiss(true)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .autoDismiss(true)
            .enableDrag(false)
            .asCustom(
                CustomListBottomDialog(this, listData) {
                    if (it < TubeConstant.baseInfoBean.tubetestTypes.size) {
                        binding.tvType.text =
                            TubeConstant.baseInfoBean.tubetestTypes[it].title
                        binding.tvType.tag =
                            TubeConstant.baseInfoBean.tubetestTypes[it].id

                        checkChoiceComplete()
                    }
                }
            ).show()
    }

    /**
     * 试管促排方案
     */
    private fun showTubeMethodDialog() {
        val listData = arrayListOf<MultiItemEntity>()

        TubeConstant.baseInfoBean.programmes.forEach {
            listData.add(
                CustomDialogTypeBean(
                    it.title,
                    2
                )
            )
        }


        listData.add(
            CustomDialogTypeBean(
                resources.getString(com.jmbon.widget.R.string.currency_cancle),
                4
            )
        )
        XPopup.Builder(this)
            .isDestroyOnDismiss(true)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .autoDismiss(true)
            .enableDrag(false)
            .asCustom(
                CustomListBottomDialog(this, listData) {
                    if (it < TubeConstant.baseInfoBean.programmes.size) {
                        binding.tvMethod.text =
                            TubeConstant.baseInfoBean.programmes[it].title
                        binding.tvMethod.tag =
                            TubeConstant.baseInfoBean.programmes[it].id

                        checkChoiceComplete()
                    }
                }
            ).show()
    }


}