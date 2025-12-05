package com.example.cloud.file.processor.file_processor_service.util;

import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
//import java.util.Iterator;

public class ExcelToJsonConverter {

    public static String convert(InputStream in) {
        try (Workbook workbook = WorkbookFactory.create(in)) {
            JSONArray sheetsArray = new JSONArray();

            for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
                Sheet sheet = workbook.getSheetAt(s);
                JSONArray rowsArray = new JSONArray();

                int maxCols = 0;
                for (Row r : sheet) {
                    if (r.getLastCellNum() > maxCols) maxCols = r.getLastCellNum();
                }

                for (Row row : sheet) {
                    JSONArray cellArray = new JSONArray();
                    for (int c = 0; c < maxCols; c++) {
                        Cell cell = row.getCell(c, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                        switch (cell.getCellType()) {
                            case STRING -> cellArray.put(cell.getStringCellValue());
                            case NUMERIC -> {
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    cellArray.put(cell.getLocalDateTimeCellValue().toString());
                                } else {
                                    cellArray.put(cell.getNumericCellValue());
                                }
                            }
                            case BOOLEAN -> cellArray.put(cell.getBooleanCellValue());
                            case FORMULA -> {
                                try {
                                    cellArray.put(cell.getStringCellValue());
                                } catch (Exception ex) {
                                    try { cellArray.put(cell.getNumericCellValue()); } catch (Exception ignore) { cellArray.put(""); }
                                }
                            }
                            default -> cellArray.put("");
                        }
                    }
                    rowsArray.put(cellArray);
                }

                JSONObject sheetJson = new JSONObject();
                sheetJson.put("sheetName", sheet.getSheetName());
                sheetJson.put("rows", rowsArray);
                sheetsArray.put(sheetJson);
            }

            return sheetsArray.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert excel to JSON", e);
        }
    }
}
