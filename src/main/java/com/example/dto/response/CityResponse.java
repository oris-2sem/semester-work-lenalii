package com.example.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CityResponse {

    private String source;
    private String result;
    private String postal_code;
    private String country;
    private String country_iso_code;
    private String federal_district;
    private String region_fias_id;
    private String region_kladr_id;
    private String region_iso_code;
    private String region_with_type;
    private String region_type;
    private String region_type_full;
    private String region;
    private String area_fias_id;
    private String area_kladr_id;
    private String area_with_type;
    private String area_type;
    private String area_type_full;
    private String area;
    private String city_fias_id;
    private String city_kladr_id;
    private String city_with_type;
    private String city_type;
    private String city_type_full;
    private String city;
    private String city_area;
    private String city_district_fias_id;
    private String city_district_kladr_id;
    private String city_district_with_type;
    private String city_district_type;
    private String city_district_type_full;
    private String city_district;
    private String settlement_fias_id;
    private String settlement_kladr_id;
    private String settlement_with_type;
    private String settlement_type;
    private String settlement_type_full;
    private String settlement;
    private String street_fias_id;
    private String street_kladr_id;
    private String street_with_type;
    private String street_type;
    private String street_type_full;
    private String street;
    private String stead_fias_id;
    private String stead_kladr_id;
    private String stead_cadnum;
    private String stead_type;
    private String stead_type_full;
    private String stead;
    private String house_fias_id;
    private String house_kladr_id;
    private String house_cadnum;
    private String house_type;
    private String house_type_full;
    private String house;
    private String block_type;
    private String block_type_full;
    private String block;
    private String entrance;
    private String floor;
    private String flat_fias_id;
    private String flat_cadnum;
    private String flat_type;
    private String flat_type_full;
    private String flat;
    private String flat_area;
    private String square_meter_price;
    private String flat_price;
    private String postal_box;
    private String fias_id;
    private String fias_code;
    private String fias_level;
    private String fias_actuality_state;
    private String kladr_id;
    private String capital_marker;
    private String okato;
    private String oktmo;
    private String tax_office;
    private String tax_office_legal;
    private String timezone;
    private String geo_lat;
    private String geo_lon;
    private String beltway_hit;
    private String beltway_distance;
    private String qc_geo;
    private String qc_complete;
    private String qc_house;
    private String qc;
    private String unparsed_parts;
    private String metr;
}
