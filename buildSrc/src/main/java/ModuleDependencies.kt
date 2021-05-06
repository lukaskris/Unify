/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
object ModuleDependencies {
	object Libraries {
		private const val LIBRARY = ":libraries"
		const val CORE = "$LIBRARY:core"
	}

	object Features {
		private const val FEATURES = ":features"
		const val FEED = "$FEATURES:feed"
		const val HOME = "$FEATURES:home"
		const val HOME_DETAIL = "$FEATURES:homedetail"
		const val SETTING = "$FEATURES:setting"
	}
}