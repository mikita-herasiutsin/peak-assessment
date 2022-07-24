package com.mherasiutsin.restaurants.api.repository;

import java.util.Collection;

public record FilterSpec(
        int page,
        int size,
        String query,
        Collection<String> grade
) {
}
