package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"userName","price","adcountrycode","ad","sender","message","userCount","commentsCount","keyword"})
public class Params {
    @JsonProperty("userName")
    public String userName;

    @JsonProperty("price")
    public Integer price;

    @JsonProperty("adcountrycode")
    public String adcountrycode;

    @JsonProperty("ad")
    public String ad;

    @JsonProperty("sender")
    public String sender;

    @JsonProperty("message")
    public String message;

    @JsonProperty("userCount")
    public String userCount;

    @JsonProperty("commentsCount")
    public int commentsCount;

    @JsonProperty("keyword")
    public String keyword;
}
