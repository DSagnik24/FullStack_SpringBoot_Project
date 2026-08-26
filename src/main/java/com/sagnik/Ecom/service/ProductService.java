package com.sagnik.Ecom.service;

import com.sagnik.Ecom.payload.ProductDTO;
import com.sagnik.Ecom.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
    /** Adds a product to an existing category. */
    ProductDTO addProduct(Long categoryId, ProductDTO productDTO);

    /** Retrieves products for a category. */
    ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    /** Searches for products by name without case sensitivity. */
    ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    /** Updates an existing product and returns the saved DTO. */
    ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    ProductDTO deleteProduct(Long productId);

    ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;

    ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
