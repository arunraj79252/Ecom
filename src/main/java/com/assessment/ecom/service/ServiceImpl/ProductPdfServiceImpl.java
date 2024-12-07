package com.assessment.ecom.service.ServiceImpl;

import com.assessment.ecom.entity.Product;
import com.assessment.ecom.repository.ProductRepository;
import com.assessment.ecom.service.ProductPdfService;
import com.assessment.ecom.service.ProductService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ProductPdfServiceImpl implements ProductPdfService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;

    @Override
    public ByteArrayOutputStream generateProductPdf() throws IOException {
        List<Product> products = productRepository.findAll();

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(50, 750);

        // Header row with fixed-width columns (aligned)
        String header = String.format("%-10s%-20s%-30s%-10s%-10s%-10s", "ID", "Name", "Description", "Price", "Quantity", "Revenue");
        contentStream.showText(header);
        contentStream.newLineAtOffset(0, -15);

        // Add product rows with fixed-width columns
        contentStream.setFont(PDType1Font.HELVETICA, 10);
        for (Product product : products) {
            double revenue = productService.getRevenueByProduct(product.getId());

            // Use String.format to ensure each field has a fixed width
            String row = String.format("%-14d%-30s%-42s%-15.2f%-15d%-15.2f",
                    product.getId(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getQty(),
                    revenue);

            contentStream.showText(row);
            contentStream.newLineAtOffset(0, -15);
        }

        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        return outputStream;
    }

}
