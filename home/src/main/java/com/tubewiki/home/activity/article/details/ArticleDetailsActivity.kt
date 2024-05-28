package com.tubewiki.home.activity.article.details


import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.animation.addListener
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.core.view.postDelayed
import androidx.palette.graphics.Palette
import androidx.viewpager2.widget.ViewPager2
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.*
import com.apkdv.mvvmfast.network.config.HttpCode
import com.blankj.utilcode.util.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.jmbon.middleware.BuildConfig
import com.jmbon.middleware.adapter.ArticleReferenceItemAdapter
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.bean.*
import com.jmbon.middleware.bean.event.ArticleCollectEvent
import com.jmbon.middleware.bean.event.UserLoginEvent
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.common.dialog.ShareDialog
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.databinding.ArticleReferenceFooterItemLayoutBinding
import com.jmbon.middleware.dialog.FeedbackDialog
import com.jmbon.middleware.js.JsImageLoad
import com.jmbon.middleware.kotlinbus.KotlinBus
import com.jmbon.middleware.utils.*
import com.jmbon.middleware.utils.AnimatorUtils.startLightAnimation
import com.jmbon.middleware.valid.action.Action
import com.jmbon.widget.dialog.CustomDialogTypeBean
import com.jmbon.widget.dialog.CustomListBottomDialog
import com.jmbon.widget.html.X5WebView
import com.lxj.xpopup.XPopup
import com.qmuiteam.qmui.kotlin.onClick
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient
import com.tubewiki.home.R
import com.tubewiki.home.activity.article.viewmodel.ArticleDetailViewModel
import com.tubewiki.home.adapter.ArticleCircleAdapter
import com.tubewiki.home.adapter.ColumnArticlePageAdapter
import com.tubewiki.home.bean.ArticleRelatedBean
import com.tubewiki.home.databinding.ActivityArticleDetailsContetnBinding
import com.tubewiki.home.databinding.IncludeArticleDetailsBinding
import com.tubewiki.home.dialog.WeChatDialog
import com.umeng.commonsdk.internal.crash.UMCrashManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.onCompletion
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.math.abs


/**
 * 文章详情
 */
@Route(path = "/home/article/details")
class ArticleDetailsActivity :
    ViewModelActivity<ArticleDetailViewModel, ActivityArticleDetailsContetnBinding>(),
    NetworkUtils.OnNetworkStatusChangedListener {
    private var x5WebView: X5WebView? = null


    var webScrollY = 0

    // 文章 ID
    @Autowired(name = TagConstant.ARTICLE_ID)
    @JvmField
    var articleId = 0

    // 文章实体类
    @Autowired(name = TagConstant.ARTICLE_CONTENT)
    @JvmField
    var articleDetailsBean: ArticleDetailBean? = null

    private val columnArticlePageAdapter by lazy { ColumnArticlePageAdapter() }
    private val articleCircleAdapter by lazy { ArticleCircleAdapter() }


    override fun beforeViewInit() {
        super.beforeViewInit()
        supportActionBar?.hide()
        ARouter.getInstance().inject(this)
        EventBus.getDefault().register(this)
    }

    @SuppressLint("MissingPermission")
    override fun initView(savedInstanceState: Bundle?) {
        initTitle()
        // 设置按钮为 loading 状态
        if (NetworkUtils.isConnected()) {
            initPageState()
            // setToolbar(article)
        } else {
//            binding.root.alpha = 1.0f
        }
        initContentView()
        resumed {
            initListener()
        }

    }


    private fun initListener() {

        //收藏操作
        ClickUtils.applySingleDebouncing(binding.imageCollect) {
            Action {
                articleDetailsBean?.collectStatus?.let {
                    started {
                        viewModel.articleCollect(articleId, if (it == 1) "del" else "add")
                            .netCatch {
                                message.showToast()
                            }.next {
                                if (it == 1) {
                                    articleDetailsBean?.collectStatus = 0
                                    "取消收藏".showToast()
                                    EventBus.getDefault().post(ArticleCollectEvent(true))
                                } else {
                                    articleDetailsBean?.collectStatus = 1
                                    "收藏成功".showToast()
                                }
                                showCollectImage()
                            }
                    }
                }
            }.logInToIntercept()
        }

        ClickUtils.applySingleDebouncing(binding.content.rlFeedError) {
            Action {
                articleDetailsBean?.let {
                    showFeedErrorDialog()
                }
            }.logInToIntercept()
        }

        binding.content.llZan.setOnSingleClickListener({
            if (!binding.content.llFeed.isVisible()) {
                return@setOnSingleClickListener
            }
            feedbackHelp(1) {
                doHelpAnimator(it)
            }

        })

        binding.content.llFeed.setOnSingleClickListener({
            if (!binding.content.llFeed.isVisible()) {
                return@setOnSingleClickListener
            }
            feedbackHelp(2) {
                doUnHelpAnimator()
            }
        })

        binding.content.jbFeed.setOnClickListener {
            //想要反馈
            showFeedBackDialog()
        }
        binding.content.ivLeft.setOnSingleClickListener({
            var currPos = binding.content.viewPager.currentItem
            if (currPos == 0) {
                return@setOnSingleClickListener
            }
            binding.content.viewPager.setCurrentItem(currPos - 1, true)
        })
        binding.content.ivRight.setOnSingleClickListener({
            var currPos = binding.content.viewPager.currentItem
            var totalsCount = columnArticlePageAdapter.itemCount

            if (currPos == totalsCount - 1) {
                return@setOnSingleClickListener
            }
            binding.content.viewPager.setCurrentItem(currPos + 1, true)
        })


        binding.content.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                isLimitChange(position, columnArticlePageAdapter.itemCount)

                if (columnArticlePageAdapter.selectedIndex == position) {
                    binding.content.tvIndex.text = "当前"
                } else {
                    binding.content.tvIndex.text = "${position + 1}"
                }
            }
        })


        columnArticlePageAdapter.setOnItemClickListener { adapter, view, position ->
            var data = columnArticlePageAdapter.getItem(position)
            if (!data.isFixed) {
                //非当前点击跳转文章
                ArouterUtils.toArticleDetailsActivity(data.articleId)
            }

        }
        articleCircleAdapter.setOnItemClickListener { adapter, view, position ->
            var data = articleCircleAdapter.getItem(position)
            BannerHelper.onClick(
                CommonBanner(
                    item_type = data.itemType,
                    identity = data.identity
                )
            )

        }


        NetworkUtils.registerNetworkStatusChangedListener(this)
    }

    private fun doUnHelpAnimator() {
        binding.content.llFeed.post {
            //无用
            binding.content.llZan.invisible()
            binding.content.llFeed.invisible()
            binding.content.jbFeed.visible()
            binding.content.jbFeed.post {
                //动画滚动距离 自己宽度的一半+中间的间距
                var width =
                    binding.content.llFeed.width - binding.content.jbFeed.width + binding.content.jbFeed.width / 2 + 12f.dp()

                var value = ValueAnimator.ofInt(0, width)
                value.addUpdateListener {
                    binding.content.jbFeed.translationX =
                        -((it.animatedValue as Int).toFloat())
                }
                value.duration = 300
                value.start()

            }
        }
    }

    private fun doHelpAnimator(it: Int) {
        binding.content.llZan.post {
            //动画滚动距离 自己宽度的一半+中间的间距
            var width = binding.content.llZan.width / 2 + 12f.dp()
            //点赞
            binding.content.llFeed.invisible()

            binding.content.ivZan.setImageResource(R.drawable.icon_article_zan_selected)
            binding.content.tvZanTitle.text = "$it"

            var value = ValueAnimator.ofInt(0, width)
            value.addUpdateListener {
                binding.content.llZan.translationX = ((it.animatedValue as Int).toFloat())
            }
            value.duration = 300
            value.start()

        }
    }

    /**
     * 反馈有无帮助
     * isHelpful 有无帮助 1有；2无
     */
    private fun feedbackHelp(isHelpful: Int, result: (Int) -> Unit) {
        started {
            articleDetailsBean?.let {
                viewModel.feedbackHelp(it.articleDetail.id, it.articleDetail.title, isHelpful)
                    .netCatch {
                        message.showToast()
                    }.next {
                        it.articleDetail.helpful.helpfulId = id
                        result(helpfulCount)
                    }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initContentView() {
        x5WebView = X5WebView(this)
        binding.content.scrollerLayout.addView(x5WebView, 3)
        x5WebView!!.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        x5WebView!!.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        x5WebView?.apply {
            settings?.loadWithOverviewMode = true
            settings?.allowContentAccess = true
            addJavascriptInterface(JsImageLoad(this@ArticleDetailsActivity), "imageLoader")
            addJavascriptInterface(this@ArticleDetailsActivity, "android")
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(p0: WebView?, p1: String?) {
                    resumed {
                        // 加载完成后等待300毫秒再重置高度，为防止图片未加载完成就重置高度
                        delay(300)
                        x5WebView?.loadUrl("javascript:window.android.resize(document.body.clientHeight)")
                    }
                    super.onPageFinished(p0, p1)
                    if (!this@ArticleDetailsActivity.isFinishing) {
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
        // binding.content.scrollerLayout.setSmoothScrollRate(300)
        initRecycleView()

        binding.content.scrollerLayout.setOnVerticalScrollChangeListener { v, scrollY, oldScrollY, scrollState ->

            var rate =
                abs(scrollY).toFloat() / 92f.dp()
            webScrollY = scrollY - x5WebView?.top!!
            if (scrollY < 100) {
                rate = 0f
            }
            binding.textName.alpha = rate

            if (articleId != 0 && Constant.enterArticleDetailCount == 1 && viewModel.firstShowFlow.value) {
                val popupImage = CommonViewModel.popupImageFlow.value?.popup_adv
                popupImage?.apply {
                    if (this.popupImg.isNotNullEmpty()) {
                        val dialog = WeChatDialog(ActivityUtils.getTopActivity(), this)
                        dialog.showDialog()
                        viewModel.firstShowFlow.value = false
                    }
                }
            }

        }
    }

    @JavascriptInterface
    fun resize(height: Float) {
        ThreadUtils.runOnUiThread {
            val layoutParams: ViewGroup.LayoutParams = x5WebView!!.layoutParams
            layoutParams.height = (height * ScreenUtils.getScreenDensity()).toInt()
            x5WebView!!.layoutParams = layoutParams
            binding.content.scrollerLayout.checkLayoutChange()
        }
    }

    private fun showCollectImage() {
        articleDetailsBean?.collectStatus.let {
            if (it == 1) {
                binding.imageCollect.setImageResource(R.drawable.icon_article_uncollect)
            } else {
                binding.imageCollect.setImageResource(R.drawable.icon_article_collect)
            }
        }
    }

    var linerMax = 0
    var linerMin = 0
    private fun initArticleDetails(it: ArticleDetailBean) {
        articleDetailsBean = it
        // 设置右侧点击
//        setToolbar(it.article, true)
        binding.content.apply {
            articleDetailsBean?.let { item ->
                clTubeTest.isVisible = item.leftButton != null
                tvCustomScheme.isVisible = item.rightButton != null
                tvTubeTest.setTextWhenNotEmpty(item.leftButton?.title)
                tvCustomScheme.setTextWhenNotEmpty(item.rightButton?.title)
                tvLabel.setTextWhenNotEmpty(item.rightButton?.label)
                clLabel.isVisible = !item.rightButton?.label.isNullOrBlank()
                if (clLabel.isVisible) {
                    tvAnim.startLightAnimation()
                }
                clTubeTest.setOnClickListener {
                    item.leftButton?.let {
                        BannerHelper.onClick(
                            CommonBanner(
                                item_type = it.item_type,
                                identity = it.identity
                            )
                        )
                    }
                }
                tvCustomScheme.onClick {
                    item.rightButton?.let {
                        BannerHelper.onClick(
                            CommonBanner(
                                item_type = it.item_type,
                                identity = it.identity
                            )
                        )
                    }
                }
                Constant.addArticleDetailCount()
                showCollectImage()
                //设置广告 暂时不用
                if (it.articleDetail.recommendAdv != null) {
                    clAd.isVisible = true
                    clAd.postDelayed(250) {
                        clAd.setData(it.articleDetail.recommendAdv)
                    }
                }

                if (!webPageInit) {
                    setPageLoad(
                        it.articleDetail.articleType,
                        it.articleDetail.content,
                        it.articleDetail.title,
                        it.articleDetail.introduction, it.articleDetail.info
                    )
                }

                loadH5(
                    it.articleDetail.articleType,
                    it.articleDetail.content,
                    it.articleDetail.ordinaryAdv,
                    it.articleDetail.wikiAdv
                )

                // 引用
                // 展开收起
                if (it.articleDetail.reference.isNotNullEmpty()) {
                    llReferenceMedia.visible()
                    //引用文件
                    val adapter = ArticleReferenceItemAdapter()
                    rvReference.init(adapter)
                    // 取第一个
                    val oneList = arrayListOf(it.articleDetail.reference[0])
                    adapter.setNewInstance(it.articleDetail.reference)

                    val view = ArticleReferenceFooterItemLayoutBinding.inflate(
                        LayoutInflater.from(this@ArticleDetailsActivity)
                    ).root
                    adapter.addFooterView(view)
                    // 由内容则隐藏
                    showMore.post {
                        linerMax = showMore.height
                        adapter.setNewInstance(oneList)
                        view.post {
                            linerMin = showMore.height - view.height
                            val layoutParams = showMore.layoutParams
                            layoutParams.height = linerMin
                            showMore.layoutParams = layoutParams
                        }
                    }

                    // ture 收起状态
                    textShowMore.setOnSingleClickListener({
                        textShowMore.text = if (textShowMore.isChecked) "展开" else "收起"
                        textShowMore.isChecked = !textShowMore.isChecked

                        val valueAnimator =
                            ValueAnimator.ofInt(
                                if (textShowMore.isChecked) linerMin else linerMax,
                                if (textShowMore.isChecked) linerMax else linerMin
                            )
                                .setDuration(250)
                        valueAnimator.addUpdateListener { animation ->
                            binding.content.showMore.layoutParams.height =
                                animation.animatedValue as Int
                            binding.content.showMore.requestLayout()
                        }
                        valueAnimator.addListener(onStart = { _ ->
                            if (textShowMore.isChecked) {
                                adapter.setNewInstance(item.articleDetail.reference)
                                llReferenceMedia.showDividers = LinearLayout.SHOW_DIVIDER_END
                            }
                        }, onEnd = { _ ->
                            if (!textShowMore.isChecked) {
                                adapter.setNewInstance(oneList)
                                llReferenceMedia.showDividers = LinearLayout.SHOW_DIVIDER_NONE
                            }
                        })
                        valueAnimator.start()
                    })
                } else {
                    llReferenceMedia.gone()
                }

                if (item.articleDetail.helpful.isHelpful == 1) {
                    doHelpAnimator(item.articleDetail.helpful.count)
                } else if (item.articleDetail.helpful.isHelpful == 2) {
                    doUnHelpAnimator()
                }

                llRelated.isVisible = item.circles.isNotNullEmpty()
                // 推荐群聊圈子
                if (item.circles.isNotNullEmpty()) {
                    articleCircleAdapter.setNewInstance(item.circles)
                }

                if (item.tag.isNotNullEmpty())
                    item.tag.forEach { item ->
                        binding.content.ryRelatedSearch.addView(buildHotBtn(item.id, item.tag))
                    }
                else {
                    binding.content.llRelatedSearch.visibility = View.GONE
                    binding.content.spaceRelatedSearch.visibility = View.GONE
                    binding.content.ryRelatedSearch.visibility = View.GONE
                }
            }
        }
        showContentState()
    }

    /**
     * 判断文章位置是否是临界值
     */
    private fun isLimitChange(
        currIndex: Int,
        maxSize: Int
    ) {
        if (currIndex == 0) {
            binding.content.ivLeft.alpha = 0.3f
        } else {
            binding.content.ivLeft.alpha = 1.0f
        }
        if (currIndex == maxSize - 1) {
            binding.content.ivRight.alpha = 0.3f
        } else {
            binding.content.ivRight.alpha = 1.0f
        }
    }


    private fun buildHotBtn(articleId: Int, str: String): TextView {
        val btn = TextView(this)
        btn.text = str
        btn.setTextColor(ColorUtils.getColor(R.color.text_color))
        btn.textSize = 14f
        btn.setBackgroundResource(R.drawable.shape_hot_search_bg)
        btn.setOnClickListener {
            viewModel.firstShowFlow.value = false
            ArouterUtils.toArticleDetailsActivity(articleId)
        }
        return btn
    }

    private fun setImageBitmap(imageView: ImageView, covers: String) {
        val dp8 = 8f.dp()
        Glide.with(this@ArticleDetailsActivity)
            .asBitmap()
            .load(covers.urlToWep)
            .thumbnail(
                GlideUtil.getInstance()
                    .loadRoundedTransformBitmap(
                        this@ArticleDetailsActivity,
                        R.drawable.icon_column_placeholder,
                        dp8
                    )
            )
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(dp8)))
            .apply(
                RequestOptions().placeholder(R.drawable.icon_column_placeholder)
                    .error(R.drawable.icon_column_placeholder)
            )
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val color =
                        Palette.Builder(resource).addFilter(ColorFilter.DEFAULT_FILTER).generate()

                    //imageView.setImageBitmap(ImageUtils.fastBlur(resource, 0.3f, 25f))
                    imageView.background =
                        (ImageUtils.fastBlur(resource, 0.3f, 25f)
                            .toDrawable(this@ArticleDetailsActivity.resources))
                    val backGroundColor = when {
                        color.darkVibrantSwatch != null -> color.darkVibrantSwatch!!.rgb
                        color.vibrantSwatch != null -> {
                            color.vibrantSwatch!!.rgb
                        }

                        color.mutedSwatch != null -> {
                            color.mutedSwatch!!.rgb
                        }

                        else -> {
                            ColorUtils.getColor(R.color.color_334d4e)
                        }
                    }
                    val backColor = ColorUtils.setAlphaComponent(backGroundColor, 0.9f)
                    // imageView.setImageResource(R.color.colorF9F9F9)
                    imageView.setImageDrawable(ColorDrawable(backColor))
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
    }


    private fun IncludeArticleDetailsBinding.setPageLoad(
        type: Int,
        h5Str: String, title: String,
        abstracts: String, infos: MutableList<ArticleDetailBean.ArticleInfo>
    ) {
        setTitleWithTime(HtmlTools.delHTMLTag(title))

        if (abstracts.isNotNullEmpty()) {
            textIntroduction.maxLine = 6
            textIntroduction.setCloseStr(getString(R.string.all_profile))
            textIntroduction.setOpenStr(getString(R.string.introduction_fold))
            textIntroduction.setText(
                HtmlTools.delHTMLTag(abstracts)
            )
        } else expandScroll.visibility = View.GONE

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            textIntroduction.textView.lineHeight = SizeUtils.dp2px(28f)
        }
        // infos
        // 圈子参考资料
        if (infos.isNotNullEmpty()) {
            infoLayout.visibility = View.VISIBLE
            createInfoItem(infos, llInfo)
        } else {
            infoLayout.visibility = View.GONE
        }
    }

    private fun IncludeArticleDetailsBinding.setTitleWithTime(
        title: String
    ) {
        textTitle.text = title
        binding.textName.text = title
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun loadH5(
        type: Int, h5Str: String, ordinaryAdv: ArticleDetailBean.OrdinaryAdv? = null,
        wikiAdv: ArticleDetailBean.WikiAdvBean? = null
    ) {

        x5WebView?.loadDataWithBaseURL(
            BuildConfig.H5_URL,
            if (type == 1) ArticleHtmlTools.createHtmlType1(h5Str) else ArticleHtmlTools.createHtmlType2(
                h5Str
            ),
            "text/html",
            "utf-8",
            null
        )

        var startx = 0f
        var starty = 0f
        var offsetx = 0f
        var offsety = 0f
        x5WebView?.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    v.parent.requestDisallowInterceptTouchEvent(true)
                    startx = event.x
                    starty = event.y
                }

                MotionEvent.ACTION_MOVE -> {
                    offsetx = abs(event.x - startx)
                    offsety = abs(event.y - starty)
                    if (offsetx > offsety) {
                        v.parent.requestDisallowInterceptTouchEvent(true)
                    } else {
                        v.parent.requestDisallowInterceptTouchEvent(false)
                    }
                }

                else -> {}
            }
            false
        }
        x5WebView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                if (type == 1) {
                    if (ordinaryAdv != null) {
                        val advStr = GsonUtils.toJson(ordinaryAdv)
                        resumed {
                            x5WebView?.evaluateJavascript("javascript:setAdUI('.content','${advStr}')") {
                            }
                        }
                    }
                } else {
                    if (wikiAdv != null) {
                        resumed {
                            val newAdv = wikiAdv
                            newAdv?.list?.forEach {
                                it.costStr = if (it.cost >= 5000) {
                                    String.format("%.1f万", it.cost / 10000f)
                                } else {
                                    "${it.cost} 元"
                                }
                            }
                            val advJson = GsonUtils.toJson(newAdv)
                            x5WebView?.evaluateJavascript("javascript:setAdUI('.content','${advJson}')") {
                            }
                        }
                    }
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
                request: WebResourceRequest?
            ): Boolean {
                request?.url.toString().let {
                    H5ArouterUtils.urlProcessing(it)
                }
                return true
            }
        }

        x5WebView?.loadDataWhenFinish()
    }


    private fun X5WebView.loadDataWhenFinish() {
        showContentState()
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        getData()

    }


    private fun initTitle() {

        binding.imageBack.setOnClickListener {
            onBackPressed()
        }
    }


    private fun createInfoItem(
        infoList: MutableList<ArticleDetailBean.ArticleInfo>,
        content: LinearLayout
    ) {
        val textParams =
            LinearLayout.LayoutParams(90f.dp(), LinearLayout.LayoutParams.WRAP_CONTENT)
        val textParams2 = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
        textParams2.weight = 1.0f
        infoList.forEach {
            val itemView = LinearLayout(this@ArticleDetailsActivity)
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
        val text = TextView(this@ArticleDetailsActivity)
        text.apply {
            isSingleLine = false
            setText(str)
            setTextColor(ColorUtils.getColor(R.color.color_7F7F7F))
            textSize = 16f
        }
        return text
    }

    override fun initData() {

    }

    private var webPageInit = false
    override fun getData() {
        articleDetailsBean?.let {
            webPageInit = true
            // 数据已经返回
            binding.content.setPageLoad(
                it.articleDetail.articleType,
                it.articleDetail.content,
                it.articleDetail.title,
                it.articleDetail.introduction, it.articleDetail.info
            )

        }
        if (articleId != 0) {
            started {
                viewModel.getDetails(articleId).netCatch {
                    if (code.toInt() == HttpCode.HTTP.NOT_RESOURCES) {
                        showContentState()
                        //文章不存在
                        binding.content.emptyHint.text = message
                        binding.content.emptyHint.visible()
                    } else {
                        showErrorState()
                        message.showToast()
                    }
                }.onCompletion {

                }.next {
                    initArticleDetails(this)
                    if (Constant.enterArticleDetailCount == 1) {
                        viewModel.getPopupImage()
                    }
//                    initIndex(data2)
                }
            }
        }
    }

    /**
     * 索引初始化 暂时不需要
     */
    private fun initIndex(articleRelatedBean: ArticleRelatedBean) {
        articleRelatedBean?.let {
            //专栏索引
            if (it.indexList.isNotNullEmpty()) {
                binding.content.clSort.visible()
                binding.content.viewLine.visible()

                binding.content.tvIndexTitle.text = articleRelatedBean.indexInfo.indexName ?: ""
                columnArticlePageAdapter.allCount = articleRelatedBean.indexInfo.articleNum ?: 0

                binding.content.tvAll.text = "共${articleRelatedBean.indexInfo?.articleNum ?: 0}篇"

                var data = it.indexList.find { index ->
                    index.isFixed
                }
                var currIndex = it.indexList.indexOf(data)
                columnArticlePageAdapter.selectedIndex = currIndex

                columnArticlePageAdapter.setNewInstance(it.indexList)
                isLimitChange(currIndex, it.indexList.size)

                binding.content.viewPager.currentItem = currIndex

                if (currIndex == binding.content.viewPager.currentItem) {
                    binding.content.tvIndex.text = "当前"
                } else {
                    binding.content.tvIndex.text = "${binding.content.viewPager.currentItem + 1}"
                }


            } else {
                binding.content.clSort.gone()
                binding.content.viewLine.gone()
            }
        }


    }

    private fun initRecycleView() {

        binding.content.ryRelatedReading.init(
            articleCircleAdapter
        )
        binding.content.viewPager.offscreenPageLimit = 5
        binding.content.viewPager.adapter = columnArticlePageAdapter

    }


    override fun onDestroy() {
        KotlinBus.unregister(this.hashCode().toString())
        EventBus.getDefault().unregister(this)
        try {
            binding.content.scrollerLayout.removeView(x5WebView)
            x5WebView?.destroy()
        } catch (e: Exception) {
            UMCrashManager.reportCrash(Utils.getApp(), e)
        } finally {
            super.onDestroy()
        }

        NetworkUtils.unregisterNetworkStatusChangedListener(this)
    }


    override fun onResume() {
        super.onResume()
        time = System.currentTimeMillis()
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
                                1
                            ).withInt(
                                TagConstant.ITEM_ID,
                                articleDetailsBean?.articleDetail?.id ?: 0
                            )
                            .withString(
                                TagConstant.PARAMS2,
                                articleDetailsBean?.articleDetail?.title
                            )
                            .navigation()
                    }

                }
            ).show()
    }

    /**
     * 反馈 dialog
     */
    private fun showFeedBackDialog() {
        XPopup.Builder(this)
            .isDestroyOnDismiss(true)
            .dismissOnTouchOutside(true)
            .dismissOnBackPressed(true)
            .isDestroyOnDismiss(true)
            .enableDrag(false)
            .asCustom(
                FeedbackDialog(this) { type, dialog ->
                    started {
                        viewModel.feedback(
                            articleDetailsBean?.articleDetail?.helpful?.helpfulId ?: 0, type
                        )
                            .netCatch {
                                message.showToast()
                                dialog.dismiss()
                            }.next {
                                "反馈成功".showToast()
                                dialog.dismiss()
                            }

                    }
                }
            ).show()
    }


    override fun finish() {
        overridePendingTransition(
            R.anim.activity_bottom_out,
            R.anim.activity_background
        )
        super.finish()
    }

    override fun onDisconnected() {
    }

    override fun onConnected(networkType: NetworkUtils.NetworkType?) {
        getData()
    }

    override fun onPause() {
        super.onPause()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun loginEvent(event: UserLoginEvent) {

        if (event.login) {
            getData()
        }
    }

    private var time = 0L


}