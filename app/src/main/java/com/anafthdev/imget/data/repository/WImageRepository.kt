package com.anafthdev.imget.data.repository

import com.anafthdev.imget.data.database.WImageDao
import com.anafthdev.imget.data.model.WImage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WImageRepository @Inject constructor(
    private val wImageDao: WImageDao
) {

    fun getAllImage(): Flow<List<WImage>> = wImageDao.getAllImage()

    fun getImagesForWidget(): Flow<List<WImage>> = wImageDao.getImagesForWidget()

    suspend fun updateWImages(vararg images: WImage) = wImageDao.updateWImages(*images)

    suspend fun deleteWImages(vararg images: WImage) = wImageDao.deleteWImages(*images)

    suspend fun insertWImages(vararg images: WImage) = wImageDao.insertWImages(*images)

}