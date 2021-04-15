package id.co.app.core.domain.repository

import id.co.app.core.base.Result
import id.co.app.core.domain.network.PokedexClient
import id.co.app.core.domain.persistence.PokemonDao
import id.co.app.core.extension.whatIfNotNull
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val pokedexClient: PokedexClient
) {
    fun fetchPokemonList(
        page: Int
    ) = flow {
        try{
            emit(Result.Loading)
            var pokemons = pokemonDao.getPokemonList(page)
            if (pokemons.isEmpty()) {
                pokedexClient.fetchPokemonList(page = page).result.whatIfNotNull { data ->
                    pokemons = data
                    pokemons.forEach { pokemon -> pokemon.page = page }
                    pokemonDao.insertPokemonList(pokemons)
                    emit(Result.Success(pokemons))
                }
            } else {
                emit(Result.Success(pokemons))
            }
        }catch (ex: Exception){
            emit(Result.Failure(ex))
        }
    }
}