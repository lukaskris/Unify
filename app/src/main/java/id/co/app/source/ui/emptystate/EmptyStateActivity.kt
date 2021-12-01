package id.co.app.source.ui.emptystate

import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import id.co.app.components.emptystate.EmptyStateUnify
import id.co.app.source.R
import java.util.*

class EmptyStateActivity : AppCompatActivity() {

    private fun onCtaClick() {
        println("emptystate cta click")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.empty_state_sample_layout)

        title = "Empty State"

        Handler().postDelayed({
            findViewById<View>(R.id.emptyStateID).visibility = View.VISIBLE
        }, 5000)

//        emptyStateID.setImageUrl("https://ecs7.tokopedia.net/assets-unify/img/il-footer-2.jpg")
//        emptyState.setTitle("Title Goes Here")
//        emptyState.setDescription("Type your information about the hint in a compact way.")
//        emptyState.setPrimaryCTAClickListener(::onCtaClick)
//        emptyState.setOrientation(EmptyState.Orientation.VERTICAL)

        Objects.requireNonNull<ActionBar>(supportActionBar).setDisplayHomeAsUpEnabled(true)

        findViewById<EmptyStateUnify>(R.id.emptyState2).setSecondaryCTAClickListener {
            Toast.makeText(this, "secondary CTA clicked", Toast.LENGTH_SHORT).show()
        }

        findViewById<EmptyStateUnify>(R.id.emptyState2).setPrimaryCTAClickListener {
            Toast.makeText(this, "primary CTA clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }
}

