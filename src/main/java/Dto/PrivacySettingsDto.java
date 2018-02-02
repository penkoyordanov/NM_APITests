package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"showPhone", "showEmail","showAddress"})
public class PrivacySettingsDto {
    @JsonProperty("showPhone")
    public boolean showPhone;

    @JsonProperty("showEmail")
    public boolean showEmail;

    @JsonProperty("showAddress")
    public boolean showAddress;

    public PrivacySettingsDto(boolean showPhone, boolean showEmail, boolean showAddress) {
        this.showPhone = showPhone;
        this.showEmail = showEmail;
        this.showAddress = showAddress;
    }

}
