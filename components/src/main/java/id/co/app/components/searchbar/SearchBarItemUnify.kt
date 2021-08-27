package id.co.app.components.searchbar

import android.graphics.drawable.Drawable
import id.co.app.components.image.ImageUnify

class SearchBarItemUnify(val asset: Drawable?, val listener: () -> Unit) {
    var iconRef: ImageUnify? = null
}