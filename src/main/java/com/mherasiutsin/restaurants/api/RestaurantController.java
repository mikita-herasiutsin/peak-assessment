package com.mherasiutsin.restaurants.api;

import com.mherasiutsin.restaurants.api.dto.FilterDTO;
import com.mherasiutsin.restaurants.api.dto.PagedDTO;
import com.mherasiutsin.restaurants.api.model.Restaurant;
import com.mherasiutsin.restaurants.api.repository.FilterSpec;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService service;

    @ApiOperation("Returns restaurant filters")
    @GetMapping("/filters")
    public FilterDTO getFilters() {
        return service.findFilters();
    }

    @ApiOperation(
            value = "Returns pages restaurants",
            notes = """
                    It's possible to take grade and name(dba) values from /filters endpoint.
                    Page param starts from 1.
                    Q param searches for name(dba) OR cuisine(cuisine_description).
                    Grade searches by multiple values.
                    """
    )
    @GetMapping
    public PagedDTO<Restaurant> getPaged(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(name = "q", required = false) String query,
            @RequestParam(required = false) Collection<String> grade
    ) {
        Page<Restaurant> paged = service.find(new FilterSpec(page, size, query, grade));
        return new PagedDTO<>(
                paged.getPageable().getPageNumber(),
                paged.getPageable().getPageSize(),
                paged.getTotalPages(),
                paged.getTotalElements(),
                paged.getContent()
        );
    }

}
