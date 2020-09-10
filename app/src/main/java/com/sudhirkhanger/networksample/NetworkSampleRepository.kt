package com.sudhirkhanger.networksample

import com.sudhirkhanger.networksample.ui.CountriesResponse
import com.sudhirkhanger.networksample.network.NetworkSampleService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NetworkSampleRepository private constructor(
    private val networkSampleService: NetworkSampleService
) {

    companion object {
        @Volatile
        private var instance: NetworkSampleRepository? = null

        fun getInstance(networkSampleService: NetworkSampleService) =
            instance
                ?: synchronized(this) {
                    instance
                        ?: NetworkSampleRepository(networkSampleService)
                            .also { instance = it }
                }
    }

    fun getCountries(): Flow<CountriesResponse> = flow { emit(networkSampleService.countries()) }
}