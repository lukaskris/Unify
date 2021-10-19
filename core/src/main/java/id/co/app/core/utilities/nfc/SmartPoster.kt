package id.co.app.core.utilities.nfc


/**
 * Created by Lukas Kristianto on 09/09/21 09.29.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */

import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import com.google.common.base.Charsets
import com.google.common.base.Preconditions
import com.google.common.collect.ImmutableMap
import com.google.common.collect.Iterables
import java.util.*


/**
 * A representation of an NFC Forum "Smart Poster".
 */
class SmartPoster(private val uriRecord: UriRecord, private val title: TextRecord, private val action: RecommendedAction, type: String?) :
    ParsedNdefRecord {

    /**
     * NFC Forum Smart Poster Record Type Definition section 3.2.1.
     *
     * "The Type record. If the URI references an external entity (e.g., via a
     * URL), the Type record may be used to declare the MIME type of the entity.
     * This can be used to tell the mobile device what kind of an object it can
     * expect before it opens the connection. The Type record is optional."
     */
    override fun str(): String? {
        return if (title != null) {
            """
     ${title.str()}
     ${uriRecord.str()}
     """.trimIndent()
        } else {
            uriRecord.str()
        }
    }

    enum class RecommendedAction(private val byte: Byte) {
        UNKNOWN((-1).toByte()), DO_ACTION(0.toByte()), SAVE_FOR_LATER(1.toByte()), OPEN_FOR_EDITING(
            2.toByte()
        );

        companion object {
            var LOOKUP: ImmutableMap<Byte, RecommendedAction>? = null

            init {
                val builder: ImmutableMap.Builder<Byte, RecommendedAction> = ImmutableMap.builder()
                for (action in values()) {
                    builder.put(action.byte, action)
                }
                LOOKUP = builder.build()
            }
        }

    }

    companion object {
        fun parse(record: NdefRecord): SmartPoster {
            Preconditions.checkArgument(record.tnf == NdefRecord.TNF_WELL_KNOWN)
            Preconditions.checkArgument(Arrays.equals(record.type, NdefRecord.RTD_SMART_POSTER))
            return try {
                val subRecords = NdefMessage(record.payload)
                parse(subRecords.records)
            } catch (e: FormatException) {
                throw IllegalArgumentException(e)
            }
        }

        fun parse(recordsRaw: Array<NdefRecord>): SmartPoster {
            return try {
                val records: Iterable<ParsedNdefRecord> = NdefMessageParser.getRecords(recordsRaw)
                val uri = Iterables.getOnlyElement(
                    Iterables.filter(
                        records,
                        UriRecord::class.java
                    )
                )
                val title = getFirstIfExists(
                    records,
                    TextRecord::class.java
                )!!
                val action = parseRecommendedAction(recordsRaw)
                val type = parseType(recordsRaw)
                SmartPoster(uri, title, action, type)
            } catch (e: NoSuchElementException) {
                throw IllegalArgumentException(e)
            }
        }

        fun isPoster(record: NdefRecord): Boolean {
            return try {
                parse(record)
                true
            } catch (e: IllegalArgumentException) {
                false
            }
        }

        /**
         * Returns the first element of `elements` which is an instance of
         * `type`, or `null` if no such element exists.
         */
        private fun <T> getFirstIfExists(elements: Iterable<*>, type: Class<T>): T? {
            val filtered = Iterables.filter(elements, type)
            var instance: T? = null
            if (!Iterables.isEmpty(filtered)) {
                instance = Iterables.get(filtered, 0)
            }
            return instance
        }

        private fun getByType(type: ByteArray, records: Array<NdefRecord>): NdefRecord? {
            for (record in records) {
                if (Arrays.equals(type, record.type)) {
                    return record
                }
            }
            return null
        }

        private val ACTION_RECORD_TYPE = byteArrayOf('a'.toByte(), 'c'.toByte(), 't'.toByte())
        private fun parseRecommendedAction(records: Array<NdefRecord>): RecommendedAction {
            val record =
                getByType(ACTION_RECORD_TYPE, records)
                    ?: return RecommendedAction.UNKNOWN
            val action = record.payload[0]
            return if (RecommendedAction.LOOKUP!!.containsKey(action)) {
                RecommendedAction.LOOKUP!![action]!!
            } else RecommendedAction.UNKNOWN
        }

        private val TYPE_TYPE = byteArrayOf('t'.toByte())
        private fun parseType(records: Array<NdefRecord>): String? {
            val type = getByType(TYPE_TYPE, records)
                ?: return null
            return String(type.payload, Charsets.UTF_8)
        }
    }
}