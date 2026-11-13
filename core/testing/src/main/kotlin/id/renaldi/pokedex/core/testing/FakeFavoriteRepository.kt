package id.renaldi.pokedex.core.testing

import id.renaldi.pokedex.core.domain.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeFavoriteRepository(
    initial: Set<Int> = emptySet(),
) : FavoriteRepository {

    private val ids = MutableStateFlow(initial)

    val current: Set<Int> get() = ids.value

    val toggled: MutableList<Int> = mutableListOf()

    fun emit(values: Set<Int>) {
        ids.value = values
    }

    override fun observeIds(): Flow<Set<Int>> = ids

    override suspend fun toggle(id: Int) {
        toggled += id
        ids.value = if (id in ids.value) ids.value - id else ids.value + id
    }
}
