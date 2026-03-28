package utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Apache POI utility for reading test data from TestData.xlsx.
 *
 * Expected file location:
 *   src/test/resources/TestData.xlsx
 *
 * Sheets and columns used:
 *   EMIData        → row 1, col 0 = LoanAmount  | col 1 = InterestRate
 *   ContactData    → row 1, col 0 = Name        | col 1 = Email  | col 2 = Mobile
 *   PlotFilterData → row 1, col 0 = MinArea     | col 1 = MaxArea
 *
 * Note: Row and column indices are 0-based.
 *       Row 0 = header, Row 1 = first data row.
 *
 * The step-definition helper readExcelOrDefault() catches any exception from
 * this class and falls back to the inline Gherkin step value, so tests will
 * still run if TestData.xlsx is not present.
 */
public class ExcelUtils {

    private static final String FILE_PATH =
        System.getProperty("user.dir") + "/src/test/resources/TestData.xlsx";

    // Utility class – no instantiation
    private ExcelUtils() {}

    /**
     * Reads a single cell value as a String.
     *
     * @param sheetName name of the sheet tab
     * @param rowIndex  0-based row index
     * @param colIndex  0-based column index
     * @return trimmed String value of the cell
     * @throws Exception if the file, sheet, row, or cell cannot be found
     */
    public static String getCellData(String sheetName, int rowIndex, int colIndex)
            throws Exception {

        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException(
                    "Sheet '" + sheetName + "' not found in TestData.xlsx");
            }

            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                throw new IllegalArgumentException(
                    "Row " + rowIndex + " not found in sheet '" + sheetName + "'");
            }

            Cell cell = row.getCell(colIndex);
            if (cell == null) {
                throw new IllegalArgumentException(
                    "Cell [" + rowIndex + "," + colIndex + "] is null in sheet '"
                    + sheetName + "'");
            }

            // DataFormatter returns the cell value as displayed in Excel
            // regardless of whether it is a String, Number, or Date type
            return new DataFormatter().formatCellValue(cell).trim();

        } catch (IOException e) {
            throw new Exception("Failed to read TestData.xlsx: " + e.getMessage(), e);
        }
    }
}