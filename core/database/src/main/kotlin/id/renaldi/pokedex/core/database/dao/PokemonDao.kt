package id.renaldi.pokedex.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import id.renaldi.pokedex.core.database.entity.PokemonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDao {

    @Query("SELECT * FROM pokemon ORDER BY id ASC")
    fun observeAll(): Flow<List<PokemonEntity>>

    @Query("SELECT * FROM pokemon WHERE id = :id LIMIT 1")
    fun observeById(id: Int): Flow<PokemonEntity?>

    @Query("SELECT * FROM pokemon WHERE id IN (:ids) ORDER BY id ASC")
    suspend fun findByIds(ids: Collection<Int>): List<PokemonEntity>

    @Query("SELECT COUNT(*) FROM pokemon")
    suspend fun count(): Int

    @Upsert
    suspend fun upsertAll(pokemon: List<PokemonEntity>)

    @Query("DELETE FROM pokemon")
    suspend fun deleteAll()
}
