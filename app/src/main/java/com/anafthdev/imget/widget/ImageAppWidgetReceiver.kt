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
import com.anafthdev.imget.data.repository.WImageRepository
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ImageAppWidgetReceiver: GlanceAppWidgetReceiver() {

    @Inject
    lateinit var wImageRepository: WImageRepository

    @Inject
    lateinit var imageScheduler: ImageScheduler

    private val coroutineScope = MainScope()

    override val glanceAppWidget: GlanceAppWidget
        get() = ImageAppWidget()

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        observe(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        observe(context)

        when (intent.action) {
            ImageScheduler.ACTION_INCREMENT_ORDER -> {
                coroutineScope.launch {
                    // Get glance id from class
                    GlanceAppWidgetManager(context).getGlanceIds(ImageAppWidget::class.java).firstOrNull()?.let { glanceId ->
                        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                            prefs.toMutablePreferences().apply {
                                this[currentOrder] = this[currentOrder]?.plus(1) ?: 0
                            }
                        }

                        Timber.i("glance: update widget with id $glanceId")
                        glanceAppWidget.update(context, glanceId)
                    }
                }
            }
        }
    }

    private fun observe(context: Context) {
        coroutineScope.launch {
            // Get glance id from class
            GlanceAppWidgetManager(context).getGlanceIds(ImageAppWidget::class.java).firstOrNull()?.let { glanceId ->
                // Observe images
                wImageRepository.getImagesForWidget()
                    .map { it.sortedBy { it.order } }
                    .collect { images ->
                        updateAppWidgetState(context, PreferencesGlanceStateDefinition, glanceId) { prefs ->
                            prefs.toMutablePreferences().apply {
                                this[wImages] = Gson().toJson(images)
                            }
                        }

                        Timber.i("glance: update widget with id $glanceId")
                        glanceAppWidget.update(context, glanceId)
                    }
            }
        }
    }

    companion object {
        val wImages = stringPreferencesKey("images")
        val currentOrder = intPreferencesKey("current_order")
    }
}