package com.sudhirkhanger.mvvmsample

import com.sudhirkhanger.mvvmsample.network.CountryService
import com.sudhirkhanger.mvvmsample.utils.DefaultViewModelFactory

object MvvmSampleComponent {

    private fun getNetworkSampleRepository() =
        MvvmSampleRepository.getInstance(CountryService.create())

    fun provideMainViewModelFactory(): DefaultViewModelFactory =
        DefaultViewModelFactory(getNetworkSampleRepository())
}