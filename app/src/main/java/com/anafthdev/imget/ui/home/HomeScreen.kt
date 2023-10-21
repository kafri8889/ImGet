package com.anafthdev.imget.ui.home

import android.graphics.BitmapFactory
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.anafthdev.imget.R
import com.anafthdev.imget.data.model.WImage
import com.anafthdev.reorderable.ReorderableItem
import com.anafthdev.reorderable.detectReorderAfterLongPress
import com.anafthdev.reorderable.rememberReorderableLazyVerticalStaggeredGridState
import com.anafthdev.reorderable.reorderable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = { imageUris ->
            // Save uris to database
            viewModel.copyUriContentsAndAddImages(imageUris)
        }
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        TopAppBar(
            title = {
                Text(stringResource(id = R.string.app_name))
            },
            actions = {
                IconButton(
                    onClick = {
                        photoPicker.launch(arrayOf("image/*"))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = null
                    )
                }

                IconButton(
                    onClick = {

                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_setting),
                        contentDescription = null
                    )
                }
            }
        )

        LazyStaggeredGridImage(
            images = viewModel.images.collectAsStateWithLifecycle(emptyList()).value,
            onImageMoved = { from, to ->

            },
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight()
        )

//        FilledTonalButton(
//            shape = RoundedCornerShape(25),
//            onClick = {
//
//            },
//            modifier = Modifier
//                .fillMaxWidth(0.92f)
//        ) {
//            Text(text = "Add Image to widget")
//        }
    }
}

@Composable
private fun LazyStaggeredGridImage(
    images: List<WImage>,
    onImageMoved: (from: Int, to: Int) -> Unit,
    modifier: Modifier = Modifier
) {

    val reorderableLazyVerticalStaggeredGridState = rememberReorderableLazyVerticalStaggeredGridState(
        onMove = { from, to ->
            onImageMoved(from.index, to.index)
        }
    )

    LazyVerticalStaggeredGrid(
        state = reorderableLazyVerticalStaggeredGridState.gridState,
        columns = StaggeredGridCells.Fixed(2),
        contentPadding = PaddingValues(bottom = 16.dp),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .reorderable(reorderableLazyVerticalStaggeredGridState)
    ) {
        items(
            items = images,
            key = { item -> item.id }
        ) { wImage ->
            val bitmap = remember(wImage.filePath) {
                // Get image bitmap from file path
                BitmapFactory.decodeFile(wImage.filePath)
            }

            if (bitmap != null) {
                // Find bitmap aspect ratio
                val aspectRatio = bitmap.width.toFloat() / bitmap.height.toFloat()

                ReorderableItem(
                    state = reorderableLazyVerticalStaggeredGridState,
                    key = wImage.id
                ) { isDragging ->
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .detectReorderAfterLongPress(reorderableLazyVerticalStaggeredGridState)
                            .fillMaxWidth()
                            .aspectRatio(aspectRatio)
                            .clip(RoundedCornerShape(8))
                    )
                }
            }
        }
    }
}
