package id.co.app.components.player

import com.google.android.exoplayer2.upstream.DataSource

/**
 * Created by Lukas Kristianto on 06/07/21 19.45.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
class EncryptedDataSourceFactory(
    private val key: String
) : DataSource.Factory {
    override fun createDataSource(): EncryptedDataSource =
        EncryptedDataSource(key)
}