package com.cartobucket.auth.data.domain

data class Page(val limit: Int, val offset: Int) {
    init {
        require(limit > 0) { "Page limit must be > 0 : $limit" }
        require(limit <= 100) { "Page limit must be <= 100 : $limit" }
        require(offset >= 0) { "Page offset must be >= 0 : $offset" }
    }

    fun getNextRowsCount(): Int = (offset + limit) - 1
    fun offset(): Int {
        return offset
    }
    fun limit(): Int {
        return limit
    }
}