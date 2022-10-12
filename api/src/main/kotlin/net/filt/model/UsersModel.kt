package net.filt.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

object UsersModel : UUIDTable(name = "user") {

    val username = varchar("username", 50).uniqueIndex()
    val password = varchar("password", 64)

}

class UserModel(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<UserModel>(UsersModel)

    var username by UsersModel.username
    var password by UsersModel.password

}