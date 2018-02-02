package Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"usernames"})
public class ShareUserParameters extends ShareParameters{
    @JsonProperty("usernames")
    private List<String> usernames=new ArrayList<>();

    public void setUsernames(String username) {
        usernames.add(username);
    }

    public void setMessage(String message) {
        super.message = message;
    }

}
