package id.co.app.source.ui.label

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import id.co.app.components.label.Label
import id.co.app.components.label.LabelProgress
import id.co.app.components.utils.toPx
import id.co.app.source.R
import java.util.*


class LabelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.label_sample_layout)
        title = "Label"
        Objects.requireNonNull<ActionBar>(supportActionBar).setDisplayHomeAsUpEnabled(true)

        val labelSample1 = findViewById<Label>(R.id.LabelView1)
        labelSample1.setLabel("Label")
        labelSample1.opacityLevel = 0.5f
        labelSample1.setLabelType(Label.GENERAL_DARK_GREEN)
        labelSample1.timerIcon = true

        val labelSample2 = findViewById<Label>(R.id.LabelView2)
        labelSample2.setLabel("Labels")
        labelSample2.setLabelType(Label.GENERAL_DARK_BLUE)

        val labelSample3 = findViewById<Label>(R.id.LabelView3)
        labelSample3.setLabel("Label")
        labelSample3.setLabelType(Label.GENERAL_DARK_ORANGE)

        val labelSample4 = findViewById<Label>(R.id.LabelView4)
        labelSample4.setLabel("Label")
        labelSample4.setLabelType(Label.GENERAL_DARK_RED)

        val labelSample5 = findViewById<Label>(R.id.LabelView5)
        labelSample5.setLabel("Label")
        labelSample5.setLabelType(Label.GENERAL_LIGHT_RED)

        val labelSample6 = findViewById<Label>(R.id.LabelView6)
        labelSample6.setLabel("Label")
        labelSample6.setLabelType(Label.GENERAL_LIGHT_GREEN)

        val labelSample7 = findViewById<Label>(R.id.LabelView7)
        labelSample7.setLabel("Label")
        labelSample7.setLabelType(Label.GENERAL_LIGHT_BLUE)

        val labelSample8 = findViewById<Label>(R.id.LabelView8)
        labelSample8.setLabel("Label")
        labelSample8.setLabelType(Label.GENERAL_LIGHT_GREY)

        val labelSample9 = findViewById<Label>(R.id.LabelView9)
        labelSample9.setLabel("Label")
        labelSample9.setLabelType(Label.GENERAL_LIGHT_ORANGE)

        val labelSample10 = findViewById<Label>(R.id.LabelView10)
        labelSample10.setLabel("1 hari")
        labelSample10.setLabelType(Label.TIME_HIGH_PRIORITY)
        labelSample10.setLabelImage(R.drawable.ic_android_black_24dp)

        val labelProgress = findViewById<LabelProgress>(R.id.label_progressbar_red)
        labelProgress.setLabel("Terjual 80%", 80)

        val labelSample13 = findViewById<Label>(R.id.LabelView13)
        labelSample13.setLabel("Set via hex color")

        /**
         *  need to unlock the override feature first for implement color that didn't exist on unify label color list
         *  without unlock the feature override, every hex color except color from unify list color will be default color GENERAL_DARK_GREEN
         **/

//        labelSample13.unlockFeature = true

        labelSample13.fontColorByPass = "#FF0000"
        labelSample13.setLabelType("#BC80FF")

        findViewById<Label>(R.id.label_sample_programatically).setLabelImage(R.drawable.cricket)

        val drawable = AppCompatResources.getDrawable(this, R.drawable.cricket)
        findViewById<Label>(R.id.label_sample_programatically2).setLabelImage(drawable, 24.toPx(), 24.toPx())

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return true
    }
}