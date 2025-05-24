package com.itccompliance.order.service;

import com.itccompliance.order.entity.Order;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class PdfGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(PdfGeneratorService.class);

    public void generatePdf(Order order) {
        try (InputStream templateStream = getClass().getResourceAsStream("/templates/OrderTemplate.docx")) {
            if (templateStream == null) {
                logger.error("Word template not found at /templates/OrderTemplate.docx");
                return;
            }

            XWPFDocument document = new XWPFDocument(templateStream);

            for (XWPFParagraph paragraph : document.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String text = run.getText(0);
                    if (text != null) {
                        text = text.replace("customerName", order.getCustomerName())
                                .replace("totalPrice", String.valueOf(order.getTotalPrice()))
                                .replace("orderStatus", order.getStatus());
                        run.setText(text, 0);
                    }
                }
            }

            PdfWriter pdfWriter = new PdfWriter("order_" + order.getId()+ ".pdf");
            PdfDocument pdf = new PdfDocument(pdfWriter);
            Document document1 = new Document(pdf);
            for(XWPFParagraph para : document.getParagraphs()){
                document1.add(new Paragraph(para.getText()));
            }

            /*File outFile = File.createTempFile("order_" + order.getId(), ".docx");
            try (FileOutputStream out = new FileOutputStream(outFile)) {
                document.write(out);
            }*/

            logger.info("Generated document for order {} at {}", order.getId(), "order_" + order.getId()+ ".pdf");
            document1.close();
            pdf.close();
        } catch (Exception e) {
            logger.error("Failed to generate PDF for order {}", order.getId(), e);
        }
    }
}
