/*
 * Created by Hendry Syamsudin on 22/12/20 17:36
 * Copyright (c) APP Sinarmas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

package id.co.app.source.ui.main

import android.app.DownloadManager
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import id.co.app.components.player.CryptoUtil
import id.co.app.source.databinding.ActivityMainBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val url = "https://drive.google.com/file/d/1T92LHvCLJHlnhbFn0rLLBZo7ffVsLrMR/view?usp=sharing"

    private val password = "encryptPassword1"
    private val path by lazy { File(application.getExternalFilesDir(null), "videos") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(!path.exists()) path.mkdir()
        val FILE = File(path, "bigbunny.mp4")

        val FILE_ENCRYPTED = File(path, "bigbunny-encrypted.mp4")

        binding.secureAppPlayer.setLifecycleOwner(this)
        if(!FILE.exists()) {
            downloadFile(url, FILE)
        }

        if(!FILE_ENCRYPTED.exists())
            encryptBackupFile(FILE_ENCRYPTED)

        binding.secureAppPlayer.setVideo(FILE_ENCRYPTED.toUri(), password)
        binding.secureAppPlayer.play()
    }

    private fun downloadFile(url: String, outputFile: File) {
        try {
            val request1: DownloadManager.Request = DownloadManager.Request(url.toUri())
            request1.setDescription("Sample Music File") //appears the same in Notification bar while downloading

            request1.setTitle("bigbunny.mp4")

            request1.setDestinationInExternalFilesDir(applicationContext, "/videos/", "bigbunny.mp4")

            val manager1: DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            manager1.enqueue(request1)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            return  // swallow a 404
        } catch (e: IOException) {
            e.printStackTrace()
            return  // swallow a 404
        }
    }

    private fun encryptBackupFile(backuppath: File) {
        try {
            val path = File(application.getExternalFilesDir(null), "videos")
            val FILE = File(path, "bigbunny.mp4")
            CryptoUtil.encrypt(password, FILE, backuppath)

        } catch (e: Exception) {
            e.printStackTrace()
            return
            //    throw Exception("error during encryption: $e")
        }
    }
}