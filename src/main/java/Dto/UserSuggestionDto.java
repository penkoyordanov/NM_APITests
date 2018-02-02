package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "type","data","title","imgPath"})
public class UserSuggestionDto {
    @JsonProperty("type")
    public SuggestionType type;

    @JsonProperty("data")
    public Object data;

    @JsonProperty("title")
    public String title;

    @JsonProperty("imgPath")
    public String imgPath;
}

enum SuggestionType{
    Location,
    InviteFriend,
    CreateAd,
    AddProfileInfo,
    UploadProfilePic,
    UploadCoverImage

}
