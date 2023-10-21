package com.anafthdev.imget.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Image that will be displayed in the widget
 *
 * @param filePath image path
 * @param order the order in which the image should be displayed in the widget.
 * @param showInWidget true if the image will be displayed to the widget, false otherwise
 */
@Entity(tableName = "wimage")
data class WImage(
    @PrimaryKey
    @ColumnInfo(name = "id_wimage") val id: Int,
    @ColumnInfo(name = "order_wimage") val order: Int,
    @ColumnInfo(name = "filePath_wimage") val filePath: String,
    @ColumnInfo(name = "showInWidget_wimage") val showInWidget: Boolean
)
