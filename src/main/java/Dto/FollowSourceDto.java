package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "fromDefaultArea", "fromFollowedArea","fromFollowedUser","fromBusinessUser"})
public class FollowSourceDto {

    @JsonProperty("fromDefaultArea")
    public boolean fromDefaultArea;

    @JsonProperty("fromFollowedArea")
    public boolean fromFollowedArea;

    @JsonProperty("fromFollowedUser")
    public boolean fromFollowedUser;

    @JsonProperty("fromBusinessUser")
    public boolean fromBusinessUser;

}
