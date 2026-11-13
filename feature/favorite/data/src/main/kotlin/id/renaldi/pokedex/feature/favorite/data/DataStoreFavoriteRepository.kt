package id.renaldi.pokedex.feature.favorite.data

import id.renaldi.pokedex.core.database.favorite.FavoriteStore
import id.renaldi.pokedex.core.domain.FavoriteRepository
import kotlinx.coroutines.flow.Flow

class DataStoreFavoriteRepository(private val store: FavoriteStore) : FavoriteRepository {

    override fun observeIds(): Flow<Set<Int>> = store.observeIds()

    override suspend fun toggle(id: Int) = store.toggle(id)
}
