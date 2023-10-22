package com.anafthdev.imget.ui.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.anafthdev.imget.data.Constant
import com.anafthdev.imget.data.SwitchImageMode
import com.anafthdev.imget.theme.ImGetTheme
import com.anafthdev.imget.uicomponent.SettingItem
import kotlin.math.ceil

@Preview(showSystemUi = true)
@Composable
private fun SettingScreenContentPreview() {

    ImGetTheme(darkTheme = false) {
        SettingScreenContent(
            widgetRoundCornersInDp = 8,
            imageSwitchingIntervalInSecond = 5,
            selectedSwitchImageMode = SwitchImageMode.Click
        )
    }
}

@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    onNavigationIconClicked: () -> Unit
) {

    SettingScreenContent(
        widgetRoundCornersInDp = viewModel.widgetRoundCornersInDp,
        imageSwitchingIntervalInSecond = viewModel.imageSwitchingIntervalInSecond,
        selectedSwitchImageMode = viewModel.selectedSwitchImageMode,
        onWidgetRoundCornersInDpChange = viewModel::updateWidgetRoundCornersInDp,
        onImageSwitchingIntervalInSecondChange = viewModel::updateImageSwitchingIntervalInSecond,
        onSelectedSwitchImageModeChange = viewModel::updateSelectedSwitchImageMode,
        onNavigationIconClicked = onNavigationIconClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingScreenContent(
    widgetRoundCornersInDp: Int,
    imageSwitchingIntervalInSecond: Int,
    selectedSwitchImageMode: SwitchImageMode,
    onNavigationIconClicked: () -> Unit = {},
    onWidgetRoundCornersInDpChange: (Int) -> Unit = {},
    onImageSwitchingIntervalInSecondChange: (Int) -> Unit = {},
    onSelectedSwitchImageModeChange: (SwitchImageMode) -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .systemBarsPadding()
        ) {
            item {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onNavigationIconClicked) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                )
            }

            item {
                SettingItem(
                    title = "Widget round corners",
                    summary = "Add corners to widget",
                    value = {
                        Text(
                            text = "$widgetRoundCornersInDp Dp",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                            )
                        )
                    },
                    content = {
                        Slider(
                            value = widgetRoundCornersInDp.toFloat(),
                            valueRange = Constant.MIN_WIDGET_CORNER_SIZE_IN_DP..Constant.MAX_WIDGET_CORNER_SIZE_IN_DP,
                            steps = Constant.MAX_WIDGET_CORNER_SIZE_IN_DP.toInt() - Constant.MIN_WIDGET_CORNER_SIZE_IN_DP.toInt(),
                            onValueChange = { value ->
                                onWidgetRoundCornersInDpChange(value.toInt())
                            }
                        )
                    }
                )
            }

            item {
                SettingItem(
                    title = "Image switching interval",
                    summary = "Switch image every $imageSwitchingIntervalInSecond seconds",
                    value = {
                        Text(
                            text = "$imageSwitchingIntervalInSecond Second",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.primary,
                            )
                        )
                    },
                    content = {
                        Slider(
                            value = imageSwitchingIntervalInSecond.toFloat(),
                            valueRange = Constant.MIN_INTERVAL_TO_SWITCH_IMAGE.toFloat()..Constant.MAX_INTERVAL_TO_SWITCH_IMAGE.toFloat(),
                            steps = (Constant.MAX_INTERVAL_TO_SWITCH_IMAGE / Constant.MIN_INTERVAL_TO_SWITCH_IMAGE) - 2,
                            onValueChange = { value ->
                                onImageSwitchingIntervalInSecondChange(ceil(value).toInt())
                            }
                        )
                    }
                )
            }

            item {
                SettingItem(
                    title = "Widget switch mode",
                    summary = "Mode for switching image in widget",
                    value = {
                        SingleChoiceSegmentedButtonRow {
                            SegmentedButton(
                                shape = RoundedCornerShape(topStartPercent = 100, bottomStartPercent = 100),
                                selected = selectedSwitchImageMode == SwitchImageMode.Click,
                                icon = {},
                                onClick = {
                                    onSelectedSwitchImageModeChange(SwitchImageMode.Click)
                                }
                            ) {
                                Text(text = "Click")
                            }

                            SegmentedButton(
                                shape = RoundedCornerShape(topEndPercent = 100, bottomEndPercent = 100),
                                selected = selectedSwitchImageMode == SwitchImageMode.Timer,
                                icon = {},
                                onClick = {
                                    onSelectedSwitchImageModeChange(SwitchImageMode.Timer)
                                }
                            ) {
                                Text(text = "Timer")
                            }
                        }
                    }
                )
            }
        }
    }
}
