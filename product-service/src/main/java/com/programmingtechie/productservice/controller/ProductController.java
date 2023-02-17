package com.programmingtechie.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.productservice.dto.ProductRequest;
import com.programmingtechie.productservice.dto.ProductResponse;
import com.programmingtechie.productservice.model.Product;
import com.programmingtechie.productservice.service.ProductService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    private final ObjectMapper objectMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProduct(@RequestParam(name="id", required = false) String id){
        try{
            if(id != null){
                ProductResponse productResponse = productService.deleteProduct(id);
                if(productResponse != null){
                    return new ResponseEntity("Successfully deleted the selected product.", HttpStatus.OK);
                }
                return new ResponseEntity<>("Failed to delete the selected product.", HttpStatus.UNPROCESSABLE_ENTITY);
            }
            else{
                return new ResponseEntity<>("This id is not given. Please insert the id.", HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception ex){
            return new ResponseEntity<>("Something went wrong. Please contact with system administrator for further details.", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<?> updateProduct(@RequestParam(name="id", required = false) String id,
                                           @RequestParam(name="productName", required = false) String productName,
                                           @RequestParam(name="price", required = false) String price,
                                           @RequestParam(name="description", required = false) String description){
        try{
            if(id != null){
                Product product = productService.findProductById(id);
                if(product != null){
                    ProductResponse productResponse = objectMapper.convertValue(product, ProductResponse.class);
                    if(productName != null){
                        productResponse.setName(productName);
                    }
                    if(price != null){
                        productResponse.setPrice(new BigDecimal(price));
                    }
                    if(description != null){
                        productResponse.setDescription(description);
                    }
                    ProductResponse productAfterUpdate = productService.updateProduct(productResponse);
                    if(productResponse != null){
                        return new ResponseEntity<>("Successfully updated the selected product.", HttpStatus.OK);
                    }
                    return new ResponseEntity<>("Failed to update the selected product.", HttpStatus.UNPROCESSABLE_ENTITY);
                }
                else{
                    return new ResponseEntity<>("The product with this id is not exist.", HttpStatus.UNPROCESSABLE_ENTITY);
                }
            }
            else{
                return new ResponseEntity<>("id cannot be null. Please insert the id.", HttpStatus.BAD_REQUEST);
            }
        }
        catch(Exception ex){
            return new ResponseEntity<>("Something went wrong. Please contact with system administrator for further details.", HttpStatus.BAD_REQUEST);
        }
    }
}
