package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "id", "description","order","startAt","endAt","mute","originalUrl","originalName","name","url","type" })
public class AdMultimediaDto {
    @JsonProperty("id")
    public int id;

    @JsonProperty("description")
    public String description;

    @JsonProperty("order")
    public int order=0;

    @JsonProperty("startAt")
    public int startAt=0;

    @JsonProperty("endAt")
    public int endAt=0;

    @JsonProperty("mute")
    public boolean mute=false;

    @JsonProperty("originalUrl")
    public String originalUrl;

    @JsonProperty("originalName")
    public String originalName;

    @JsonProperty("name")
    public String name;

    @JsonProperty("url")
    public String url;

    @JsonProperty("type")
    public int type=0;

    public int getStartAt() {
        return startAt;
    }

    public int getEndAt() {
        return endAt;
    }

    public boolean isMute() {
        return mute;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public int getType() {
        return type;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }
}
