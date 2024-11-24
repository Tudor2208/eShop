package org.sdi.usermanager.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PatchUserRequest {

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters long and contain uppercase, lowercase, a digit, and a special character")
    @Size(min = 1, message = "Password cannot be empty")
    private String password;
    @Size(min = 1, message = "First name cannot be empty")
    private String firstName;
    @Size(min = 1, message = "Last name cannot be empty")
    private String lastName;
    @Size(min = 1, message = "Phone number cannot be empty")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;
}
