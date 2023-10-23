package com.anafthdev.imget.ui.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.imget.common.WidgetStateManager
import com.anafthdev.imget.data.Constant
import com.anafthdev.imget.data.SwitchImageMode
import com.anafthdev.imget.data.datastore.UserPreferenceDataStore
import com.anafthdev.imget.widget.ImageAppWidgetReceiver
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userPreferenceDataStore: UserPreferenceDataStore,
    private val widgetStateManager: WidgetStateManager
): ViewModel() {

    var widgetRoundCornersInDp by mutableIntStateOf(Constant.MIN_WIDGET_CORNER_SIZE_IN_DP.toInt())
    var imageSwitchingIntervalInSecond by mutableIntStateOf(Constant.MIN_INTERVAL_TO_SWITCH_IMAGE)
    var selectedSwitchImageMode by mutableStateOf(SwitchImageMode.Timer)

    init {
        viewModelScope.launch {
            combine(
                userPreferenceDataStore.getWidgetRoundCornersInDp,
                userPreferenceDataStore.getImageSwitchingIntervalInSecond,
                userPreferenceDataStore.getSelectedSwitchImageMode,
            ) { dp, sec, mode ->
                Triple(dp, sec, mode)
            }.collect { (dp, sec, mode) ->
                widgetStateManager.updateState(ImageAppWidgetReceiver::class.java)

                widgetRoundCornersInDp = dp
                imageSwitchingIntervalInSecond = sec
                selectedSwitchImageMode = mode
            }
        }
    }

    fun updateWidgetRoundCornersInDp(value: Int) {
        viewModelScope.launch { userPreferenceDataStore.setWidgetRoundCornersInDp(value) }
    }

    fun updateImageSwitchingIntervalInSecond(value: Int) {
        viewModelScope.launch { userPreferenceDataStore.setImageSwitchingIntervalInSecond(value) }
    }

    fun updateSelectedSwitchImageMode(value: SwitchImageMode) {
        viewModelScope.launch { userPreferenceDataStore.setSelectedSwitchImageMode(value) }
    }


}