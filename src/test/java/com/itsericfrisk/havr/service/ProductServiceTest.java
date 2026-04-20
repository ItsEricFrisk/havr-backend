package com.itsericfrisk.havr.service;

import com.itsericfrisk.havr.dto.ProductRequest;
import com.itsericfrisk.havr.dto.ProductResponse;
import com.itsericfrisk.havr.model.Category;
import com.itsericfrisk.havr.model.Product;
import com.itsericfrisk.havr.model.ProductStatus;
import com.itsericfrisk.havr.model.User;
import com.itsericfrisk.havr.repository.CategoryRepository;
import com.itsericfrisk.havr.repository.ProductRepository;
import com.itsericfrisk.havr.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductService productService;

    private static final Long USER_ID = 2L;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(USER_ID);

        Category category = new Category();
        category.setName("Category");
        category.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setName("Product");
        product.setStatus(ProductStatus.HAS);
        product.setCategory(category);
        product.setUser(user);
    }

    @Test
    @DisplayName("findAll returns products for specific user")
    void findAll_returnsProducts() {
        Pageable pageable = PageRequest.of(0, 1);

        when(productRepository.findByUserId(USER_ID, pageable))
                .thenReturn(new PageImpl<>(List.of(product)));

        Page<ProductResponse> result = productService.findAll(USER_ID, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst().name()).isEqualTo("Product");
    }

    @Test
    @DisplayName("findById returns product if user owns it")
    void findById_returnsProduct_whenUserOwnsIt() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse result = productService.findById(1L, user.getId());

        assertThat(result.name()).isEqualTo("Product");
    }

    @Test
    @DisplayName("findById throws AccessDeniedException when user does not own the product")
    void findById_throwsAccessDenied_whenUserDoesNotOwnProduct() {
        user.setId(999L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.findById(1L, USER_ID))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("create saves and returns new product")
    void create_savesAndReturnsProduct() {
        ProductRequest request = new ProductRequest("product", null, "brand", "www.url.test", ProductStatus.HAS, 1L);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(product.getCategory()));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse result = productService.create(request, USER_ID);

        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("product");
    }

    @Test
    @DisplayName("update changes product when user owns it")
    void update_changeProduct_whenUserOwnsIt() {
        ProductRequest request = new ProductRequest("updated", null, "brand", "www.url.test", ProductStatus.HAS, 1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(product.getCategory()));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse result = productService.update(1L, request, USER_ID);

        assertThat(result.name()).isEqualTo("updated");
    }

    @Test
    @DisplayName("update throws AccessDeniedException when user does not own it")
    void update_throwsAccessDeniedException_whenUserDoesNotOwnIt() {
        ProductRequest request = new ProductRequest("updated", null, "brand", "www.url.test", ProductStatus.HAS, 1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.update(1L, request, 999L))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("delete removes product when user owns it")
    void delete_removesProduct_whenUserOwnsIt() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        productService.delete(1L, USER_ID);

        verify(productRepository).delete(product);
    }
}
