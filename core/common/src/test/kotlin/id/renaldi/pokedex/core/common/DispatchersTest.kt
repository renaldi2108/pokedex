package id.renaldi.pokedex.core.common

import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertSame
import org.junit.Test

class DispatchersTest {

    @Test
    fun `the default provider hands back the standard dispatchers`() {
        val provider = DefaultDispatcherProvider()
        assertSame(Dispatchers.IO, provider.io)
        assertSame(Dispatchers.Default, provider.default)
    }
}
