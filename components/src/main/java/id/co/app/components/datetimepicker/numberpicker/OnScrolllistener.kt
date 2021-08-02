package id.co.app.components.datetimepicker.numberpicker



interface OnScrollListener {

    fun onScrollStateChange(scrollState: Int)

    companion object {
        const val SCROLL_STATE_IDLE = 0
        const val SCROLL_STATE_TOUCH_SCROLL = 1
        const val SCROLL_STATE_FLING = 2
    }
}