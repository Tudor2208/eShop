package org.sdi.productmanager.service;

import org.sdi.productmanager.dto.CreateCategoryRequest;
import org.sdi.productmanager.dto.PaginatedResponse;
import org.sdi.productmanager.entity.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public interface CategoryService {

    Category createCategory(CreateCategoryRequest request);
    PaginatedResponse<Category> getCategories(Pageable pageable);
    Category getCategory(Long id);
    void deleteCategory(Long id);
}
