package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "id","username","firstName","lastName","image","email" })
public class BaseUserDto {
    @JsonProperty("id")
    public int id = 0;

    @JsonProperty("contactId")
    public int contactId ;

    @JsonProperty("phone")
    public String phone ;

    @JsonProperty("isOnline")
    public boolean isOnline ;

    @JsonProperty("username")
    public String username;

    @JsonProperty("firstName")
    public String firstName;

    @JsonProperty("lastName")
    public String lastName;

    @JsonProperty("email")
    public String email;

    @JsonProperty("image")
    public String image;

    @JsonProperty("countryCode")
    public String countryCode;

    @JsonProperty("locationLatitude")
    public double locationLatitude;

    @JsonProperty("locationLongitude")
    public double locationLongitude;

    @JsonProperty("placeId")
    public String placeId;

    @JsonProperty("suggestions")
    public UserSuggestionDto[] suggestions;

    public String getFirstName() {
        return firstName;
    }
}
