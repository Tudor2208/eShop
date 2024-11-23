package org.sdi.ordermanager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PatchProductRequest {

    private Integer stock;
}
