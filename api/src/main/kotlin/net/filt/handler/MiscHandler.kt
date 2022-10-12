package net.filt.handler

import io.javalin.http.Context

class MiscHandler {

    fun search(ctx: Context) {
        ctx.status(200).json(Result("Search"))
    }

}