package com.assessment.ecom.controller;


import com.assessment.ecom.dto.ProductDTO;
import com.assessment.ecom.dto.SaleDTO;
import com.assessment.ecom.entity.Sale;
import com.assessment.ecom.service.ProductPdfService;
import com.assessment.ecom.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    ProductService productService;
    @Autowired
    ProductPdfService productPdfService;

    //for adding new product
    @PostMapping("/product")
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
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

    //for creating sale
    @PostMapping("/sale")
    public ResponseEntity<Sale> createSale(@RequestBody SaleDTO saleDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createSale(saleDTO));
    }

    @GetMapping("/sale/{productId}")
    public ResponseEntity<Double> getRevenueByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getRevenueByProduct(productId));
    }

    @GetMapping("/sale")
    public ResponseEntity<Double> getTotalRevenue() {
        return ResponseEntity.ok(productService.getTotalRevenue());
    }

    @GetMapping("/product/download-pdf")
    public ResponseEntity<byte[]> downloadProductPdf() {
        try {
            ByteArrayOutputStream pdfBytes = productPdfService.generateProductPdf();

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=products.pdf");

            return new ResponseEntity<>(pdfBytes.toByteArray(), headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
