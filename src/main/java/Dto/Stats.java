package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"adsCount","followersCount","followingCount"})
public class Stats {
    @JsonProperty("adsCount")
    private int adsCount =0;

    @JsonProperty("followersCount")
    private int followersCount =0;

    @JsonProperty("followingCount")
    private int followingCount =0;


}
