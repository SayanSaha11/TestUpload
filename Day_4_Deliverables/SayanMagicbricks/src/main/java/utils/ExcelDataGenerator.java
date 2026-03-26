package utils;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Run this class ONCE (as a plain Java main) to generate
 * src/test/resources/testdata/TestData.xlsx
 *
 * After that the file is committed and ExcelUtils reads from it at runtime.
 */
public class ExcelDataGenerator {

    public static void main(String[] args) throws IOException {

        XSSFWorkbook wb = new XSSFWorkbook();

        // ── Sheet 1: EMIData ──────────────────────────────────────────
        XSSFSheet emi = wb.createSheet("EMIData");
        XSSFRow emiHeader = emi.createRow(0);
        emiHeader.createCell(0).setCellValue("LoanAmount");
        emiHeader.createCell(1).setCellValue("InterestRate");
        XSSFRow emiData = emi.createRow(1);
        emiData.createCell(0).setCellValue("3000000");   // raw numeric string → sendKeys
        emiData.createCell(1).setCellValue("7.5");

        // ── Sheet 2: ContactData ──────────────────────────────────────
        XSSFSheet contact = wb.createSheet("ContactData");
        XSSFRow cHeader = contact.createRow(0);
        cHeader.createCell(0).setCellValue("Name");
        cHeader.createCell(1).setCellValue("Email");
        cHeader.createCell(2).setCellValue("Mobile");
        XSSFRow cData = contact.createRow(1);
        cData.createCell(0).setCellValue("Test User");
        cData.createCell(1).setCellValue("testuser@gmail.com");
        cData.createCell(2).setCellValue("9330827089");

        // ── Sheet 3: PlotFilterData ───────────────────────────────────
        XSSFSheet plot = wb.createSheet("PlotFilterData");
        XSSFRow pHeader = plot.createRow(0);
        pHeader.createCell(0).setCellValue("MinArea");
        pHeader.createCell(1).setCellValue("MaxArea");
        XSSFRow pData = plot.createRow(1);
        pData.createCell(0).setCellValue("500");
        pData.createCell(1).setCellValue("2000");

        // ── Write file ────────────────────────────────────────────────
        
        String path = "src/test/resources/testdata/TestData.xlsx";
        try (FileOutputStream fos = new FileOutputStream(path)) {
            wb.write(fos);
        }
        wb.close();
        System.out.println("TestData.xlsx written to: " + path);
    }
}