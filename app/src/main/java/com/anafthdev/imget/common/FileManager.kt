package com.anafthdev.imget.common

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class FileManager @Inject constructor(private val context: Context) {

    /**
     * Copy uri contents to app storage
     *
     * @return converted uri to file
     */
    fun copyContent(uris: List<Uri>): List<File> {
        val files = arrayListOf<File>()

        // The directory that will be used to store the image selected by the user
        val dir = getParentDir(context).also { dir ->
            // If directory not exists, create it
            if (!dir.exists()) dir.mkdir()
        }

        for (uri in uris) {
            val file = File(dir, "${System.currentTimeMillis()}.jpg")

            // If true, file is successfully created, otherwise file is exists
            if (!file.createNewFile()) continue

            val out = FileOutputStream(file)
            val inp = context.contentResolver.openInputStream(uri)

            // If input stream not null, copy contents to output stream
            if (inp != null) out.use(inp::copyTo)

            inp?.close()
            files.add(file)
        }

        return files
    }

    companion object {

        /**
         * App directory for saved files
         */
        fun getParentDir(context: Context): File = File(context.filesDir, "wimage")

    }

}