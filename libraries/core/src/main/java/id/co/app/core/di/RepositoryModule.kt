package id.co.app.core.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import id.co.app.core.domain.network.PokedexClient
import id.co.app.core.domain.persistence.PokemonDao
import id.co.app.core.domain.repository.MainRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideMainRepository(
        pokedexClient: PokedexClient,
        pokemonDao: PokemonDao
    ): MainRepository {
        return MainRepository(pokemonDao, pokedexClient)
    }

}