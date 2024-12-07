package com.assessment.ecom.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface ProductPdfService {
    public ByteArrayOutputStream generateProductPdf() throws IOException;
}
