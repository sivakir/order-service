package com.itccompliance.order.service;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;
import com.itccompliance.order.entity.Order;
import java.io.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PdfGeneratorServiceTest {

    private PdfGeneratorService pdfGeneratorService;
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        pdfGeneratorService = new PdfGeneratorService();
        sampleOrder = new Order();
        sampleOrder.setId(1L);
        sampleOrder.setCustomerName("Alice");
        sampleOrder.setItems(List.of("Item1", "Item2"));
        sampleOrder.setTotalPrice(199.99);
        sampleOrder.setStatus("CREATED");


        // Ensure the template exists in test resources
        createTestTemplate();
    }

    @Test
    @DisplayName("Should generate Word document for order without errors")
    void generatePdf_ShouldGenerateFile() {
        assertDoesNotThrow(() -> pdfGeneratorService.generatePdf(sampleOrder));
    }

    /**
     * Utility to create a test Word template if not present
     */
    private void createTestTemplate() {
        File templateDir = new File("src/test/resources/templates");
        File templateFile = new File(templateDir, "OrderTemplate.docx");

        if (!templateFile.exists()) {
            try {
                if (!templateDir.exists()) templateDir.mkdirs();

                try (XWPFDocument doc = new XWPFDocument()) {
                    var para = doc.createParagraph().createRun();
                    para.setText("Customer: {{customerName}}, Price: {{totalPrice}}, Status: {{status}}");

                    try (FileOutputStream out = new FileOutputStream(templateFile)) {
                        doc.write(out);
                    }
                }
            } catch (IOException e) {
                LoggerFactory.getLogger(PdfGeneratorServiceTest.class)
                        .error("Failed to create test Word template", e);
            }
        }
    }
}

