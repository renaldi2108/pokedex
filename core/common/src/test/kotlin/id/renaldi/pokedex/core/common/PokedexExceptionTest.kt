package id.renaldi.pokedex.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

class PokedexExceptionTest {

    @Test
    fun `a network failure carries the supplied message`() {
        assertEquals("offline", PokedexNetworkException("offline").message)
    }

    @Test
    fun `an empty response explains itself`() {
        assertEquals("PokeAPI returned no data", PokedexEmptyResponseException().message)
    }
}
