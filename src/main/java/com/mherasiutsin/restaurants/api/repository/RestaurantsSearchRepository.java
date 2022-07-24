package com.mherasiutsin.restaurants.api.repository;

import com.mherasiutsin.restaurants.api.model.Restaurant;
import com.mherasiutsin.restaurants.api.model.RestaurantFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RestaurantsSearchRepository {

    private static final String CUISINE = "cuisineDescription";
    private static final String GRADE = "grade";
    private static final String NAME = "dba";
    private final RestaurantRepository repository;
    private final EntityManager entityManager;

    public Page<Restaurant> find(FilterSpec filter) {

        return repository.findAll(
                (r, query, cb) -> {
                    List<Predicate> conditions = new ArrayList<>();
                    Optional.ofNullable(filter.query())
                            .ifPresent(q -> conditions.add(cb.or(
                                    cb.like(cb.lower(r.get(CUISINE)), "%" + q.toLowerCase() + "%"),
                                    cb.like(cb.lower(r.get(NAME)), "%" + q.toLowerCase() + "%")
                            )));
                    Optional.ofNullable(filter.grade())
                            .ifPresent(grade -> conditions.add(cb.lower(r.get(GRADE)).in(filter.grade())));
                    return cb.and(conditions.toArray(new Predicate[0]));
                },
                PageRequest.of(
                        filter.page() - 1,
                        filter.size(),
                        Sort.unsorted()
                )
        );
    }

    public List<RestaurantFilter> findFilters() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<RestaurantFilter> query = cb.createQuery(RestaurantFilter.class);
        Root<RestaurantFilter> root = query.from(RestaurantFilter.class);
        return entityManager.createQuery(
                query
                    .distinct(true)
                    .where(cb.and(
                            cb.isNotNull(root.get(CUISINE)),
                            cb.isNotNull(root.get(GRADE)),
                            cb.notEqual(root.get(CUISINE), ""),
                            cb.notEqual(root.get(GRADE), "")
                    ))
                    .groupBy(root.get(CUISINE), root.get(GRADE))
        ).getResultList();
    }

}
