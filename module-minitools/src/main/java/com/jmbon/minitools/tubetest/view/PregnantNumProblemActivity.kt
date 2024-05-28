package com.jmbon.minitools.tubetest.view

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.minitools.R
import com.jmbon.minitools.base.util.MiniMenuUtils
import com.jmbon.minitools.databinding.ActivityPregnantNumProblemBinding
import com.jmbon.minitools.tubetest.util.ArouterUtil
import com.jmbon.minitools.tubetest.util.TubeArouterConstant
import com.jmbon.minitools.tubetest.util.TubeConstant
import com.jmbon.minitools.tubetest.viewmodel.PregnantTipsViewModel
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.lxj.xpopup.XPopup


@Route(path = TubeArouterConstant.TUBE_PREG_NUM_PROBLEM, name = "怀孕次数问题页面")
class PregnantNumProblemActivity :
    ViewModelActivity<PregnantTipsViewModel, ActivityPregnantNumProblemBinding>() {
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


        binding.rlPregnantNum.setOnSingleClickListener({
            showPregnantNumDialog()
        })
        binding.rlBabyNum.setOnSingleClickListener({
            showBabyNumDialog()
        })
        binding.rlTubeNum.setOnSingleClickListener({
            showTubeNumDialog()
        })

        binding.btnSure.setOnClickListener {

            TubeConstant.userInfoBean.birth_number = "${binding.tvBabyNum.tag as Int}"
            TubeConstant.userInfoBean.pregnancy_number = "${binding.tvPregnantNum.tag as Int}"
            TubeConstant.userInfoBean.tubetest_number = "${binding.tvTubeNum.tag as Int}"
            // 如果选择的已尝试的试管次数为0, 则直接跳过询问上次试管技术页面
            if (TubeConstant.baseInfoBean.tubetestNumbers.find { it1 -> it1.id == binding.tvTubeNum.tag as? Int }?.selectValue == 0) {
                // 如果是返回后重新选择, 则需要将上次选择清空
                TubeConstant.userInfoBean.tubetest_type = ""
                TubeConstant.userInfoBean.programme = ""
                ArouterUtil.toActivity(TubeArouterConstant.TUBE_TRY_PROBLEM)
            } else {
                ArouterUtil.toActivity(TubeArouterConstant.TUBE_TYPE_PROBLEM)
            }
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
        if (binding.tvBabyNum.text.isNotNullEmpty() && binding.tvPregnantNum.text.isNotNullEmpty() && binding.tvTubeNum.text.isNotNullEmpty()) {
            binding.btnSure.isEnabled = true
        } else {
            binding.btnSure.isEnabled = false

        }
    }


    /**
     * 怀孕次数
     */
    private fun showPregnantNumDialog() {
        val listData = arrayListOf<MultiItemEntity>()

        TubeConstant.baseInfoBean.pregnancyNumbers.forEach {
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
                    if (it < TubeConstant.baseInfoBean.pregnancyNumbers.size) {
                        binding.tvPregnantNum.text =
                            TubeConstant.baseInfoBean.pregnancyNumbers[it].title
                        binding.tvPregnantNum.tag =
                            TubeConstant.baseInfoBean.pregnancyNumbers[it].id

                        checkChoiceComplete()
                    }
                }
            ).show()
    }

    /**
     * 宝宝个数
     */
    private fun showBabyNumDialog() {
        val listData = arrayListOf<MultiItemEntity>()

        TubeConstant.baseInfoBean.birthNumbers.forEach {
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
                    if (it < TubeConstant.baseInfoBean.birthNumbers.size) {
                        binding.tvBabyNum.text =
                            TubeConstant.baseInfoBean.birthNumbers[it].title
                        binding.tvBabyNum.tag =
                            TubeConstant.baseInfoBean.birthNumbers[it].id

                        checkChoiceComplete()
                    }
                }
            ).show()
    }

    /**
     * 试管次数
     */
    private fun showTubeNumDialog() {
        val listData = arrayListOf<MultiItemEntity>()

        TubeConstant.baseInfoBean.tubetestNumbers.forEach {
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
                    if (it < TubeConstant.baseInfoBean.tubetestNumbers.size) {
                        binding.tvTubeNum.text =
                            TubeConstant.baseInfoBean.tubetestNumbers[it].title
                        binding.tvTubeNum.tag =
                            TubeConstant.baseInfoBean.tubetestNumbers[it].id

                        checkChoiceComplete()
                    }
                }
            ).show()
    }

}