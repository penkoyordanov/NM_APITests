package Responses;

import Dto.NotificationDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "items", "totalCount"})
public class NotificationsGetResponse {

    @JsonProperty("items")
    public NotificationDto[] items;

    @JsonProperty("totalCount")
    public int totalCount;
}
