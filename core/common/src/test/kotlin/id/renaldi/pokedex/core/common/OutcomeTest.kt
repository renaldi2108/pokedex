package id.renaldi.pokedex.core.common

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class OutcomeTest {

    private val boom = IllegalStateException("boom")

    @Test
    fun `dataOrNull returns the payload on success and null on failure`() {
        assertEquals(7, Outcome.Success(7).dataOrNull())
        assertNull(Outcome.Failure(boom).dataOrNull())
    }

    @Test
    fun `dataOr substitutes the fallback only on failure`() {
        assertEquals(7, Outcome.Success(7).dataOr(0))
        assertEquals(0, (Outcome.Failure(boom) as Outcome<Int>).dataOr(0))
    }

    @Test
    fun `errorOrNull exposes the cause only on failure`() {
        assertSame(boom, Outcome.Failure(boom).errorOrNull())
        assertNull(Outcome.Success(7).errorOrNull())
    }

    @Test
    fun `map transforms a success and passes a failure through untouched`() {
        assertEquals(Outcome.Success("7"), Outcome.Success(7).map { it.toString() })

        val failure: Outcome<Int> = Outcome.Failure(boom)
        assertSame(failure, failure.map { it.toString() })
    }

    @Test
    fun `runOutcome captures a value or the thrown error`() = runTest {
        assertEquals(Outcome.Success(3), runOutcome { 3 })
        assertSame(boom, runOutcome<Int> { throw boom }.errorOrNull())
    }

    @Test
    fun `value semantics hold`() {
        val success = Outcome.Success(1)
        assertEquals(success, success.copy())
        assertEquals(success.hashCode(), success.copy().hashCode())
        assertNotEquals(success, success.copy(data = 2))
        assertTrue(success.toString().contains("1"))
        assertEquals(1, success.component1())

        val failure = Outcome.Failure(boom)
        assertEquals(failure, failure.copy())
        assertEquals(failure.hashCode(), failure.copy().hashCode())
        assertNotEquals(failure, failure.copy(error = IllegalStateException("other")))
        assertTrue(failure.toString().contains("boom"))
        assertSame(boom, failure.component1())
    }
}
