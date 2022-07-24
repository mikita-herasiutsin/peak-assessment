package com.mherasiutsin.restaurants.api;

import com.mherasiutsin.restaurants.api.dto.FilterDTO;
import com.mherasiutsin.restaurants.api.model.Restaurant;
import com.mherasiutsin.restaurants.api.model.RestaurantFilter;
import com.mherasiutsin.restaurants.api.repository.FilterSpec;
import com.mherasiutsin.restaurants.api.repository.RestaurantsSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public record RestaurantService(RestaurantsSearchRepository repository) {

    public Page<Restaurant> find(FilterSpec filter) {
        return repository.find(filter);
    }

    public FilterDTO findFilters() {
        List<RestaurantFilter> filters = repository.findFilters();
        return new FilterDTO(
                filters.stream()
                        .map(RestaurantFilter::getCuisineDescription)
                        .collect(Collectors.toSet()),
                filters.stream()
                        .map(RestaurantFilter::getGrade)
                        .collect(Collectors.toSet())
        );
    }
}
