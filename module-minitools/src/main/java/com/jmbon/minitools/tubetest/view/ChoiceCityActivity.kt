package com.jmbon.minitools.tubetest.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.AppBaseActivity
import com.blankj.utilcode.util.ActivityUtils
import com.jmbon.middleware.utils.init
import com.jmbon.minitools.R
import com.jmbon.minitools.base.util.MiniMenuUtils
import com.jmbon.minitools.base.util.ParamsConstant
import com.jmbon.minitools.databinding.ActivityChoiceCityBinding
import com.jmbon.minitools.tubetest.adapter.CityItemAdapter
import com.jmbon.minitools.tubetest.adapter.ProvinceItemAdapter
import com.jmbon.minitools.tubetest.util.TubeArouterConstant
import com.jmbon.minitools.tubetest.util.TubeConstant
import com.jmbon.widget.recyclerview.CenterLayoutManager


@Route(path = TubeArouterConstant.TUBE_CHOICE_CITY, name = "选择城市页面")
class ChoiceCityActivity : AppBaseActivity<ActivityChoiceCityBinding>() {

    @Autowired(name = ParamsConstant.PROVIINCE)
    @JvmField
    var province: Int = 0

    @Autowired(name = ParamsConstant.CITY)
    @JvmField
    var city: Int = -1

    val provinceItemAdapter by lazy { ProvinceItemAdapter() }
    val cityItemAdapter by lazy { CityItemAdapter() }


    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {

        titleBarView.setRightView(R.layout.layout_minitool_right)
        setTitleName(getString(R.string.choice_city))
        initListener()

        binding.recycleProvince.init(
            provinceItemAdapter,
            layoutManager = CenterLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        )
        binding.recycleCity.init(cityItemAdapter)


        provinceItemAdapter.selectedId = province
        cityItemAdapter.selectedId = city

        provinceItemAdapter.setNewInstance(TubeConstant.baseInfoBean.citys.china.children)


        if (province != 0) {
            //binding.recycleProvince.scrollToPosition(province)
            (binding.recycleProvince.layoutManager as CenterLayoutManager).smoothScrollToPosition(
                binding.recycleProvince,
                RecyclerView.State(),
                province
            )

            cityItemAdapter.setNewInstance(TubeConstant.baseInfoBean.citys.china.children[province].children)

        } else {
            cityItemAdapter.setNewInstance(TubeConstant.baseInfoBean.citys.china.children[0].children)

        }
        if (city != -1) {
            binding.recycleCity.scrollToPosition(city)
        }

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



        provinceItemAdapter.setOnItemClickListener { adapter, view, pos ->
            provinceItemAdapter.selectedId = pos
            cityItemAdapter.selectedId = -1
            cityItemAdapter.setNewInstance(provinceItemAdapter.getItem(pos).children)

            provinceItemAdapter.notifyDataSetChanged()
        }
        cityItemAdapter.setOnItemClickListener { adapter, view, pos ->
            cityItemAdapter.selectedId = pos
            cityItemAdapter.notifyDataSetChanged()
            val intent = Intent()
            intent.putExtra(ParamsConstant.ADDRESS, cityItemAdapter.getItem(pos).title)
            intent.putExtra(ParamsConstant.CITY, pos)
            intent.putExtra(ParamsConstant.CITY_ID, cityItemAdapter.getItem(pos).id)
            intent.putExtra(ParamsConstant.PROVIINCE, provinceItemAdapter.selectedId)
            setResult(101, intent)

            finish()

        }

    }


    override fun initData() {
    }

    override fun getData() {
    }

}