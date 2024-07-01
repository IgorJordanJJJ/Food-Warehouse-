package ru.jordan.food_storage.service.excel;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import ru.jordan.food_storage.model.Category;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryExcelGenerator {

    private static final List<String> HEADERS = Arrays.asList(
            "ID", "Name", "Description", "Created Date", "Last Updated Date", "Active"
    );
    private static final int HEADER_FONT_SIZE = 16;
    private static final int DATA_FONT_SIZE = 14;



    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) HEADER_FONT_SIZE);
        font.setColor(IndexedColors.GREEN.getIndex()); // Зеленый цвет шрифта
        style.setFont(font);
        return style;
    }

    private CellStyle createDataStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) DATA_FONT_SIZE);
        font.setColor(IndexedColors.ORANGE.getIndex()); // Оранжевый цвет шрифта для данных
        style.setFont(font);
        return style;
    }

    private void createCell(Row row, int columnCount, Object valueOfCell, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (valueOfCell == null) {
            cell.setCellValue("");
        } else {
            setCellValueBasedOnType(cell, valueOfCell);
        }
        cell.setCellStyle(style);
    }

    private void setCellValueBasedOnType(Cell cell, Object value) {
        if (value instanceof String) {
            cell.setCellValue((String) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof LocalDateTime) {
            cell.setCellValue(value.toString());
        }
    }

    private void writeHeader(XSSFSheet sheet, XSSFWorkbook workbook) {
        Row row = sheet.createRow(0);
        CellStyle style = createHeaderStyle(workbook);

        for (int i = 0; i < HEADERS.size(); i++) {
            createCell(row, i, HEADERS.get(i), style);
            // Enable filter for each column
            sheet.autoSizeColumn(i);
            sheet.setAutoFilter(CellRangeAddress.valueOf("A1:" + CellReference.convertNumToColString(i) + "1"));
        }
    }

    private void writeData(XSSFSheet sheet, List<Category> categoryList, XSSFWorkbook workbook) {
        int rowCount = 1;
        CellStyle style = createDataStyle(workbook);

        for (Category category : categoryList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, category.getId(), style);
            createCell(row, columnCount++, category.getName(), style);
            createCell(row, columnCount++, category.getDescription(), style);
            createCell(row, columnCount++, category.getCreatedDate(), style);
            createCell(row, columnCount++, category.getLastUpdatedDate(), style);
            createCell(row, columnCount++, category.getActive(), style);
        }

    }

    public void exportToExcel(HttpServletResponse response, List<Category> categoryList) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(); // Создаем новую рабочую книгу
            XSSFSheet sheet = workbook.createSheet("Data"); // Создаем лист в рабочей книге
            writeHeader(sheet, workbook);
            writeData(sheet, categoryList, workbook);
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        }catch (IOException e){
            log.info(e.toString());
        }
    }
}
