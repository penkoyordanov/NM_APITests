package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"id", "type","dateCreated","viewDate","readDate","params","topicText","image","advertisementId","userId","username","actioned"})
public class NotificationDto {
    @JsonProperty("id")
    public int id;

    @JsonProperty("type")
    public int type;

    @JsonProperty("dateCreated")
    public String dateCreated;

    @JsonProperty("viewDate")
    public String viewDate;

    @JsonProperty("readDate")
    public String readDate;

    @JsonProperty("params")
    public Params params=new Params();

    @JsonProperty("topicText")
    public String topicText;

    @JsonProperty("image")
    public String image;

    @JsonProperty("advertisementId")
    public int advertisementId;

    @JsonProperty("userId")
    public int userId;

    @JsonProperty("username")
    public String username;

    @JsonProperty("actioned")
    public boolean actioned;
}
