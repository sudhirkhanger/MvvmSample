package com.sudhirkhanger.networksample.utils

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    message: String,
    length: Int = Snackbar.LENGTH_SHORT
) = Snackbar.make(this, message, length).show()

fun View.showSnackbar(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_LONG
) = Snackbar.make(this, resources.getString(messageRes), length).show()