package com.jmbon.minitools.tubetest.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.isVisible
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.minitools.R
import com.jmbon.minitools.base.util.MiniMenuUtils
import com.jmbon.minitools.base.util.ParamsConstant
import com.jmbon.minitools.databinding.ActivityTryTubeProblemBinding
import com.jmbon.minitools.tubetest.util.ArouterUtil
import com.jmbon.minitools.tubetest.util.TubeArouterConstant
import com.jmbon.minitools.tubetest.util.TubeConstant
import com.jmbon.minitools.tubetest.viewmodel.PregnantTipsViewModel
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.lxj.xpopup.XPopup


@Route(path = TubeArouterConstant.TUBE_TRY_PROBLEM, name = "尝试做试管问题页面")
class TryTubeProblemActivity :
    ViewModelActivity<PregnantTipsViewModel, ActivityTryTubeProblemBinding>() {


    var city = -1
    var province = 0
    var cityId = 0

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


        binding.rlCity.setOnSingleClickListener({
            //选择城市
            ARouter.getInstance().build(TubeArouterConstant.TUBE_CHOICE_CITY)
                .withInt(ParamsConstant.PROVIINCE, province).withInt(ParamsConstant.CITY, city)
                .navigation(this, 100)
        })
        binding.rlTubeWant.setOnSingleClickListener({
            showTubeWantDialog()
        })
        binding.rlTubeWantCity.setOnSingleClickListener({
            showTubeWantCityDialog()
        })
        binding.rlTubeCost.setOnSingleClickListener({
            showTubeWantCostDialog()
        })

        binding.btnSure.setOnClickListener {

            TubeConstant.userInfoBean.city_id = "${binding.tvCity.tag as Int}"
            TubeConstant.userInfoBean.consider_tube = "${binding.tvTubeWant.tag as Int}"
            TubeConstant.userInfoBean.tube_city =
                "${if (binding.tvWantCity.tag != null) binding.tvWantCity.tag as Int else 0}"
            TubeConstant.userInfoBean.tube_cost =
                "${if (binding.tvWantCost.tag != null) binding.tvWantCost.tag as Int else 0}"


            if (binding.tvTubeWant.tag != null && binding.tvTubeWant.tag as Int == 2) {
                //没有意愿，直接预测
                ArouterUtil.toActivity(TubeArouterConstant.TUBE_CALCULATE_RESULT)
            } else {
                //有意愿
                ArouterUtil.toActivity(TubeArouterConstant.TUBE_SPECIAL_PROBLEM)

            }


        }
    }

    override fun initData() {
    }

    override fun getData() {


    }


    fun checkChoiceComplete() {

        if (binding.groupWant.isVisible()) {
            if (binding.tvCity.text.isNotNullEmpty() && binding.tvTubeWant.text.isNotNullEmpty() &&
                binding.tvWantCity.text.isNotNullEmpty() && binding.tvWantCost.text.isNotNullEmpty()
            ) {
                binding.btnSure.isEnabled = true
            } else {
                binding.btnSure.isEnabled = false
            }
        } else {
            if (binding.tvCity.text.isNotNullEmpty() && binding.tvTubeWant.text.isNotNullEmpty()) {
                binding.btnSure.isEnabled = true
            } else {
                binding.btnSure.isEnabled = false

            }
        }

    }


    /**
     * 意愿城市
     */
    private fun showTubeWantCityDialog() {
        val listData = arrayListOf<MultiItemEntity>()
        val array = resources.getStringArray(com.jmbon.minitools.R.array.tube_want_city)
        array.forEach {
            listData.add(
                CustomDialogTypeBean(
                    it,
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
                    if (it < array.size) {
                        binding.tvWantCity.text =
                            array[it]

                        // 0:未选择，1：省内，2：国内，3：海外
                        binding.tvWantCity.tag =
                            it + 1

                        checkChoiceComplete()
                    }
                }
            ).show()
    }

    /**
     * 意愿花费
     */
    private fun showTubeWantCostDialog() {
        val listData = arrayListOf<MultiItemEntity>()
        val array = resources.getStringArray(com.jmbon.minitools.R.array.tube_want_cost)
        array.forEach {
            listData.add(
                CustomDialogTypeBean(
                    it,
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
                    if (it < array.size) {
                        binding.tvWantCost.text =
                            array[it]
                        //0：没有费用，1:5万以下，2:10万以下，3:20万以下，4:30万以下，5:30万以上
                        binding.tvWantCost.tag =
                            it + 1

                        checkChoiceComplete()
                    }
                }
            ).show()
    }


    /**
     *试管 是否有意愿
     */
    private fun showTubeWantDialog() {
        val listData = arrayListOf<MultiItemEntity>()

        val array = resources.getStringArray(com.jmbon.minitools.R.array.tube_want)
        array.forEach {
            listData.add(
                CustomDialogTypeBean(
                    it,
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
                    if (it < array.size) {
                        binding.tvTubeWant.text = array[it]
                        //1：有意愿，2：没有意愿，3：考虑中
                        binding.tvTubeWant.tag = it + 1

                        if (it != 1) {
                            binding.groupWant.visible()
                            binding.btnSure.text = "下一步"
                        } else {
                            binding.groupWant.gone()
                            binding.btnSure.text = "马上预测"
                        }

                        checkChoiceComplete()
                    }
                }
            ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            data?.let {
                binding.tvCity.text = it.getStringExtra(ParamsConstant.ADDRESS)
                binding.tvCity.tag = it.getIntExtra(ParamsConstant.CITY_ID, -1)
                city = it.getIntExtra(ParamsConstant.CITY, -1)
                province = it.getIntExtra(ParamsConstant.PROVIINCE, 0)
            }
        }

    }

}