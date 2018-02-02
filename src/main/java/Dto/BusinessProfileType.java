package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"id","type"})
public class BusinessProfileType {
    @JsonProperty("id")
    private int id=1;

    @JsonProperty("type")
    private String type="Default";

}
