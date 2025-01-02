package com.example.myapplication

import android.os.Environment
import java.io.File
import java.io.FileWriter
import java.io.IOException


class LogHelper {
    fun log(message: String?) {
        try {
            val logFile: File = File(getFilePath())
            val writer = FileWriter(logFile, true) // Append to file
            writer.write(message.toString() + "\n")
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private val instance = LogHelper()

        fun getInstance(): LogHelper {
            return instance
        }

  

        private fun getFilePath(): String {
            // Hier den richtigen Pfad zur Datei setzen
            return Environment.getExternalStorageDirectory().toString() + "/" + "app_log.txt"
        }
    }
}