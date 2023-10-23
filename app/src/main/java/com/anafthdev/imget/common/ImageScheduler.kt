package com.anafthdev.imget.common

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.anafthdev.imget.widget.ImageAppWidgetReceiver
import javax.inject.Inject

class ImageScheduler @Inject constructor(
    private val context: Context
) {

    private var alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    private fun getPendingIntent(): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, ImageAppWidgetReceiver::class.java).apply {
                action = ImageAppWidgetReceiver.ACTION_INCREMENT_ORDER
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    /**
     * Start scheduler
     */
    fun start(triggerAt: Long) {
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            triggerAt,
            getPendingIntent()
        )
    }

}