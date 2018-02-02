package Dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "name", "url","type","id","description","order","startAt","endAt","mute","originalUrl","originalName" })
public class UploadedAdMultimediaDto {

    @JsonProperty("name")
    public String name;

    @JsonProperty("url")
    public String url;

    @JsonProperty("type")
    public int type=0;

    @JsonProperty("id")
    public int id=0;

    @JsonProperty("description")
    public String description=null;

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

    public UploadedAdMultimediaDto(AdMultimediaDto files){
        AdMultimediaDto adMultimediaDto=files;
        this.originalName=adMultimediaDto.getOriginalName();
        this.url=adMultimediaDto.getUrl();
        this.name=adMultimediaDto.getName();
        this.type=adMultimediaDto.getType();

    }

}
