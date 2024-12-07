package com.assessment.ecom.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SaleDTO {
    private Long productId;
    private int quantity;
    private LocalDate saleDate;
}
