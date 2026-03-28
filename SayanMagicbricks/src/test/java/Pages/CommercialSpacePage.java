package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * TC16 – Commercial Space Page (STABLE VERSION)
 * ✔ Handles dynamic filters + sorting reliably
 * ✔ Prevents StaleElementReferenceException
 */
public class CommercialSpacePage extends BasePage {

    // ── Locators ──────────────────────────────────────────────────────

    private final By businessTypeFilter =
            By.xpath("//div[text()='Business Type']");

    // First sub-type option (generic, first visible option)
    private final By firstSubTypeOption =
            By.xpath("(//div[contains(@class,'filter-business__item')])[1]");

    private final By sortDropdown =
            By.xpath("(//div[contains(@class,'sortby')])[1]");

    private final By sortHighToLow =
            By.xpath("//li[contains(text(),'Price - High to Low')]");

    private final By priceAmounts =
            By.xpath("//div[contains(@class,'mb-srp__card__price--amount')]");

    // ── ACTIONS ──────────────────────────────────────────────────────

    public void clickBusinessTypeFilter() {
        waitPageReady();

        WebElement filter = waitClickable(businessTypeFilter);
        jsScrollIntoView(filter);
        jsClick(filter);

        // wait for sub-type options to appear
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstSubTypeOption));
    }

    public void selectFirstBusinessSubType() {
        // Refetch the element just before clicking
        WebElement option = wait.until(ExpectedConditions.elementToBeClickable(firstSubTypeOption));

        jsScrollIntoView(option);
        jsClick(option);

        // Wait for results to refresh (at least 5 prices)
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(priceAmounts, 5));
    }

    public void clickSortDropdown() {
        WebElement sort = waitClickable(sortDropdown);
        jsScrollIntoView(sort);
        jsClick(sort);

        wait.until(ExpectedConditions.visibilityOfElementLocated(sortHighToLow));
    }

    public void selectPriceHighToLow() {
        WebElement option = waitClickable(sortHighToLow);
        jsClick(option);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(priceAmounts, 5));
    }

    public void printFirst5CommercialPrices() {
        List<WebElement> prices =
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(priceAmounts));

        for (int i = 0; i < 5; i++) {
            System.out.println("Price of property " + (i + 1) + " is : "
                    + prices.get(i).getText());
        }
    }
}