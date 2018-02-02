package Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "emailAddresses"})
public class ShareEmailParameters extends ShareParameters{

    @JsonProperty("emailAddresses")
    private List<String> emailAddresses=new ArrayList<>();

    public void setEmailAddress(String email) {
        emailAddresses.add(email);
    }

    public void setMessage(String message) {
        super.message = message;
    }

}
