package com.ecommerce.project.service;

import com.ecommerce.project.com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;


@Override
public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
    Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
            ?Sort.by(sortBy).ascending()
            :Sort.by(sortBy).descending();
    Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
    Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

    List<Category> categories = categoryPage.getContent();

    if (categories.isEmpty()) {
        throw new APIException("No Categories created till now");
    }
    List<CategoryDTO> categoryDTOS = categories.stream().map(category -> {
        CategoryDTO dto = new CategoryDTO();
        dto.setCategoyId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        return dto;
    }).toList();

    // Set into response wrapper
    CategoryResponse response = new CategoryResponse();
    response.setContent(categoryDTOS);
    response.setPageNumber(categoryPage.getNumber());
    response.setPageSize(categoryPage.getSize());
    response.setTotalElements(categoryPage.getTotalElements());
    response.setTotalpages(categoryPage.getTotalPages());
    response.setLastPage(categoryPage.isLast());

    return response;
}

    @Override
    public CategoryDTO  createCategory(CategoryDTO categoryDTO) {
        Category savedCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if (savedCategory != null) {
            throw new APIException("Category with the name" + categoryDTO.getCategoryName() + " already exists");
        }
        Category category = new Category();
        category.setCategoryName(categoryDTO.getCategoryName());
        Category saved = categoryRepository.save(category);
        CategoryDTO responseDTO = new CategoryDTO();
        responseDTO.setCategoyId(category.getCategoryId());
        responseDTO.setCategoryName(category.getCategoryName());

        return responseDTO;
    }


    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        categoryRepository.delete(category);

        CategoryDTO dto = new CategoryDTO();
        dto.setCategoyId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName());
        return dto;
    }


    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        // 1. Find existing category
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));

        // 2. Check for duplicate name
        Category duplicate = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if (duplicate != null && !duplicate.getCategoryId().equals(categoryId)) {
            throw new APIException("Another category with the name '" + categoryDTO.getCategoryName() + "' already exists.");
        }

        // 3. Update entity fields
        existingCategory.setCategoryName(categoryDTO.getCategoryName());

        // 4. Save updated entity
        Category updated = categoryRepository.save(existingCategory);

        // 5. Map to DTO
        CategoryDTO responseDTO = new CategoryDTO();
        responseDTO.setCategoyId(updated.getCategoryId());
        responseDTO.setCategoryName(updated.getCategoryName());

        return responseDTO;
    }
}