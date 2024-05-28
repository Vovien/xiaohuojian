package com.jmbon.middleware.model

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.apkdv.mvvmfast.base.ViewModelFactory
import com.apkdv.mvvmfast.ktx.netCatch
import com.apkdv.mvvmfast.ktx.next
import com.apkdv.mvvmfast.ktx.showToast
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class CollectViewModel : ViewModelStoreOwner {
    private val mViewModelStore by lazy { ViewModelStore() }

    private val messageCenterViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory<Any, Any?>()
        ).get(ToolsCollectViewModel::class.java)
    }


    fun collect(miniId: String, status: Int) {
        MainScope().launch {
            messageCenterViewModel.toolCollect(miniId, status).netCatch {
                message.showToast()
            }.next {
                if (status == 1) {
                    "收藏成功".showToast()
                } else {
                    "取消收藏成功".showToast()
                }
            }
        }
    }

    fun toolIsCollect(miniId: String, result: (status: Boolean) -> Unit) {
        MainScope().launch {
            messageCenterViewModel.toolIsCollect(miniId).netCatch {
                //message.showToast()
                result(false)
            }.next {
                result(is_collected)
            }
        }
    }

    override fun getViewModelStore(): ViewModelStore {
        return mViewModelStore
    }

}