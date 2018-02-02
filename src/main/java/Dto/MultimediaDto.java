package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "name","type","url","startAt","endAt","mute"})
public class MultimediaDto {
    @JsonProperty("name")
    public String name;

    @JsonProperty("type")
    public int type;

    @JsonProperty("url")
    public String url;

    @JsonProperty("startAt")
    public int startAt;

    @JsonProperty("endAt")
    public int endAt;

    @JsonProperty("mute")
    public boolean mute=false;
}
