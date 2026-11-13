package id.renaldi.pokedex.core.data

import id.renaldi.pokedex.core.common.DispatcherProvider
import id.renaldi.pokedex.core.common.Outcome
import id.renaldi.pokedex.core.common.runOutcome
import id.renaldi.pokedex.core.database.dao.PokemonDao
import id.renaldi.pokedex.core.database.mapper.toEntity
import id.renaldi.pokedex.core.database.mapper.toModel
import id.renaldi.pokedex.core.domain.PokemonRepository
import id.renaldi.pokedex.core.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class DefaultPokemonRepository(
    private val dao: PokemonDao,
    private val remote: PokemonRemoteSource,
    private val dispatchers: DispatcherProvider,
) : PokemonRepository {

    override fun observeAll(): Flow<List<Pokemon>> =
        dao.observeAll().map { entities -> entities.map { it.toModel() } }

    override fun observeById(id: Int): Flow<Pokemon?> =
        dao.observeById(id).map { entity -> entity?.toModel() }

    override suspend fun byIds(ids: Collection<Int>): List<Pokemon> =
        withContext(dispatchers.io) {
            dao.findByIds(ids).map { it.toModel() }
        }

    override suspend fun sync(): Outcome<Int> = withContext(dispatchers.io) {
        runOutcome {
            val cached = dao.count()
            if (cached > 0) {
                cached
            } else {
                val fetched = remote.fetchAll()
                dao.upsertAll(fetched.map { it.toEntity() })
                fetched.size
            }
        }
    }
}
