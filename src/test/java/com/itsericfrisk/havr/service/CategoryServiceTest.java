package com.itsericfrisk.havr.service;

import com.itsericfrisk.havr.dto.CategoryRequest;
import com.itsericfrisk.havr.dto.CategoryResponse;
import com.itsericfrisk.havr.model.Category;
import com.itsericfrisk.havr.model.User;
import com.itsericfrisk.havr.repository.CategoryRepository;
import com.itsericfrisk.havr.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CategoryService categoryService;

    private static final Long USER_ID = 1L;
    private static final Long GLOBAL_CATEGORY_ID = 1L;
    private static final Long USER_CATEGORY_ID = 2L;

    private User user;
    private Category globalCategory;
    private Category userCategory;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);

        globalCategory = new Category();
        globalCategory.setId(GLOBAL_CATEGORY_ID);
        globalCategory.setName("Global category");
        globalCategory.setParent(null);
        globalCategory.setGlobal(true);

        userCategory = new Category();
        userCategory.setId(USER_CATEGORY_ID);
        userCategory.setName("User category");
        userCategory.setParent(globalCategory);
        userCategory.setGlobal(false);
        userCategory.setUser(user);

    }

    @Test
    @DisplayName("findAll returns global and own categories")
    void findAll_returnsGlobalAndOwnCategories() {
        when(categoryRepository.findByGlobalTrueOrUserId(USER_ID)).thenReturn(List.of(globalCategory, userCategory));

        List<CategoryResponse> result = categoryService.findAll(USER_ID);

        assertThat(result).hasSize(2);
        assertThat(result.getFirst().name()).isEqualTo("Global category");
        assertThat(result.getLast().name()).isEqualTo("User category");
    }

    @Test
    @DisplayName("create adds a new category to specific user")
    void create_addsNewCategoryToUser_returnSuccessful() {
        CategoryRequest request = new CategoryRequest("category", "icon", null);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResponse result = categoryService.create(request, USER_ID);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("category");
    }

    @Test
    @DisplayName("update category if user owns it")
    void update_userOwnsTheCategory_returnSuccessful() {
        CategoryRequest request = new CategoryRequest("updated category", "icon", null);

        when(categoryRepository.findById(USER_CATEGORY_ID)).thenReturn(Optional.of(userCategory));
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        CategoryResponse result = categoryService.update(USER_CATEGORY_ID, request, USER_ID);

        assertThat(result.name()).isEqualTo("updated category");
    }

    @Test
    @DisplayName("update throws AccessDeniedException when category is global")
    void update_throwsAccessDeniedException_whenCategoryIsGlobal() {
        CategoryRequest request = new CategoryRequest("updated category", "icon", null);

        when(categoryRepository.findById(GLOBAL_CATEGORY_ID)).thenReturn(Optional.of(globalCategory));

        assertThatThrownBy(() -> categoryService.update(GLOBAL_CATEGORY_ID, request, USER_ID))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("delete removes category when user owns it")
    void delete_removesCategory_whenUserOwnsIt() {
        when(categoryRepository.findById(USER_CATEGORY_ID)).thenReturn(Optional.of(userCategory));

        categoryService.delete(USER_CATEGORY_ID, USER_ID);

        verify(categoryRepository).delete(userCategory);
    }

    @Test
    @DisplayName("delete throws AccessDeniedException when category is global")
    void delete_throwsAccessDeniedException_whenCategoryIsGlobal() {
        when(categoryRepository.findById(GLOBAL_CATEGORY_ID)).thenReturn(Optional.of(globalCategory));

        assertThatThrownBy(() -> categoryService.delete(GLOBAL_CATEGORY_ID, USER_ID))
                .isInstanceOf(AccessDeniedException.class);
    }
}
