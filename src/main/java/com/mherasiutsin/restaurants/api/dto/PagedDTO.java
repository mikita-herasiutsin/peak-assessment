package com.mherasiutsin.restaurants.api.dto;

import java.util.List;

public record PagedDTO<T>(
        int page,
        int size,
        int totalPages,
        long totalElements,
        List<T> items
) {
}
