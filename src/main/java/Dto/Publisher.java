package Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({"id", "contactId", "firstName", "lastName", "isOnline"})
class Publisher {

   @JsonProperty("id")
   private final int id=0;

   @JsonProperty("contactId")
    private final int contactId=0;

   @JsonProperty("firstName")
   private final String firstName="";

   @JsonProperty("lastName")
   private final String lastName="";

   @JsonProperty("isOnline")
   private final boolean isOnline=false;
}
