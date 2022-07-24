package com.mherasiutsin.restaurants.api.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "restaurants")
public class Restaurant {

    @Id
    private Integer camis;
    private String dba;
    private String boro;
    private String building;
    private String street;
    private String zipcode;
    private String phone;
    private String cuisineDescription;
    private String inspectionDate;
    private String action;
    private String violationCode;
    private String violationDescription;
    private String criticalFlag;
    private String score;
    private String grade;
    private String gradeDate;
    private String recordDate;
    private String inspection_type;
    private String latitude;
    private String longitude;
    private String communityBoard;
    private String councilDistrict;
    private String censusTract;
    private String bin;
    private String bbl;
    private String nta;

}
