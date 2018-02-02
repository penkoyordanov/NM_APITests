package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"email", "language","name"})
public class RequestDto {
    @JsonProperty("email")
    public String email;

    @JsonProperty("language")
    private String language="en";

    @JsonProperty("name")
    public String name;

    public RequestDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
