/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
object ModuleDependencies {
	object Libraries {
		private const val LIBRARY = ":libraries"
		const val CORE = "$LIBRARY:core"
		const val CAMERA = "$LIBRARY:camera"
		const val USER_SESSION = "$LIBRARY:usersession"
		const val QUERY_BUILDER = "$LIBRARY:querybuilder"
	}

	object Features {
		private const val FEATURES = ":features"
		const val LOGIN = "$FEATURES:login"
		const val HOME = "$FEATURES:home"
	}

	val features = arrayListOf<String>().apply {
		add(Features.LOGIN)
		add(Features.HOME)
	}

	val libraries = arrayListOf<String>().apply {
		add(Libraries.CORE)
		add(Libraries.CAMERA)
		add(Libraries.USER_SESSION)
		add(Libraries.QUERY_BUILDER)
	}
}