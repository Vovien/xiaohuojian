package com.tubewiki.home.activity

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.allen.library.helper.ShapeType
import com.allen.library.shape.ShapeFrameLayout
import com.apkdv.mvvmfast.base.ViewModelActivity
import com.apkdv.mvvmfast.databinding.DefaultViewstatusBoxEmpty4Binding
import com.apkdv.mvvmfast.glide.GlideUtil
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import com.apkdv.mvvmfast.ktx.started
import com.apkdv.mvvmfast.ktx.visible
import com.apkdv.mvvmfast.network.config.HttpCode
import com.apkdv.mvvmfast.utils.ColorCompute
import com.apkdv.mvvmfast.utils.StatusBarCompat
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ColorUtils
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.jmbon.middleware.arouter.ArouterUtils
import com.jmbon.middleware.bean.CommonBanner
import com.jmbon.middleware.common.CommonViewModel
import com.jmbon.middleware.config.Constant
import com.jmbon.middleware.utils.AnimatorUtils.startLightAnimation
import com.jmbon.middleware.utils.BannerHelper
import com.jmbon.middleware.utils.ColorFilter
import com.jmbon.middleware.utils.TagConstant
import com.jmbon.middleware.utils.dp
import com.jmbon.middleware.utils.init
import com.jmbon.middleware.utils.isNotNullEmpty
import com.jmbon.middleware.utils.setOnSingleClickListener
import com.jmbon.middleware.utils.setTextWhenNotEmpty
import com.jmbon.middleware.utils.toWxMiniProgram
import com.jmbon.middleware.utils.urlToWep
import com.qmuiteam.qmui.kotlin.onClick
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.tubewiki.home.R
import com.jmbon.middleware.adapter.ColumnDetailAdapter
import com.jmbon.middleware.utils.MiniTypeEnum
import com.tubewiki.home.databinding.ActivityArticleColumnDetailBinding
import com.tubewiki.home.databinding.ItemArticleColumnDetailHeadLayoutBinding
import com.tubewiki.home.dialog.WeChatDialog
import com.tubewiki.home.viewmodel.MainFragmentViewModel
import kotlin.math.abs


/**
 * 指南聚合 - 专栏详情页
 */
@Route(path = "/home/activity/column_detail")
class ArticleColumnDetailActivity :
    ViewModelActivity<MainFragmentViewModel, ActivityArticleColumnDetailBinding>(),
    OnRefreshLoadMoreListener {

    @Autowired(name = TagConstant.TOPIC_ID)
    @JvmField
    var columnId = 0

    var page = 1


    private val columnDetailAdapter by lazy {
        ColumnDetailAdapter()
    }

    override fun beforeViewInit() {
        super.beforeViewInit()
        StatusBarCompat.setTransparentStatusBar(this.window)
        ARouter.getInstance().inject(this)
    }

    override fun initView(savedInstanceState: Bundle?) {

        initPageState()

        val statusHeight =
            StatusBarCompat.getStatusBarHeight(this@ArticleColumnDetailActivity) + binding.titleBarLayout.paddingTop
        binding.titleBarLayout.setPadding(
            binding.titleBarLayout.paddingStart,
            statusHeight,
            binding.titleBarLayout.paddingEnd,
            binding.titleBarLayout.paddingBottom
        )


        binding.smartRefresh.setOnRefreshLoadMoreListener(this)

        (binding.recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations =
            false


        binding.recyclerView.init(columnDetailAdapter)


        var maxOffset = 200f.dp()

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //已经滚动的距离，为0时表示已处于顶部。
                val offsetY = recyclerView.computeVerticalScrollOffset()
                Log.e("xyh", "dx: $dx,  dy: $dy,  offsetY: $offsetY")

                onChanged(offsetY.toFloat() / maxOffset)

                if (abs(offsetY) > 10) {
                    if (Constant.enterArticleColumnDetailCount == 1 && viewModel.firstShowFlow.value) {
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
        })

        binding.imageBack.setOnSingleClickListener({
            onBackPressed()
        })

        columnDetailAdapter.setOnItemClickListener { adapter, view, pos ->
            val data = columnDetailAdapter.getItem(pos)
            BannerHelper.onClick(
                CommonBanner(
                    item_type = data.itemType,
                    item_id = data.identity.toIntOrNull() ?: 0,
                    identity = data.identity,
                    topicId = columnId
                )
            )
        }

    }

    private fun onChanged(scale: Float) {
        binding.tvTitle.visibility = if (scale > 0.4) View.VISIBLE else View.INVISIBLE
        binding.tvTitle.alpha = scale

        binding.titleBarLayout.setBackgroundColor(
            ColorCompute.computeColor(
                Color.argb(0, 255, 255, 255),
                Color.WHITE,
                scale
            )
        )

        val btnColor = ColorCompute.computeColor(
            Color.argb(255, 255, 255, 255),
            Color.BLACK,
            scale
        )

        binding.imageBack.setColorFilter(btnColor)

    }


    override fun initData() {

        started {
            viewModel.topicList(columnId, page).netCatch {
                if (code.toInt() == HttpCode.HTTP.NOT_RESOURCES) {
                    showContentState()
                    //专题不存在
                    binding.emptyHint.text = message
                    binding.emptyHint.visible()
                } else {
                    showErrorState()
                    message.showToast()
                }
            }.next {
                showContentState()
                if (page == 1)  {
                    Constant.addArticleColumnDetailCount()
                    if (Constant.enterArticleColumnDetailCount == 1) {
                        viewModel.getPopupImage()
                    }
                    val bindingHeader =
                        ItemArticleColumnDetailHeadLayoutBinding.inflate(layoutInflater)
                    val headerView = bindingHeader.root
                    bindingHeader.tvTitle.text =
                        data2.topic.customTitle.ifEmpty { data2.topic.topicName }
                    binding.tvTitle.text = data2.topic.customTitle.ifEmpty { data2.topic.topicName }
                    bindingHeader.tvDesc.text =
                        "/  ${data2.topic.customDescription.ifEmpty { data2.topic.description }}"
                    setImageBitmap(
                        bindingHeader.ivCover,
                        bindingHeader.shapeFl,
                        data2.topic.cover,
                        data2.topic.coverColor
                    )
                    columnDetailAdapter.setHeaderView(headerView)

                    columnDetailAdapter.setNewInstance(data1.articleList)

                    if (data1.articleList.isNullOrEmpty()) {
                        var viewBinding =
                            DefaultViewstatusBoxEmpty4Binding.inflate(LayoutInflater.from(this@ArticleColumnDetailActivity))


                        columnDetailAdapter.setFooterView(viewBinding.root)
                        var params = viewBinding.root.layoutParams
                        params.height = ScreenUtils.getScreenHeight() - 200f.dp()
                        viewBinding.root.layoutParams = params
                        binding.smartRefresh.setEnableLoadMore(false)
                    }
                    binding.apply {
                        clTubeTest.isVisible = data2.topic.leftButton != null
                        tvCustomScheme.isVisible = data2.topic.rightButton != null
                        tvTubeTest.setTextWhenNotEmpty(data2.topic.leftButton?.title)
                        tvCustomScheme.setTextWhenNotEmpty(data2.topic.rightButton?.title)
                        tvLabel.setTextWhenNotEmpty(data2.topic.rightButton?.label)
                        clLabel.isVisible = !data2.topic.rightButton?.label.isNullOrBlank()
                        if (clLabel.isVisible) {
                            tvAnim.startLightAnimation()
                        }
                        binding.clTubeTest.setOnClickListener {
                            data2.topic.leftButton?.let {
                                BannerHelper.onClick(
                                    CommonBanner(
                                        item_type = it.item_type,
                                        identity = it.identity
                                    )
                                )
                            } ?: kotlin.run {
                                toWxMiniProgram(
                                    this@ArticleColumnDetailActivity,
                                    type = MiniTypeEnum.MINI_TYPE_INVITE_GROUP.value,
                                    groupName = binding.tvTubeTest.text.toString()
                                )
                            }
                        }
                        tvCustomScheme.onClick {
                            data2.topic.rightButton?.let {
                                BannerHelper.onClick(
                                    CommonBanner(
                                        item_type = it.item_type,
                                        identity = it.identity
                                    )
                                )
                            }
                        }
                    }
                } else {
                    data1.articleList?.let { columnDetailAdapter.addData(it) }
                }

                page++
                binding.smartRefresh.finishLoadMore()

                if (data1.articleList != null) {
                    if (data1.articleList!!.size < Constant.PAGE_SIZE) {
                        binding.smartRefresh.finishLoadMoreWithNoMoreData()
                    }
                } else {
                    binding.smartRefresh.finishLoadMoreWithNoMoreData()
                }
            }
        }

    }


    override fun getData() {
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {

    }

    override fun onLoadMore(refreshLayout: RefreshLayout) {
        initData()
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun refreshDataWhenError() {
        super.refreshDataWhenError()
        page = 1
        binding.smartRefresh.setEnableLoadMore(true)
        columnDetailAdapter.removeAllFooterView()
        initData()

    }


    override fun onDestroy() {
        super.onDestroy()
    }


    private fun setImageBitmap(
        imageView: ImageView,
        shape: ShapeFrameLayout,
        covers: String,
        coverColor: String
    ) {


        GlideUtil.getInstance().loadUrlCornerRadius(
            imageView,
            covers,
            0f,
            0f,
            32f.dp().toFloat(),
            0f,
            R.drawable.icon_tube_placeholder
        )
        Glide.with(this)
            .asBitmap()
            .load(covers.urlToWep)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val color =
                        Palette.Builder(resource).addFilter(ColorFilter.DEFAULT_FILTER).generate()

                    imageView.background =
                        resource.toDrawable(this@ArticleColumnDetailActivity.resources)

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
                    val backColor = ColorUtils.setAlphaComponent(backGroundColor, 0.8f)
                    val backColorF =
                        ColorUtils.setAlphaComponent(Color.parseColor(coverColor), 0.8f)

                    shape.shapeBuilder?.setShapeType(ShapeType.RECTANGLE)
                        ?.setShapeSolidColor(backColorF)
                        ?.into(shape)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })

    }

}