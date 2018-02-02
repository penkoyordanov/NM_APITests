package Requests;

import Dto.UserDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "businessProfile","user","requestGuid","verificationCode" })
public class RegisterRequest {

    @JsonProperty("user")
    public UserDto user = new UserDto();

    @JsonProperty("verificationCode")
    public String verificationCode;

    @JsonProperty("requestGuid")
    public String requestGuid;

    @JsonProperty("businessProfile")
    public String businessProfile=null;

}
