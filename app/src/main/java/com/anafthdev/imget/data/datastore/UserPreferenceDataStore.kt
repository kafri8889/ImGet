package com.anafthdev.imget.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.anafthdev.imget.data.Constant
import com.anafthdev.imget.data.SwitchImageMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(
    name = "user_preferences"
)

class UserPreferenceDataStore @Inject constructor(
    private val context: Context
) {

    suspend fun setWidgetRoundCornersInDp(roundCorners: Int) {
        context.dataStore.edit { pref ->
            pref[widgetRoundCornersInDpPreference] = roundCorners
        }
    }

    suspend fun setImageSwitchingIntervalInSecond(interval: Int) {
        context.dataStore.edit { pref ->
            pref[imageSwitchingIntervalInSecondPreference] = interval
        }
    }

    suspend fun setSelectedSwitchImageMode(switchImageMode: SwitchImageMode) {
        context.dataStore.edit { pref ->
            pref[selectedSwitchImageModePreference] = switchImageMode.ordinal
        }
    }

    val getWidgetRoundCornersInDp: Flow<Int> = context.dataStore.data.map { pref ->
        pref[widgetRoundCornersInDpPreference] ?: Constant.MIN_WIDGET_CORNER_SIZE_IN_DP.toInt()
    }

    val getImageSwitchingIntervalInSecond: Flow<Int> = context.dataStore.data.map { pref ->
        pref[imageSwitchingIntervalInSecondPreference] ?: Constant.MIN_INTERVAL_TO_SWITCH_IMAGE
    }

    val getSelectedSwitchImageMode: Flow<SwitchImageMode> = context.dataStore.data.map { pref ->
        SwitchImageMode.entries[pref[selectedSwitchImageModePreference] ?: 0]
    }

    companion object {
        val widgetRoundCornersInDpPreference = intPreferencesKey("widgetRoundCornersInDp")
        val imageSwitchingIntervalInSecondPreference = intPreferencesKey("imageSwitchingIntervalInSecond")
        val selectedSwitchImageModePreference = intPreferencesKey("selectedSwitchImageMode")
    }

}