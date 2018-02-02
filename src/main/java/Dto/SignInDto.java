package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"email", "password", "accessCode"})
public class SignInDto {
    @JsonProperty("email")
    public String email;
    @JsonProperty("password")
    public String password;
    /*@JsonProperty("accessCode")
    public String accessCode = null;*/


}
