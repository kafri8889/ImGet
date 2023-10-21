package com.anafthdev.imget.ui.home

import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.imget.common.FileManager
import com.anafthdev.imget.common.NoCompareMutableStateFlow
import com.anafthdev.imget.data.model.WImage
import com.anafthdev.imget.data.repository.WImageRepository
import com.anafthdev.imget.extension.swap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val wImageRepository: WImageRepository,
    private val fileManager: FileManager
): ViewModel() {

    /**
     * "images" that will be displayed on the UI
     */
    val images = mutableStateListOf<WImage>()

    /**
     * Private mutable state flow used to manage a list of [WImage] objects.
     * This state flow is intended for internal use within the [HomeViewModel] to track
     * the list of images that are not yet persisted to the database.
     */
    private val _fImages = NoCompareMutableStateFlow(emptyList<WImage>())
    private val fImages: StateFlow<List<WImage>> = _fImages

    init {
        viewModelScope.launch(Dispatchers.IO) {
            wImageRepository.getAllImage().collectLatest { wImages ->
                withContext(Dispatchers.Main) {
                    // update "images" and sort by order
                    images.swap(wImages.sortedBy { it.order })
                }
            }
        }

        viewModelScope.launch {
            // wait until there is no change before updating images to db
            fImages.debounce(800).collectLatest { wImages ->
                // update images to db
                wImageRepository.updateWImages(*wImages.toTypedArray())
            }
        }
    }

    fun addImages(images: List<WImage>) {
        viewModelScope.launch { wImageRepository.insertWImages(*images.toTypedArray()) }
    }

    fun removeImages(images: List<WImage>) {
        viewModelScope.launch { wImageRepository.deleteWImages(*images.toTypedArray()) }
    }

    fun moveImage(from: Int, to: Int) {
        images.apply {
            add(to, removeAt(from))

            // update images
            swap(
                // Update images order
                mapIndexed { index, wImage ->
                    wImage.copy(
                        order = index
                    )
                }.also { updatedImages ->
                    // emit to _fImages
                    viewModelScope.launch { _fImages.emit(updatedImages) }
                }
            )
        }
    }

    fun copyUriContentsAndAddImages(uris: List<Uri>) {
        addImages(
            fileManager.copyContent(uris).mapIndexed { i, file ->
                WImage(
                    id = Random.nextInt(),
                    order = images.lastIndex + i + 1,
                    filePath = file.path,
                    showInWidget = true
                )
            }
        )
    }

}