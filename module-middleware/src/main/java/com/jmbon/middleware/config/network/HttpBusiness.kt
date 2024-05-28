package com.jmbon.middleware.config.network

import com.apkdv.mvvmfast.BuildConfig
import rxhttp.wrapper.annotation.DefaultDomain
import rxhttp.wrapper.annotation.Domain
import rxhttp.wrapper.param.*

object HttpBusiness {
    @Domain(name = "Business", className = "Business")
    const val BASE_COMMON_URL =  BuildConfig.BASE_BUSINESS_URL

    fun get(url: String): RxHttpNoBodyParam {
        return RxBusinessHttp.get(url)
    }

    fun post(url: String): RxHttpFormParam {
        return RxBusinessHttp.postForm(url)
    }

    fun postJson(url: String): RxHttpJsonParam {
        return RxBusinessHttp.postJson(url)
    }

    fun postBody(url: String): RxHttpBodyParam {
        return RxBusinessHttp.postBody(url)
    }

    fun postJsonArray(url: String): RxHttpJsonArrayParam {
        return RxBusinessHttp.postJsonArray(url)
    }
}