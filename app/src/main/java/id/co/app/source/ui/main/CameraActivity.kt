package id.co.app.source.ui.main

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import id.co.app.camera.CameraFragment
import id.co.app.source.R
import id.co.app.source.databinding.ActivityCameraBinding


/**
 * Created by Lukas Kristianto on 09/04/22 19.17.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
class CameraActivity : AppCompatActivity() {
    private val binding by lazy { ActivityCameraBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, CameraFragment().apply {
                arguments = bundleOf("scanner" to "true")
            })
            .commitAllowingStateLoss()
    }


}