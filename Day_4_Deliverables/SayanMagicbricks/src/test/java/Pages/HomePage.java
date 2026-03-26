package Pages;

import org.openqa.selenium.By;

/**
 * Handles the MagicBricks home / landing page.
 * Provides navigation to the Buy menu and sub-links exactly as in the A_xx originals.
 */
public class HomePage extends BasePage {

    private static final String MUMBAI_URL =
            "https://www.magicbricks.com/property-for-sale-rent-in-Mumbai/residential-real-estate-Mumbai";

    // ── Locators ──────────────────────────────────────────────────────
    private final By buyHeading = By.xpath("//a[@id=\"buyheading\"]");

    // ── Actions ───────────────────────────────────────────────────────

    public void openMumbaiPage() throws InterruptedException {
        driver.get(MUMBAI_URL);
        Thread.sleep(2000);
    }

    public void clickBuyHeading() throws InterruptedException {
        driver.findElement(buyHeading).click();
        Thread.sleep(3000);
    }

    /**
     * Clicks any link inside the Buy dropdown by its visible text.
     * Covers: "Budget Homes", "Premium Homes", "House for sale in Mumbai",
     *         "Office Space in Mumbai", "Commercial Space in Mumbai"
     */
    public void clickBuyMenuLink(String linkText) throws InterruptedException {
        driver.findElement(By.xpath("//a[contains(text(),\"" + linkText + "\")]")).click();
        Thread.sleep(3000);
    }
}