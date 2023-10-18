package com.anafthdev.imget.ui.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.imget.common.FileManager
import com.anafthdev.imget.data.model.WImage
import com.anafthdev.imget.data.repository.WImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val wImageRepository: WImageRepository,
    private val fileManager: FileManager
): ViewModel() {

    val images = wImageRepository.getAllImage()

    fun addImages(images: List<WImage>) {
        viewModelScope.launch { wImageRepository.insertWImages(*images.toTypedArray()) }
    }

    fun updateImages(images: List<WImage>) {
        viewModelScope.launch { wImageRepository.updateWImages(*images.toTypedArray()) }
    }

    fun removeImages(images: List<WImage>) {
        viewModelScope.launch { wImageRepository.deleteWImages(*images.toTypedArray()) }
    }

    fun copyUriContentsAndAddImages(uris: List<Uri>) {
        addImages(
            fileManager.copyContent(uris).map { file ->
                WImage(
                    id = Random.nextInt(),
                    filePath = file.path,
                    showInWidget = true
                )
            }
        )
    }

}