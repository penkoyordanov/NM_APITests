package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"title","description","category","rentCategory","condition","price","minPrice",
        "keywords","savedate","publishdate","allowComments","address","contact","isSold","isInactive",
        "publisher","media","ownerId","isSaved","publicUrl","bidEndDate","bidEndTime","beforePrice",
        "discount","showSubmitForm","answersPublic","prizeText","businessProfileId"})
public class AdDto {
    @JsonProperty("category")
    private Integer category = null;

    @JsonProperty("rentCategory")
    private Integer rentCategory = null;

    @JsonProperty("condition")
    private Integer condition = null;

    @JsonProperty("title")
    private String title = "";

    @JsonProperty("price")
    private Integer price = null;

    @JsonProperty("minPrice")
    private Integer minPrice = null;

    @JsonProperty("keywords")
    public String[] keywords;

    @JsonProperty("allowComments")
    private boolean allowComments=true;

    @JsonProperty("address")
    private LocationDto address;

    @JsonProperty("contact")
    private Contact contact;

    @JsonProperty("isSold")
    boolean isSold = false;

    @JsonProperty("isInactive")
    boolean isInactive = false;

    @JsonProperty("publisher")
    private Publisher publisher;

    @JsonProperty("media")
    private UploadedAdMultimediaDto[] media;

    @JsonProperty("isSaved")
    boolean isSaved = false;

    @JsonProperty("description")
    private String description;

    @JsonProperty("savedate")
    private String savedate;

    @JsonProperty("publishDate")
    private String publishDate;

    @JsonProperty("ownerId")
    private Integer ownerId;

    @JsonProperty("publicUrl")
    private String publicUrl;

    @JsonProperty("bidEndDate")
    private String bidEndDate;

    @JsonProperty("bidEndTime")
    private String bidEndTime;

    @JsonProperty("beforePrice")
    private Integer beforePrice;

    @JsonProperty("discount")
    private Integer discount;

    @JsonProperty("showSubmitForm")
    private boolean showSubmitForm;

    @JsonProperty("answersPublic")
    private boolean answersPublic;

    @JsonProperty("prizeText")
    private String prizeText;

    @JsonProperty("businessProfileId")
    private Integer businessProfileId;



    public AdDto() {
        this.publisher = new Publisher();
        this.allowComments = true;
        this.keywords = new String[]{};
        this.media = new UploadedAdMultimediaDto[]{};
    }

    public Integer getCategory() {
        return category;
    }

    public Integer getCondition() {
        return condition;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setCondition(int condition) {
        if (this.category != 4 && this.category != 5) {
            this.condition = condition;
        }

    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(int price) {
        if (price != 0) {
            this.price = price;
        }

    }

    public void setMinPrice(int minPrice) {
        if (minPrice != 0) {
            this.minPrice = minPrice;
        }

    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
    }

    public boolean isAllowComments() {
        return allowComments;
    }

    public void setAllowComments(boolean allowComments) {
        this.allowComments = allowComments;
    }

    public void setLocationDto(LocationDto address) {
        this.address = address;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMedia(AdMultimediaDto adMultimediaDto) {
        this.media = new UploadedAdMultimediaDto[]{new UploadedAdMultimediaDto(adMultimediaDto)};
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public void setBidEndDate(String bidEndDate) {
        this.bidEndDate = bidEndDate;
    }

    public void setBidEndTime(String bidEndTime) {
        this.bidEndTime = bidEndTime;
    }

    public String getTitle() {
        return title;
    }

    public UploadedAdMultimediaDto[] getMedia() {
        return media;
    }

    public String getDescription() {
        return description;
    }

}
enum Category{

}
