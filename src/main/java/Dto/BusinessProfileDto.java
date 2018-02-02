package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"id","vat","name","phone","email","isSubscriptionActive","businessProfileType","address","image","stripeToken","totalToPay","paymentError"})
public class BusinessProfileDto {
    @JsonProperty("id")
    private int id;

    @JsonProperty("vat")
    private int vat;

    @JsonProperty("name")
    public String name;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("email")
    private String email;

    @JsonProperty("stripeToken")
    private String stripeToken;

    @JsonProperty("totalToPay")
    private int totalToPay;

    @JsonProperty("paymentError")
    private String paymentError;

    @JsonProperty("isSubscriptionActive")
    private boolean isSubscriptionActive=false;

    @JsonProperty("businessProfileType")
    private BusinessProfileType businessProfileType;

    @JsonProperty("address")
    public LocationDto address=new LocationDto();

    @JsonProperty("image")
    public MultimediaDto image=new MultimediaDto();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSubscriptionActive() {
        return isSubscriptionActive;
    }

    public void setSubscriptionActive(boolean subscriptionActive) {
        isSubscriptionActive = subscriptionActive;
    }

    public BusinessProfileType getBusinessProfileType() {
        return businessProfileType;
    }

    public void setBusinessProfileType(BusinessProfileType businessProfileType) {
        this.businessProfileType = businessProfileType;
    }

    public LocationDto getAddress() {
        return address;
    }

    public void setAddress(LocationDto address) {
        this.address = address;
    }

    public MultimediaDto getImage() {
        return image;
    }

    public void setImage(MultimediaDto image) {
        this.image = image;
    }

    public String getStripeToken() {
        return stripeToken;
    }

    public void setStripeToken(String stripeToken) {
        this.stripeToken = stripeToken;
    }

    public int getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(int totalToPay) {
        this.totalToPay = totalToPay;
    }

    public String getPaymentError() {
        return paymentError;
    }

    public void setPaymentError(String paymentError) {
        this.paymentError = paymentError;
    }
}
