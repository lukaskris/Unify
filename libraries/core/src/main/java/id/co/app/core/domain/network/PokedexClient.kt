package id.co.app.core.domain.network

import id.co.app.core.domain.entities.PokemonInfo
import id.co.app.core.domain.entities.PokemonResponse

class PokedexClient (private val service: PokedexService) {

    suspend fun fetchPokemonList(page: Int): PokemonResponse =
        service.fetchPokemonList(
            limit = PAGING_SIZE,
            offset = page * PAGING_SIZE
        )

    suspend fun fetchPokemonInfo(
        name: String
    ): PokemonInfo =
        service.fetchPokemonInfo(
            name = name
        )

    companion object{
        private const val PAGING_SIZE = 20
    }
}