package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Thin Apache POI wrapper.
 * Reads a cell from TestData.xlsx by sheet name, row index (0-based), and column index.
 * Row 0 = header, Row 1 = first data row.
 */
public class ExcelUtils {

    private static final String FILE_PATH =
            System.getProperty("user.dir") +
            "/src/test/resources/testdata/TestData.xlsx";

    /**
     * @param sheetName  e.g. "EMIData", "ContactData", "PlotFilterData"
     * @param rowIndex   0-based (0 = header, 1 = first data row)
     * @param colIndex   0-based column index
     * @return cell value as String (numeric cells are converted without decimal if whole number)
     */
    public static String getCellData(String sheetName, int rowIndex, int colIndex) {
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             XSSFWorkbook wb = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = wb.getSheet(sheetName);
            if (sheet == null) throw new RuntimeException("Sheet not found: " + sheetName);

            Row row = sheet.getRow(rowIndex);
            if (row == null) throw new RuntimeException("Row " + rowIndex + " not found in " + sheetName);

            Cell cell = row.getCell(colIndex);
            if (cell == null) return "";

            if (cell.getCellType() == CellType.NUMERIC) {
                double d = cell.getNumericCellValue();
                // return as integer string if no fractional part
                if (d == Math.floor(d) && !Double.isInfinite(d)) {
                    return String.valueOf((long) d);
                }
                return String.valueOf(d);
            }
            return cell.getStringCellValue().trim();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel: " + FILE_PATH, e);
        }
    }
}