package org.sdi.usermanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
