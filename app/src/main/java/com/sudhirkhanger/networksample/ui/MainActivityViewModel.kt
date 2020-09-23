package com.sudhirkhanger.networksample.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sudhirkhanger.networksample.NetworkSampleRepository
import kotlinx.coroutines.Dispatchers

class MainActivityViewModel internal constructor(repository: NetworkSampleRepository) :
    ViewModel() {

    private val countriesGlobalLiveData = MutableLiveData(repository.getCountries(Dispatchers.IO))
    val countries = MediatorLiveData<List<Country?>>()
    val networkState = countriesGlobalLiveData.switchMap { it.networkState }

    init {
        countriesGlobalLiveData.value?.data?.let { countriesLiveData ->
            countries.addSource(countriesLiveData) {
                countries.value = it
            }
        }
    }

    fun refresh() = countriesGlobalLiveData.value?.refresh?.invoke()

    fun search(query: String) {
        val countries = countriesGlobalLiveData.value?.data?.value
        val filteredList =
            countries?.filter { country -> country?.name?.contains(query, true) ?: false }
        this.countries.value = filteredList
    }

    fun clearSearch() =
        countriesGlobalLiveData.value?.data?.let { countriesLiveData ->
            countries.value = countriesLiveData.value
        }
}
