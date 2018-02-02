package Requests;

import Dto.BaseUserDto;
import Dto.Receivers;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"receivers", "reporteduserreason", "adid","reportedadreason"})
public class ShareRequest {

    @JsonProperty("receivers")
    private Receivers[] receivers;

    @JsonProperty("message")
    private String message;

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("adId")
    private int adId;

    public Receivers[] getReceivers() {
        return receivers;
    }

    public void setReceivers(Receivers[] receivers) {
        this.receivers = receivers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAdId() {
        return adId;
    }

    public void setAdId(int adId) {
        this.adId = adId;
    }
}
