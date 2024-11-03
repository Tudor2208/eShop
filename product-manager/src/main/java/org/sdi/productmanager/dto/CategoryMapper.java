package org.sdi.productmanager.dto;

import org.sdi.productmanager.controller.CreateCategoryRequest;
import org.sdi.productmanager.entity.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toEntity(CreateCategoryRequest request) {
        return Category.builder()
                .name(request.getName())
                .build();
    }
}
