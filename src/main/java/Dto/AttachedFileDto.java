package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;


@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "name", "url","type","size","thumbnail","originalName" })
public class AttachedFileDto {
    @JsonProperty("name")
    public String name;

    @JsonProperty("url")
    public String url;

    @JsonProperty("type")
    public int type=1;

    @JsonProperty("size")
    public int size=1;

    @JsonProperty("thumbnail")
    public String thumbnail=null;

    @JsonProperty("originalName")
    public String originalName;

    public AttachedFileDto(Map<String, String> files){
        this.name=files.get("name");
        this.url=files.get("url");
        this.originalName=files.get("originalName");
    }

    public AttachedFileDto(){

    }
}
