package com.anafthdev.imget.ui.home

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.anafthdev.imget.R
import com.anafthdev.imget.data.ImGetDestination
import com.anafthdev.imget.data.model.WImage
import com.anafthdev.imget.uicomponent.WImageItem
import com.anafthdev.reorderable.ReorderableItem
import com.anafthdev.reorderable.animateDraggeableItemPlacement
import com.anafthdev.reorderable.detectReorderAfterLongPress
import com.anafthdev.reorderable.rememberReorderableLazyVerticalStaggeredGridState
import com.anafthdev.reorderable.reorderable

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navigateTo: (ImGetDestination) -> Unit
) {

    Box {
        if (viewModel.imageToDelete != null) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.imageToDelete = null
                },
                title = {
                    Text("Delete Image")
                },
                text = {
                    Text("Are you sure you want to delete this image?")
                },
                icon = {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = null,
                        modifier = Modifier
                            .size(32.dp)
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteImages(viewModel.imageToDelete!!)
                            viewModel.imageToDelete = null
                        }
                    ) {
                        Text("Ok")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            viewModel.imageToDelete = null
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        HomeScreenContent(
            images = viewModel.images,
            navigateTo = navigateTo,
            onImageMoved = viewModel::moveImage,
            onPhotoPickerResult = { imageUris ->
                // Save uris to database
                viewModel.copyUriContentsAndAddImages(imageUris)
            },
            onImageUpdated = { wImage ->
                viewModel.updateImages(wImage)
            },
            onDeleteClicked = { wImage ->
                viewModel.imageToDelete = wImage
            },
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContent(
    images: List<WImage>,
    modifier: Modifier = Modifier,
    navigateTo: (ImGetDestination) -> Unit = {},
    onPhotoPickerResult: (imageUris: List<Uri>) -> Unit = {},
    onImageUpdated: (updatedWImage: WImage) -> Unit = {},
    onImageMoved: (from: Int, to: Int) -> Unit = { _, _ -> },
    onDeleteClicked: (WImage) -> Unit = {}
) {

    val photoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenMultipleDocuments(),
        onResult = onPhotoPickerResult
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
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
                        navigateTo(ImGetDestination.Setting)
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
            images = images,
            onImageMoved = onImageMoved,
            onImageUpdated = onImageUpdated,
            onDeleteClicked = onDeleteClicked,
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight()
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyStaggeredGridImage(
    images: List<WImage>,
    onImageMoved: (from: Int, to: Int) -> Unit,
    onImageUpdated: (updatedWImage: WImage) -> Unit,
    onDeleteClicked: (WImage) -> Unit,
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
            ReorderableItem(
                key = wImage.id,
                orientationLocked = false,
                state = reorderableLazyVerticalStaggeredGridState,
                modifier = Modifier
                    .animateDraggeableItemPlacement()
            ) { _ ->
                WImageItem(
                    wImage = wImage,
                    isDragging = reorderableLazyVerticalStaggeredGridState.draggingItemIndex != null,
                    onWImageUpdated = onImageUpdated,
                    onDeleteClicked = {
                        onDeleteClicked(wImage)
                    },
                    modifier = Modifier
                        .detectReorderAfterLongPress(reorderableLazyVerticalStaggeredGridState)
                )
            }
        }
    }
}
