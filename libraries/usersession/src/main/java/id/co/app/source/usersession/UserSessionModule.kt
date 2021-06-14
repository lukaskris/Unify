package id.co.app.source.usersession

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * Created by Lukas Kristianto on 4/27/2021.
 * App Sinarmas
 * lukas_kristianto@app.co.id
 */
@Module
@InstallIn(SingletonComponent::class)
object UserSessionModule {
	@Provides
	@Singleton
	fun provideUserSession(@ApplicationContext context: Context, moshi: Moshi): UserSession = UserSessionImpl(context, moshi)
}