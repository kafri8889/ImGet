package com.anafthdev.imget.data.model

import com.anafthdev.imget.data.SwitchImageMode

data class UserPreference(
    val widgetRoundCornersInDp: Int,
    val imageSwitchingIntervalInSecond: Int,
    val selectedSwitchImageMode: SwitchImageMode,
)
