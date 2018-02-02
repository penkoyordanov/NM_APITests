package Dto;

import io.restassured.response.Response;

import java.util.Map;

public class Contact{

    private int id;
    public String firstName;
    public String lastName;
    public String phone="";
    private String email="";

    public Contact(Response response){
        this.id=Integer.parseInt(response.getBody().jsonPath().getString("contactId"));
        this.firstName=response.getBody().jsonPath().getString("firstName");
        this.lastName=response.getBody().jsonPath().getString("lastName");
        this.email=response.getBody().jsonPath().getString("email");
        this.phone=response.getBody().jsonPath().getString("phone");
    }

    public Contact(Map<String,String> randomContact){
        this.id=0;
        this.firstName=randomContact.get("firstName");
        this.lastName=randomContact.get("lastName");
        this.email=randomContact.get("email");
        this.phone=randomContact.get("phone");
    }

    public Contact(String firstName, String lastName){
        this.id=0;
        this.firstName=firstName;
        this.lastName=lastName;
    }

    public Contact(){
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
