package com.assessment.ecom.service;

import com.assessment.ecom.dto.ProductDTO;
import com.assessment.ecom.dto.SaleDTO;
import com.assessment.ecom.entity.Sale;
import org.springframework.data.domain.Page;

public interface ProductService {
    ProductDTO createProduct(ProductDTO productDTO) throws Exception;

    ProductDTO getProductById(Long id);

    Page<ProductDTO> getAllProducts(Integer pageNo, Integer pageSize, String sortKey) throws Exception;

    ProductDTO updateProductById(Long id, ProductDTO productDTO);

    void deleteProductById(Long id);

    double getTotalRevenue();

    double getRevenueByProduct(Long productId);

    Sale createSale(SaleDTO saleDTO);
}
