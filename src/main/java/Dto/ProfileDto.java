package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"lang","image","city","cover","description","stats","isOwn","isFollowed","isBlocked","publicUrl"})
public class ProfileDto extends UserDto{
    @JsonProperty("lang")
    public String lang;

    @JsonProperty("image")
    public MultimediaDto image;

    @JsonProperty("cover")
    public MultimediaDto cover;

    @JsonProperty("description")
    public String description;

    @JsonProperty("stats")
    public String stats = null;

    @JsonProperty("city")
    public String city;

    @JsonProperty("isOwn")
    public boolean isOwn = false;

    @JsonProperty("isFollowed")
    public boolean isFollowed = false;

    @JsonProperty("isBlocked")
    public boolean isBlocked = false;

    @JsonProperty("publicUrl")
    public String publicUrl;


}
