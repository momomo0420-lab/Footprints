package com.example.footprints.ui.main

import android.location.Location
import androidx.lifecycle.*
import androidx.work.WorkManager
import com.example.footprints.model.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: LocationRepository,
    private val workManager: WorkManager
) : ViewModel() {
    private val _startButtonIsEnabled = MutableLiveData(true)
    val startButtonIsEnabled: LiveData<Boolean> = _startButtonIsEnabled

    private val _stopButtonIsEnabled = MutableLiveData(false)
    val stopButtonIsEnabled: LiveData<Boolean> = _stopButtonIsEnabled

    private val _currentAddress = MutableLiveData("")
    val currentAddress: LiveData<String> = _currentAddress

    val myLocationList = repository.loadAll().asLiveData()

    private fun switchRunnableFlag() {
        _startButtonIsEnabled.value = !startButtonIsEnabled.value!!
        _stopButtonIsEnabled.value = !stopButtonIsEnabled.value!!
    }

    fun startLocationUpdate() {
        repository.startLocationUpdate(listener)
        switchRunnableFlag()
    }

    private val listener = object : (Location) -> Unit {
        override fun invoke(location: Location) {
            onLocationUpdate(location)
        }
    }

    private fun onLocationUpdate(location: Location) {
        val address = repository.convertLocationToAddress(location)
        _currentAddress.value = address

        viewModelScope.launch {
            try {
                val lastLocation = repository.loadLastLocation()
                if(address == lastLocation) {
                    return@launch
                }
                repository.insert(location, address)

            } catch (e: Exception) {
            }
        }
    }

    fun stopLocationUpdate() {
        repository.stopLocationUpdate()
        switchRunnableFlag()
    }
}