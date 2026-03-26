package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * TC10 – House for Sale in Mumbai SRP + Property Detail + Locality Map.
 * Tab flow (mirrors A_10):
 *   Tab-1 (mainTab)   → MagicBricks Mumbai landing
 *   Tab-2 (secondTab) → House SRP        ← switch here after clicking link
 *   Tab-3 (thirdTab)  → Property Detail  ← switch here after clicking property
 *   Tab-4 (fourthTab) → Locality page    ← switch here after clicking Explore Locality
 */
public class HouseForSalePage extends BasePage {

    // ── SRP locators ──────────────────────────────────────────────────
    // A_10 uses a specific h2 text – kept identical
    private final By specificProperty =
            By.xpath("//h2[text()=\"1 BHK House for Sale in  Ghatkopar East, Mumbai\"]");

    // ── Detail page locators ──────────────────────────────────────────
    private final By aboutLocalityTab   = By.xpath("//a[text()=\"About Locality\"]");
    private final By exploreLocalityLink = By.xpath("//a[text()=\"Explore Locality\"]");

    // ── Locality page locators ────────────────────────────────────────
    private final By mapCtaButton   = By.xpath("//a[@class=\"loc-det__banner__cta\"]");
    private final By mapCanvas      = By.xpath("//canvas[@class=\"mapboxgl-canvas\"]");

    // ── Actions ───────────────────────────────────────────────────────

    /** Prints and clicks the specific house property heading – mirrors A_10. */
    public void printAndClickHouseProperty() throws InterruptedException {
        WebElement prop = driver.findElement(specificProperty);
        System.out.println("Selected Property Details :- \n");
        System.out.println(prop.getText() + "\n");
        prop.click();
    }

    /** Scrolls down 2000 px total (two 1000px calls as in A_10). */
    public void scrollDownOnDetailPage() throws InterruptedException {
        scrollBy(0, 1000);
        scrollBy(0, 1000);
        Thread.sleep(2000);
    }

    public void clickAboutLocalityTab() throws InterruptedException {
        driver.findElement(aboutLocalityTab).click();
        Thread.sleep(2000);
    }

    /** Scroll up 100 px then click Explore Locality – mirrors A_10. */
    public void scrollUpAndClickExploreLocality() throws InterruptedException {
        scrollBy(0, -100);
        driver.findElement(exploreLocalityLink).click();
    }

    public void clickMapCtaButton() throws InterruptedException {
        driver.findElement(mapCtaButton).click();
        Thread.sleep(2000);
    }

    public void printMapConfirmation() {
        System.out.println("Map displayed and the property is marked");
        // canvas has no text content – getText() returns empty string as in A_10
        System.out.println(driver.findElement(mapCanvas).getText());
    }
}