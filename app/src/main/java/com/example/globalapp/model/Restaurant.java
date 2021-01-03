package com.example.globalapp.model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Restaurant implements Serializable {

    private String name;
    private String phone;
    private String website;
    private String address;
    private String menu;
    private double lat;
    private double lon;
    private String category;

}
