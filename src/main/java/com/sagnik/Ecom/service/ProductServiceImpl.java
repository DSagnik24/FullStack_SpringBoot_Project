package com.sagnik.Ecom.service;

import com.sagnik.Ecom.Exceptions.ResourceNotFoundException;
import com.sagnik.Ecom.model.Category;
import com.sagnik.Ecom.model.Product;
import com.sagnik.Ecom.payload.ProductDTO;
import com.sagnik.Ecom.payload.ProductResponse;
import com.sagnik.Ecom.repository.CategoryRepository;
import com.sagnik.Ecom.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileServiceImpl fileService;

    @Value("${PROJECT.IMAGE}")
    private String path;


        @Override
        /** Associates a product with a category, calculates its special price, and saves it. */
        public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException(categoryId,"categoryId","Category"));

        Product product = modelMapper.map(productDTO,Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        double specialPrice = product.getPrice() -
                ((product.getDiscount() * 0.01)*product.getPrice());
        product.setSpecialPrice(specialPrice);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

        @Override
        /** Retrieves every product and converts the results to DTOs. */
        public ProductResponse getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

        @Override
        /** Retrieves and converts products belonging to the requested category. */
        public ProductResponse searchByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException(categoryId,"categoryId","Category"));

        List<Product> productsByCategory = productRepository.findByCategoryOrderByPriceAsc(category);
        List<ProductDTO> productDTOS = productsByCategory.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

        @Override
        /** Searches product names using a case-insensitive keyword match. */
        public ProductResponse searchProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductNameLikeIgnoreCase(keyword);
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        return productResponse;
    }

        @Override
        /** Updates editable product fields, recalculates the special price, and saves the product. */
        public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(()->
                new ResourceNotFoundException(productId,"productId","Product"));

        Product product = modelMapper.map(productDTO,Product.class);

        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setQuantity(product.getQuantity());
        double specialPrice = product.getPrice() -
                ((product.getDiscount() * 0.01)*product.getPrice());
        product.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(productFromDb);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
       public ProductDTO deleteProduct(Long productId) {
            Product existingProduct  = productRepository.findById(productId)
                    .orElseThrow(()->
                            new ResourceNotFoundException(productId,"productId","Product"));

            productRepository.delete(existingProduct);
            return modelMapper.map(existingProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
            Product productFromDb = productRepository.findById(productId)
                    .orElseThrow(()->new ResourceNotFoundException(productId,"productId","Product"));


            String fileName = fileService.uploadImage(path, image);

            productFromDb.setImage(fileName);

            Product updatedProduct = productRepository.save(productFromDb);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }



}
