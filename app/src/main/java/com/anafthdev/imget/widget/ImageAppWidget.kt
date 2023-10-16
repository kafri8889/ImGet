package com.anafthdev.imget.widget

import android.content.Context
import android.graphics.BitmapFactory
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize

class ImageAppWidget: GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WidgetContent(
                context = context
            )
        }
    }

    @Composable
    private fun WidgetContent(context: Context) {
        Image(
            provider = ImageProvider(context.assets.open("linhui.jpg").use(BitmapFactory::decodeStream)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = GlanceModifier
                .fillMaxSize()
        )
    }

}
