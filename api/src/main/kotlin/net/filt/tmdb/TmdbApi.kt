package net.filt.tmdb

import io.github.cdimascio.dotenv.Dotenv
import io.javalin.http.HttpResponseException
import io.javalin.json.JavalinJackson
import net.filt.genericResponse
import okhttp3.OkHttpClient
import okhttp3.Request

class TmdbApi(dotenv: Dotenv, private val httpClient: OkHttpClient, private val javalinJackson: JavalinJackson) {

    private val apiKey: String = dotenv.get("tmdb_api_key")

    fun search(term: String): TmdbSearchResponse {
        val request = builder("search/multi", mapOf(Pair("query", term))).get()
            .build()
        return fetch(request, TmdbSearchResponse::class.java)
    }

    private fun <T> fetch(request: Request, type: Class<T>): T {
        httpClient.newCall(request).execute().use {
            if (!it.isSuccessful) {
                throw HttpResponseException(502, "TMDB Failure", genericResponse("TMDB Failure"))
            }
            val body = it.body?.string() ?: throw HttpResponseException(502, "TMDB Failure", genericResponse("TMDB Failure"))
            return javalinJackson.fromJsonString(body, type)
        }
    }

    private fun builder(path: String): Request.Builder {
        return builder(path, mapOf())
    }

    private fun builder(path: String, params: Map<String, String>): Request.Builder {
        val paramString = if (params.isEmpty()) {
            ""
        } else {
            "&" + params.entries.joinToString { "${it.key}=${it.value}" }
        }
        return Request.Builder()
            .url("https://api.themoviedb.org/3/$path?api_key=$apiKey$paramString")
    }

}