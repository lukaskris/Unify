package id.co.app.source.core.base.base.adapterdelegate


/**
 * Created by Lukas Kristianto on 4/28/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */

interface DelegateAdapterItem {

	fun id(): Any

	fun content(): Any

	fun payload(other: Any): Payloadable = Payloadable.None

	/**
	 * Simple marker interface for payloads
	 */
	interface Payloadable {
		object None: Payloadable
	}
}