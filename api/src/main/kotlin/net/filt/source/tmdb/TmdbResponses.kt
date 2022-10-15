package net.filt.source.tmdb

import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.annotation.JsonCreator
import io.javalin.http.InternalServerErrorResponse
import net.filt.source.*

data class TmdbSearchResponse(val page: Int, val results: List<TmdbSearchMetadataResponse>) {

    fun asDirectory(): SearchResponse {
        return SearchResponse(page, results.filter { it.mediaType != TmdbMediaType.OTHER }.map { it.asDirectory() })
    }

}

private const val IMAGE_URL = "https://image.tmdb.org/t/p/w500/"

data class TmdbSearchMetadataResponse(
    @JsonAlias("media_type") val mediaType: TmdbMediaType,
    @JsonAlias("poster_path", "backdrop_path") val imagePath: String?,
    @JsonAlias("overview") val overview: String,
    @JsonAlias("original_title", "name") val title: String,
    val id: Int
) {
    fun asDirectory(): SearchMetadataResponse {
        return SearchMetadataResponse(
            mediaType.asDirectory(),
            imagePath?.let { "$IMAGE_URL$it" } ?: UNKNOWN,
            overview,
            title,
            id
        )
    }
}

data class TmdbMovieMetadataResponse(
    @JsonAlias("poster_path") val imagePath: String?,
    @JsonAlias("overview") val overview: String,
    @JsonAlias("original_title") val title: String,
    val id: Int
) {
    fun asDirectory(): MovieMetadataResponse {
        return MovieMetadataResponse(
            imagePath?.let { "$IMAGE_URL$it" } ?: UNKNOWN,
            overview,
            title,
            id
        )
    }
}

data class TmdbSeriesMetadataResponse(
    @JsonAlias("backdrop_path") val imagePath: String?,
    @JsonAlias("overview") val overview: String,
    @JsonAlias("name") val title: String,
    val id: Int,
    val seasons: List<Season>
) {

    data class Season(@JsonAlias("season_number") val seasonNumber: Int)

    fun asDirectory(seasons: List<SeriesSeasonMetadataResponse>): SeriesMetadataResponse {
        return SeriesMetadataResponse(
            imagePath?.let { "$IMAGE_URL$it" } ?: UNKNOWN,
            overview,
            title,
            id,
            seasons.sortedBy { it.number }
        )
    }
}

data class TmdbSeriesSeasonMetadataResponse(
    val id: Int,
    @JsonAlias("season_number") val number: Int,
    val name: String,
    @JsonAlias("poster_path") val imagePath: String?,
    val episodes: List<Episode>
) {

    data class Episode(
        val id: Int,
        @JsonAlias("episode_number") val number: Int,
        val name: String,
        val overview: String
    ) {
        fun asDirectory(): SeriesEpisodeMetadataResponse {
            return SeriesEpisodeMetadataResponse(id, number, name, overview)
        }
    }

    fun asDirectory(): SeriesSeasonMetadataResponse {
        return SeriesSeasonMetadataResponse(
            id,
            number,
            name,
            episodes.map { it.asDirectory() }.sortedBy { it.number }
        )
    }

}

enum class TmdbMediaType {

    TV {
        override fun asDirectory(): MediaType {
            return MediaType.SERIES
        }
    },
    MOVIE {
        override fun asDirectory(): MediaType {
            return MediaType.MOVIE
        }
    },
    OTHER {
        override fun asDirectory(): MediaType {
            throw InternalServerErrorResponse("Error")
        }
    };

    companion object {
        @JsonCreator
        @JvmStatic
        @Suppress("unused")
        fun fromString(string: String): TmdbMediaType {
            return TmdbMediaType.values().firstOrNull() { it.name.equals(string, true) } ?: OTHER
        }
    }

    abstract fun asDirectory(): MediaType

}