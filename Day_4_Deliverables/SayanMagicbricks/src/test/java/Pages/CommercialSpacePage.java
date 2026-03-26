package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * TC16 – Commercial Space in Mumbai SRP + Business Type filter + Sort.
 * Tab flow (mirrors A_16):
 *   Tab-1 (mainTab)   → MagicBricks Mumbai landing
 *   Tab-2 (secondTab) → Commercial SRP   ← switch here after clicking link
 */
public class CommercialSpacePage extends BasePage {

    // ── Filter / sort locators (A_16 exact xpaths) ───────────────────
    private final By businessTypeFilter =
            By.xpath("//div[text()=\"Business Type\"]");

    // A_16 uses the full absolute xpath for the first sub-type option
    private final By firstSubTypeOption =
            By.xpath("//*[@id=\"body\"]/div[1]/div/div[2]/div[4]/div/div[2]/div[1]/div/div/div[2]/div[1]");

    private final By sortDropdown =
            By.xpath("//div[@class=\"mb-srp__tabs__sortby--title\"]");

    private final By sortHighToLow =
            By.xpath("//li[text()=\"Price - High to Low\"]");

    private final By priceAmounts =
            By.xpath("//div[@class=\"mb-srp__card__price--amount\"]");

    // ── Actions ───────────────────────────────────────────────────────

    public void clickBusinessTypeFilter() throws InterruptedException {
        driver.findElement(businessTypeFilter).click();
        Thread.sleep(3000);
    }

    public void selectFirstBusinessSubType() throws InterruptedException {
        driver.findElement(firstSubTypeOption).click();
        Thread.sleep(3000);
    }

    public void clickSortDropdown() throws InterruptedException {
        driver.findElement(sortDropdown).click();
        Thread.sleep(1000);
    }

    public void selectPriceHighToLow() throws InterruptedException {
        driver.findElement(sortHighToLow).click();
        Thread.sleep(3000);
    }

    /**
     * Prints prices of first 5 commercial properties.
     * A_16 uses index i+1 (skips index 0) – preserved exactly.
     */
    public void printFirst5CommercialPrices() {
        List<WebElement> properties = driver.findElements(priceAmounts);
        for (int i = 0; i < 5; i++) {
            System.out.println("Price of property " + (i + 1) + " is : "
                    + properties.get(i + 1).getText());
        }
    }
}