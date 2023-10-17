package com.anafthdev.imget.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anafthdev.imget.data.model.WImage
import com.anafthdev.imget.data.repository.WImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val wImageRepository: WImageRepository
): ViewModel() {

    val images = wImageRepository.getAllImage()

    fun addImages(images: Array<WImage>) {
        viewModelScope.launch { wImageRepository.insertWImages(*images) }
    }

    fun updateImages(images: Array<WImage>) {
        viewModelScope.launch { wImageRepository.updateWImages(*images) }
    }

    fun removeImages(images: Array<WImage>) {
        viewModelScope.launch { wImageRepository.deleteWImages(*images) }
    }

}