package com.assessment.ecom.controller;


import com.assessment.ecom.dto.ProductDTO;
import com.assessment.ecom.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    ProductService productService;

    //for adding new product
    @PostMapping("/product")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(productDTO));
    }

    //for listing a product with given id
    @GetMapping("/product/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    //for updating a product with given id
    @PutMapping("/product/{id}")
    public ResponseEntity<ProductDTO> updateProductById(@PathVariable Long id, @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(productService.updateProductById(id, productDTO));
    }

    //for deleting a product with given id
    @DeleteMapping("/product/{id}")
    public ResponseEntity<ProductDTO> deleteProductById(@PathVariable Long id) throws Exception {
        productService.deleteProductById(id);
        return ResponseEntity.noContent().build();
    }

    //paginated list for products
    @GetMapping("/product")
    public ResponseEntity<Page<ProductDTO>> getAllProducts(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
                                                           @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                           @RequestParam(name = "sortKey", defaultValue = "name") String sortKey) throws Exception {
        return ResponseEntity.ok(productService.getAllProducts(pageNo, pageSize, sortKey));
    }

    @GetMapping("/sale/{productId}")
    public ResponseEntity<Double> getRevenueByProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getRevenueByProduct(id));
    }

    @GetMapping("/sale")
    public ResponseEntity<Double> getTotalRevenue() {
        return ResponseEntity.ok(productService.getTotalRevenue());
    }
}
