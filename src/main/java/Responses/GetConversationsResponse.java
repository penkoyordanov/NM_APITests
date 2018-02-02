package Responses;

import Dto.ConversationDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "items", "totalCount"})
public class GetConversationsResponse {
    @JsonProperty("items")
    public ConversationDto[] items;

    @JsonProperty("totalCount")
    public int totalCount;
}
