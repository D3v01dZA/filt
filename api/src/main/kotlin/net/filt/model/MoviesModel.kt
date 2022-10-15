package net.filt.model

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object MoviesModel : UUIDTable(name = "movies") {

    val tmdbId = varchar("tmdb_id", 50)

}

class MovieModel(id: EntityID<UUID>) : UUIDEntity(id) {

    companion object : UUIDEntityClass<MovieModel>(MoviesModel)

    var tmdbId by MoviesModel.tmdbId

}