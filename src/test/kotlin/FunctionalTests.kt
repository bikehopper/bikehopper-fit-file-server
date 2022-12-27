import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.assertEquals
import kotlinx.coroutines.runBlocking

@DisplayName("Functional Tests for Fit-Server")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FunctionalTests {
    private val fitServer = FitServer()
    private val client = HttpClient(CIO)

    @BeforeAll
    fun beforeTests() {
        fitServer.start()
    }

    @AfterAll
    fun afterTests() {
        fitServer.stop()
    }

    @DisplayName("Test GET - /health/, expected OK")
    @Test
    fun checkHealth() {
        assertEquals("OK", runBlocking {
            client.get("http://localhost:9001/health").body()
        } )
    }
}