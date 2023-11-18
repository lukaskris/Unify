/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
object ModuleDependencies {

	private const val COMPONENTS = ":components"
	private const val CORE = ":core"
	private const val CAMERA = ":camera"
	private const val RETROFIT_URL_BUILDER = ":retrofiturlhandler"
	private const val LS_DOWNLOADER = ":lsdownloader"
	private const val NOTIFICATION = ":notification"

	val widgets = arrayListOf<String>().apply {
		add(COMPONENTS)
		add(CAMERA)
		add(CORE)
		add(RETROFIT_URL_BUILDER)
		add(LS_DOWNLOADER)
		add(NOTIFICATION)
	}
}