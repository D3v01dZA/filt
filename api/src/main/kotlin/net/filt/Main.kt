package net.filt

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.github.cdimascio.dotenv.Dotenv
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.json.JavalinJackson
import net.filt.handler.*
import net.filt.model.initDB
import net.filt.source.tmdb.TmdbApi
import okhttp3.OkHttpClient

const val ID = "id"

fun main() {
    val dotenv = Dotenv.load()
    val javalinJackson = javalinJackson()
    initDB(dotenv)
    val httpClient = OkHttpClient()
    val tmdbApi = TmdbApi(dotenv, httpClient, javalinJackson)
    val directoryHandler = DirectoryHandler(tmdbApi)
    val movieHandler = MovieHandler()
    val accessManager = UserAccessManager()
    val logHandler = LogHandler()

    Javalin.create {
        it.accessManager(accessManager)
        it.jsonMapper(javalinJackson)
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
                path("directory") {
                    post("search", directoryHandler::search, Role.AUTHENTICATED)
                    get("movie/{$ID}", directoryHandler::getMovie, Role.AUTHENTICATED)
                    get("series/{$ID}", directoryHandler::getSeries, Role.AUTHENTICATED)
                }
                path("saved") {
                    path("movie") {
                        crud("{$ID}", movieHandler, Role.AUTHENTICATED)
                    }
                }
            }
        }
        .start(Integer.parseInt(dotenv.get("port")))
}

fun genericResponse(code: String): Map<String, String> {
    return mapOf(Pair("code", code))
}

private fun javalinJackson(): JavalinJackson {
    val javalinJackson = JavalinJackson()
    javalinJackson.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    javalinJackson.mapper.registerModule(KotlinModule.Builder().build())
    return javalinJackson
}