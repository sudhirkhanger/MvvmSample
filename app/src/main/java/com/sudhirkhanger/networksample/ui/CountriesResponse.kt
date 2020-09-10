package com.sudhirkhanger.networksample.ui

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountriesResponse(

    @field:SerializedName("data")
	val data: List<Country?>? = null,

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("status")
	val status: String? = null
) : Parcelable

@Parcelize
data class Country(

	@field:SerializedName("back_image")
	val backImage: String? = null,

	@field:SerializedName("icon_image")
	val iconImage: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("title")
	val title: String? = null
) : Parcelable
