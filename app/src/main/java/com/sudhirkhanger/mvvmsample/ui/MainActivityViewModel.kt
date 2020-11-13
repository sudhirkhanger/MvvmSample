package com.sudhirkhanger.mvvmsample.ui

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.sudhirkhanger.mvvmsample.MvvmSampleRepository
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class MainActivityViewModel internal constructor(
    repository: MvvmSampleRepository
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
        Timber.e(query)
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
