package id.co.app.source.ui.chip

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import id.co.app.components.chips.ChipUnify
import id.co.app.components.image.ImageUnify
import id.co.app.components.notification.NotificationUnify
import id.co.app.components.typography.Typography
import id.co.app.components.utils.toPx
import id.co.app.source.R
import id.co.app.source.databinding.ChipsSampleLayoutBinding

class ChipsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ChipsSampleLayoutBinding>(this, R.layout.chips_sample_layout)

        val sampleChipWithIcon = findViewById<ChipUnify>(R.id.sample_medium_chip_icon)
        sampleChipWithIcon.setOnClickListener {
            Toast.makeText(this, "Chips sampleChipWithIcon is clicked!", Toast.LENGTH_SHORT).show()
        }

        sampleChipWithIcon.setOnRemoveListener {
            Toast.makeText(baseContext, "Close chip sampleChipWithIcon!", Toast.LENGTH_SHORT).show()
        }

        val sampleChipNoIcon = findViewById<ChipUnify>(R.id.sample_medium_chip_no_icon)
        sampleChipNoIcon.setOnClickListener {
            Toast.makeText(this, "Chips sampleChipNoIcon is clicked!", Toast.LENGTH_SHORT).show()
            sampleChipWithIcon.chipType = ChipUnify.TYPE_SELECTED
        }

        sampleChipNoIcon.setOnRemoveListener {
            Toast.makeText(baseContext, "Close chip sampleChipNoIcon!", Toast.LENGTH_SHORT).show()
        }

        val sampleSmallChipIcon = findViewById<ChipUnify>(R.id.sample_small_chip_icon)
        sampleSmallChipIcon.setOnClickListener {
            Toast.makeText(this, "Chip sampleSmallChipIcon is clicked!", Toast.LENGTH_SHORT).show()
        }

        sampleSmallChipIcon.setOnRemoveListener {
            Toast.makeText(baseContext, "Close chip sampleChipNoIcon!", Toast.LENGTH_SHORT).show()
        }

        val sampleSmallChipNoIcon = findViewById<ChipUnify>(R.id.sample_small_chip_no_icon)
        sampleSmallChipNoIcon.setOnClickListener {
            Toast.makeText(this, "Chip sampleSmallChipIcon is clicked!", Toast.LENGTH_SHORT).show()
        }

        sampleSmallChipNoIcon.setOnRemoveListener {
            Toast.makeText(baseContext, "Chip sampleSmallChipIcon is closed!", Toast.LENGTH_SHORT).show()
        }

        val sampleMediumChipAlternate = findViewById<ChipUnify>(R.id.sample_medium_chip_alternate)
        sampleMediumChipAlternate.setOnClickListener {
            Toast.makeText(this, "Chip sampleMediumChipAlternate is clicked!", Toast.LENGTH_SHORT).show()
        }

        val sampleSmallChipAlternate = findViewById<ChipUnify>(R.id.sample_small_chip_alternate)
        sampleSmallChipAlternate.setOnClickListener {
            Toast.makeText(this, "Chip sampleSmallChipAlternate is clicked!", Toast.LENGTH_SHORT).show()
        }


        val sampleMediumChipSelected = findViewById<ChipUnify>(R.id.sample_medium_chip_selected)
        sampleMediumChipSelected.setOnClickListener {
            if(sampleMediumChipSelected.chipType == ChipUnify.TYPE_SELECTED) {
                sampleMediumChipSelected.chipType = ChipUnify.TYPE_NORMAL
            } else {
                sampleMediumChipSelected.chipType = ChipUnify.TYPE_SELECTED
            }
            Toast.makeText(this, "Chip sample_medium_chip_selected is clicked!", Toast.LENGTH_SHORT).show()
        }

        val sampleSmallChipSelected = findViewById<ChipUnify>(R.id.sample_small_chip_selected)
        sampleSmallChipSelected.setOnClickListener {
            Toast.makeText(this, "Chip sample_small_chip_selected is clicked!", Toast.LENGTH_SHORT).show()
        }


        val sampleMediumChipDisabled = findViewById<ChipUnify>(R.id.sample_medium_chip_disabled)
        sampleMediumChipDisabled.setOnClickListener {
            Toast.makeText(this, "Chip sampleMediumChipDisabled is clicked!", Toast.LENGTH_SHORT).show()
        }

        binding.sampleSmallChipChevronNormal.setTextCenter(true)

        binding.sampleSmallChipChevronNormal.setOnClickListener {
            binding.sampleSmallChipChevronNormal.chipType = ChipUnify.TYPE_SELECTED
        }

        binding.sampleSmallChipChevronNormal.setChevronClickListener {
            binding.sampleSmallChipChevronNormal.clearRightIcon()
            Toast.makeText(this,"Chevron is clicked",Toast.LENGTH_SHORT).show()
        }

        val wrapper = LinearLayout(this)
        wrapper.apply {
            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            param.leftMargin = 4.toPx()
            param.rightMargin = 4.toPx()
            layoutParams = param
        }

        val notif = NotificationUnify(this)
        notif.apply {
            val param = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            param.rightMargin = 8.toPx()
            param.gravity = Gravity.CENTER
            layoutParams = param
        }
        notif.setNotification("3", NotificationUnify.COUNTER_TYPE, NotificationUnify.COLOR_SECONDARY)

        val textView = Typography(this)
        textView.setType(Typography.BODY_2)
        textView.text = "Filters"

        wrapper.addView(notif)
        wrapper.addView(textView)

        binding.sampleChipUnlockMode.addCustomView(wrapper)

        binding.sampleChipUnlockMode.setOnClickListener {
            if(binding.sampleChipUnlockMode.chipType == ChipUnify.TYPE_NORMAL) {
                binding.sampleChipUnlockMode.chipType = ChipUnify.TYPE_SELECTED
            } else {
                binding.sampleChipUnlockMode.chipType = ChipUnify.TYPE_NORMAL
            }
        }

        binding.sampleChipUnlockMode.selectedChangeListener = { isSelected ->
            if (isSelected) {
                Toast.makeText(this,"Chip is selected",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"Chip is deselected",Toast.LENGTH_SHORT).show()
            }
        }

        binding.sampleChipWithColor.chip_image_icon.type = ImageUnify.TYPE_CIRCLE
        binding.sampleChipWithColor.chipImageResource = ContextCompat.getDrawable(this, R.color.Unify_B600)

//        val sampleSmallChipDisabled = findViewById<Chips>(R.id.sample_small_chip_disabled)
//        sampleSmallChipDisabled.setOnClickListener {
//            Toast.makeText(this, "Chip sampleSmallChipDisabled is clicked!", Toast.LENGTH_SHORT).show()
//        }
    }

}