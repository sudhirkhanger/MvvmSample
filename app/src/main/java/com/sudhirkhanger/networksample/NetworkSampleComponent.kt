package com.sudhirkhanger.networksample

import com.sudhirkhanger.networksample.network.NetworkSampleService
import com.sudhirkhanger.networksample.utils.DefaultViewModelFactory

object NetworkSampleComponent {

    private fun getNetworkSampleRepository() =
        NetworkSampleRepository.getInstance(NetworkSampleService.create())

    fun provideMainViewModelFactory(): DefaultViewModelFactory =
        DefaultViewModelFactory(getNetworkSampleRepository())
}