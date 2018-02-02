package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"id","category", "rentCategory", "title", "price", "image","publicUrl","countryCode","businessId","beforePrice","discount"})
public class BaseAdDto {
    @JsonProperty("id")
    public
    int id;

    @JsonProperty("category")
    private Integer category = null;

    @JsonProperty("condition")
    private Integer condition = null;

    @JsonProperty("title")
    private String title = "";

    @JsonProperty("price")
    private Long price = null;

    @JsonProperty("beforePrice")
    private Long beforePrice = null;

    @JsonProperty("discount")
    private Double discount = null;

    @JsonProperty("rentCategory")
    private Integer rentCategory = null;


    @JsonProperty("image")
    private String image;

    @JsonProperty("publicUrl")
    String publicUrl ;

    @JsonProperty("countryCode")
    public String countryCode;

    @JsonProperty("businessId")
    public double businessId;

    public Long getPrice() {
        return price;
    }

    public String getTitle() {
        return title;
    }

    public Integer getCategory() {
        return category;
    }

    public String getPublicUrl() {
        return publicUrl;
    }

    public int getId() {
        return id;
    }
}
