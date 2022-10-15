package net.filt.handler

import io.javalin.http.Context
import net.filt.ID
import net.filt.source.DirectoryApi

class DirectoryHandler(private val directoryApi: DirectoryApi) {

    fun search(ctx: Context) {
        val request = ctx.bodyAsClass(SearchRequest::class.java)
        val search = directoryApi.search(request.query, request.page)
        ctx.status(200).json(search)
    }

    fun getMovie(ctx: Context) {
        val id = ctx.pathParamAsClass(ID, Int::class.java).get()
        val movie = directoryApi.getMovie(id)
        ctx.status(200).json(movie)
    }

    fun getSeries(ctx: Context) {
        val id = ctx.pathParamAsClass(ID, Int::class.java).get()
        val movie = directoryApi.getSeries(id)
        ctx.status(200).json(movie)
    }

}