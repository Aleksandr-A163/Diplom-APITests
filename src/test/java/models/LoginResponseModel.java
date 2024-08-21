package models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponseModel {

    @JsonProperty("userID")
    private String userId;

    @JsonProperty("expires")
    private String expires;

    @JsonProperty("token")
    private String token;
}