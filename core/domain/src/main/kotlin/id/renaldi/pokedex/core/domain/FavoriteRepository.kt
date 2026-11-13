package id.renaldi.pokedex.core.domain

import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun observeIds(): Flow<Set<Int>>

    suspend fun toggle(id: Int)
}
