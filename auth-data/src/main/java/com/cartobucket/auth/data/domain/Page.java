
package com.cartobucket.auth.data.domain;

public record Page(Integer limit, Integer offset) {
    public Page {
        if (limit <= 0)
            throw new IllegalArgumentException("Page limit must be > 0 : " + limit);
        if (limit > 100)
            throw new IllegalArgumentException("Page limit must be <= 100 : " + limit);
        if (offset < 0)
            throw new IllegalArgumentException("Page offset must be >= 0 : " + offset);
    }

    public int getNextRowsCount() {
        return (offset + limit) - 1;
    }
}