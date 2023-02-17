package com.programmingtechie.productservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.exception.ResourceNotFoundException;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    private final ObjectMapper objectMapper;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId());
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public ProductResponse deleteProduct(String id) {
        try{
            ProductResponse productResponseToBeDeleted = new ProductResponse();
            //if product response provide id -> proceed, 
            //else -> name -> proceed, 
            //else -> price -> proceed, 
            //else -> throw error

            Optional<Product> productFromRepo =  productRepository.findById(id);
            if(productFromRepo.isPresent()){
                productRepository.delete(productFromRepo.get());
                return objectMapper.convertValue(productFromRepo.get(), ProductResponse.class);
            }
            else{
                throw new ResourceNotFoundException("The product is not found with id " + id);
            }
            
        }
        catch(Exception ex){
            return null;
        }
    }

    public ProductResponse updateProduct(ProductResponse productResponse) {
        try{
            Product productToBeUpdated = objectMapper.convertValue(productResponse, Product.class);

            if(productRepository.save(productToBeUpdated) != null){
                return productResponse;
            }
            else{
                throw new RuntimeException("There is some error happened. Please contact with system administrator.");
            }
        }
        catch(Exception ex){
            return null;
        }
    }

    public Product findProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }
}
