package com.sudhirkhanger.mvvmsample

import android.app.Application
import com.sudhirkhanger.mvvmsample.utils.CustomDebugTree
import timber.log.Timber

class MvvmSampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(CustomDebugTree())
    }
}
