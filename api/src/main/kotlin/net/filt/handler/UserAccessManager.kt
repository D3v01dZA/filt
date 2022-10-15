package net.filt.handler

import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.security.AccessManager
import io.javalin.security.RouteRole
import net.filt.genericResponse
import net.filt.model.UserModel
import net.filt.model.UsersModel
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

class UserAccessManager : AccessManager {

    private val encoder = BCryptPasswordEncoder(12)

    fun register(ctx: Context) {
        transaction {
            val credentialsRequest = ctx.bodyAsClass(CredentialsRequest::class.java)
            val user: UserModel? = UserModel.find{ UsersModel.username eq credentialsRequest.username }.firstOrNull()
            if (user == null) {
                UserModel.new {
                    username = credentialsRequest.username
                    password = encoder.encode(credentialsRequest.password)
                }
                ctx.status(200).json(genericResponse("Success"))
            } else {
                ctx.status(400).json(genericResponse("Exists"))
            }
        }
    }

    fun login(ctx: Context) {
        transaction {
            val credentialsRequest = ctx.bodyAsClass(CredentialsRequest::class.java)
            val user: UserModel? = UserModel.find{ UsersModel.username eq credentialsRequest.username }.firstOrNull()
            if (user != null && encoder.matches(credentialsRequest.password, user.password)) {
                ctx.sessionAttribute("userId", user.id.value)
                ctx.status(200).json(genericResponse("Success"))
            } else {
                ctx.status(401).json(genericResponse("Unauthorized"))
            }
        }
    }

    fun logout(ctx: Context) {
        ctx.consumeSessionAttribute<UUID>("userId")
        ctx.status(200).json(genericResponse("Success"))
    }

    override fun manage(handler: Handler, ctx: Context, routeRoles: Set<RouteRole>) {
        val userId = ctx.sessionAttribute<UUID>("userId")
        if (routeRoles.contains(Role.ANYONE) || (routeRoles.contains(Role.AUTHENTICATED) && userId != null)) {
            handler.handle(ctx)
        } else {
            ctx.status(401).json(genericResponse("Unauthorized"))
        }
    }

}

data class CredentialsRequest(val username: String, val password: String)

enum class Role : RouteRole {
    ANYONE,
    AUTHENTICATED
}