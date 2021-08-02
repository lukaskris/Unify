package id.co.app.source.ui.icon

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.co.app.components.bottomsheet.BottomSheetUnify
import id.co.app.components.button.UnifyButton
import id.co.app.components.icon.IconUnify
import id.co.app.components.icon.getIconUnifyDrawable
import id.co.app.components.quantity.QuantityEditorUnify
import id.co.app.components.typography.Typography
import id.co.app.components.utils.toPx
import id.co.app.source.R
import id.co.app.source.databinding.ActivityIconUnifyBinding
import id.co.app.source.databinding.UnifyiconSampleRecyclerLayoutBinding
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Lukas Kristianto on 15/07/21 00.44.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class UnifyIconActivity : AppCompatActivity() {
    private val binding by lazy { ActivityIconUnifyBinding.inflate(layoutInflater) }
    private lateinit var btmSheet: BottomSheetUnify

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val dataCollection = ArrayList<IconData>()
        dataCollection.add(IconData(0,"BELL"))
        dataCollection.add(IconData(1,"BELL_RING"))
        dataCollection.add(IconData(2,"CALENDAR"))
        dataCollection.add(IconData(3,"CAMERA"))
        dataCollection.add(IconData(4,"CART"))
        dataCollection.add(IconData(5,"CLOCK"))
        dataCollection.add(IconData(6,"HEART"))
        dataCollection.add(IconData(7,"HEART_FILLED"))
        dataCollection.add(IconData(8,"IMAGE"))
        dataCollection.add(IconData(9,"IMAGE_ADD"))
        dataCollection.add(IconData(10,"IMAGE_BROKEN"))
        dataCollection.add(IconData(11,"IMAGE_SEARCH"))
        dataCollection.add(IconData(12,"INFORMATION"))
        dataCollection.add(IconData(13,"LIGHT_BULB"))
        dataCollection.add(IconData(14,"LOCATION"))
        dataCollection.add(IconData(15,"MESSAGE"))
        dataCollection.add(IconData(16,"MICROPHONE"))
        dataCollection.add(IconData(17,"SETTING"))
        dataCollection.add(IconData(18,"SIGN_IN"))
        dataCollection.add(IconData(19,"SIGN_OUT"))
        dataCollection.add(IconData(20,"TARGET"))
        dataCollection.add(IconData(21,"THUMB"))
        dataCollection.add(IconData(22,"THUMB_FILLED"))
        dataCollection.add(IconData(23,"VIDEO"))
        dataCollection.add(IconData(24,"VIDEO_BROKEN"))
        dataCollection.add(IconData(25,"WALLET"))

        dataCollection.add(IconData(26,"ARROW_BACK"))
        dataCollection.add(IconData(27,"BACK_TO_TOP"))
        dataCollection.add(IconData(28,"CHEVRON_DOWN"))
        dataCollection.add(IconData(29,"CHEVRON_RIGHT"))
        dataCollection.add(IconData(30,"CHEVRON_UP"))
        dataCollection.add(IconData(31,"CLOSE"))
        dataCollection.add(IconData(32,"ERROR - Delete on 1.0.2"))
        dataCollection.add(IconData(33,"MENU_HAMBURGER"))
        dataCollection.add(IconData(34,"MENU_KEBAB_HORIZONTAL"))
        dataCollection.add(IconData(35,"MENU_KEBAB_VERTICAL"))
        dataCollection.add(IconData(36,"VIEW_GRID"))
        dataCollection.add(IconData(37,"VIEW_GRID_BIG"))
        dataCollection.add(IconData(38,"VIEW_LIST"))
        dataCollection.add(IconData(39,"WARNING"))

        dataCollection.add(IconData(40,"COUPON"))
        dataCollection.add(IconData(41,"FORMAT_ALIGN_LEFT"))
        dataCollection.add(IconData(42,"FORMAT_ALIGN_RIGHT"))
        dataCollection.add(IconData(43,"FORMAT_BOLD"))
        dataCollection.add(IconData(44,"FORMAT_CENTER"))
        dataCollection.add(IconData(45,"FORMAT_ITALIC"))
        dataCollection.add(IconData(46,"FORMAT_JUSTIFY"))
        dataCollection.add(IconData(47,"FORMAT_LIST_BULLET"))
        dataCollection.add(IconData(48,"FORMAT_UNDERLINE"))
        dataCollection.add(IconData(49,"INVITATION"))
        dataCollection.add(IconData(50,"PROMO"))
        dataCollection.add(IconData(51,"REDO"))
        dataCollection.add(IconData(52,"SPEAKER"))
        dataCollection.add(IconData(53,"TICKET_ACTIVE"))
        dataCollection.add(IconData(54,"TICKET_CHANGE"))
        dataCollection.add(IconData(55,"TICKET_HISTORY"))
        dataCollection.add(IconData(56,"TICKET_INSTANT"))
        dataCollection.add(IconData(59,"UNDO"))

        dataCollection.add(IconData(60,"CALL"))
        dataCollection.add(IconData(61,"CALL_MISSED"))
        dataCollection.add(IconData(62,"CHAT"))
        dataCollection.add(IconData(63,"DISCUSSION"))
        dataCollection.add(IconData(64,"FACEBOOK"))
        dataCollection.add(IconData(65,"GOOGLE_AUTHENTICATOR"))
        dataCollection.add(IconData(66,"INSTAGRAM"))
        dataCollection.add(IconData(67,"LINE"))
        dataCollection.add(IconData(68,"LINK"))
        dataCollection.add(IconData(69,"READ"))
        dataCollection.add(IconData(70,"SHARE"))
        dataCollection.add(IconData(71,"SHARE_MOBILE"))
        dataCollection.add(IconData(72,"TWITTER"))
        dataCollection.add(IconData(73,"UNREAD"))
        dataCollection.add(IconData(74,"WHATSAPP"))

        dataCollection.add(IconData(75,"AUTO_DEBIT"))
        dataCollection.add(IconData(76,"BADGE_OS"))
        dataCollection.add(IconData(77,"BADGE_VERIFIED"))
        dataCollection.add(IconData(78,"BILL"))
        dataCollection.add(IconData(79,"BOOKMARK"))
        dataCollection.add(IconData(80,"BOOKMARK_FILLED"))
        dataCollection.add(IconData(81,"CALL_CENTER"))
        dataCollection.add(IconData(82,"FINANCE"))
        dataCollection.add(IconData(83,"FLASH_SALE"))
        dataCollection.add(IconData(84,"GOLD"))
        dataCollection.add(IconData(85,"KEYBOARD"))
        dataCollection.add(IconData(86,"KEYWORD"))
        dataCollection.add(IconData(87,"KEYWORD_NEGATIVE"))
        dataCollection.add(IconData(88,"POLL"))
        dataCollection.add(IconData(89,"PROTECTION"))
        dataCollection.add(IconData(90,"RATING"))
        dataCollection.add(IconData(91,"REFUND"))
        dataCollection.add(IconData(92,"REFUND_NON"))
        dataCollection.add(IconData(93,"STAR"))
        dataCollection.add(IconData(94,"STAR_FILLED"))
        dataCollection.add(IconData(95,"STICKER"))

        dataCollection.add(IconData(96,"BILL_ALL"))
        dataCollection.add(IconData(97,"BILL_CHECK"))
        dataCollection.add(IconData(98,"CLIPBOARD"))
        dataCollection.add(IconData(99,"GIFT"))
        dataCollection.add(IconData(100,"GRAPH"))
        dataCollection.add(IconData(101,"GRAPH_REPORT"))
        dataCollection.add(IconData(102,"NOTEBOOK"))
        dataCollection.add(IconData(103,"PRODUCT"))
        dataCollection.add(IconData(104,"PRODUCT_ADD"))
        dataCollection.add(IconData(105,"PRODUCT_INFO"))
        dataCollection.add(IconData(106,"PRODUCT_NEXT"))
        dataCollection.add(IconData(107,"PRODUCT_PROMO"))
        dataCollection.add(IconData(108,"PRODUCT_SETTING"))
        dataCollection.add(IconData(109,"SHOP"))
        dataCollection.add(IconData(110,"SHOP_INFO"))
        dataCollection.add(IconData(111,"SHOP_REPORT"))
        dataCollection.add(IconData(112,"SHOP_SETTING"))
        dataCollection.add(IconData(113,"SHOPPING_BAG"))
        dataCollection.add(IconData(114,"STAMP"))

        dataCollection.add(IconData(115,"ADD"))
        dataCollection.add(IconData(116,"ADD_CIRCLE"))
        dataCollection.add(IconData(117,"BACKGROUND"))
        dataCollection.add(IconData(118,"BROOM"))
        dataCollection.add(IconData(119,"CHECK"))
        dataCollection.add(IconData(120,"CHECK_DOUBLE"))
        dataCollection.add(IconData(121,"CLEAR"))
        dataCollection.add(IconData(122,"COMPARE"))
        dataCollection.add(IconData(123,"COPY"))
        dataCollection.add(IconData(124,"CROP"))
        dataCollection.add(IconData(125,"DELETE"))
        dataCollection.add(IconData(126,"DOWNLOAD"))
        dataCollection.add(IconData(127,"EDIT"))
        dataCollection.add(IconData(128,"LOCK"))
        dataCollection.add(IconData(129,"PRINT"))
        dataCollection.add(IconData(130,"PUSH_PIN"))
        dataCollection.add(IconData(131,"PUSH_PIN_FILLED"))
        dataCollection.add(IconData(132,"QR_CODE"))
        dataCollection.add(IconData(133,"REMOVE_CIRCLE"))
        dataCollection.add(IconData(134,"SEARCH"))
        dataCollection.add(IconData(135,"SEND_TEXT"))
        dataCollection.add(IconData(136,"SORT_FILTER"))
        dataCollection.add(IconData(137,"TEXT"))
        dataCollection.add(IconData(138,"TEXT_ADD"))
        dataCollection.add(IconData(139,"VISIBILITY"))
        dataCollection.add(IconData(140,"VISIBILITY_OFF"))

        dataCollection.add(IconData(141,"ACTIVITY"))
        dataCollection.add(IconData(142,"CAMERA_SWITCH"))
        dataCollection.add(IconData(143,"FLASH_OFF"))
        dataCollection.add(IconData(144,"FLASH_ON"))
        dataCollection.add(IconData(145,"PAUSE"))
        dataCollection.add(IconData(146,"PLAY"))
        dataCollection.add(IconData(147,"REPLAY - Delete on 1.0.2"))
        dataCollection.add(IconData(148,"SCREEN_FULL"))
        dataCollection.add(IconData(149,"SCREEN_NORMAL"))
        dataCollection.add(IconData(150,"SKIP_NEXT"))
        dataCollection.add(IconData(151,"SWIPE"))
        dataCollection.add(IconData(152,"SWIPE_RIGHT"))
        dataCollection.add(IconData(153,"TAP"))
        dataCollection.add(IconData(154,"VOLUME_DOWN"))
        dataCollection.add(IconData(155,"VOLUME_MUTE"))
        dataCollection.add(IconData(156,"VOLUME_UP"))

        dataCollection.add(IconData(157,"CARD"))
        dataCollection.add(IconData(158,"CHILD"))
        dataCollection.add(IconData(159,"CONTACT"))
        dataCollection.add(IconData(160,"INFANT"))
        dataCollection.add(IconData(161,"LAPTOP"))
        dataCollection.add(IconData(162,"PHONE"))
        dataCollection.add(IconData(163,"PHONE_SETTING"))
        dataCollection.add(IconData(164,"SALDO"))
        dataCollection.add(IconData(165,"USER"))
        dataCollection.add(IconData(166,"USER_ADD"))
        dataCollection.add(IconData(167,"USER_SUCCESS"))

        dataCollection.add(IconData(168,"LIST_TRANSACTION"))
        dataCollection.add(IconData(169,"PRODUCT_FILLED"))
        dataCollection.add(IconData(170,"SHOP_FAVORITE"))
        dataCollection.add(IconData(171,"SHOP_FILLED"))

        dataCollection.add(IconData(172,"POLICY_PRIVACY"))
        dataCollection.add(IconData(173,"QUICK_BUY"))
        dataCollection.add(IconData(174,"SHAKE"))

        dataCollection.add(IconData(175,"COMPLAINT"))

        dataCollection.add(IconData(176,"BELL_FILLED"))
        dataCollection.add(IconData(177,"CABINET"))
        dataCollection.add(IconData(178,"CABINET_FILLED"))
        dataCollection.add(IconData(179,"FEED"))
        dataCollection.add(IconData(180,"FEED_FILLED"))
        dataCollection.add(IconData(181,"HOME"))
        dataCollection.add(IconData(182,"HOME_FILLED"))
        dataCollection.add(IconData(183,"MESSAGE_HELP"))
        dataCollection.add(IconData(184,"MODE_SCREEN"))
        dataCollection.add(IconData(185,"SUBSCRIPTION"))

        dataCollection.add(IconData(186,"CALENDAR_ADD"))
        dataCollection.add(IconData(187,"FIRE_FILLED"))
        dataCollection.add(IconData(188,"FIRE"))
        dataCollection.add(IconData(189,"STAR_CIRCLE"))
        dataCollection.add(IconData(190,"DISCOUNT"))
        dataCollection.add(IconData(191,"PROMO_ADS"))
        dataCollection.add(IconData(192,"USER_SETTING"))
        dataCollection.add(IconData(193,"ARROW_TOP_RIGHT"))
        dataCollection.add(IconData(194,"ARROW_TOP_LEFT"))

        dataCollection.add(IconData(195,"CHEVRON_LEFT"))
        dataCollection.add(IconData(196,"CHAT_FILLED"))
        dataCollection.add(IconData(197,"PROTECTION_CHECK"))
        dataCollection.add(IconData(198,"CLIPBOARD_FILLED"))
        dataCollection.add(IconData(199,"COURIER"))
        dataCollection.add(IconData(200,"VIEW_GRID_FILLED"))
        dataCollection.add(IconData(201,"FOLDER"))

        dataCollection.add(IconData(202,"COURIER_FAST"))
        dataCollection.add(IconData(203,"PRODUCT_REPORT"))
        dataCollection.add(IconData(204,"RELOAD"))
        dataCollection.add(IconData(205,"CHECK_CIRCLE"))
        dataCollection.add(IconData(206,"CHECK_BIG"))

        dataCollection.add(IconData(207,"PRODUCT_BUDGET"))
        dataCollection.add(IconData(208,"SHIRT_BUDGET"))

        dataCollection.add(IconData(209,"GRAPH_FILLED"))
        dataCollection.add(IconData(210,"SHOP_INFO_FILLED"))
        dataCollection.add(IconData(211,"BADGE_VERIFIED_FILLED"))
        dataCollection.add(IconData(212,"CALL_CENTER_FILLED"))
        dataCollection.add(IconData(213,"USER_TALK"))
        dataCollection.add(IconData(214,"USER_TALK_FILLED"))
        dataCollection.add(IconData(215,"USER_REPORT"))
        dataCollection.add(IconData(216,"USER_BLOCK"))
        dataCollection.add(IconData(217,"DISCUSSION_FILLED"))
        dataCollection.add(IconData(218,"BROADCAST"))
        dataCollection.add(IconData(219,"PRINT_FILLED"))
        dataCollection.add(IconData(220,"PROMO_ADS_FILLED"))
        dataCollection.add(IconData(221,"PROMO_BLOCK"))
        dataCollection.add(IconData(222,"SETTING_FILLED"))
        dataCollection.add(IconData(223,"SIGN_OUT_FILLED"))

        dataCollection.add(IconData(224, "CALL_RING"))
        dataCollection.add(IconData(225, "CITY"))
        dataCollection.add(IconData(226, "LOCATION_FILLED"))
        dataCollection.add(IconData(227, "LOCATION_OFF"))
        dataCollection.add(IconData(228, "PACKAGE"))
        dataCollection.add(IconData(229, "PACKAGE_FILLED"))
        dataCollection.add(IconData(230, "PHONE_BELL"))
        dataCollection.add(IconData(231, "USER_REMOVE"))

        dataCollection.add(IconData(232, "CLOCK_FILLED"))
        dataCollection.add(IconData(233, "FILE_DOC"))
        dataCollection.add(IconData(234, "FILE_PDF"))
        dataCollection.add(IconData(235, "FILE_XLS"))
        dataCollection.add(IconData(236, "RISK_HIGH"))
        dataCollection.add(IconData(237, "RISK_LOW"))
        dataCollection.add(IconData(238, "RISK_MODERATE"))

        dataCollection.add(IconData(239, "CHAT_REPORT"))
        dataCollection.add(IconData(240, "HELP"))
        dataCollection.add(IconData(241, "MAP"))
        dataCollection.add(IconData(242, "ZOOM_IN"))
        dataCollection.add(IconData(243, "ZOOM_OUT"))

        dataCollection.add(IconData(244, "SERVICE"))
        dataCollection.add(IconData(245, "SERVICE_FILLED"))
        dataCollection.add(IconData(246, "SALDO_INCOME"))
        dataCollection.add(IconData(247, "SALDO_OUTCOME"))
        dataCollection.add(IconData(248, "SALDO_TEMPO"))
        dataCollection.add(IconData(249, "PROMO_LIVE"))
        dataCollection.add(IconData(250, "REMOVE"))
        dataCollection.add(IconData(251, "RELOAD_24H"))
        dataCollection.add(IconData(252, "CHAT_BELL"))

        dataCollection.add(IconData(253, "FILE_CSV"))
        dataCollection.add(IconData(254, "FILE_JSON"))
        dataCollection.add(IconData(255, "FOLDER_FILLED"))
        dataCollection.add(IconData(256, "PROJECT"))
        dataCollection.add(IconData(257, "PROJECT_FILLED"))
        dataCollection.add(IconData(258, "USER_FILLED"))
        dataCollection.add(IconData(259, "IP"))
        dataCollection.add(IconData(260, "FREE"))
        dataCollection.add(IconData(261, "UPLOAD"))
        dataCollection.add(IconData(262, "PERFORMANCE"))

        dataCollection.add(IconData(263,"ARROW_UP"))
        dataCollection.add(IconData(264,"ARROW_DOWN"))
        dataCollection.add(IconData(265,"AUDIO_WAVE"))
        dataCollection.add(IconData(266,"SCREEN_FIT"))

        findViewById<RecyclerView>(R.id.unifyicon_rv).layoutManager = LinearLayoutManager(this)
        val adapter = UnifyIconAdapter(dataCollection)
        findViewById<RecyclerView>(R.id.unifyicon_rv).adapter = adapter

        findViewById<UnifyButton>(R.id.unifyicon_sample_color_btn).setOnClickListener {
            if(!::btmSheet.isInitialized){
                btmSheet = BottomSheetUnify()
                btmSheet.setTitle("Color list")

                val content = View.inflate(this, R.layout.unifyicon_sample_list_layout, null)
                val list = content.findViewById<ListUnify>(R.id.unifyicon_sample_color_list)

                val listCollection = ArrayList<ListItemUnify>()
                listCollection.add(ListItemUnify("N500",R.color.Unify_N500.toString()))
                listCollection.add(ListItemUnify("R500",R.color.Unify_R500.toString()))
                listCollection.add(ListItemUnify("G500",R.color.Unify_G500.toString()))
                listCollection.add(ListItemUnify("B500",R.color.Unify_B500.toString()))
                listCollection.add(ListItemUnify("P500",R.color.Unify_P500.toString()))
                listCollection.add(ListItemUnify("T500",R.color.Unify_T500.toString()))
                listCollection.add(ListItemUnify("Y500",R.color.Unify_Y500.toString()))

                list.setOnItemClickListener { _, _, position, _ ->
                    val color = ContextCompat.getColor(this, listCollection[position].listDescriptionText.toInt())
                    adapter.iconColor = color
                    adapter.notifyDataSetChanged()

                    findViewById<Typography>(R.id.active_color_text).text = listCollection[position].listTitleText

                    btmSheet.dismiss()
                }

                list.setData(listCollection)
                btmSheet.setChild(content)
            }

            btmSheet.show(supportFragmentManager,"UnifyIcon")
        }

        /**
         * UnifyIcon object
         */
        binding.iconSampleRef.setOnClickListener {
            binding.iconSampleRef.isEnabled = !binding.iconSampleRef.isEnabled
            Toast.makeText(this,"icon enable = ${binding.iconSampleRef.isEnabled}", Toast.LENGTH_LONG).show()
        }

        val colorLightEnable = ContextCompat.getColor(this, R.color.Unify_G500)
        val colorLightDisabled = ContextCompat.getColor(this,R.color.Unify_Y500)
        val colorDarkEnable = ContextCompat.getColor(this,R.color.Unify_B500)
        val colorDarkDisable = ContextCompat.getColor(this,R.color.Unify_P500)
        binding.iconSampleRef2.setImage(null, colorLightEnable, colorLightDisabled, colorDarkEnable, colorDarkDisable)

        /**
         * UnifyIcon object generated programatically
         */
        binding.iconSampleRef2.setOnClickListener {
            binding.iconSampleRef2.isEnabled = !binding.iconSampleRef2.isEnabled
        }

        /**
         * Imageview using UnifyIcon asset
         */
        var ref3Color = ContextCompat.getColor(this, R.color.Unify_R500)
        var ref3Asset = getIconUnifyDrawable(this, IconUnify.WARNING, ref3Color)
        binding.iconSampleRef3.setImageDrawable(ref3Asset)

        binding.iconSampleRef3.setOnClickListener {
            ref3Color = ContextCompat.getColor(this, R.color.Unify_P500)
            ref3Asset = getIconUnifyDrawable(this, IconUnify.WARNING, ref3Color)
            binding.iconSampleRef3.setImageDrawable(ref3Asset)
        }

        /**
         * direct component
         */
        binding.iconSampleRef4.setOnClickListener {
            binding.iconSampleRef4.isEnabled = !binding.iconSampleRef4.isEnabled
        }

        binding.unifyiconSampleBoundSwitch.setOnCheckedChangeListener { _, isChecked ->
            adapter.isBound = isChecked
            adapter.notifyDataSetChanged()
        }

        binding.unifyiconSampleSizeBtn.setOnClickListener {
            adapter.size = findViewById<QuantityEditorUnify>(R.id.unifyicon_sample_size_qty).getValue()
            adapter.notifyDataSetChanged()
        }

        binding.iconunifySearchBtn.setOnClickListener {
            var keyword = binding.iconunifySearchText.textInputLayout.editText?.text.toString()
            keyword = keyword.toUpperCase(Locale.ROOT)

            var isResultFound = false
            dataCollection.forEach {
                if(it.iconName == keyword){
                    findViewById<RecyclerView>(R.id.unifyicon_rv).scrollToPosition(it.iconId)
                    isResultFound = true
                    return@forEach
                }
            }

            if(!isResultFound){
                binding.iconunifySearchText.setMessage("Icon not found")
                binding.iconunifySearchText.setError(true)
            }else{
                binding.iconunifySearchText.setMessage("")
                binding.iconunifySearchText.setError(false)
            }

            (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            binding.iconunifySearchText.clearFocus()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        return if (id == android.R.id.home) {
            onBackPressed()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }
}

class IconData(val iconId: Int, val iconName: String)

class UnifyIconAdapter(
    private val listData: ArrayList<IconData>,
    var iconColor: Int? = null,
    var isBound: Boolean = false
) : RecyclerView.Adapter<UnifyIconAdapter.Row>(){
    var size: Int? = null

    class Row(binding: UnifyiconSampleRecyclerLayoutBinding, val iconRef: IconUnify, val textRef: Typography, val rowNumber: Typography) : RecyclerView.ViewHolder(binding.root)

    override fun getItemCount(): Int {
        return listData.count()
    }

    override fun onBindViewHolder(holder: Row, position: Int) {
        val data = listData[position]

        holder.rowNumber.text = position.toString()

        holder.textRef.text = data.iconName

        if(isBound) {
            holder.iconRef.setBackgroundColor(Color.GRAY)
        }else{
            holder.iconRef.setBackgroundColor(Color.TRANSPARENT)
        }

        if(size != null){
            val refIcon = holder.iconRef.layoutParams
            refIcon.width = (size ?: 32).toPx()
            refIcon.height = (size ?: 32).toPx()
        }

        iconColor?.let {
            holder.iconRef.setImage(data.iconId, it)
        } ?: run {
            holder.iconRef.setImage(data.iconId)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Row {
        val binding: UnifyiconSampleRecyclerLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.unifyicon_sample_recycler_layout, parent, false)
        return Row(binding, binding.iconSampleRvIcon, binding.iconSampleRvText, binding.iconSampleRvNumber)
    }
}