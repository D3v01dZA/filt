package net.filt

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.path

fun main() {
    val app = Javalin.create()
        .routes {
            path("api") {
                path("search") {

                }
            }
        }
        .get("/") { ctx -> ctx.result("Hello World") }
        .start(7070)
}