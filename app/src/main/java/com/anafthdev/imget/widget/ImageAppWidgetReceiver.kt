package com.anafthdev.imget.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.state.PreferencesGlanceStateDefinition
import com.anafthdev.imget.common.ImageScheduler
import com.anafthdev.imget.data.datastore.UserPreferenceDataStore
import com.anafthdev.imget.data.repository.WImageRepository
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ImageAppWidgetReceiver: GlanceAppWidgetReceiver() {

    @Inject
    lateinit var wImageRepository: WImageRepository

    @Inject
    lateinit var userPreferenceDataStore: UserPreferenceDataStore

    @Inject
    lateinit var imageScheduler: ImageScheduler

    private val coroutineScope = MainScope()

    override val glanceAppWidget: GlanceAppWidget
        get() = ImageAppWidget()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        coroutineScope.launch {
            observe(context)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        coroutineScope.launch {
            observe(context)
        }
    }

    private suspend fun observe(context: Context) {
        // Get glance id from class
        GlanceAppWidgetManager(context).getGlanceIds(ImageAppWidget::class.java).firstOrNull()?.let { glanceId ->
            // Observe data
            combine(
                wImageRepository.getImagesForWidget().map { it.sortedBy { it.order } },
                userPreferenceDataStore.getWidgetRoundCornersInDp,
                userPreferenceDataStore.getSelectedSwitchImageMode,
            ) { images, dp, mode ->
                Triple(images, dp, mode)
            }.collect { (images, dp, mode) ->
                updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                    prefs.toMutablePreferences().apply {
                        this[wImages] = Gson().toJson(images)
                        this[widgetRoundCornerInDp] = dp
                        this[switchImageMode] = mode.ordinal
                    }
                }

                Timber.i("glance observe: update widget with id $glanceId")
                glanceAppWidget.update(context, glanceId)
            }
        }
    }

    companion object {
        const val ACTION_INCREMENT_ORDER = "com.anafthdev.imget.action.ACTION_INCREMENT_ORDER"
        const val ACTION_UPDATE_STATE = "com.anafthdev.imget.action.ACTION_UPDATE_STATE"

        val wImages = stringPreferencesKey("images")
        val currentOrder = intPreferencesKey("current_order")
        val switchImageMode = intPreferencesKey("switch_image_mode")
        val widgetRoundCornerInDp = intPreferencesKey("widget_round_corner")
    }
}