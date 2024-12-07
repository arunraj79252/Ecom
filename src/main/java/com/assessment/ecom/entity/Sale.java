package com.assessment.ecom.entity;

import com.assessment.ecom.dto.SaleDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productId;
    private int quantity;
    private LocalDate saleDate;

    public Sale(SaleDTO saleDTO) {
        this.productId = saleDTO.getProductId();
        this.quantity = saleDTO.getQuantity();
        this.saleDate = LocalDate.now();
    }
}
