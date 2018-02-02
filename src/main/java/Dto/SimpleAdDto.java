package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "publishdate", "description","location","isSold","isSaved","isInactive","media","publisherName","ownerid","sharedcount",
        "savedcount","commentscount","publisherImage","followSource","locationLatitude","locationLongitude"})
public class SimpleAdDto extends BaseAdDto{
    @JsonProperty("publishdate")
    private String publishdate;

    @JsonProperty("description")
    private String description;

    @JsonProperty("location")
    private String location;

    @JsonProperty("isSold")
    boolean isSold = false;

    @JsonProperty("isSaved")
    boolean isSaved = false;

    @JsonProperty("isInactive")
    boolean isInactive = false;

    @JsonProperty("media")
    private AdMultimediaDto media=new AdMultimediaDto();

    @JsonProperty("publisherName")
    public String publisherName;

    @JsonProperty("ownerid")
    private String ownerid;

    @JsonProperty("sharedcount")
    public int sharedcount;

    @JsonProperty("savedcount")
    public int savedcount;

    @JsonProperty("commentscount")
    public int commentscount;

    @JsonProperty("publisherImage")
    public String publisherImage;

    @JsonProperty("followSource")
    public FollowSourceDto followSource=new FollowSourceDto();

    @JsonProperty("locationLatitude")
    public double locationLatitude;

    @JsonProperty("locationLongitude")
    public double locationLongitude;

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }
}
