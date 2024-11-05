package org.sdi.productmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotBlank(message="Name cannot be blank")
    private String name;
}
