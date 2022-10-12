package net.filt.handler

import io.javalin.http.Context
import org.slf4j.LoggerFactory

class LogHandler {

    private val logger = LoggerFactory.getLogger(LogHandler::class.java)

    fun before(ctx: Context) {
        ctx.attribute("started", System.currentTimeMillis())
    }

    fun after(ctx: Context) {
        val started = ctx.attribute<Long>("started")!!
        val userId = ctx.sessionAttribute<Any>("userId")
        logger.info(
            "{} [{}] {} took {}ms - User {}",
            ctx.statusCode(),
            ctx.method(),
            ctx.url(),
            System.currentTimeMillis() - started,
            userId
        )
    }

}