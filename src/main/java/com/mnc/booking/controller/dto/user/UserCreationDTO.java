package com.mnc.booking.controller.dto.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserCreationDTO {

  @NotBlank(message = "email address cannot be null neither empty.")
  @Email(message = "Invalid email address.")
  private String email;
  @NotBlank(message = "username cannot be null neither empty.")
  @Pattern(regexp = "[a-zA-Z0-9]{5,}", message = "username should consist of letters and numbers and have minimum 5 characters.")
  private String username;
  @NotBlank(message = "password cannot be null neither empty.")
  private String password;
  @NotBlank(message = "name cannot be null neither empty.")
  private String name;
  @NotBlank(message = "surname cannot be null neither empty.")
  private String surname;
  @NotBlank(message = "date of birth must be specified.")
  @Pattern(regexp = "^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", message = "dateOfBirth must be correct and in format of YYYY-MM-DD")
  private String dateOfBirth;
  @NotBlank(message = "phone number cannot be null neither empty.")
  // https://stackoverflow.com/questions/8634139/phone-validation-regex
  @Pattern(regexp = "^([+]?[\\s0-9]+)?(\\d{3}|[(]?[0-9]+[)])?([-]?[\\s]?[0-9])+$")
  private String phoneNumber;
  private String photoURI;

}
