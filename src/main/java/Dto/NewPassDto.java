package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"uniqueId", "password"})
public class NewPassDto {
    @JsonProperty("uniqueId")
    public String uniqueId = null;

    @JsonProperty("password")
    public String password = null;
}
