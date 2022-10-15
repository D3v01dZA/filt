package net.filt.source

const val UNKNOWN = "UNKNOWN"

data class SearchResponse(val page: Int, val results: List<SearchMetadataResponse>)

data class SearchMetadataResponse(
    val mediaType: MediaType,
    val imagePath: String,
    val description: String,
    val title: String,
    val id: Int
)

data class MovieMetadataResponse(
    val imagePath: String,
    val description: String,
    val title: String,
    val id: Int
)

data class SeriesMetadataResponse(
    val imagePath: String,
    val description: String,
    val title: String,
    val id: Int,
    val seasons: List<SeriesSeasonMetadataResponse>
)

data class SeriesSeasonMetadataResponse(
    val id: Int,
    val number: Int,
    val name: String,
    val episodes: List<SeriesEpisodeMetadataResponse>
)

data class SeriesEpisodeMetadataResponse(
    val id: Int,
    val number: Int,
    val title: String,
    val description: String
)

enum class MediaType {
    SERIES,
    MOVIE;
}
