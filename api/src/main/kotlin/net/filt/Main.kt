package net.filt

import io.github.cdimascio.dotenv.Dotenv
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import net.filt.handler.*
import net.filt.model.initDB

fun main() {
    val dotenv = Dotenv.load()
    initDB(dotenv)
    val miscHandler = MiscHandler()
    val movieHandler = MovieHandler()
    val accessManager = UserAccessManager()
    val logHandler = LogHandler()

    Javalin.create { config ->
        config.accessManager(accessManager)
    }
        .before(logHandler::before)
        .after(logHandler::after)
        .routes {
            path("auth") {
                post("register", accessManager::register, Role.ANYONE)
                post("login", accessManager::login, Role.ANYONE)
                post("logout", accessManager::logout, Role.AUTHENTICATED)
            }
            path("api") {
                post("search", miscHandler::search, Role.AUTHENTICATED)
                path("movie") {
                    crud("{movie-id}", movieHandler, Role.AUTHENTICATED)
                }
            }
        }
        .start(Integer.parseInt(dotenv.get("port")))
}