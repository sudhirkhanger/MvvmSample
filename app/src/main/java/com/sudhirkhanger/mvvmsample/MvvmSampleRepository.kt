package com.sudhirkhanger.mvvmsample

import androidx.lifecycle.MutableLiveData
import com.sudhirkhanger.mvvmsample.network.CountryService
import com.sudhirkhanger.mvvmsample.network.model.Listing
import com.sudhirkhanger.mvvmsample.network.model.NetworkState
import com.sudhirkhanger.mvvmsample.ui.Country
import com.sudhirkhanger.mvvmsample.utils.Event
import com.sudhirkhanger.mvvmsample.utils.NETWORK_SUCCESS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MvvmSampleRepository private constructor(
    private val countryService: CountryService
) {

    companion object {
        @Volatile
        private var instance: MvvmSampleRepository? = null

        fun getInstance(countryService: CountryService) =
            instance
                ?: synchronized(this) {
                    instance
                        ?: MvvmSampleRepository(countryService)
                            .also { instance = it }
                }
    }

    private suspend fun fetchCountries() = countryService.countries()
    private val countries = MutableLiveData<List<Country?>>(listOf())
    private val networkState = MutableLiveData<Event<NetworkState>>()

    fun getCountries(coroutineContext: CoroutineContext): Listing<Country> {
        return Listing(
            data = countries,
            networkState = networkState,
            refresh = { refresh(coroutineContext) })
    }

    private fun refresh(coroutineContext: CoroutineContext) {
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

            this@MvvmSampleRepository.countries.postValue(countries)
            networkState.postValue(Event(NetworkState.SUCCESS))
        }
    }
}