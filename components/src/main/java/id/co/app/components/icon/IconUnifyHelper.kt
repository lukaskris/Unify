package id.co.app.components.icon

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import id.co.app.components.R
import id.co.app.components.R.drawable as iconR

/**
 * Created by Lukas Kristianto on 7/3/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
internal fun getResourceRef(iconId: Int): Int {
    return when (iconId) {
        IconUnify.BELL -> iconR.iconunify_bell
        IconUnify.BELL_RING -> iconR.iconunify_bell_ring
        IconUnify.CALENDAR -> iconR.iconunify_calendar
        IconUnify.CAMERA -> iconR.iconunify_camera
        IconUnify.CART -> iconR.iconunify_cart
        IconUnify.CLOCK -> iconR.iconunify_clock
        IconUnify.HEART -> iconR.iconunify_heart
        IconUnify.HEART_FILLED -> iconR.iconunify_heart_filled
        IconUnify.IMAGE -> iconR.iconunify_image
        IconUnify.IMAGE_ADD -> iconR.iconunify_image_add
        IconUnify.IMAGE_BROKEN -> iconR.iconunify_image_broken
        IconUnify.IMAGE_SEARCH -> iconR.iconunify_image_search
        IconUnify.INFORMATION -> iconR.iconunify_information
        IconUnify.LIGHT_BULB -> iconR.iconunify_lightbulb
        IconUnify.LOCATION -> iconR.iconunify_location
        IconUnify.MESSAGE -> iconR.iconunify_message
        IconUnify.MICROPHONE -> iconR.iconunify_microphone
        IconUnify.SETTING -> iconR.iconunify_setting
        IconUnify.SIGN_IN -> iconR.iconunify_sign_in
        IconUnify.SIGN_OUT -> iconR.iconunify_sign_out
        IconUnify.TARGET -> iconR.iconunify_target
        IconUnify.THUMB -> iconR.iconunify_thumb
        IconUnify.THUMB_FILLED -> iconR.iconunify_thumb_filled
        IconUnify.VIDEO -> iconR.iconunify_video
        IconUnify.VIDEO_BROKEN -> iconR.iconunify_video_broken
        IconUnify.WALLET -> iconR.iconunify_wallet

        IconUnify.ARROW_BACK -> iconR.iconunify_arrow_back
        IconUnify.BACK_TO_TOP -> iconR.iconunify_back_to_top
        IconUnify.CHEVRON_DOWN -> iconR.iconunify_chevron_down
        IconUnify.CHEVRON_RIGHT -> iconR.iconunify_chevron_right
        IconUnify.CHEVRON_UP -> iconR.iconunify_chevron_up
        IconUnify.CLOSE -> iconR.iconunify_close
        IconUnify.ERROR -> iconR.iconunify_error // soft remove on 1.0.2
        IconUnify.MENU_HAMBURGER -> iconR.iconunify_menu_hamburger
        IconUnify.MENU_KEBAB_HORIZONTAL -> iconR.iconunify_menu_kebab_horizontal
        IconUnify.MENU_KEBAB_VERTICAL -> iconR.iconunify_menu_kebab_vertical
        IconUnify.VIEW_GRID -> iconR.iconunify_view_grid
        IconUnify.VIEW_GRID_BIG -> iconR.iconunify_view_grid_big
        IconUnify.VIEW_LIST -> iconR.iconunify_view_list
        IconUnify.WARNING -> iconR.iconunify_warning

        IconUnify.COUPON -> iconR.iconunify_coupon
        IconUnify.FORMAT_ALIGN_LEFT -> iconR.iconunify_format_align_left
        IconUnify.FORMAT_ALIGN_RIGHT -> iconR.iconunify_format_align_right
        IconUnify.FORMAT_BOLD -> iconR.iconunify_format_bold
        IconUnify.FORMAT_CENTER -> iconR.iconunify_format_center
        IconUnify.FORMAT_ITALIC -> iconR.iconunify_format_italic
        IconUnify.FORMAT_JUSTIFY -> iconR.iconunify_format_justify
        IconUnify.FORMAT_LIST_BULLET -> iconR.iconunify_format_list_bullet
        IconUnify.FORMAT_UNDERLINE -> iconR.iconunify_format_underline
        IconUnify.INVITATION -> iconR.iconunify_invitation
        IconUnify.PROMO -> iconR.iconunify_promo
        IconUnify.REDO -> iconR.iconunify_redo
        IconUnify.SPEAKER -> iconR.iconunify_speaker
        IconUnify.TICKET_ACTIVE -> iconR.iconunify_ticket_active
        IconUnify.TICKET_CHANGE -> iconR.iconunify_ticket_change
        IconUnify.TICKET_HISTORY -> iconR.iconunify_ticket_history
        IconUnify.TICKET_INSTANT -> iconR.iconunify_ticket_instant
        IconUnify.TOPQUEST -> iconR.iconunify_topquest
        IconUnify.UNDO -> iconR.iconunify_undo

        IconUnify.CALL -> iconR.iconunify_call
        IconUnify.CALL_MISSED -> iconR.iconunify_call_missed
        IconUnify.CHAT -> iconR.iconunify_chat
        IconUnify.DISCUSSION -> iconR.iconunify_discussion
        IconUnify.FACEBOOK -> iconR.iconunify_facebook
        IconUnify.GOOGLE_AUTHENTICATOR -> iconR.iconunify_google_authenticator
        IconUnify.INSTAGRAM -> iconR.iconunify_instagram
        IconUnify.LINE -> iconR.iconunify_line
        IconUnify.LINK -> iconR.iconunify_link
        IconUnify.READ -> iconR.iconunify_read
        IconUnify.SHARE -> iconR.iconunify_share
        IconUnify.SHARE_MOBILE -> iconR.iconunify_share_mobile
        IconUnify.TWITTER -> iconR.iconunify_twitter
        IconUnify.UNREAD -> iconR.iconunify_unread
        IconUnify.WHATSAPP -> iconR.iconunify_whatsapp

        IconUnify.AUTO_DEBIT -> iconR.iconunify_auto_debit
        IconUnify.BADGE_VERIFIED -> iconR.iconunify_badge_verified
        IconUnify.BILL -> iconR.iconunify_bill
        IconUnify.BOOKMARK -> iconR.iconunify_bookmark
        IconUnify.BOOKMARK_FILLED -> iconR.iconunify_bookmark_filled
        IconUnify.CALL_CENTER -> iconR.iconunify_call_center
        IconUnify.FINANCE -> iconR.iconunify_finance
        IconUnify.FLASH_SALE -> iconR.iconunify_flash_sale
        IconUnify.GOLD -> iconR.iconunify_gold
        IconUnify.KEYBOARD -> iconR.iconunify_keyboard
        IconUnify.KEYWORD -> iconR.iconunify_keyword
        IconUnify.KEYWORD_NEGATIVE -> iconR.iconunify_keyword_negative
        IconUnify.POLL -> iconR.iconunify_poll
        IconUnify.PROTECTION -> iconR.iconunify_protection
        IconUnify.RATING -> iconR.iconunify_rating
        IconUnify.REFUND -> iconR.iconunify_refund
        IconUnify.REFUND_NON -> iconR.iconunify_refund_non
        IconUnify.STAR -> iconR.iconunify_star
        IconUnify.STAR_FILLED -> iconR.iconunify_star_filled
        IconUnify.STICKER -> iconR.iconunify_sticker

        IconUnify.BILL_ALL -> iconR.iconunify_bill_all
        IconUnify.BILL_CHECK -> iconR.iconunify_bill_check
        IconUnify.CLIPBOARD -> iconR.iconunify_clipboard
        IconUnify.GIFT -> iconR.iconunify_gift
        IconUnify.GRAPH -> iconR.iconunify_graph
        IconUnify.GRAPH_REPORT -> iconR.iconunify_graph_report
        IconUnify.NOTEBOOK -> iconR.iconunify_notebook
        IconUnify.PRODUCT -> iconR.iconunify_product
        IconUnify.PRODUCT_ADD -> iconR.iconunify_product_add
        IconUnify.PRODUCT_INFO -> iconR.iconunify_product_info
        IconUnify.PRODUCT_NEXT -> iconR.iconunify_product_next
        IconUnify.PRODUCT_PROMO -> iconR.iconunify_product_promo
        IconUnify.PRODUCT_SETTING -> iconR.iconunify_product_setting
        IconUnify.SHOP -> iconR.iconunify_shop
        IconUnify.SHOP_INFO -> iconR.iconunify_shop_info
        IconUnify.SHOP_REPORT -> iconR.iconunify_shop_report
        IconUnify.SHOP_SETTING -> iconR.iconunify_shop_setting
        IconUnify.SHOPPING_BAG -> iconR.iconunify_shopping_bag
        IconUnify.STAMP -> iconR.iconunify_stamp

        IconUnify.ADD -> iconR.iconunify_add
        IconUnify.ADD_CIRCLE -> iconR.iconunify_add_circle
        IconUnify.BACKGROUND -> iconR.iconunify_background
        IconUnify.BROOM -> iconR.iconunify_broom
        IconUnify.CHECK -> iconR.iconunify_check
        IconUnify.CHECK_DOUBLE -> iconR.iconunify_check_double
        IconUnify.CLEAR -> iconR.iconunify_clear
        IconUnify.COMPARE -> iconR.iconunify_compare
        IconUnify.COPY -> iconR.iconunify_copy
        IconUnify.CROP -> iconR.iconunify_crop
        IconUnify.DELETE -> iconR.iconunify_delete
        IconUnify.DOWNLOAD -> iconR.iconunify_download
        IconUnify.EDIT -> iconR.iconunify_edit
        IconUnify.LOCK -> iconR.iconunify_lock
        IconUnify.PRINT -> iconR.iconunify_print
        IconUnify.PUSH_PIN -> iconR.iconunify_push_pin
        IconUnify.PUSH_PIN_FILLED -> iconR.iconunify_push_pin_filled
        IconUnify.QR_CODE -> iconR.iconunify_qr_code
        IconUnify.REMOVE_CIRCLE -> iconR.iconunify_remove_circle
        IconUnify.SEARCH -> iconR.iconunify_search
        IconUnify.SEND_TEXT -> iconR.iconunify_send_text
        IconUnify.SORT_FILTER -> iconR.iconunify_sort_filter
        IconUnify.TEXT -> iconR.iconunify_text
        IconUnify.TEXT_ADD -> iconR.iconunify_text_add
        IconUnify.VISIBILITY -> iconR.iconunify_visibility
        IconUnify.VISIBILITY_OFF -> iconR.iconunify_visibility_off

        IconUnify.ACTIVITY -> iconR.iconunify_activity
        IconUnify.CAMERA_SWITCH -> iconR.iconunify_camera_switch
        IconUnify.FLASH_OFF -> iconR.iconunify_flash_off
        IconUnify.FLASH_ON -> iconR.iconunify_flash_on
        IconUnify.PAUSE -> iconR.iconunify_pause
        IconUnify.PLAY -> iconR.iconunify_play
        IconUnify.REPLAY -> iconR.iconunify_error // soft remove on 1.0.2
        IconUnify.SCREEN_FULL -> iconR.iconunify_screen_full
        IconUnify.SCREEN_NORMAL -> iconR.iconunify_screen_normal
        IconUnify.SKIP_NEXT -> iconR.iconunify_skip_next
        IconUnify.SWIPE -> iconR.iconunify_swipe
        IconUnify.SWIPE_RIGHT -> iconR.iconunify_swipe_right
        IconUnify.TAP -> iconR.iconunify_tap
        IconUnify.VOLUME_DOWN -> iconR.iconunify_volume_down
        IconUnify.VOLUME_MUTE -> iconR.iconunify_volume_mute
        IconUnify.VOLUME_UP -> iconR.iconunify_volume_up

        IconUnify.CARD -> iconR.iconunify_card
        IconUnify.CHILD -> iconR.iconunify_child
        IconUnify.CONTACT -> iconR.iconunify_contact
        IconUnify.INFANT -> iconR.iconunify_infant
        IconUnify.LAPTOP -> iconR.iconunify_laptop
        IconUnify.PHONE -> iconR.iconunify_phone
        IconUnify.PHONE_SETTING -> iconR.iconunify_phone_setting
        IconUnify.SALDO -> iconR.iconunify_saldo
        IconUnify.USER -> iconR.iconunify_user
        IconUnify.USER_ADD -> iconR.iconunify_user_add
        IconUnify.USER_SUCCESS -> iconR.iconunify_user_success

        IconUnify.LIST_TRANSACTION -> iconR.iconunify_list_transaction
        IconUnify.PRODUCT_FILLED -> iconR.iconunify_product_filled
        IconUnify.SHOP_FAVORITE -> iconR.iconunify_shop_favorite
        IconUnify.SHOP_FILLED -> iconR.iconunify_shop_filled

        IconUnify.POLICY_PRIVACY -> iconR.iconunify_policy_privacy
        IconUnify.QUICK_BUY -> iconR.iconunify_quick_buy
        IconUnify.SHAKE -> iconR.iconunify_shake

        IconUnify.COMPLAINT -> iconR.iconunify_complaint

        IconUnify.BELL_FILLED -> iconR.iconunify_bell_filled
        IconUnify.CABINET -> iconR.iconunify_cabinet
        IconUnify.CABINET_FILLED -> iconR.iconunify_cabinet_filled
        IconUnify.FEED -> iconR.iconunify_feed
        IconUnify.FEED_FILLED -> iconR.iconunify_feed_filled
        IconUnify.HOME -> iconR.iconunify_home
        IconUnify.HOME_FILLED -> iconR.iconunify_home_filled
        IconUnify.MESSAGE_HELP -> iconR.iconunify_message_help
        IconUnify.MODE_SCREEN -> iconR.iconunify_mode_screen
        IconUnify.SUBSCRIPTION -> iconR.iconunify_subscription

        IconUnify.CALENDAR_ADD -> iconR.iconunify_calendar_add
        IconUnify.FIRE_FILLED -> iconR.iconunify_fire_filled
        IconUnify.FIRE -> iconR.iconunify_fire

        IconUnify.STAR_CIRCLE -> iconR.iconunify_star_circle
        IconUnify.DISCOUNT -> iconR.iconunify_discount
        IconUnify.PROMO_ADS -> iconR.iconunify_promo_ads

        IconUnify.USER_SETTING -> iconR.iconunify_user_setting

        IconUnify.ARROW_TOP_RIGHT -> iconR.iconunify_arrow_top_right
        IconUnify.ARROW_TOP_LEFT -> iconR.iconunify_arrow_top_left

        IconUnify.CHEVRON_LEFT -> iconR.iconunify_chevron_left
        IconUnify.CHAT_FILLED -> iconR.iconunify_chat_filled
        IconUnify.PROTECTION_CHECK -> iconR.iconunify_protection_check
        IconUnify.CLIPBOARD_FILLED -> iconR.iconunify_clipboard_filled
        IconUnify.COURIER -> iconR.iconunify_courier
        IconUnify.VIEW_GRID_FILLED -> iconR.iconunify_view_grid_filled
        IconUnify.FOLDER -> iconR.iconunify_folder

        IconUnify.COURIER_FAST -> iconR.iconunify_courier_fast
        IconUnify.PRODUCT_REPORT -> iconR.iconunify_product_report
        IconUnify.RELOAD -> iconR.iconunify_reload
        IconUnify.CHECK_CIRCLE -> iconR.iconunify_check_circle
        IconUnify.CHECK_BIG -> iconR.iconunify_check_big

        IconUnify.PRODUCT_BUDGET -> iconR.iconunify_product_budget
        IconUnify.SHIRT_BUDGET -> iconR.iconunify_shirt_budget

        IconUnify.GRAPH_FILLED -> iconR.iconunify_graph_filled
        IconUnify.SHOP_INFO_FILLED -> iconR.iconunify_shop_info_filled
        IconUnify.BADGE_VERIFIED_FILLED -> iconR.iconunify_badge_verified_filled
        IconUnify.CALL_CENTER_FILLED -> iconR.iconunify_call_center_filled
        IconUnify.USER_TALK -> iconR.iconunify_user_talk
        IconUnify.USER_TALK_FILLED -> iconR.iconunify_user_talk_filled
        IconUnify.USER_REPORT -> iconR.iconunify_user_report
        IconUnify.USER_BLOCK -> iconR.iconunify_user_block
        IconUnify.DISCUSSION_FILLED -> iconR.iconunify_discussion_filled
        IconUnify.BROADCAST -> iconR.iconunify_broadcast
        IconUnify.PRINT_FILLED -> iconR.iconunify_print_filled
        IconUnify.PROMO_ADS_FILLED -> iconR.iconunify_promo_ads_filled
        IconUnify.PROMO_BLOCK -> iconR.iconunify_promo_block
        IconUnify.SETTING_FILLED -> iconR.iconunify_setting_filled
        IconUnify.SIGN_OUT_FILLED -> iconR.iconunify_sign_out_filled

        IconUnify.CALL_RING -> iconR.iconunify_call_ring
        IconUnify.CITY -> iconR.iconunify_city
        IconUnify.LOCATION_FILLED -> iconR.iconunify_location_filled
        IconUnify.LOCATION_OFF -> iconR.iconunify_location_off
        IconUnify.PACKAGE -> iconR.iconunify_package
        IconUnify.PACKAGE_FILLED -> iconR.iconunify_package_filled
        IconUnify.PHONE_BELL -> iconR.iconunify_phone_bell
        IconUnify.USER_REMOVE -> iconR.iconunify_user_remove

        IconUnify.CLOCK_FILLED -> iconR.iconunify_clock_filled
        IconUnify.FILE_DOC -> iconR.iconunify_file_doc
        IconUnify.FILE_PDF -> iconR.iconunify_file_pdf
        IconUnify.FILE_XLS -> iconR.iconunify_file_xls
        IconUnify.RISK_HIGH -> iconR.iconunify_risk_high
        IconUnify.RISK_LOW -> iconR.iconunify_risk_low
        IconUnify.RISK_MODERATE -> iconR.iconunify_risk_moderate

        IconUnify.CHAT_REPORT -> iconR.iconunify_chat_report
        IconUnify.HELP -> iconR.iconunify_help
        IconUnify.MAP -> iconR.iconunify_map
        IconUnify.ZOOM_IN -> iconR.iconunify_zoom_in
        IconUnify.ZOOM_OUT -> iconR.iconunify_zoom_out

        IconUnify.SERVICE -> iconR.iconunify_service
        IconUnify.SERVICE_FILLED -> iconR.iconunify_service_filled
        IconUnify.SALDO_INCOME -> iconR.iconunify_saldo_income
        IconUnify.SALDO_OUTCOME -> iconR.iconunify_saldo_outcome
        IconUnify.SALDO_TEMPO -> iconR.iconunify_saldo_tempo
        IconUnify.PROMO_LIVE -> iconR.iconunify_promo_live
        IconUnify.REMOVE -> iconR.iconunify_remove
        IconUnify.RELOAD_24H -> iconR.iconunify_24h
        IconUnify.CHAT_BELL -> iconR.iconunify_chat_bell

        IconUnify.FILE_CSV -> iconR.iconunify_file_csv
        IconUnify.FILE_JSON -> iconR.iconunify_file_json
        IconUnify.FOLDER_FILLED -> iconR.iconunify_folder_filled
        IconUnify.PROJECT -> iconR.iconunify_project
        IconUnify.PROJECT_FILLED -> iconR.iconunify_project_filled
        IconUnify.USER_FILLED -> iconR.iconunify_user_filled
        IconUnify.IP -> iconR.iconunify_ip
        IconUnify.FREE -> iconR.iconunify_free
        IconUnify.UPLOAD -> iconR.iconunify_upload
        IconUnify.PERFORMANCE -> iconR.iconunify_performance

        IconUnify.ARROW_UP -> iconR.iconunify_arrow_up
        IconUnify.ARROW_DOWN -> iconR.iconunify_arrow_down
        IconUnify.AUDIO_WAVE -> iconR.iconunify_audio_wave
        IconUnify.SCREEN_FIT -> iconR.iconunify_screen_fit

        else -> iconR.iconunify_error
    }
}

internal fun isExcluded(iconId: Int): Boolean {
    return when (iconId) {
        IconUnify.FACEBOOK,
        IconUnify.GOOGLE_AUTHENTICATOR,
        IconUnify.INSTAGRAM,
        IconUnify.LINE,
        IconUnify.TWITTER,
        IconUnify.WHATSAPP -> true
        else -> false
    }
}

fun getIconUnifyDrawable(
    context: Context,
    iconId: Int,
    assetColor: Int? = null
): Drawable? {
    val img = AppCompatResources.getDrawable(context, getResourceRef(iconId))

    if(!isExcluded(iconId)){
        val color = assetColor ?: ContextCompat.getColor(context, R.color.icon_enable_default_color)
        img?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
    }

    img?.mutate()
    return img
}

fun applyIconUnifyColor(drawable: Drawable, color: Int) {
    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
}