package Responses;

import Dto.SimpleAdDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "items", "totalCount"})
public class UserAdsResponse {
    @JsonProperty("items")
    public SimpleAdDto[] items;

    @JsonProperty("totalCount")
    public int totalCount;
}
