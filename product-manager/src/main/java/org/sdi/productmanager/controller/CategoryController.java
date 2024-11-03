package org.sdi.productmanager.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sdi.productmanager.dto.PaginatedResponse;
import org.sdi.productmanager.entity.Category;
import org.sdi.productmanager.service.CategoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static org.sdi.productmanager.Constants.CATEGORIES;
import static org.sdi.productmanager.Constants.V1;
import static org.sdi.productmanager.Constants.SLASH;

@RestController
@RequestMapping(V1 + CATEGORIES)
@RequiredArgsConstructor
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<Category> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        Category category = categoryService.createCategory(request);
        return ResponseEntity.created(URI.create(V1 + CATEGORIES + SLASH + category.getId())).body(category);
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<Category>> getCategories(@RequestParam(value="page", defaultValue="0") int page,
                                                                     @RequestParam(value="size", defaultValue="10") int size,
                                                                     @RequestParam(value="dir", defaultValue="asc") String dir) {
        Sort.Direction direction = dir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, "name"));
        return ResponseEntity.ok(categoryService.getCategories(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable(value="id") Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
