package id.co.app.core.utilities.nfc


/**
 * Created by Lukas Kristianto on 08/09/21 22.05.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */
class ByteUtils {
    companion object {
        fun isNullOrEmpty(array: ByteArray?): Boolean {
            if (array == null) {
                return true
            }
            val length = array.size
            for (i in 0 until length) {
                if (array[i].toInt() != 0) {
                    return false
                }
            }
            return true
        }
    }
}