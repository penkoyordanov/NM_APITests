package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"otherLocationDtos","gender","password","facebookId","fbGroups","birth","clientIp"})
public class EditProfileDto extends ProfileDto{
    @JsonProperty("otherLocationDtos")
    public LocationDto[] otherLocationDtos =new LocationDto[]{};

    @JsonProperty("gender")
    public int gender;

    @JsonProperty("password")
    public String password;

    @JsonProperty("facebookId")
    public String facebookId;

    @JsonProperty("fbGroups")
    public String fbGroups;

    @JsonProperty("birth")
    public String birth;

    @JsonProperty("clientIp")
    public String clientIp;

}
