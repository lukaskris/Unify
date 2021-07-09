package id.co.app.components.icon

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import id.co.app.components.R

/**
 * Created by Lukas Kristianto on 7/3/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class IconUnify : AppCompatImageView {
    var iconImg: Drawable? = null

    private var iconId = BELL

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)

        updateImage()
    }

    private var iconColorLightEnable: Int = 0
    private var iconColorLightDisable: Int = 0
    private var iconColorNightEnable: Int = 0
    private var iconColorNightDisable: Int = 0

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(
        context: Context,
        newIconId: Int,
        newLightEnable: Int = 0,
        newLightDisable: Int = 0,
        newDarkEnable: Int = 0,
        newDarkDisable: Int = 0
    ) : super(context) {
        iconId = newIconId


        iconColorLightEnable = newLightEnable
        iconColorLightDisable = newLightDisable
        iconColorNightEnable = newDarkEnable
        iconColorNightDisable = newDarkDisable

        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init(context, attributeSet)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs, defStyleAttr)
    }

    private fun init(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int? = null) {
        val defaultColorEnable = ContextCompat.getColor(context, R.color.icon_enable_default_color)
        val defaultColorDisable = ContextCompat.getColor(context, R.color.icon_disable_default_color)


        if(iconColorLightEnable == 0){
            iconColorLightEnable = defaultColorEnable
        }
        
        if(iconColorLightDisable == 0){
            iconColorLightDisable = defaultColorDisable
        }

        attrs?.let {
            val xmlAttr = context.obtainStyledAttributes(attrs, R.styleable.UnifyIcon)
            iconId = xmlAttr.getInteger(R.styleable.UnifyIcon_icon_name, BELL)

            isEnabled = xmlAttr.getBoolean(R.styleable.UnifyIcon_icon_enable_state, true)

            /**
             * get color for every state
             */
            val colorList = Array<Int>(4) { 0 }
            //iconColorLightEnableRef
            colorList[0] =
                xmlAttr.getInteger(
                    R.styleable.UnifyIcon_icon_color_light_enable,
                    defaultColorEnable
                )

            //iconColorLightDisableRef
            colorList[1] = xmlAttr.getInteger(
                R.styleable.UnifyIcon_icon_color_light_disabled,
                defaultColorDisable
            )

            //iconColorNightEnableRef
            colorList[2] = xmlAttr.getInteger(R.styleable.UnifyIcon_icon_color_night_enable, 0)

            // iconColorNightDisableRef
            colorList[3] = xmlAttr.getInteger(R.styleable.UnifyIcon_icon_color_night_disabled, 0)

            /**
             * get color ref source to be color
             */
            colorList.forEachIndexed { index, colorRef ->
                if (colorRef != 0) {
                    when (index) {
                        0 -> iconColorLightEnable = colorRef
                        1 -> iconColorLightDisable = colorRef
                        2 -> iconColorNightEnable = colorRef
                        3 -> iconColorNightDisable = colorRef
                    }
                }
            }
        }

        updateImage()
    }

    private fun updateImage(){
        /**
         * Check enable state & night mode
         */
        val iconColor =
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) { // night mode
                // disabled & disabled color ready
                if (!isEnabled && iconColorNightDisable != 0) {
                    iconColorNightDisable
                } else if (isEnabled && iconColorNightEnable != 0) { // enabled & enable color ready
                    iconColorNightEnable
                } else { // if night color is not set
                    iconColorLightEnable
                }
            } else { // light mode
                // light disabled
                if (!isEnabled && iconColorLightDisable != 0) {
                    iconColorLightDisable
                } else { // light enable
                    iconColorLightEnable
                }
            }

        iconImg = AppCompatResources.getDrawable(context, getResourceRef(iconId))
        iconImg?.mutate()

        /**
         * check if icon is listed on excluded list or not
         * if listed then color filter will be ignored
         */
        if(!isExcluded(iconId)){
            iconImg?.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP)
        }

        this.setImageDrawable(iconImg)
    }

    fun setImage(
        newIconId: Int? = null,
        newLightEnable: Int? = null,
        newLightDisable: Int? = null,
        newDarkEnable: Int? = null,
        newDarkDisable: Int? = null
    ) {
        if (newIconId != null) {
            iconId = newIconId
        }

        if (newLightEnable != null) {
            iconColorLightEnable = newLightEnable
        }

        if (newLightDisable != null) {
            iconColorLightDisable = newLightDisable
        }

        if (newDarkEnable != null) {
            iconColorNightEnable = newDarkEnable
        }

        if (newDarkDisable != null) {
            iconColorNightDisable = newDarkDisable
        }

        updateImage()
    }

    companion object {
        /**
         * General
         */
        const val BELL = 0
        const val BELL_RING = 1
        const val CALENDAR = 2
        const val CAMERA = 3
        const val CART = 4
        const val CLOCK = 5
        const val HEART = 6
        const val HEART_FILLED = 7
        const val IMAGE = 8
        const val IMAGE_ADD = 9
        const val IMAGE_BROKEN = 10
        const val IMAGE_SEARCH = 11
        const val INFORMATION = 12
        const val LIGHT_BULB = 13
        const val LOCATION = 14
        const val MESSAGE = 15
        const val MICROPHONE = 16
        const val SETTING = 17
        const val SIGN_IN = 18
        const val SIGN_OUT = 19
        const val TARGET = 20
        const val THUMB = 21
        const val THUMB_FILLED = 22
        const val VIDEO = 23
        const val VIDEO_BROKEN = 24
        const val WALLET = 25

        /**
         * navigation
         */
        const val ARROW_BACK = 26
        const val BACK_TO_TOP = 27
        const val CHEVRON_DOWN = 28
        const val CHEVRON_RIGHT = 29
        const val CHEVRON_UP = 30
        const val CLOSE = 31
        const val ERROR = 32    // soft remove on 1.0.2
        const val MENU_HAMBURGER = 33
        const val MENU_KEBAB_HORIZONTAL = 34
        const val MENU_KEBAB_VERTICAL = 35
        const val VIEW_GRID = 36
        const val VIEW_GRID_BIG = 37
        const val VIEW_LIST = 38
        const val WARNING = 39

        /**
         * promo
         */
        const val COUPON = 40
        const val FORMAT_ALIGN_LEFT = 41
        const val FORMAT_ALIGN_RIGHT = 42
        const val FORMAT_BOLD = 43
        const val FORMAT_CENTER = 44
        const val FORMAT_ITALIC = 45
        const val FORMAT_JUSTIFY = 46
        const val FORMAT_LIST_BULLET = 47
        const val FORMAT_UNDERLINE = 48
        const val INVITATION = 49
        const val PROMO = 50
        const val REDO = 51
        const val SPEAKER = 52
        const val TICKET_ACTIVE = 53
        const val TICKET_CHANGE = 54
        const val TICKET_HISTORY = 55
        const val TICKET_INSTANT = 56
        const val TOPQUEST = 58
        const val UNDO = 59

        /**
         * social
         */
        const val CALL = 60
        const val CALL_MISSED = 61
        const val CHAT = 62
        const val DISCUSSION = 63
        const val FACEBOOK = 64
        const val GOOGLE_AUTHENTICATOR = 65
        const val INSTAGRAM = 66
        const val LINE = 67
        const val LINK = 68
        const val READ = 69
        const val SHARE = 70
        const val SHARE_MOBILE = 71
        const val TWITTER = 72
        const val UNREAD = 73
        const val WHATSAPP = 74

        /**
         * misc
         */
        const val AUTO_DEBIT = 75
        const val BADGE_OS = 76
        const val BADGE_VERIFIED = 77
        const val BILL = 78
        const val BOOKMARK = 79
        const val BOOKMARK_FILLED = 80
        const val CALL_CENTER = 81
        const val FINANCE = 82
        const val FLASH_SALE = 83
        const val GOLD = 84
        const val KEYBOARD = 85
        const val KEYWORD = 86
        const val KEYWORD_NEGATIVE = 87
        const val POLL = 88
        const val PROTECTION = 89
        const val RATING = 90
        const val REFUND = 91
        const val REFUND_NON = 92
        const val STAR = 93
        const val STAR_FILLED = 94
        const val STICKER = 95

        /**
         * toko
         */
        const val BILL_ALL = 96
        const val BILL_CHECK = 97
        const val CLIPBOARD = 98
        const val GIFT = 99
        const val GRAPH = 100
        const val GRAPH_REPORT = 101
        const val NOTEBOOK = 102
        const val PRODUCT = 103
        const val PRODUCT_ADD = 104
        const val PRODUCT_INFO = 105
        const val PRODUCT_NEXT = 106
        const val PRODUCT_PROMO = 107
        const val PRODUCT_SETTING = 108
        const val SHOP = 109
        const val SHOP_INFO = 110
        const val SHOP_REPORT = 111
        const val SHOP_SETTING = 112
        const val SHOPPING_BAG = 113
        const val STAMP = 114

        /**
         * action
         */
        const val ADD = 115
        const val ADD_CIRCLE = 116
        const val BACKGROUND = 117
        const val BROOM = 118
        const val CHECK = 119
        const val CHECK_DOUBLE = 120
        const val CLEAR = 121
        const val COMPARE = 122
        const val COPY = 123
        const val CROP = 124
        const val DELETE = 125
        const val DOWNLOAD = 126
        const val EDIT = 127
        const val LOCK = 128
        const val PRINT = 129
        const val PUSH_PIN = 130
        const val PUSH_PIN_FILLED = 131
        const val QR_CODE = 132
        const val REMOVE_CIRCLE = 133
        const val SEARCH = 134
        const val SEND_TEXT = 135
        const val SORT_FILTER = 136
        const val TEXT = 137
        const val TEXT_ADD = 138
        const val VISIBILITY = 139
        const val VISIBILITY_OFF = 140

        /**
         * audiovisual
         */
        const val ACTIVITY = 141
        const val CAMERA_SWITCH = 142
        const val FLASH_OFF = 143
        const val FLASH_ON = 144
        const val PAUSE = 145
        const val PLAY = 146
        const val REPLAY = 147  // soft remove on 1.0.2
        const val SCREEN_FULL = 148
        const val SCREEN_NORMAL = 149
        const val SKIP_NEXT = 150
        const val SWIPE = 151
        const val SWIPE_RIGHT = 152
        const val TAP = 153
        const val VOLUME_DOWN = 154
        const val VOLUME_MUTE = 155
        const val VOLUME_UP = 156

        /**
         * user
         */
        const val CARD = 157
        const val CHILD = 158
        const val CONTACT = 159
        const val INFANT = 160
        const val LAPTOP = 161
        const val PHONE = 162
        const val PHONE_SETTING = 163
        const val SALDO = 164
        const val USER = 165
        const val USER_ADD = 166
        const val USER_SUCCESS = 167

        /**
         * toko update
         */
        const val LIST_TRANSACTION = 168
        const val PRODUCT_FILLED = 169
        const val SHOP_FAVORITE = 170
        const val SHOP_FILLED = 171

        /**
         * misc update
         */
        const val POLICY_PRIVACY = 173
        const val QUICK_BUY = 174
        const val SHAKE = 175

        /**
         * user update
         */
        const val COMPLAINT = 177

        /**
         * general update
         */
        const val BELL_FILLED = 178
        const val CABINET = 179
        const val CABINET_FILLED = 180
        const val FEED = 181
        const val FEED_FILLED = 182
        const val HOME = 183
        const val HOME_FILLED = 184
        const val MESSAGE_HELP = 185
        const val MODE_SCREEN = 186
        const val SUBSCRIPTION = 187

        /**
         * general update
         */
        const val CALENDAR_ADD = 188
        const val FIRE_FILLED = 189
        const val FIRE = 190

        /**
         * promo update
         */
        const val STAR_CIRCLE = 191
        const val DISCOUNT = 192
        const val PROMO_ADS = 193

        /**
         * user update
         */
        const val USER_SETTING = 194

        /**
         * navigation update
         */
        const val ARROW_TOP_RIGHT = 195
        const val ARROW_TOP_LEFT = 196

        /**
         * update
         * navigation - social - misc - toko - view - general
         */
        const val CHEVRON_LEFT = 197
        const val CHAT_FILLED = 198
        const val PROTECTION_CHECK = 199
        const val CLIPBOARD_FILLED = 200
        const val COURIER = 201
        const val VIEW_GRID_FILLED = 202
        const val FOLDER = 203

        /**
         * update
         * toko - action
         */
        const val COURIER_FAST = 204
        const val PRODUCT_REPORT = 205
        const val RELOAD = 206
        const val CHECK_CIRCLE = 207
        const val CHECK_BIG = 208

        /**
         * update
         * toko
         */
        const val PRODUCT_BUDGET = 209
        const val SHIRT_BUDGET = 210

        /**
         * update
         * toko - misc - user - social - action - promo - general
         */
        const val GRAPH_FILLED = 211
        const val SHOP_INFO_FILLED = 212
        const val BADGE_VERIFIED_FILLED = 213
        const val CALL_CENTER_FILLED = 214
        const val USER_TALK = 215
        const val USER_TALK_FILLED = 216
        const val USER_REPORT = 217
        const val USER_BLOCK = 218
        const val DISCUSSION_FILLED = 219
        const val BROADCAST = 220
        const val PRINT_FILLED = 221
        const val PROMO_ADS_FILLED = 222
        const val PROMO_BLOCK = 223
        const val SETTING_FILLED = 224
        const val SIGN_OUT_FILLED = 225

        /**
         * update
         */
        const val CALL_RING = 226
        const val CITY = 227
        const val LOCATION_FILLED = 228
        const val LOCATION_OFF = 229
        const val PACKAGE = 230
        const val PACKAGE_FILLED = 231
        const val PHONE_BELL = 232
        const val USER_REMOVE = 233

        /**
         * update
         */
        const val CLOCK_FILLED = 240
        const val FILE_DOC = 241
        const val FILE_PDF = 242
        const val FILE_XLS = 243
        const val RISK_HIGH = 244
        const val RISK_LOW = 245
        const val RISK_MODERATE = 246

        /**
         * update - 18/02/2021
         */
        const val CHAT_REPORT = 248
        const val HELP = 249
        const val MAP = 250
        const val ZOOM_IN = 251
        const val ZOOM_OUT = 252

        /**
         * update - 25/03/2021
         */
        const val SERVICE = 253
        const val SERVICE_FILLED = 254
        const val SALDO_INCOME = 255
        const val SALDO_OUTCOME = 256
        const val SALDO_TEMPO = 257
        const val PROMO_LIVE = 258
        const val REMOVE = 259
        const val RELOAD_24H = 260
        const val CHAT_BELL = 261

        /**
         * update - 07/04/2021
         */
        const val FILE_CSV = 262
        const val FILE_JSON = 263
        const val FOLDER_FILLED = 264
        const val PROJECT = 265
        const val PROJECT_FILLED = 266
        const val USER_FILLED = 267
        const val IP = 268
        const val FREE = 269
        const val UPLOAD = 270
        const val PERFORMANCE = 271

        /**
         * update - 23/04/2021
         */
        const val ARROW_UP = 272
        const val ARROW_DOWN = 273
        const val AUDIO_WAVE = 274
        const val SCREEN_FIT = 275
    }
}