package id.co.app.source.ui.typography

import android.R
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import id.co.app.source.databinding.ActivityTypograhpyBinding

/**
 * Created by Lukas Kristianto on 14/07/21 23.55.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class TypographyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityTypograhpyBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}