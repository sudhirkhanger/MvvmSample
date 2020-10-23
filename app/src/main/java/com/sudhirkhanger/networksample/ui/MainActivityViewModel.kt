package com.sudhirkhanger.networksample.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sudhirkhanger.networksample.NetworkSampleRepository
import kotlinx.coroutines.Dispatchers

class MainActivityViewModel internal constructor(
    repository: NetworkSampleRepository
) : ViewModel() {

    private val countriesListing = MutableLiveData(repository.getCountries(Dispatchers.IO))
    val countries = MediatorLiveData<List<Country?>>()
    val networkState = countriesListing.switchMap { it.networkState }
    fun refresh() = countriesListing.value?.refresh?.invoke()

    init {
        countriesListing.value?.data?.apply {
            countries.addSource(this) { countries.value = it }
        }

        if (countriesListing.value?.data?.value?.isEmpty() == true) refresh()
    }

    fun search(query: String) {
        val countries = countriesListing.value?.data?.value
        val filteredList = countries?.filter { country ->
            country?.name?.contains(query, true) ?: false
        }
        this.countries.value = filteredList
    }

    fun clearSearch() {
        countries.value = countriesListing.value?.data?.value
    }
}
