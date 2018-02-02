package Requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "data","type"})
public class FollowRequestDto {
    @JsonProperty("data")
    public String data;

    @JsonProperty("type")
    public int type;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public FollowRequestDto(String data, int type) {
        this.data = data;
        this.type = type;
    }

    public FollowRequestDto() {
    }
}
