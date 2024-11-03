package org.sdi.usermanager.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorDTO {

    private String status;
    private String message;
}
