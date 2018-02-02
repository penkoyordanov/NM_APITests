package Responses;

import Dto.SimpleAdDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({ "count", "countnewtoday","ads"})
public class GetFeedAdsResponse {
    @JsonProperty("ads")
    public SimpleAdDto[] ads;

    @JsonProperty("countnewtoday")
    public int countnewtoday;

    @JsonProperty("count")
    public int count;
}
