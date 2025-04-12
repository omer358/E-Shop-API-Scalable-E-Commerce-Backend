package com.omo.shop.category.service;

import com.omo.shop.category.dto.CategoryDto;
import com.omo.shop.category.mapper.CategoryMapper;
import com.omo.shop.category.model.Category;
import com.omo.shop.category.repository.CategoryRepository;
import com.omo.shop.common.exceptions.AlreadyExistsException;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static com.omo.shop.common.constants.ExceptionMessages.CATEGORY_EXISTED;
import static com.omo.shop.common.constants.ExceptionMessages.CATEGORY_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    private static final Long CATEGORY_ID = 1L;
    private static final String CATEGORY_NAME = "Electronics";
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryService categoryService;
    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        category = Category.builder()
                .id(CATEGORY_ID)
                .name(CATEGORY_NAME)
                .build();

        categoryDto = CategoryDto.builder()
                .id(CATEGORY_ID)
                .name(CATEGORY_NAME)
                .build();
    }

    @Test
    @DisplayName("should return categoryDto when found by ID")
    void getCategoryById_shouldReturnCategoryDto_whenIdExists() {
        //Arrange
        when(categoryRepository.findById(CATEGORY_ID))
                .thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        //act
        CategoryDto result = categoryService.getCategoryById(CATEGORY_ID);

        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());

        verify(categoryRepository).findById(CATEGORY_ID);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("should throw exception when category not found by name")
    void getCategoryById_shouldThrowException_whenNotFound() {

        when(categoryRepository.findById(CATEGORY_ID))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getCategoryById(CATEGORY_ID));

        assertEquals(CATEGORY_NOT_FOUND, exception.getMessage());
        verify(categoryMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("should return categoryDto when found by name")
    void getCategoryByName_shouldReturnCategoryDto_whenNameExists() {
        //Arrange
        when(categoryRepository.findByName(CATEGORY_NAME))
                .thenReturn(Optional.of(category));
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        //act
        CategoryDto result = categoryService.getCategoryByName(CATEGORY_NAME);

        assertEquals(categoryDto.getId(), result.getId());
        assertEquals(categoryDto.getName(), result.getName());

        verify(categoryRepository).findByName(CATEGORY_NAME);
        verify(categoryMapper).toDto(category);
    }

    @Test
    @DisplayName("should throw exception when category not found by name")
    void getCategoryById_shouldThrowException_whenNotFoundByName() {

        when(categoryRepository.findByName(CATEGORY_NAME))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> categoryService.getCategoryByName(CATEGORY_NAME));

        assertEquals(CATEGORY_NOT_FOUND, exception.getMessage());
        verify(categoryMapper, never()).toDto(any());
    }

    @Test
    @DisplayName("should return a list of all categories")
    void getAllCategories() {
        // Given
        List<Category> categoryList = List.of(category);
        List<CategoryDto> categoryDtoList = List.of(categoryDto);

        //Arrange
        when(categoryRepository.findAll()).thenReturn(categoryList);
        when(categoryMapper.toDtoList(categoryList)).thenReturn(categoryDtoList);

        //Act
        List<CategoryDto> result = categoryService.getAllCategories();

        assertEquals(1, result.size());
        verify(categoryRepository).findAll();
        verify(categoryMapper).toDtoList(categoryList);
    }

    @Test
    @DisplayName("should add a new category and return a categoryDto when category does not exists")
    void addCategory_shouldSaveReturnCategoryDto_whenCategoryNotExist() {
        //Arrange
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(categoryRepository.existsByName(CATEGORY_NAME)).thenReturn(false);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        //Act
        CategoryDto result = categoryService.addCategory(CATEGORY_NAME);

        //assert
        assertEquals(CATEGORY_NAME, result.getName());
        verify(categoryRepository).save(any(Category.class));
        verify(categoryRepository).existsByName(CATEGORY_NAME);
        verify(categoryMapper).toDto(category);

    }

    @Test
    @DisplayName("should throw exception when category already exists")
    void addCategory_shouldThroughException_whenCategoryExist() {
        //Arrange
        when(categoryRepository.existsByName(CATEGORY_NAME)).thenReturn(true);

        //Act
        Exception result = assertThrows(AlreadyExistsException.class,
                () -> categoryService.addCategory(CATEGORY_NAME));

        //assert
        assert (result.getMessage().contains(CATEGORY_EXISTED));
        verify(categoryRepository, never()).save(any());
        verify(categoryRepository).existsByName(CATEGORY_NAME);
        verify(categoryMapper, never()).toDto(category);

    }

    @Test
    @DisplayName("Should delete category when found")
    void deleteCategoryById_shouldDeleteCategory_whenFound() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));

        categoryService.deleteCategory(CATEGORY_ID);

        verify(categoryRepository).delete(category);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent category")
    void deleteCategoryById_shouldThrowException_whenNotFound() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> categoryService.getCategoryById(CATEGORY_ID));

        assertEquals(CATEGORY_NOT_FOUND, exception.getMessage());
        verify(categoryRepository).findById(CATEGORY_ID);
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should update category when category exist")
    void updateCategory_shouldReturnUpdatedCategory_whenCategoryExist() {
        final String updatedName = "Men";
        final Category updatedCategory = Category.builder().id(1L).name("Men").build();
        final CategoryDto updatedCategoryDto = CategoryDto.builder().id(1L).name("Men").build();

        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName(any(String.class))).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);
        when(categoryMapper.toDto(any(Category.class))).thenReturn(updatedCategoryDto);

        CategoryDto result = categoryService
                .updateCategory(updatedName, CATEGORY_ID);

        assertEquals(updatedName, result.getName());
        verify(categoryRepository).save(any(Category.class));
        verify(categoryRepository).existsByName(updatedName);
        verify(categoryMapper).toDto(any(Category.class));
    }

    @Test
    @DisplayName("Should throw exception when category does not exist for update")
    void updateCategory_shouldThrowResourceNotFoundException_whenCategoryNotFound() {
        when(categoryRepository.existsByName(CATEGORY_NAME)).thenReturn(true);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> categoryService.updateCategory("Men", CATEGORY_ID));

        assertEquals(CATEGORY_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Should throw exception when updating category with a name that already exists")
    void updateCategory_shouldThrowAlreadyExistsException_whenNameAlreadyExists() {
        when(categoryRepository.findById(CATEGORY_ID)).thenReturn(Optional.of(category));
        when(categoryRepository.existsByName("Duplicate")).thenReturn(true);

        AlreadyExistsException exception = assertThrows(AlreadyExistsException.class,
                () -> categoryService.updateCategory("Duplicate", CATEGORY_ID));

        assertEquals(CATEGORY_EXISTED, exception.getMessage());
    }
}