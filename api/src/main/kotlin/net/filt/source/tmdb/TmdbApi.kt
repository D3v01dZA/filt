package net.filt.source.tmdb

import io.github.cdimascio.dotenv.Dotenv
import io.javalin.http.HttpResponseException
import io.javalin.json.JavalinJackson
import net.filt.source.*
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder

class TmdbApi(dotenv: Dotenv, private val httpClient: OkHttpClient, private val javalinJackson: JavalinJackson) : DirectoryApi {

    private val apiKey: String = dotenv.get("tmdb_api_key")

    override fun search(term: String, page: Int): SearchResponse {
        val request = builder("search/multi", mapOf(Pair("query", term))).get().build()
        return fetch(request, TmdbSearchResponse::class.java).asDirectory()
    }

    override fun getMovie(id: Int): MovieMetadataResponse {
        val request = builder("movie/$id").get().build()
        return fetch(request, TmdbMovieMetadataResponse::class.java).asDirectory()
    }

    override fun getSeries(id: Int): SeriesMetadataResponse {
        val request = builder("tv/$id").get().build()
        val seriesMetadata = fetch(request, TmdbSeriesMetadataResponse::class.java)
        val seasonsMetadata = seriesMetadata.seasons.map { getSeason(id, it.seasonNumber) }
        return seriesMetadata.asDirectory(seasonsMetadata)
    }

    private fun getSeason(seriesId: Int, seasonNumber: Int): SeriesSeasonMetadataResponse {
        val request = builder("tv/$seriesId/season/$seasonNumber").get().build()
        return fetch(request, TmdbSeriesSeasonMetadataResponse::class.java).asDirectory()
    }

    private fun <T> fetch(request: Request, type: Class<T>): T {
        httpClient.newCall(request).execute().use {
            if (!it.isSuccessful) {
                throw HttpResponseException(502, "TMDB Failure")
            }
            val body = it.body?.string() ?: throw HttpResponseException(502, "TMDB Failure")
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
            "&" + params.entries.joinToString { "${URLEncoder.encode(it.key, "UTF-8")}=${URLEncoder.encode(it.value, "UTF-8")}" }
        }
        return Request.Builder()
            .url("https://api.themoviedb.org/3/$path?api_key=$apiKey$paramString")
    }

}