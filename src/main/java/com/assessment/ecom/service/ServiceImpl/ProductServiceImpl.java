package com.assessment.ecom.service.ServiceImpl;

import com.assessment.ecom.dto.ProductDTO;
import com.assessment.ecom.dto.SaleDTO;
import com.assessment.ecom.entity.Product;
import com.assessment.ecom.entity.Sale;
import com.assessment.ecom.exception.CustomException;
import com.assessment.ecom.repository.ProductRepository;
import com.assessment.ecom.repository.SaleRepository;
import com.assessment.ecom.service.ProductService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    ProductRepository productRepository;
    @Autowired
    SaleRepository saleRepository;

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        try {
            Product savedProduct = productRepository.save(new Product(productDTO));
            LOGGER.info("Product created successfully");
            return new ProductDTO(savedProduct);
        } catch (Exception e) {
            LOGGER.error("createProduct()" + e);
            throw new CustomException.ServerErrorException("Exception when saving product");
        }
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new CustomException.BadRequestException("Product not found"));
        return new ProductDTO(product);
    }

    @Override
    public Page<ProductDTO> getAllProducts(Integer pageNo, Integer pageSize, String sortKey) throws Exception {
        Sort sort = Sort.by(sortKey).ascending();
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> list = productRepository.findAll(pageable);
        return list.map(this::covertToProductDTO);
    }

    @Override
    public ProductDTO updateProductById(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new CustomException.BadRequestException("Product not found"));
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setQty(productDTO.getQty());
        return covertToProductDTO(productRepository.save(product));
    }

    @Override
    public void deleteProductById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new CustomException.BadRequestException("Product not found"));
        productRepository.deleteById(id);
        LOGGER.info("Product deleted");
    }

    //to get total revenue
    public double getTotalRevenue() {
        try {
            List<Sale> sales = saleRepository.findAll();
            return sales.stream()
                    .mapToDouble(sale -> sale.getQuantity() * productRepository.findById(sale.getProductId()).orElseThrow().getPrice())
                    .sum();
        } catch (Exception e) {
            LOGGER.info("Exception in getTotalRevenue() " + e);
            throw new CustomException.BadRequestException("Error when generating total revenue..");
        }

    }

    //get revenue by a product
    public double getRevenueByProduct(Long productId) {
        try {
            List<Sale> sales = saleRepository.findByProductId(productId);
            return sales.stream()
                    .mapToDouble(sale -> sale.getQuantity() * productRepository.findById(sale.getProductId()).orElseThrow().getPrice())
                    .sum();
        } catch (Exception e) {
            LOGGER.info("Exception in getRevenueByProduct() " + e);
            throw new CustomException.BadRequestException("Error when generating revenue by product:" + productId);
        }
    }

    @Override
    @Transactional
    public Sale createSale(SaleDTO saleDTO) {
        Long productId = saleDTO.getProductId();
        Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException.BadRequestException("Invalid productId"));
        if (product.getQty() < saleDTO.getQuantity())
            throw new CustomException.BadRequestException("Product is out of stock");
        product.setQty(product.getQty() - saleDTO.getQuantity());
        productRepository.save(product);
        LOGGER.info("Product qty updated");
        Sale sale = saleRepository.save(new Sale(saleDTO));
        LOGGER.info("Sale created successfully");
        return sale;
    }

    private ProductDTO covertToProductDTO(Product product) {
        return new ProductDTO(product);
    }
}
