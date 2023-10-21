package com.anafthdev.imget.uicomponent

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.anafthdev.imget.data.model.WImage
import kotlinx.coroutines.launch

@Composable
fun WImageItem(
    wImage: WImage,
    modifier: Modifier = Modifier,
    isDragging: Boolean = false,
    onWImageUpdated: (WImage) -> Unit,
    onDeleteClicked: () -> Unit,
) {

    val coroutineScope = rememberCoroutineScope()
    val flippableCardState = rememberFlippableCardState()

    val bitmap = remember(wImage.filePath) {
        // Get image bitmap from file path
        BitmapFactory.decodeFile(wImage.filePath)
    }

    if (bitmap != null) {
        // Find bitmap aspect ratio
        val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

        LaunchedEffect(isDragging) {
            if (isDragging && flippableCardState.cardSide.isBack) flippableCardState.flip(tween(512))
        }

        FlippableCard(
            state = flippableCardState,
            front = {
                FrontContent(
                    wImage = wImage,
                    bitmap = bitmap,
                    isDragging = isDragging,
                    onMenuClick = {
                        coroutineScope.launch {
                            flippableCardState.flip(tween(512))
                        }
                    }
                )
            },
            back = {
                BackContent(
                    wImage = wImage,
                    onWImageUpdated = onWImageUpdated,
                    onDeleteClicked = onDeleteClicked,
                    onNavigationIconClicked = {
                        coroutineScope.launch {
                            flippableCardState.flip(tween(512))
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                )
            },
            modifier = modifier
                .sizeIn(minWidth = 96.dp, minHeight = 96.dp)
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
        )
    }
}

@Composable
private fun FrontContent(
    wImage: WImage,
    bitmap: Bitmap,
    isDragging: Boolean,
    modifier: Modifier = Modifier,
    onMenuClick: () -> Unit
) {
    Box(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .zIndex(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    label = "image_order",
                    visible = isDragging,
                    enter = scaleIn(tween(256)),
                    exit = scaleOut(tween(256))
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Text(
                            text = wImage.order.toString(),
                            style = MaterialTheme.typography.titleSmall
                        )
                    }
                }
            }

            FilledTonalIconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Rounded.MoreVert,
                    contentDescription = null
                )
            }
        }

        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
private fun BackContent(
    wImage: WImage,
    modifier: Modifier = Modifier,
    onWImageUpdated: (WImage) -> Unit,
    onDeleteClicked: () -> Unit,
    onNavigationIconClicked: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            IconButton(onClick = onNavigationIconClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = null
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth(0.92f)
        ) {
            Text(
                text = "Enable",
                style = MaterialTheme.typography.titleSmall
            )

            Switch(
                checked = wImage.showInWidget,
                onCheckedChange = { checked ->
                    onWImageUpdated(
                        wImage.copy(
                            showInWidget = checked
                        )
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onDeleteClicked,
            modifier = Modifier
                .fillMaxWidth(0.92f)
        ) {
            Text("Delete")
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}