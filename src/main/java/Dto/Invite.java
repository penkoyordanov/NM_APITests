package Dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.List;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"receivers", "message"})
public class Invite {

    //"receivers" : ["hari@abv.bg"],	"message" : "Come and join us for social buying and selling!"

    private List<String> receivers;

    public String message="Come and join us for social buying and selling!";

    public Invite(){
        this.receivers=new ArrayList<String>();
    }

    public void setReceivers(String receiver) {
        this.receivers.add(receiver);
    }

    public List<String> getReceivers() {
        return receivers;
    }
}
