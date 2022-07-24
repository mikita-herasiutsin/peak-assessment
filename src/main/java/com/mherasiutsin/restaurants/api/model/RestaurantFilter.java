package com.mherasiutsin.restaurants.api.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "restaurants")
public class RestaurantFilter {

    @Id
    private Integer camis;
    private String grade;
    private String cuisineDescription;

}
