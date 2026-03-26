package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * TC12 – Residential Plots Mumbai page + More Filters (plot area range).
 * Tab flow (mirrors A_12):
 *   Tab-1 → Plots page (direct URL – no new tab opens, stays in same tab)
 *
 * NOTE: A_12 calls driver.getWindowHandles() but the loop never switches
 * because no new tab is opened; we preserve that behaviour.
 */
public class PlotPage extends BasePage {

    private static final String PLOTS_URL =
            "https://www.magicbricks.com/residential-plots-land-for-sale-in-mumbai-pppfs";

    // ── Locators ──────────────────────────────────────────────────────
    private final By filterTitle      = By.xpath("//div[@class='filter__component__title more-title']");
    private final By moreFiltersBtn   = By.xpath("//div[text()='More Filters']");
    private final By minAreaDropdown  = By.xpath("//*[@id=\"moreFilter_0\"]/div[2]/div/div/div/div[1]/div[1]/select");
    private final By maxAreaDropdown  = By.xpath("(//*[@id='moreFilter_0']/div[2]/div/div/div/div[1]/div/select)[2]");
    private final By applyBtn         = By.xpath("//div[@class='filter__component__btn']");
    private final By activeFilter     = By.xpath("//div[@class='top-filter__item-all-filter']");
    private final By plotAreaItems    = By.xpath("//div[@data-summary='plot-area']");

    // ── Actions ───────────────────────────────────────────────────────

    public void openPlotsPage() throws InterruptedException {
        driver.get(PLOTS_URL);
        Thread.sleep(2000);
    }

    public void printCurrentFilterSpec() {
        WebElement spec = driver.findElement(filterTitle);
        System.out.println(spec.getText());
    }

    public void clickMoreFilters() throws InterruptedException {
        Thread.sleep(3000);
        driver.findElement(moreFiltersBtn).click();
        Thread.sleep(2000);
    }

    /** Selects minimum plot area from the first dropdown using Select (mirrors A_12). */
    public void selectMinPlotArea(String value) throws InterruptedException {
        WebElement el = driver.findElement(minAreaDropdown);
        new Select(el).selectByValue(value);
        Thread.sleep(2000);
    }

    /** Closes the More Filters panel by clicking the toggle again (mirrors A_12). */
    public void closeMoreFilters() throws InterruptedException {
        driver.findElement(moreFiltersBtn).click();
        Thread.sleep(2000);
    }

    /** Re-opens the More Filters panel (mirrors A_12 – open again for max dropdown). */
    public void reopenMoreFilters() throws InterruptedException {
        driver.findElement(moreFiltersBtn).click();
        Thread.sleep(2000);
    }

    /** Selects maximum plot area from the second dropdown using Select (mirrors A_12). */
    public void selectMaxPlotArea(String value) throws InterruptedException {
        WebElement el = driver.findElement(maxAreaDropdown);
        new Select(el).selectByValue(value);
        Thread.sleep(2000);
    }

    public void clickApplyFilters() throws InterruptedException {
        driver.findElement(applyBtn).click();
        Thread.sleep(2000);
    }

    public void printActiveFilterLabel() {
        WebElement spec = driver.findElement(activeFilter);
        System.out.println(spec.getText());
    }

    /** Prints plot area of first 5 filtered properties – mirrors A_12. */
    public void printFirst5PlotAreas() {
        List<WebElement> plotAreas = driver.findElements(plotAreaItems);
        for (int i = 0; i < 5; i++) {
            System.out.println("Plot Area of Property " + (i + 1) + " is : "
                    + plotAreas.get(i).getText());
        }
    }
}