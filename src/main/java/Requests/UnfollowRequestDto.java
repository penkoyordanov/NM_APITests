package Requests;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "id","type"})
public class UnfollowRequestDto {

    @JsonProperty("id")
    public int id;

    @JsonProperty("type")
    public int type;

}
