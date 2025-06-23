package com.ecommerce.api.EcommerceAPI.product;

import com.ecommerce.api.EcommerceAPI.helpers.ApiResponse;
import com.ecommerce.api.EcommerceAPI.helpers.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        Category createdCategory = categoryService.createCategory(category);
        CategoryResponse categoryResponse = convertToCategoryResponse(createdCategory);
        return ResponseHelper.created(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<CategoryResponse> categoryResponses = categories.stream()
                .map(this::convertToCategoryResponse)
                .collect(Collectors.toList());
        return ResponseHelper.ok(categoryResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable UUID id) {
        return categoryService.getCategoryById(id)
                .map(category -> ResponseHelper.ok(convertToCategoryResponse(category)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body((ApiResponse<CategoryResponse>) ApiResponse.error("Category not found with id: " + id, null, null)));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable UUID id, @Valid @RequestBody CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());

        Category updatedCategory = categoryService.updateCategory(id, category);
        CategoryResponse categoryResponse = convertToCategoryResponse(updatedCategory);
        return ResponseHelper.ok(categoryResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseHelper.ok("Category deleted successfully");
    }

    // Helper method to convert Category entity to CategoryResponse DTO
    private CategoryResponse convertToCategoryResponse(Category category) {
        CategoryResponse response = new CategoryResponse();
        response.setId(category.getId());
        response.setName(category.getName());
        response.setDescription(category.getDescription());
        return response;
    }
}