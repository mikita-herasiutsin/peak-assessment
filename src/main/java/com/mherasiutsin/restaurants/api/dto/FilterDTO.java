package com.mherasiutsin.restaurants.api.dto;

import java.util.Set;

public record FilterDTO(
        Set<String> cuisines,
        Set<String> grades
) {
}
