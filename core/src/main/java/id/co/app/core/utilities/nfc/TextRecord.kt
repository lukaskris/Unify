package id.co.app.core.utilities.nfc


/**
 * Created by Lukas Kristianto on 09/09/21 09.25.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

import android.nfc.NdefRecord
import com.google.common.base.Preconditions
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.experimental.and


/**
 * An NFC Text Record
 */
class TextRecord(languageCode: String, text: String) : ParsedNdefRecord {
    /**
     * Returns the ISO/IANA language code associated with this text element.
     */
    /** ISO/IANA language code  */
    val languageCode: String = Preconditions.checkNotNull(languageCode)
    val text: String = Preconditions.checkNotNull(text)

    override fun str(): String? {
        return text
    }

    companion object {
        // TODO: deal with text fields which span multiple NdefRecords
        fun parse(record: NdefRecord): TextRecord {
            Preconditions.checkArgument(record.tnf == NdefRecord.TNF_WELL_KNOWN)
            Preconditions.checkArgument(Arrays.equals(record.type, NdefRecord.RTD_TEXT))
            return try {
                val payload = record.payload
                /*
                      * payload[0] contains the "Status Byte Encodings" field, per the
                      * NFC Forum "Text Record Type Definition" section 3.2.1.
                      *
                      * bit7 is the Text Encoding Field.
                      *
                      * if (Bit_7 == 0): The text is encoded in UTF-8 if (Bit_7 == 1):
                      * The text is encoded in UTF16
                      *
                      * Bit_6 is reserved for future use and must be set to zero.
                      *
                      * Bits 5 to 0 are the length of the IANA language code.
                      */
                val textEncoding = if (payload[0].toInt() and 128 == 0) Charsets.UTF_8 else Charsets.UTF_16
                val languageCodeLength: Int = payload[0].toInt() and 63
                val languageCode = String(payload, 1, languageCodeLength, Charsets.US_ASCII)
                val text = String(
                    payload, languageCodeLength + 1,
                    payload.size - languageCodeLength - 1, textEncoding
                )
                TextRecord(languageCode, text)
            } catch (e: UnsupportedEncodingException) {
                // should never happen unless we get a malformed tag.
                throw IllegalArgumentException(e)
            }
        }

        fun isText(record: NdefRecord): Boolean {
            return try {
                parse(record)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }
    }

}