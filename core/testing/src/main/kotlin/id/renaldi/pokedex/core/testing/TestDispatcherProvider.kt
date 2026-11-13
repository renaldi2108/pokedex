package id.renaldi.pokedex.core.testing

import id.renaldi.pokedex.core.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher

class TestDispatcherProvider(
    private val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher(),
) : DispatcherProvider {

    override val io: CoroutineDispatcher get() = dispatcher

    override val default: CoroutineDispatcher get() = dispatcher
}
