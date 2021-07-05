package id.co.app.components.typography

import androidx.core.view.animation.PathInterpolatorCompat

/**
 * Created by Lukas Kristianto on 7/3/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
object UnifyMotion {
    val LINEAR = PathInterpolatorCompat.create(0f, 0f, 1f, 1f)
    val EASE_IN_OUT = PathInterpolatorCompat.create(.63f, .01f, .29f, 1f)
    val EASE_OUT = PathInterpolatorCompat.create(.2f, .64f, .21f, 1f)
    val EASE_OVERSHOOT = PathInterpolatorCompat.create(.63f, .01f, .19f, 1.55f)

    val T1: Long = 120
    val T2: Long = 200
    val T3: Long = 300
    val T4: Long = 400
    val T5: Long = 600
}