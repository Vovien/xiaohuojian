package com.yxbabe.xiaohuojian.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.apkdv.mvvmfast.base.BaseViewModel
import com.apkdv.mvvmfast.bean.ResultTwoData
import com.apkdv.mvvmfast.glide.GlideApp
import com.blankj.utilcode.util.Utils
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.yxbabe.xiaohuojian.api.MainApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 启动广告页vm层
 * @author MilkCoder
 * @date 2023/5/29
 * @version 6.2.0
 * @copyright All copyrights reserved to ManTang.
 */
class LaunchAdvertisingViewModel : BaseViewModel() {

    /**
     * 广告图片
     */
    private val _advBitmapLD = MutableLiveData<Bitmap?>()
    val advBitmapLD: LiveData<Bitmap?> = _advBitmapLD

    /**
     * 进入App事件
     */
    private val _enterAppEventLD = MutableLiveData<Boolean>()
    val enterAppEventLD = _enterAppEventLD

    /**
     * 更新进入App事件
     */
    fun postEnterAppEvent(event: Boolean) {
        _enterAppEventLD.postValue(event)
    }

    /**
     * 倒计时工作
     * @date 2023/5/29 17:03
     * @param total 从数字几开始倒计时
     * @param onTick 每一步要做的事
     * @param onStart 开始时要做的事
     * @param onFinish 完成时要做的事
     */
    fun countDownCoroutines(
        total: Int,
        onTick: (Int) -> Unit,
        onStart: (() -> Unit)? = null,
        onFinish: (() -> Unit)? = null,
    ): Job {
        return flow {
            for (i in total downTo 1) {
                emit(i)
                delay(1000)
            }
        }.flowOn(Dispatchers.IO)
            .onStart { onStart?.invoke() }
            .onEach { onTick.invoke(it) }
            .onCompletion { onFinish?.invoke() }
            .launchIn(viewModelScope)
    }

    /**
     * 获取开屏广告
     * @date 2023/6/25 14:50
     */
    fun getScreenAdv() = launchWithFlow({
        val s = MainApi.getScreenAdv()
        ResultTwoData(s,null)
    }, handleError = false)

    /**
     * 将图片转成bitmap
     * @date 2023/6/30 11:04
     * @param
     */
    fun loadUrlGetBitmap(imgUrl: String?) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                runCatching {
                    GlideApp.with(Utils.getApp())
                        .asBitmap()
                        .load(imgUrl ?: "")
                        .apply(RequestOptions().timeout(3000))
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .submit()
                        .get()
                }.getOrNull()
            }.apply {
                _advBitmapLD.postValue(this)
            }
        }
    }
}