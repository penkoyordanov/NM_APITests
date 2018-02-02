package Requests;

import Dto.AttachedFileDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "files","advertisementId", "commentText" })
public class CommentRequest {
    @JsonProperty("advertisementId")
    public int advertisementId;

    @JsonProperty("commentText")
    public String commentText;

    @JsonProperty("files")
    public AttachedFileDto[] files;

    public void setFiles(Map<String,String> response) {
        this.files=new AttachedFileDto[]{new AttachedFileDto(response)};


    }

}
