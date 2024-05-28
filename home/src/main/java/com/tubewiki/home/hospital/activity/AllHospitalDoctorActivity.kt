package com.tubewiki.home.hospital.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.bean.ResultThreeData
import com.apkdv.mvvmfast.ktx.launch
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.showToast
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.finish
import com.jmbon.middleware.utils.init
import com.tubewiki.home.router.WikiRouter
import com.tubewiki.home.R
import com.tubewiki.home.bean.hospital.bean.HospitalDetailBean
import com.tubewiki.home.databinding.ActivityAllHospitalDoctorBinding
import com.tubewiki.home.doctor.viewmodel.DoctorListViewModel
import com.tubewiki.home.hospital.adpater.HospitalDoctorAdapter
import kotlinx.coroutines.flow.collectLatest


/**
 * 全部医院医生
 */
@Route(path = "/hospital/activity/all_hospital_doctor")
class AllHospitalDoctorActivity :
    ViewModelActivity<DoctorListViewModel, ActivityAllHospitalDoctorBinding>() {

    @Autowired(name = TagConstant.HOSPITAL_ID)
    @JvmField
    var hospitalId = 0


    private val localHospitalAdapter by lazy {
        HospitalDoctorAdapter()
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
        viewModel.setHospitalId(hospitalId)
    }

    override fun initView(savedInstanceState: Bundle?) {
        setTitleName(getString(R.string.all_hospital_doctor))
        initStateLayout(binding.stateLayout)

        binding.smartRefresh.setOnLoadMoreListener {
            getDoctorList()
        }

        binding.recyclerView.apply {
            init(
                localHospitalAdapter,
                layoutManager = LinearLayoutManager(
                    this@AllHospitalDoctorActivity,
                    RecyclerView.VERTICAL,
                    false
                )

            )
        }

        localHospitalAdapter.setOnItemClickListener { adapter, view, pos ->
            WikiRouter.toDoctorDetailsActivity(localHospitalAdapter.getItem(pos).doctorId)
        }

    }

    private fun getDoctorList() {
        launch {
            viewModel.hospitalDoctorListFlow.netCatch {
                message.showToast()
            }.collectLatest(::setDoctorAdapter)
        }
    }

    private fun setDoctorAdapter(list: ResultThreeData<MutableList<HospitalDetailBean.Doctor>, Boolean, Boolean>) {
        if (list.data3) {
            binding.smartRefresh.finishLoadMoreWithNoMoreData()
        }
        binding.smartRefresh.finish()
        if (list.data2) {
            localHospitalAdapter.setNewInstance(list.data1)
        } else {
            localHospitalAdapter.addData(list.data1)
        }
    }

    override fun getData() {
        super.getData()
        getDoctorList()
    }


    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        viewModel.setPageInit()
        getData()
    }


}