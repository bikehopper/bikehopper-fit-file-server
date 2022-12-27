import io.javalin.http.Context

import bikehopperclient.BikeHopperClient
import bikehopperfilecreator.BikeHopperFileCreator

object FitFileHandler {
    fun getFitFile(ctx: Context) {
        val bhClient = BikeHopperClient()
        // Use bhClient to fetch our routes after pulling out the "path" param which is only used locally for us.
        val routeData = bhClient.fetchRoute(ctx.queryParamMap().filter { it.key != "path" })

        // Quick check that the path we choose actually exists and has just a single leg.
        val path = ctx.queryParamAsClass<Int>("path", Int::class.java).check(
            { it >= 0 && it < routeData.paths.size && routeData.paths[it].legs.size == 1 },
            "Invalid path selection, must be between 0 and ${routeData.paths.size}"
        ).get()
        val bikeHopperFileCreator = BikeHopperFileCreator(routeData, path)

        ctx.contentType("application/vnd.ant.fit")
        ctx.result(bikeHopperFileCreator.getBuffer())
    }
}