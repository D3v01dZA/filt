package net.filt.source

interface DirectoryApi {

    fun search(term: String, page: Int): SearchResponse

    fun getMovie(id: Int): MovieMetadataResponse

    fun getSeries(id: Int): SeriesMetadataResponse

}