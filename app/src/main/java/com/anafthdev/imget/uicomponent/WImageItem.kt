package com.anafthdev.imget.uicomponent

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import com.anafthdev.imget.data.model.WImage

@Composable
fun WImageItem(
    wImage: WImage,
    modifier: Modifier = Modifier
) {

    val bitmap = remember(wImage.filePath) {
        // Get image bitmap from file path
        BitmapFactory.decodeFile(wImage.filePath)
    }

    if (bitmap != null) {
        // Find bitmap aspect ratio
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
                .clip(RoundedCornerShape(8))
        )
    }
}