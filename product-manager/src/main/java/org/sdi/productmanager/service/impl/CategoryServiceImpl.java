package org.sdi.productmanager.service.impl;

import lombok.RequiredArgsConstructor;
import org.sdi.productmanager.controller.CreateCategoryRequest;
import org.sdi.productmanager.dto.CategoryMapper;
import org.sdi.productmanager.dto.PaginatedResponse;
import org.sdi.productmanager.entity.Category;
import org.sdi.productmanager.exception.CategoryAlreadyExists;
import org.sdi.productmanager.exception.NotFoundException;
import org.sdi.productmanager.repository.CategoryRepository;
import org.sdi.productmanager.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public Category createCategory(CreateCategoryRequest request) {
        categoryRepository.findByName(request.getName()).ifPresent(category -> {
            throw new CategoryAlreadyExists(String.format("Category with name %s already exists", request.getName()));
        });

        Category category = categoryMapper.toEntity(request);
        return categoryRepository.save(category);
    }

    @Override
    public PaginatedResponse<Category> getCategories(Pageable pageable) {
        Page<Category> pageableCategories = categoryRepository.findAll(pageable);
        List<Category> content = pageableCategories.getContent();

        return PaginatedResponse.<Category>builder()
                .content(content)
                .page(pageableCategories.getNumber())
                .size(pageableCategories.getSize())
                .totalElements(pageableCategories.getTotalElements())
                .totalPages(pageableCategories.getTotalPages())
                .build();
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.findById(id).ifPresentOrElse(
                categoryRepository::delete,
                () -> {
                    throw new NotFoundException(String.format("Category with ID %d not found", id));
                }
        );
    }
}
