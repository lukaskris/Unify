/*
 * Created by Hendry Syamsudin on 22/12/20 17:36
 * Copyright (c) APP Sinarmas 2020. All rights reserved.
 * Last modified 22/12/20 17:34
 */

package id.co.app.source.ui.main

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.co.app.components.player.CryptoUtil
import id.co.app.source.databinding.ActivityMainBinding
import id.co.app.source.ui.icon.UnifyIconActivity
import id.co.app.source.ui.label.LabelActivity
import id.co.app.source.ui.textfield.TextFieldActivity
import id.co.app.source.ui.typography.TypographyActivity
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity(), MyRecyclerViewAdapter.ItemClickListener {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val url = "https://drive.google.com/file/d/1T92LHvCLJHlnhbFn0rLLBZo7ffVsLrMR/view?usp=sharing"

    private val password = "encryptPassword3"
    private val path by lazy { File(application.getExternalFilesDir(null), "videos") }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initList()
//        if(!path.exists()) path.mkdir()
//        val FILE = File(path, "bigbunny.mp4")
//
//        val FILE_ENCRYPTED = File(path, "bigbunny-encrypted.mp4")
//
//        binding.secureAppPlayer.setLifecycleOwner(this)
//        binding.secureAppPlayer.setVideo(FILE_ENCRYPTED.toUri(), password)
//        binding.secureAppPlayer.play()
//        if(!FILE.exists()) {
//            downloadFile(url, FILE)
//        }
//
//        if(!FILE_ENCRYPTED.exists())
//            encryptBackupFile(FILE_ENCRYPTED)

    }

    private fun initList(){

        val menu = ArrayList<String>()
        menu.add("Typography")
        menu.add("Button")
        menu.add("Label")
        menu.add("Unify Icon")
        menu.add("Timer New")
        menu.add("Loader Dialog")
        menu.add("TextField")
        menu.add("TextArea")

        val dividerItemDecoration = androidx.recyclerview.widget.DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        binding.recyclerView.addItemDecoration(dividerItemDecoration)
        val adapter = MyRecyclerViewAdapter(this, menu)
        adapter.setClickListener(this)
        binding.recyclerView.adapter = adapter
    }

    override fun onItemClick(view: View, position: Int) {
        startActivity(
            when(position){
                0 -> Intent(this, TypographyActivity::class.java)
                2 -> Intent(this, LabelActivity::class.java)
                6 -> Intent(this, TextFieldActivity::class.java)
                3 -> Intent(this, UnifyIconActivity::class.java)
                else -> Intent(this, TypographyActivity::class.java)
            }
        )
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