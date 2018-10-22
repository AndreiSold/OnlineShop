package ro.msg.learning.shop.utilities;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import ro.msg.learning.shop.dtos.ProductDto;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
@Slf4j
public class ExcelFileCreator {

    @SneakyThrows
    public byte[] createExcelForProducts(List<ProductDto> productList) {

        String[] columns = {"ID", "NAME", "DESCRIPTION", "PRICE", "WEIGHT"};

        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Products");

        // Create a Font for styling header cells
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create cells
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }

        // Create Other rows and cells with employees data
        int rowNum = 1;
        for (ProductDto product : productList) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                .setCellValue(product.getId());

            row.createCell(1)
                .setCellValue(product.getName());

            row.createCell(2)
                .setCellValue(product.getDescription());

            row.createCell(3)
                .setCellValue(product.getPrice());

            row.createCell(4)
                .setCellValue(product.getWeight());
        }

        // Resize all columns to fit the content size
        for (int i = 0; i < columns.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to a file
        FileOutputStream fileOut = new FileOutputStream("resulted_products.xlsx");
        workbook.write(fileOut);
        fileOut.close();

        // Closing the workbook
        workbook.close();

        return Files.readAllBytes(Paths.get("resulted_products.xlsx"));
    }

}
