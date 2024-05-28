package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.DefaultDomain
import rxhttp.wrapper.annotation.Domain
import rxhttp.wrapper.param.*

object HttpCommon {
    @Domain(name = "common", className = "common")
    const val BASE_COMMON_URL =  "${BuildConfig.BASE_COMMON_URL}"

    fun get(url: String): RxHttpNoBodyParam {
        return RxcommonHttp.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxcommonHttp.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxcommonHttp.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxcommonHttp.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxcommonHttp.postJsonArray(url)
    }
}