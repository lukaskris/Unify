package id.co.app.components.carousell

import androidx.annotation.DrawableRes


/**
 * Created by Lukas Kristianto on 27/08/21 14.55.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
data class CarouselItem(
    val url: String = "",
    @DrawableRes val res: Int = -1,
    val id: Int = 0,
)