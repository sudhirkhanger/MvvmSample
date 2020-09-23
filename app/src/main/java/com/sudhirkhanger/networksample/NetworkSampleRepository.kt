package com.sudhirkhanger.networksample

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sudhirkhanger.networksample.network.NetworkSampleService
import com.sudhirkhanger.networksample.network.model.Listing
import com.sudhirkhanger.networksample.network.model.NetworkState
import com.sudhirkhanger.networksample.ui.Country
import com.sudhirkhanger.networksample.utils.Event
import com.sudhirkhanger.networksample.utils.NETWORK_SUCCESS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

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

    private suspend fun fetchCountries() = networkSampleService.countries()
    private val countriesGlobal = MutableLiveData<List<Country?>>(listOf())
    private val networkState = MutableLiveData<Event<NetworkState>>()

    fun getCountries(coroutineContext: CoroutineContext): Listing<Country> {
        refresh(coroutineContext)

        return Listing(
            data = countriesGlobal,
            networkState = networkState,
            refresh = { refresh(coroutineContext) }
        )
    }

    private fun refresh(coroutineContext: CoroutineContext): LiveData<Event<NetworkState>> {
        val networkState = MutableLiveData<Event<NetworkState>>()
        CoroutineScope(coroutineContext).launch {
            networkState.postValue(Event(NetworkState.LOADING))
            val countries: List<Country?>?
            try {
                val countriesResponse = fetchCountries()
                if (countriesResponse.isSuccessful && countriesResponse.body() != null) {
                    val body = countriesResponse.body()
                    if (body?.status == NETWORK_SUCCESS) {
                        countries = body.data
                    } else {
                        networkState.postValue(Event(NetworkState.error(body?.message)))
                        return@launch
                    }
                } else {
                    networkState.postValue(Event(NetworkState.error("Response is null")))
                    return@launch
                }
            } catch (e: Exception) {
                networkState.postValue(Event(NetworkState.error(e.message)))
                return@launch
            }

            if (countries == null) {
                networkState.postValue(Event(NetworkState.error("Some error occurred")))
                return@launch
            }

            countriesGlobal.postValue(countries)
            networkState.postValue(Event(NetworkState.LOADED))
        }
        return networkState
    }
}