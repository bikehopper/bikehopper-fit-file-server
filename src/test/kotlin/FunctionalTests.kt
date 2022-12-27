import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import com.garmin.fit.*

@DisplayName("Functional Tests for Fit-Server")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FunctionalTests {
    private val fitServer = FitServer()
    private val client = HttpClient(CIO)
    private val port = 9001

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
            client.get("http://localhost:${port}/health").bodyAsText()
        } )
    }

    @DisplayName("Test GET - /fit/, FIT data created is valid Fit Data")
    @Test
    fun checkFitFile() {
        val params = mapOf(
            "locale" to listOf("en-US"),
            "elevation" to listOf("true"),
            "useMiles" to listOf("false"),
            "layer" to listOf("OpenStreetMap"),
            "profile" to listOf("pt"),
            "optimize" to listOf("true"),
            "pointsEncoded" to listOf("false"),
            "pt.earliest_departure_time" to listOf("2022-11-01T02:31:44.439Z"),
            "pt.connecting_profile" to listOf("bike2"),
            "pt.arrive_by" to listOf("false"),
            "details" to listOf("cycleway", "road_class", "street_name"),
            "point" to listOf("37.78306,-122.45867", "37.78516,-122.46238"),
            "path" to listOf("0")
        )

        // Get fit byte array.
        val fitData: ByteArray = runBlocking {
            client.get("http://localhost:${port}/fit"){
                url {
                    params.forEach { (key, values) -> values.forEach { value -> parameters.append(key, value)}}
                }
            }.body()
        }

        val decoder = Decode()
        assertTrue(decoder.checkFileIntegrity(fitData.inputStream()), "Data is not in Fit format")
    }
}