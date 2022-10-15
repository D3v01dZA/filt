package net.filt.handler

data class SearchRequest(val query: String, val page: Int = 1)