package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "id","username","email","userTypeId","contact","address","lang","image","otherAddresses","gender","password","facebookId","stats" })
public class UserDto {

    @JsonProperty("id")
    private int id = 0;

    @JsonProperty("username")
    public String username = "";

    @JsonProperty("email")
    public String email = null;

    @JsonProperty("userTypeId")
    public int userTypeId = 1;

    @JsonProperty("contact")
    public Contact contact=new Contact();
//    public Contact contact=new Contact("Hari","BAri34");

    @JsonProperty("address")
    public LocationDto address =new LocationDto();
//    public LocationDto locationDto=new LocationDto("Spjutgatan 9, 452 32 Strömstad, Sweden","ChIJ8RfoP4E-REYRsHu1fzuv_rI","se") ;

    @JsonProperty("lang")
    private String lang = "en";

    @JsonProperty("image")
    private MultimediaDto image;

    @JsonProperty("otherAddresses")
    private LocationDto[] otherAddresses=new LocationDto[]{new LocationDto(),new LocationDto()};

    @JsonProperty("gender")
    private int gender = 1;

    @JsonProperty("password")
    public String password = null;

    @JsonProperty("facebookId")
    private String facebookId = null;

    @JsonProperty("stats")
    private Stats stats = new Stats();

}
