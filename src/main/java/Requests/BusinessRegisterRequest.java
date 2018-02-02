package Requests;

import Dto.BusinessProfileDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"profile","followData"})
public class BusinessRegisterRequest {

    @JsonProperty("profile")
    private BusinessProfileDto profile;

    @JsonProperty("followData")
    private FollowRequestDto[] followData;


    public BusinessProfileDto getProfile() {
        return profile;
    }

    public void setProfile(BusinessProfileDto profile) {
        this.profile = profile;
    }

    public FollowRequestDto[] getFollowData() {
        return followData;
    }

    public void setFollowData(FollowRequestDto[] followData) {
        this.followData = followData;
    }

    public BusinessRegisterRequest() {
        this.profile = new BusinessProfileDto();
    }
}
