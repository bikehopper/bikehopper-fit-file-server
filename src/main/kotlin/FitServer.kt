import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.ktor.client.plugins.*
import org.slf4j.LoggerFactory

import bikehopperfilecreator.BikeHopperFileCreatorException

class FitServer(private val port: Int = 9001) {
    private val app = Javalin.create(/* TODO: Config */)
    private val logger = LoggerFactory.getLogger("Server")

    fun start() {
        logger.info("Starting Fit-Server v0.1.4")

        app.before { ctx ->
            logger.info("Request: ${ctx.method()} ${ctx.path()} params: ${ctx.queryParamMap()}")
        }

        app.routes {
            // Get a fit file
            path("fit") {
                get(FitFileHandler::getFitFile)
            }

            // Simple health check.  Returns OK on get.
            path("health") {
                get(HealthHandler::getHealth)
            }
        }

        // Handle exceptions thrown from the BikeHopperClient's fetchRoute method
        app.exception(ClientRequestException::class.java) { e, ctx ->
            logger.error("ClientRequestException: ${e.message}")
            ctx.status(400)
            ctx.result(e.localizedMessage)
        }

        // Handle exceptions thrown when creating the FIT file
        app.exception(BikeHopperFileCreatorException::class.java) { e, ctx ->
            logger.error("BikeHopperFileCreatorException: ${e.message}")
            ctx.status(500)
            ctx.result(e.localizedMessage)
        }

        app.start(port)
    }

    fun stop() {
        logger.info("Stopping Fit-Server")
        app.stop()
    }
}