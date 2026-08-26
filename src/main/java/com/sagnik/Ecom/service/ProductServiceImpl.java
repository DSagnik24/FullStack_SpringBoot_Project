package com.sagnik.Ecom.service;

import com.sagnik.Ecom.Exceptions.APIException;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

        boolean isProductNotPresent = true;

        List<Product> products = category.getProducts();
        for(int i = 0;i<products.size();i++){
            if(products.get(i).getProductName().equals(productDTO.getProductName())){
                isProductNotPresent = false;
                break;
            }
        }

        if(isProductNotPresent){
            Product product = modelMapper.map(productDTO,Product.class);
            product.setImage("default.png");
            product.setCategory(category);
            double specialPrice = product.getPrice() -
                    ((product.getDiscount() * 0.01)*product.getPrice());
            product.setSpecialPrice(specialPrice);
            Product savedProduct = productRepository.save(product);
            return modelMapper.map(savedProduct, ProductDTO.class);
        }else{
            throw  new APIException("Product  already exists");
        }

    }


    @Override
        /** Retrieves every product and converts the results to DTOs. */
        public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
            Page<Product> pageProducts = productRepository.findAll(pageDetails);

            List<Product> products = pageProducts.getContent();
            List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();


        if(products.isEmpty()){
            throw new APIException("No Products Exists!");
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(pageProducts.getNumber());
        productResponse.setPageSize(pageProducts.getSize());
        productResponse.setTotalPages(pageProducts.getTotalPages());
        productResponse.setTotalElements(pageProducts.getTotalElements());
        productResponse.setLastPage(pageProducts.isLast());
        return productResponse;
    }

        @Override
        /** Retrieves and converts products belonging to the requested category. */
        public ProductResponse searchByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()->
                        new ResourceNotFoundException(categoryId,"categoryId","Category"));

            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
            Page<Product> pageProducts = productRepository.findByCategoryOrderByPriceAsc(category,pageDetails);

            List<Product> products = pageProducts.getContent();

            if(products.isEmpty()){
                throw new APIException(category.getCategoryName()+ "category does not have any content");
            }

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
            productResponse.setPageNumber(pageProducts.getNumber());
            productResponse.setPageSize(pageProducts.getSize());
            productResponse.setTotalPages(pageProducts.getTotalPages());
            productResponse.setTotalElements(pageProducts.getTotalElements());
            productResponse.setLastPage(pageProducts.isLast());
            return productResponse;
    }

        @Override
        /** Searches product names using a case-insensitive keyword match. */
        public ProductResponse searchProductByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
            Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")
                    ? Sort.by(sortBy).ascending()
                    : Sort.by(sortBy).descending();

            Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
            Page<Product> pageProducts = productRepository.findByProductNameLikeIgnoreCase('%'+ keyword +'%',pageDetails);

            List<Product> products = pageProducts.getContent();
        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .toList();

        if(products.isEmpty()){
            throw new APIException("Products not found with keyword: "+ keyword);
        }
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
