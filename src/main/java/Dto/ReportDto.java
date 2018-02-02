package Dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"userid", "reporteduserreason", "adid","reportedadreason"})
public class ReportDto {
    @JsonProperty("userid")
    public String userid;

    @JsonProperty("reporteduserreason")
    public Integer reporteduserreason;

    @JsonProperty("adid")
    public Integer adid;

    @JsonProperty("reportedadreason")
    public Integer reportedadreason;
}
