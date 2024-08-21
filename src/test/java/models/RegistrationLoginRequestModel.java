package models;

import lombok.Data;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@Data
public class RegistrationLoginRequestModel {
    String userName, password;
}



