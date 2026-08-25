package com.sagnik.Ecom.controller;

import com.sagnik.Ecom.model.Product;
import com.sagnik.Ecom.payload.ProductDTO;
import com.sagnik.Ecom.payload.ProductResponse;
import com.sagnik.Ecom.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/products")
	/** Adds a product to the specified category. */
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO productDTO
            , @PathVariable Long categoryId){
        ProductDTO  savedProductDTO = productService.addProduct(categoryId, productDTO);
        return new ResponseEntity<>(savedProductDTO, HttpStatus.CREATED);
    }
    @GetMapping("/public/products")
	/** Returns all products. */
    public ResponseEntity<ProductResponse> getAllProducts(){
        ProductResponse productResponse = productService.getAllProducts();
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }
    @GetMapping("/public/categories/{categoryId}/products")
	/** Returns products belonging to the specified category. */
    public ResponseEntity<ProductResponse> getProductsByCategory(@PathVariable Long categoryId){
        ProductResponse productResponse = productService.searchByCategory(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    @GetMapping("/public/products/keyword/{keyword}")
	/** Returns products whose names match the supplied keyword. */
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword){
        ProductResponse productResponse = productService.searchProductByKeyword(keyword);
        return new ResponseEntity<>(productResponse,HttpStatus.OK);
    }
    @PutMapping("/admin/products/{productId}")
	/** Updates the product identified by the path variable. */
    public ResponseEntity<ProductDTO> updateProduct(@RequestBody ProductDTO productDTO,
            @PathVariable Long productId){
        ProductDTO updatedProductDTO = productService.updateProduct(productId,productDTO);
        return new ResponseEntity<>(updatedProductDTO,HttpStatus.OK);
    }
    @DeleteMapping("/admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
        ProductDTO productDTO = productService.deleteProduct(productId);
        return  new ResponseEntity<>(productDTO, HttpStatus.OK);
    }
    @PutMapping("/products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image")MultipartFile image) throws IOException {
        ProductDTO updateProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updateProduct,HttpStatus.OK);
    }
}
