package com.anafthdev.imget.widget

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.LayoutDirection
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.Text
import com.anafthdev.imget.data.SwitchImageMode
import com.anafthdev.imget.data.model.WImage
import com.google.gson.Gson
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import timber.log.Timber

class ImageAppWidget: GlanceAppWidget() {

    private val coroutineScope = MainScope()

    override val stateDefinition: GlanceStateDefinition<*>
        get() = PreferencesGlanceStateDefinition

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            GlanceTheme {
                CompositionLocalProvider(
                    LocalLayoutDirection provides if (context.resources.configuration.layoutDirection == LayoutDirection.LTR) {
                        androidx.compose.ui.unit.LayoutDirection.Ltr
                    } else androidx.compose.ui.unit.LayoutDirection.Rtl
                ) {
                    WidgetContent(
                        context = context,
                        glanceId = id
                    )
                }
            }
        }
    }

    @Composable
    private fun WidgetContent(context: Context, glanceId: GlanceId) {

        val state = currentState<Preferences>()
        val wImage = remember(state[ImageAppWidgetReceiver.wImages], state[ImageAppWidgetReceiver.currentOrder]) {
            state[ImageAppWidgetReceiver.wImages].let { json ->
                Timber.i("glance: wImages = $json")

                val wImages = Gson().fromJson(json ?: "[]", Array<WImage>::class.java).toList()
                val currentOrder = (state[ImageAppWidgetReceiver.currentOrder] ?: 0).let {
                    // If current order is greater than last order, set to zero
                    if (it > wImages.lastIndex) {
                        coroutineScope.launch {
                            updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                                prefs.toMutablePreferences().apply {
                                    this[ImageAppWidgetReceiver.currentOrder] = 0
                                }
                            }
                        }

                        0
                    } else it
                }

                wImages.getOrNull(currentOrder)
            }
        }

        val clickableModifier = if (SwitchImageMode.entries[state[ImageAppWidgetReceiver.switchImageMode] ?: 0] == SwitchImageMode.Click) {
            GlanceModifier
                .clickable {
                    context.sendBroadcast(
                        Intent(context, ImageAppWidgetReceiver::class.java).apply {
                            action = ImageAppWidgetReceiver.ACTION_INCREMENT_ORDER
                        }
                    )
                }
        } else GlanceModifier

        if (wImage != null) {
            val bitmap = remember(wImage) {
                BitmapFactory.decodeFile(wImage.filePath)
            }

            Image(
                provider = ImageProvider(bitmap),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = GlanceModifier
                    .fillMaxSize()
                    .cornerRadius(8.dp)
                    .then(clickableModifier)
            )
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = GlanceModifier
                    .fillMaxSize()
                    .cornerRadius(8.dp)
                    .background(GlanceTheme.colors.primaryContainer)
            ) {
                Text(text = "Loading")
            }
        }
    }

}
