package id.co.app.core.utilities.nfc

import android.nfc.NdefMessage
import android.nfc.NdefRecord


/**
 * Created by Lukas Kristianto on 09/09/21 09.32.
 * Sinarmas APP
 * lukas_kristianto@app.co.id
 */


object NdefMessageParser {
    fun parse(message: NdefMessage): List<ParsedNdefRecord> {
        return getRecords(message.records)
    }

    fun getRecords(records: Array<NdefRecord>): List<ParsedNdefRecord> {
        val elements: MutableList<ParsedNdefRecord> = ArrayList()
        for (record in records) {
            if (UriRecord.isUri(record)) {
                elements.add(UriRecord.parse(record))
            } else if (TextRecord.isText(record)) {
                elements.add(TextRecord.parse(record))
            } else if (SmartPoster.isPoster(record)) {
                elements.add(SmartPoster.parse(record))
            } else {
                elements.add(object : ParsedNdefRecord {
                    override fun str(): String? {
                        return String(record.payload)
                    }
                })
            }
        }
        return elements
    }
}