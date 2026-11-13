package id.renaldi.pokedex.feature.splash.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SplashRoutesTest {

    @Test
    fun `the splash screen is a singleton destination`() {
        assertEquals(SplashRoute, SplashRoute)
        assertTrue(SplashRoute.toString().contains("SplashRoute"))
    }
}
