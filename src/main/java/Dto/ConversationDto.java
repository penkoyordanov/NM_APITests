package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "id", "title","ad","messageCount","message","isActive","participants"})
public class ConversationDto {
    @JsonProperty("id")
    public int id;

    @JsonProperty("title")
    public String title;

    @JsonProperty("ad")
    public BaseAdDto ad;

    @JsonProperty("messageCount")
    public int messageCount;

    @JsonProperty("message")
    public MessageDto message;

    @JsonProperty("isActive")
    public boolean isActive;

    @JsonProperty("participants")
    public BaseUserDto[] participants;

}
