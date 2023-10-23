package com.anafthdev.imget.common

import android.content.Context
import android.content.Intent
import com.anafthdev.imget.widget.ImageAppWidgetReceiver
import javax.inject.Inject

/**
 * Used for updating widget state, e.g from view model
 */
class WidgetStateManager @Inject constructor(
    private val context: Context
) {

    fun updateState(receiver: Class<*>) {
        // send broadcast to widget receiver
        context.sendBroadcast(
            Intent(context, receiver).apply {
                action = ImageAppWidgetReceiver.ACTION_UPDATE_STATE
            }
        )
    }

}