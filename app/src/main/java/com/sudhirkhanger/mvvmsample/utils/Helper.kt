package com.sudhirkhanger.mvvmsample.utils

import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    message: String,
    length: Int = Snackbar.LENGTH_SHORT
) = Snackbar.make(this, message, length).show()

fun View.showSnackbar(
    @StringRes messageRes: Int,
    length: Int = Snackbar.LENGTH_LONG
) = Snackbar.make(this, resources.getString(messageRes), length).show()

fun <T1, T2> combineTuple(
    f1: LiveData<T1>,
    f2: LiveData<T2>
): LiveData<Pair<T1?, T2?>> =
    MediatorLiveData<Pair<T1?, T2?>>().also { mediator ->
        mediator.value = Pair(f1.value, f2.value)

        mediator.addSource(f1) { t1: T1? ->
            val (_, t2) = mediator.value!!
            mediator.value = Pair(t1, t2)
        }

        mediator.addSource(f2) { t2: T2? ->
            val (t1, _) = mediator.value!!
            mediator.value = Pair(t1, t2)
        }
    }