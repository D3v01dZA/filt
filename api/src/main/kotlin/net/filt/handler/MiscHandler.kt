package net.filt.handler

import io.javalin.http.Context
import net.filt.tmdb.TmdbApi

class MiscHandler(private val tmdbApi: TmdbApi) {

    fun search(ctx: Context) {
        ctx.status(200).json(tmdbApi.search("testi"))
    }

}