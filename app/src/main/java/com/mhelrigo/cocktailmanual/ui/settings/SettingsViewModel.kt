package com.mhelrigo.cocktailmanual.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mhelrigo.cocktailmanual.domain.entity.SettingEntity
import mhelrigo.cocktailmanual.domain.usecase.settings.GetSettings
import mhelrigo.cocktailmanual.domain.usecase.settings.ModifySettings
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    var getSettings: GetSettings,
    var modifySetting: ModifySettings
) : ViewModel() {
    private val _isNightMode = MutableLiveData<Boolean>()
    val isNightMode: LiveData<Boolean> get() = _isNightMode

    private val _isNetworkAvailable = MutableStateFlow(true)
    val isNetworkAvailable: StateFlow<Boolean> get() = _isNetworkAvailable

    fun requestForSettings() = viewModelScope.launch {
        getSettings.buildExecutable(GetSettings.Params()).collect {
            _isNightMode.postValue(it.isNightMode)
        }
    }

    fun toggleNightMode(p0: Boolean) = viewModelScope.launch {
        val v0 = SettingEntity(p0)
        modifySetting.buildExecutable(ModifySettings.Params(v0))
        _isNightMode.postValue(p0)
    }

    fun changeNetworkAvailability(p0: Boolean) {
        viewModelScope.launch {
            _isNetworkAvailable.emit(p0)
        }
    }
}