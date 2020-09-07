package com.sudhirkhanger.networksample

import android.app.Application
import com.sudhirkhanger.networksample.utils.CustomDebugTree
import timber.log.Timber

class NetworkSampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(CustomDebugTree())
    }
}
