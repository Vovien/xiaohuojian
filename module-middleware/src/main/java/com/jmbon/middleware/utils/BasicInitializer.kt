package com.jmbon.middleware.utils

import android.app.Application
import cn.numeron.discovery.Implementation
import com.apkdv.mvvmfast.utils.abs.IInitializer
import com.jmbon.middleware.factory.DataLayerCenter

@Implementation(order = -1)
class BasicInitializer : IInitializer {

    override fun init(application: Application) {
        DataLayerCenter.addDaoFactory(database)
    }

}