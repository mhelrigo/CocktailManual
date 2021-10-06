package com.mhelrigo.cocktailmanual.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor() : ViewModel() {
    private val _isNightMode = MutableLiveData<Boolean>()
    val isNightMode: LiveData<Boolean> get() = _isNightMode

    private val _isNetworkAvailable = MutableStateFlow(true)
    val isNetworkAvailable: StateFlow<Boolean> get() = _isNetworkAvailable

    fun toggleNightMode(a0: Boolean) {
        _isNightMode.postValue(a0)
    }

    fun changeNetworkAvailability(p0: Boolean) {
        viewModelScope.launch {
            _isNetworkAvailable.emit(p0)
        }
    }
}