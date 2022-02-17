package com.example.footprints.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _locationClientIsEnabled = MutableLiveData(false)
    val locationClientIsEnabled: LiveData<Boolean> get() =  _locationClientIsEnabled

    fun setLocationClientIsEnabled(boolean: Boolean) {
        _locationClientIsEnabled.value = boolean
    }
}