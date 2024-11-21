package org.datepollsystems.waiterrobot.mediator.utils

import org.datepollsystems.waiterrobot.mediator.App
import java.io.File
import java.io.RandomAccessFile
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import kotlin.system.exitProcess

object SingleAppInstanceLock {
    private var file: File? = null
    private var channel: FileChannel? = null
    private var lock: FileLock? = null

    fun ensureSingleInstance() {
        if (tryLock()) {
            // This process holds the lock and is the single instance allowed to run
            Runtime.getRuntime().addShutdownHook(object : Thread() {
                // destroy the lock when the JVM is closing
                override fun run() {
                    closeLock()
                    deleteFile()
                }
            })
        } else {
            // There is already another instance of this app running -> exit this process
            println("Kellner.team is already running. Closing this instance.")
            exitProcess(1)
        }
    }

    private fun tryLock(): Boolean {
        return try {
            file = File(App.config.basePath, "kellner.team.lock")
            channel = RandomAccessFile(file, "rw").getChannel()
            lock = channel!!.tryLock()

            if (lock == null) {
                closeLock()
                return false
            }

            true
        } catch (_: Exception) {
            closeLock()
            false
        }
    }

    private fun closeLock() {
        try {
            lock?.release()
        } catch (_: Exception) {
        }
        try {
            channel?.close()
        } catch (_: Exception) {
        }
    }

    private fun deleteFile() {
        try {
            file?.delete()
        } catch (_: Exception) {
        }
    }
}
