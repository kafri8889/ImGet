package com.anafthdev.imget.uicomponent

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8))
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(8.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                    .zIndex(1f)
            ) {
                Text(
                    text = wImage.order.toString(),
                    style = MaterialTheme.typography.titleSmall
                )
            }

            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier
                    .sizeIn(minWidth = 96.dp, minHeight = 96.dp)
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio)
            )
        }
    }
}