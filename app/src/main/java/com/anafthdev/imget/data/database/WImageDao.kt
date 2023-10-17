package com.anafthdev.imget.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.anafthdev.imget.data.model.WImage
import kotlinx.coroutines.flow.Flow

@Dao
interface WImageDao {

    @Query("SELECT * FROM wimage")
    fun getAllImage(): Flow<List<WImage>>

    @Query("SELECT * FROM wimage WHERE showInWidget_wimage=1")
    fun getImagesForWidget(): Flow<List<WImage>>

    @Update
    suspend fun updateWImages(vararg images: WImage)

    @Delete
    suspend fun deleteWImages(vararg images: WImage)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWImages(vararg images: WImage)

}