package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.restassured.response.Response;

import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "address","placeId","countryCode"})
public class LocationDto {
    @JsonProperty("address")
    public String address;

    @JsonProperty("placeId")
    public String placeId;

    @JsonProperty("countryCode")
    public String countryCode;

    public LocationDto(Response response) {
        this.address = response.getBody().jsonPath().getString("addresses[0].address").replace("\"","\'");
        this.placeId = response.getBody().jsonPath().getString( "addresses[0].placeId");
        this.countryCode = response.getBody().jsonPath().getString("addresses[0].countryCode");
    }

    public LocationDto(Map<String,String> randomAddress) {
        this.address = randomAddress.get("streetAddress");
        this.placeId = randomAddress.get("placeId");
        this.countryCode = randomAddress.get("countryCode");
    }

    public LocationDto() {
        this.address="";
        this.placeId="";
        this.countryCode="NO";

    }

    public LocationDto(String address, String placeId, String countryCode) {
        this.address = address;
        this.placeId = placeId;
        this.countryCode = countryCode;
    }

    public String getAddress() {
        return address;
    }


    public String getPlaceId() {
        return placeId;
    }


    public String getCountryCode() {
        return countryCode;
    }

}
