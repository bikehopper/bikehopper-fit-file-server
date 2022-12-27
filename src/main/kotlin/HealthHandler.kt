import io.javalin.http.Context
object HealthHandler {
    fun getHealth(ctx: Context) {
        ctx.result("OK")
    }
}