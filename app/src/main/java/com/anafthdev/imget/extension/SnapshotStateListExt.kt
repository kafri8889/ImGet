package com.anafthdev.imget.extension

import androidx.compose.runtime.snapshots.SnapshotStateList

fun <T> SnapshotStateList<T>.swap(newList: List<T>) {
    clear()
    addAll(newList)
}
