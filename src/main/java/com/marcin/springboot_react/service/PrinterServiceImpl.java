package com.marcin.springboot_react.service;

import com.marcin.springboot_react.exception.ProductPrinterException;
import com.marcin.springboot_react.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Component
public class PrinterServiceImpl implements PrinterService {

    private static final String FILENAME = "products.pdf";

    private static final String FIRST_LINE = "Products List:";

    private static final float TEXT_START_X = 50;

    private static final float TEXT_START_Y = PDRectangle.A4.getHeight() - 100;

    private static final int FONT_SIZE = 12;

    private static final int LEADING = 20;

    @Override
    public File download(List<Product> productList) throws IOException {
        log.info("Generating PDF with products data");
        File file = new File(FILENAME);
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);

        writeContentToPage(document, page, productList);

        document.addPage(page);
        document.save(file);
        document.close();

        log.info("File generated");
        return file;
    }

    private void writeContentToPage(PDDocument document, PDPage page, List<Product> products) throws IOException {
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        contentStream.beginText();
        contentStream.newLineAtOffset(TEXT_START_X, TEXT_START_Y);
        contentStream.setFont(PDType1Font.TIMES_ROMAN, FONT_SIZE);
        contentStream.setLeading(LEADING);

        contentStream.showText(FIRST_LINE);
        contentStream.newLine();

        log.info("Adding product data into the document");
        products.forEach(product -> writeSingleProductData(product, contentStream));

        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText(writeTotalPrice(products));

        String footer = String.format("Generated: %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/mm/yyyy HH:mm:ss")));
        contentStream.newLine();
        contentStream.newLine();
        contentStream.showText(footer);
        log.info("Data added");

        contentStream.endText();
        contentStream.close();
    }

    private String writeTotalPrice(List<Product> products) {
        BigDecimal totalPrice = products.stream()
                .map(product ->
                        product.getPrice().multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, (prev, current) -> prev.add(current));

        return String.format("Total price: %s", totalPrice.toPlainString());
    }

    private void writeSingleProductData(Product product, PDPageContentStream contentStream) {
        try {
            contentStream.newLine();
            contentStream.showText(String.format("%s: x%d", product.getName(), product.getQuantity()));
        } catch (IOException e) {
            String message = String.format("Failed to write product data into the document for product: '%s'.", product);
            log.error(message, e);
            throw new ProductPrinterException(message, e);
        }
    }
}
