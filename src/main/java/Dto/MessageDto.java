package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "id", "sender","conversationId","text","dateCreated","isSeen","files"})
public class MessageDto {
    @JsonProperty("id")
    public int id;

    @JsonProperty("sender")
    public BaseUserDto sender;

    @JsonProperty("conversationId")
    public int conversationId;

    @JsonProperty("text")
    public String text;

    @JsonProperty("dateCreated")
    public String dateCreated;

    @JsonProperty("isSeen")
    public boolean isSeen;

    @JsonProperty("files")
    public AttachedFileDto[] files;



}
