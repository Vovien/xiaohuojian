package com.tubewiki.home.doctor.activity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Rect
import android.graphics.Typeface
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.core.view.setPadding
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.ktx.gone
import com.apkdv.mvvmfast.ktx.isVisible
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.resumed
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.ktx.visible
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.ScreenUtils
import com.blankj.utilcode.util.SizeUtils
import com.blankj.utilcode.util.ThreadUtils
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.bean.WebScrollOffset
import com.jmbon.middleware.js.JsImageLoad
import com.jmbon.middleware.utils.Color
import com.jmbon.middleware.utils.H5ArouterUtils
import com.jmbon.middleware.utils.HospitalDoctorHtmlTools
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.loadCircle
import com.jmbon.middleware.utils.logInToIntercept
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.middleware.valid.action.Action
import com.jmbon.minitools.base.util.MiniMenuUtils
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.jmbon.widget.html.X5WebView
import com.jmbon.widget.tablayout.listener.OnTabSelectListener
import com.tubewiki.home.router.WikiRouter
import com.luck.picture.lib.tools.DoubleUtils
import com.lxj.xpopup.XPopup
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.tubewiki.home.R
import com.tubewiki.home.bean.hospital.bean.DoctorDetailBean
import com.tubewiki.home.bean.hospital.bean.HospitalDetailBean
import com.tubewiki.home.databinding.ActivityDoctorDetailBinding
import com.tubewiki.home.dialog.HospitalDoctorTitleListDialog
import com.tubewiki.home.dialog.MoreDetailDialog
import com.tubewiki.home.hospital.adpater.HospitalDoctorAdapter
import com.tubewiki.home.hospital.viewmodel.HospitalDoctorViewModel
import com.tubewiki.home.util.DoctorConstant
import kotlinx.coroutines.delay
import kotlin.math.abs


/**
 * 医生主页
 */
@Route(path = "/hospital/activity/doctor_detail")
class DoctorDetailActivity :
    ViewModelActivity<HospitalDoctorViewModel, ActivityDoctorDetailBinding>() {
    @Autowired(name = TagConstant.DOCTOR_ID)
    @JvmField
    var doctorId = 0

    private var doctorDetails: DoctorDetailBean? = null

    private val hospitalDoctorAdapter by lazy {
        HospitalDoctorAdapter()
    }

    private val customCenterView by lazy {
        LayoutInflater.from(this@DoctorDetailActivity)
            .inflate(R.layout.title_doctor_center_layout, null)
    }

    private var x5WebView: X5WebView? = null
    var webScrollY = 0

    var titles = mutableListOf<String>()
    var titleTitleOffset = mutableListOf<WebScrollOffset>()
    var selectedPos = 0 //目录选择

    var offset = 44f.dp()

    override fun beforeViewInit() {
        super.beforeViewInit()
        ARouter.getInstance().inject(this)
    }


    override fun initView(savedInstanceState: Bundle?) {
        initPageState()

        initWebView()

        iniRecyclerView()

        initListener()

        if (DoctorConstant.lastActivity == null) {
            DoctorConstant.lastActivity = ActivityUtils.getTopActivity().javaClass
        }


    }

    private fun initTabView() {

        binding.commonTabLayout.setTitle(titles.toTypedArray())
        binding.commonTabLayout.setSnapOnTabClick(true)

        binding.commonTabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                selectedPos = position
                when (position) {
                    0 -> binding.scrollerLayout.scrollToChildWithOffset(
                        binding.expandScroll,
                        offset
                    )

                    1 -> binding.scrollerLayout.scrollToChildWithOffset(
                        binding.tvHospitalInfo,
                        offset
                    )

                    (titleTitleOffset.size - 1) -> binding.scrollerLayout.scrollToChildWithOffset(
                        binding.tvTeam,
                        offset
                    )

                    else -> binding.scrollerLayout.scrollToChildWithOffset(
                        x5WebView,
                        0 - SizeUtils.dp2px((titleTitleOffset[position].offsetTop) - 24.toFloat())

                    )
                }

                binding.commonTabLayout.setCurrentTabWithNoPager(position)
                binding.commonTabLayout.invalidate()
            }

            override fun onTabReselect(position: Int) {
            }
        })

    }

    private fun initListener() {

        var isShowIng = false
        var isHideIng = false

        binding.apply {
            binding.scrollerLayout.setSmoothScrollRate(300)
            binding.scrollerLayout.setOnVerticalScrollChangeListener { v, scrollY, oldScrollY, scrollState ->
                var rate =
                    abs(scrollY).toFloat() / 150f.dp()
                webScrollY = scrollY - x5WebView?.top!!
                val expandScrollTop = scrollY - expandScroll.top

                autoScrollToTitle(scrollY)


                Log.e(
                    "TOP",
                    "${expandScrollTop},${expandScroll.top},${scrollY}"
                )
                if (scrollY < 100) {
                    rate = 0f
                }

                customCenterView.alpha = rate

                if (rate > 0.8 && !commonTabLayout.isVisible() && binding.commonTabLayout.tabCount > 0) {
                    if (isShowIng || isHideIng) {
                        return@setOnVerticalScrollChangeListener
                    }
                    isShowIng = true

                    //执行动画
                    binding.commonTabLayout.visible()

                    val valueAnimator = ValueAnimator.ofFloat(-44f.dp().toFloat(), 0f)
                    valueAnimator.addUpdateListener {
                        val value = it.animatedValue as Float
                        binding.commonTabLayout.translationY = value
                    }
                    valueAnimator.addListener(onEnd = {
                        isShowIng = false
                    })
                    valueAnimator.duration = 200
                    valueAnimator.start()
                }


                //Log.e("TAG", "${rate},${commonTabLayout.isVisible()}")
                if (rate < 0.7 && commonTabLayout.isVisible() && binding.commonTabLayout.tabCount > 0) {

                    if (DoubleUtils.isFastDoubleClick()) {
                        return@setOnVerticalScrollChangeListener
                    }
                    if (isShowIng || isHideIng) {
                        return@setOnVerticalScrollChangeListener
                    }
                    isHideIng = true

                    //执行动画
                    val valueAnimator = ValueAnimator.ofFloat(0f, -44f.dp().toFloat())
                    valueAnimator.addUpdateListener {
                        binding.commonTabLayout.translationY = it.animatedValue as Float
                    }
                    valueAnimator.addListener(onEnd = {
                        isHideIng = false
                    })
                    valueAnimator.duration = 200
                    valueAnimator.start()

                    binding.commonTabLayout.postDelayed({
                        binding.commonTabLayout.gone()
                    }, 250)

                }


            }

            rlFeedError.setOnSingleClickListener({
                Action {
                    showFeedErrorDialog()
                }.logInToIntercept()
            })


            llList.setOnSingleClickListener({
                showTitleListDilaog()
            })

            stName.setOnSingleClickListener({
                doctorDetails?.let {
                    WikiRouter.toHospitalDetailsActivity(it.doctor.hospitalId)
                }
            })

            hospitalDoctorAdapter.setOnItemClickListener { adapter, view, pos ->
                WikiRouter.toDoctorDetailsActivity(hospitalDoctorAdapter.getItem(pos).doctorId)
            }


        }

    }

    /**
     * 自动确定滚动位置的title
     */
    private fun autoScrollToTitle(scrollY: Int) {
        val recyclerDoctorRect = Rect()
        binding.recyclerDoctor.getLocalVisibleRect(recyclerDoctorRect)

        if (scrollY < 500) {
            if (selectedPos == 0) {
                return
            }
            selectedPos = 0
            updateTab()
        } else if (scrollY < 1800) {
            if (selectedPos == 1) {
                return
            }
            selectedPos = 1
            updateTab()
        } else if (recyclerDoctorRect.top < 100 && recyclerDoctorRect.bottom > 150) {
            if (selectedPos == titleTitleOffset.size - 1) {
                return
            }
            selectedPos = titleTitleOffset.size - 1
            updateTab()
        } else {
            var scrollY2 = SizeUtils.px2dp(webScrollY.toFloat())

            for (i in 2 until titleTitleOffset.size - 1) {
                if (titleTitleOffset[i].offsetTop >= scrollY2) {
                    if (selectedPos == i) {
                        return
                    }
                    Log.e("until", "${selectedPos},${titleTitleOffset[i].offsetTop},${scrollY2}")
                    selectedPos = i
                    updateTab()
                    break
                }
            }
        }


    }

    fun updateTab() {
        binding.commonTabLayout.setCurrentTabWithNoPager(selectedPos)
        binding.commonTabLayout.invalidate()
    }


    private fun iniRecyclerView() {
        binding.apply {

            recyclerDoctor.init(
                hospitalDoctorAdapter,
                layoutManager = LinearLayoutManager(
                    this@DoctorDetailActivity,
                    RecyclerView.VERTICAL,
                    false
                )

            )
        }

    }

    override fun initData() {
        started {

            viewModel.doctorDetail(doctorId).netCatch {
                showErrorState()

            }.next {
                showContentState()
                doctorDetails = this
                loadViewData(this)
                loadH5(doctor.content)

            }

        }

    }


    private fun loadViewData(hospitalDetailBean: DoctorDetailBean) {
        binding?.apply {
            stName.leftTextView.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            stName.leftTextView.text = hospitalDetailBean.doctor.hospital.hospitalName

//            titleBarView.setCenterView(
//                customCenterView
//            )
//            customCenterView.alpha = 0f
//
//            customCenterView.findViewById<TextView>(R.id.tv_name).text =
//                hospitalDetailBean.doctor.name
//            customCenterView.findViewById<ImageView>(R.id.iv_cover)
//                .loadCircle(hospitalDetailBean.doctor.avatarFile)

            titleBarView.setRightView(R.layout.layout_minitool_doctor_right)

            val textView =
                titleBarView.rightCustomView.findViewById<TextView>(R.id.attention)
            if (hospitalDetailBean.doctor.isFocus) {
                textView.text = "已关注"
                textView.setBackgroundResource(R.drawable.shape_doctor_unselected)
            } else {
                textView.text = "关注"
                textView.setBackgroundResource(R.drawable.shape_doctor_selected)
            }

            titleBarView.rightCustomView.findViewById<TextView>(R.id.attention).setOnClickListener {
                if (hospitalDetailBean.doctor.isFocus) {
                    val textView =
                        titleBarView.rightCustomView.findViewById<TextView>(R.id.attention)
                    textView.text = "关注"
                    textView.setBackgroundResource(R.drawable.shape_doctor_selected)
                    viewModel.doctorAttention(doctorId, "unfocus") {
                        hospitalDetailBean.doctor.isFocus = false
                    }

                } else {
                    val textView =
                        titleBarView.rightCustomView.findViewById<TextView>(R.id.attention)
                    textView.text = "已关注"
                    textView.setBackgroundResource(R.drawable.shape_doctor_unselected)
                    viewModel.doctorAttention(doctorId, "focus") {
                        hospitalDetailBean.doctor.isFocus = true
                    }

                }
            }

            titleBarView.rightCustomView.findViewById<View>(R.id.iv_more).setOnClickListener {
                MiniMenuUtils.showMenuDialog(
                    this@DoctorDetailActivity,
                    DoctorConstant.miniApp,
                    4

                )
            }

            titleBarView.rightCustomView.findViewById<View>(R.id.iv_close).setOnClickListener {
                DoctorConstant.lastActivity?.let {
                    ActivityUtils.finishToActivity(it, true)
                }
            }


            tvTitle.text = hospitalDetailBean.doctor.name
            ivCover.loadCircle(hospitalDetailBean.doctor.avatarFile)
            tvSimpleName.text =
                "${hospitalDetailBean.doctor.department.department}  ${hospitalDetailBean.doctor.position}"


            if (hospitalDetailBean.doctor.doctorDescript.isNotNullEmpty()) {
                // 设置TextView可展示的宽度 ( 父控件宽度 - 左右margin - 左右padding）
                val whidth = ScreenUtils.getScreenWidth() - SizeUtils.dp2px(40f)
                textIntroduction.initWidth(whidth)
                textIntroduction.setCloseText(hospitalDetailBean.doctor.doctorDescript)

                textIntroduction.setOnClickListener {
                    // 点击副文案

                    if (!textIntroduction.isShowAll) {
                        return@setOnClickListener
                    }

                    XPopup.Builder(this@DoctorDetailActivity)
                        .enableDrag(false)
                        .isDestroyOnDismiss(true)
                        .moveUpToKeyboard(false)
                        .popupHeight((ScreenUtils.getScreenHeight() * 0.75).toInt())
                        .asCustom(
                            MoreDetailDialog(
                                this@DoctorDetailActivity,
                                "医生简介",
                                hospitalDetailBean.doctor.doctorDescript

                            )
                        )
                        .show()
                }

            } else expandScroll.visibility = View.GONE

            hospitalDoctorAdapter.setNewInstance(hospitalDetailBean.doctor.doctors)

            // 圈子参考资料
            if (hospitalDetailBean.doctor.infos.isNotNullEmpty()) {
                infoLayout.visibility = View.VISIBLE
                createInfoItem(hospitalDetailBean.doctor.infos, llInfo)
            } else {
                infoLayout.visibility = View.GONE
            }

            //擅长领域
            if (hospitalDetailBean.doctor.columnTitles.isNotNullEmpty()) {
                floatLayout.removeAllViews()
                floatLayout.visible()
                hospitalDetailBean.doctor.columnTitles.forEach {
                    floatLayout.addView(buildSpecialTag(floatLayout.context, it))
                }
            } else {
                floatLayout.gone()
            }

        }

    }

    private fun initWebView() {
        x5WebView = X5WebView(this)
//        binding.content.wevContent.addView(x5WebView)
        //  binding.flWeb.addView(x5WebView)

        binding.scrollerLayout.addView(
            x5WebView,
            binding.scrollerLayout.indexOfChild(binding.flWeb)
        )
        x5WebView!!.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        x5WebView!!.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        x5WebView?.apply {
            settings?.loadWithOverviewMode = true
            settings?.allowContentAccess = true
            addJavascriptInterface(JsImageLoad(this@DoctorDetailActivity), "imageLoader")
            addJavascriptInterface(this@DoctorDetailActivity, "android")
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(p0: WebView?, p1: String?) {
                    resumed {
                        // 加载完成后等待300毫秒再重置高度，为防止图片未加载完成就重置高度
                        delay(300)
                        x5WebView?.loadUrl("javascript:window.android.resize(document.body.clientHeight)")
                    }
                    super.onPageFinished(p0, p1)
                    if (!this@DoctorDetailActivity.isFinishing) {
                        loadDataWhenFinish()
                    }
                }

                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        H5ArouterUtils.urlProcessing(it)
                    }
                    return true
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?,
                ): Boolean {
                    request?.url.toString().let {
                        H5ArouterUtils.urlProcessing(it)
                    }
                    return true
                }
            }
        }


    }

    private fun createInfoItem(
        infoList: MutableList<DoctorDetailBean.Info>,
        content: LinearLayout
    ) {
        val textParams =
            LinearLayout.LayoutParams(90f.dp(), LinearLayout.LayoutParams.WRAP_CONTENT)
        val textParams2 = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        textParams2.weight = 1.0f
        infoList.forEach {
            val itemView = LinearLayout(this)
            itemView.orientation = LinearLayout.HORIZONTAL
//            val startText = createTextView(it.key?:"")
            val startText = createTextView(it.key ?: "")
            startText.setPadding(0, 16f.dp(), 10f.dp(), 16f.dp())
            startText.layoutParams = textParams
            itemView.addView(startText)
            val endText = createTextView(it.value ?: "")
            endText.setPadding(16f.dp(), 16f.dp(), 0, 16f.dp())
            endText.layoutParams = textParams2
            endText.setTextColor(R.color.color_262626.Color)
            itemView.addView(endText)

            content.addView(itemView)
        }
    }

    private fun createTextView(str: String): TextView {
        val text = TextView(this)
        text.apply {
            isSingleLine = false
            setText(str)
            setTextColor(ColorUtils.getColor(R.color.color_7F7F7F))
            textSize = 16f
        }
        return text
    }


    private fun X5WebView.loadDataWhenFinish() {

        val webTop = x5WebView?.top ?: 0
        titles.add("医生简介")
        titles.add("擅长领域")
        titles.add("同院医生")

        titleTitleOffset.add(
            WebScrollOffset(
                offsetTop = binding.expandScroll.top,
                allOffsetTop = binding.expandScroll.top,
                title = "医生简介"
            )
        )
        titleTitleOffset.add(
            WebScrollOffset(
                offsetTop = binding.tvHospitalInfo.top,
                allOffsetTop = binding.tvHospitalInfo.top,
                title = "擅长领域"
            )
        )
        titleTitleOffset.add(
            WebScrollOffset(
                offsetTop = binding.tvTeam.top,
                allOffsetTop = binding.tvTeam.top,
                title = "同院医生"
            )
        )

        resumed {
            evaluateJavascript("javascript:getDoctorTag()") {
                val list = GsonUtils.fromJson<ArrayList<WebScrollOffset>>(
                    it,
                    GsonUtils.getListType(WebScrollOffset::class.java)
                )
                if (list.isNullOrEmpty()) {
                    initTabView()
                    return@evaluateJavascript
                }
                // titleTitleOffset.addAll(2, list)

                list.forEach {
                    it.allOffsetTop = webTop + SizeUtils.dp2px(it.offsetTop.toFloat())
                    titleTitleOffset.add(titleTitleOffset.size - 1, it)
                }

                list.forEach {
                    it.title?.let { it1 -> titles.add(titles.size - 1, it1) }
                }

                initTabView()

                Log.e("TAG", list.toString())
            }
            showContentState()
        }

    }

    @JavascriptInterface
    fun resize(height: Float) {
        ThreadUtils.runOnUiThread {

            val layoutParams: ViewGroup.LayoutParams = x5WebView!!.layoutParams
            layoutParams.height = (height * ScreenUtils.getScreenDensity()).toInt()
            Log.e("resize", "${height},${layoutParams.height}")
            x5WebView!!.layoutParams = layoutParams
            binding.scrollerLayout.checkLayoutChange()
        }
    }

    private fun loadH5(h5Str: String) {
        var h5 =
            "<h3><span style=\\\"color: rgb(0, 0, 0); font-size: 20px;\\\">历史发展</span><br></h3><p>成都中医药大学附属生殖妇幼医院前身为1985年成立的四川省计划生育科研所附属医院。</p><p>2009年11月，由四川省卫生厅批准更名为成都中医药大学第二附属医院/四川生殖卫生医院。</p><p>2018年3月，由四川省卫生和计划生育委员会批准更名为“成都中医药大学附属生殖妇幼医院/四川生殖卫生医院。</p><h3><span style=\\\"color: rgb(0, 0, 0); font-size: 20px;\\\">研究成果</span></h3><p><span style=\\\"color: rgb(0, 0, 0); font-size: 20px;\\\">科室人员曾获得国家三等发明奖、部省级及省部级科技成果进步奖、四川省人民政府科技进步三等奖等共22次，承担科研24项(其中国家级5项)，合编出版了《男性性功能障碍》、《男科学》、《实用男科学》、《现代男性不育诊疗学》、《中国慢性前列腺炎中西医结合诊疗指南》、《中国男性不育证中西医结合诊疗指南》等专著，发表学术论文150多篇。<br></span></p><h3><span style=\\\"color: rgb(0, 0, 0); font-size: 20px;\\\">临床教学与研究机构</span><br></h3><p><span style=\\\"color: rgb(0, 0, 0); font-size: 20px;\\\"><span style=\\\"color: rgb(0, 0, 0);\\\">第二临床医学院与成都中医药大学附属第五人民医院实行院院合一管理。</span><br></span></p><h2>重点专科</h2><h3>科室设置</h3><p>开设有生殖医学中心(丈夫精液人工受精技术、体外受精—胚胎移植技术及其衍生技术)、妇科、外科、内科、中医科、中医儿科、中西医结合科、皮肤科、口腔科、中医耳鼻喉科、麻醉科、医学影像科和医学检验科、药剂科等。</p><h3>特色专科</h3><p><span style=\\\"color: rgb(0, 0, 0); font-size: 16px;\\\">成都中医药大学附属生殖妇幼医院以辅助生殖技术为特色。</span></p><h2>就医指南</h2><h3>门诊时间</h3><p>周一至周五： 门诊时间8:00~12:00;消毒时间12:00~14:00;门诊时间14:00~17:30。</p><p></p><p>周末及节假日： 门诊时间9:00~12:00;消毒时间12:00~14:00;门诊时间14:00~17:00。</p><h3>急诊时间</h3><p style=\\\"margin-left: 0px;\\\">全天24小时应诊。" +
                    "</p><h3>乘车路线</h3><p>周边交通</p><p>1、成都双流国际机场：</p><p>双流国际机场(t1航站楼)站搭乘机场专线1号线至倪家桥站下车，全程约为14.9公里。</p><p>2、成都成都站：</p><p>火车北站搭乘地铁1号线至倪家桥站下车，全程约为8.6公里。</p><p>3、市内路线：</p><p>市内搭乘地铁1号线至倪家桥站或搭乘118路、16路、501路、63路、99路、G83路、机场专线1号线等公交车至倪家桥站下车均可到达目的地。</p><h3>就诊指南</h3><p>1、为避免人群聚集，消除交叉感染的隐患，实行限号就诊，暂时取消现场挂号，提供医院微信公众号线上挂号及就医160电话预约挂号。若挂号平台显示号满，则表示该医生当日停止挂号，为减少聚集，请您按序号来院，有序就诊。</p><p>2、有我院就诊卡的患者推荐微信公众号线上挂号。预约挂号时间将于清明节后逐步恢复至可预约未来7天内的门诊，具体挂号信息以公众号挂号界面为准，每天线上挂号时间段为早上7:30到晚上23:00，除此之外的时间将不提供挂号服务。</p><p>3、无我院就诊卡患者推荐就医160电话预约挂号(400-119-1160)，目前限提前1天进行挂号，仅为预挂号，就诊优先顺序在微信公众号挂号者之后。就诊当日请携带本人身份证至窗口办卡取号。</p><p><span style=\\\"color: rgb(51, 51, 51); font-size: 15px;\\\">4、门诊时间</span><br></p><p>周一至周五： 门诊时间8:00~12:00; 消毒时间12:00~14:00;门诊时间14:00~17:30。</p><p>周末及节假日： 门诊时间9:00~12:00;消毒时间12:00~14:00;门诊时间14:00~17:00。</p><p>非本院工作人员请勿在消毒时间前往我院就医或逗留。</p><p>5、支付成功后需等待微信反馈信息，若消息提示成功方可认为挂号成功。挂号成功后，无需取号，直接到科室就诊，若失败，费用会在7个工作日内原路退回，若没有消息返回需等待确认。</p><p>6、通过微信预约的患者可通过微信端口退号。退号必须在就诊前一日23：00前完成，之后将不允许退号。7天内仅允许退号3次，30天内仅允许退号7次。</p><h2>预约咨询</h2><p style=\\\"margin-left: 0px;\\\">一、微信预约</p><p style=\\\"margin-left: 0px;\\\"><span style=\\\"color: rgb(0, 0, 0);\\\">成都中医药大学附属生殖妇幼医院中预约。</span><br></p><p style=\\\"margin-left: 0px;\\\">二、电话预约</p><p style=\\\"margin-left: 0px;\\\">医院电话预约挂号系统：<span style=\\\"color: rgb(51, 51, 51);\\\">160电话预约挂号(400-119-1160)。</span></p><p style=\\\"margin-left: 0px;\\\">三、现场预约</p><p style=\\\"margin-left: 0px;\\\">在门诊一楼大厅导医台进行预约挂号。</p><p></p>"


        var data = HospitalDoctorHtmlTools.createDoctorHtml(h5Str)

        //  LogUtils.e("TAG", "${data}")

        x5WebView?.loadDataWithBaseURL(
            BuildConfig.H5_URL,
            data,
            "text/html",
            "utf-8",
            null
        )
        // x5WebView?.loadDataWhenFinish()
    }


    override fun getData() {
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        initData()

    }


    private fun showTitleListDilaog() {
        XPopup.Builder(this@DoctorDetailActivity)
            .enableDrag(false)
            .isDestroyOnDismiss(true)
            .moveUpToKeyboard(false)
            .popupHeight((ScreenUtils.getScreenHeight() * 0.75).toInt())
            .asCustom(
                HospitalDoctorTitleListDialog(
                    this@DoctorDetailActivity,
                    "医院目录",
                    titleTitleOffset, selectedPos
                ) { it, pos ->
                    selectedPos = pos
                    binding.commonTabLayout.setCurrentTab(selectedPos, true)
                    // binding.scrollerLayout.scrollTo(0, it.offsetTop)
                    when (pos) {
                        0 -> binding.scrollerLayout.scrollToChildWithOffset(
                            binding.expandScroll,
                            offset
                        )

                        1 -> binding.scrollerLayout.scrollToChildWithOffset(
                            binding.tvHospitalInfo,
                            offset
                        )

                        (titleTitleOffset.size - 1) -> binding.scrollerLayout.scrollToChildWithOffset(
                            binding.tvTeam,
                            offset
                        )

                        else -> {
                            binding.scrollerLayout.scrollToChildWithOffset(
                                x5WebView, 0 - SizeUtils.dp2px((it.offsetTop - 24).toFloat())
                            )

                        }
                    }
                }
            )
            .show()
    }

    /**
     * 纠错dialog
     */
    private fun showFeedErrorDialog() {
        val reportListData = arrayListOf(
            CustomDialogTypeBean(
                resources.getString(R.string.find_error),
                1
            ),
            CustomDialogTypeBean(
                resources.getString(com.jmbon.widget.R.string.content_error),
                2
            ),
            CustomDialogTypeBean(
                resources.getString(com.jmbon.widget.R.string.miss_part_content),
                2
            ),
            CustomDialogTypeBean(
                resources.getString(com.jmbon.widget.R.string.currency_cancle),
                3
            ) as MultiItemEntity,
        )
        XPopup.Builder(this)
            .isDestroyOnDismiss(true)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .autoDismiss(true)
            .enableDrag(false)
            .asCustom(
                CustomListBottomDialog(this, reportListData) {
                    //举报回答
                    if (it == 1 || it == 2) {
                        ARouter.getInstance().build("/middleware/activity/feed_error")
                            .withInt(
                                TagConstant.PARAMS,
                                it
                            ).withInt(
                                TagConstant.TYPE,
                                2
                            ).withInt(
                                TagConstant.ITEM_ID,
                                doctorId
                            )
                            .withString(
                                TagConstant.PARAMS2,
                                doctorDetails?.doctor?.name
                            )
                            .navigation()
                    }

                }
            ).show()
    }

    /**
     * 擅长领域标签
     */
    private fun buildSpecialTag(
        context: Context,
        label: HospitalDetailBean.Doctor.Column,
    ): TextView {
        val text = TextView(context)
        val params =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, SizeUtils.dp2px(44f))
        text.apply {
            layoutParams = params
            setText(label.columnName)
            setTextColor(ColorUtils.getColor(R.color.color_262626))
            textSize = 13f
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
            gravity = Gravity.CENTER
            setBackgroundResource(R.drawable.shape_doctor_special_tag_black_bg)
            setPadding(12f.dp())
        }
        return text
    }

}