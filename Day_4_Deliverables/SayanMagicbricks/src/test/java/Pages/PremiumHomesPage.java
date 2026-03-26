package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * TC06 – Premium Homes SRP + Image Gallery.
 * Tab flow:
 *   Tab-1 → MagicBricks Mumbai landing
 *   Tab-2 → Premium Homes SRP  ← switch here after clicking link
 */
public class PremiumHomesPage extends BasePage {

    // ── SRP locators ──────────────────────────────────────────────────
    private final By searchBtn       = By.xpath("//div[@class=\"mb-search__btn\"]");
    private final By propertyCards   = By.xpath("//div[@class=\"mb-srp__card\"]");
    private final By imageSlider     = By.xpath("//div[@class=\"mb-srp__card__photo__fig--slider\"]");

    // ── Gallery locators ──────────────────────────────────────────────
    private final By projectPhotoTab = By.xpath("//li[@data-category=\"Project Photo\"][1]");
    private final By rightArrow      = By.xpath("//div[@class=\"arrow rightArrow\"]");

    // ── Actions ───────────────────────────────────────────────────────

    public void clickSearchButton() throws InterruptedException {
        driver.findElement(searchBtn).click();
        Thread.sleep(10000);   // A_06 uses Thread.sleep(10000) – kept as-is
    }

    /** Clicks the image slider of the first property card. */
    public void clickFirstPropertyImageSlider() throws InterruptedException {
        List<WebElement> properties = driver.findElements(propertyCards);
        WebElement firstProperty = properties.get(0);
        firstProperty.findElement(imageSlider).click();
        Thread.sleep(2000);
    }

    public void clickProjectPhotoTab() throws InterruptedException {
        driver.findElement(projectPhotoTab).click();
        Thread.sleep(2000);
    }

    /** Navigates right through 3 gallery images – mirrors A_06 loop. */
    public void navigateRight3Photos() throws InterruptedException {
        System.out.println("Photo 1 seen. Moving right");
        driver.findElement(rightArrow).click();
        Thread.sleep(2000);
        System.out.println("Photo 2 seen. Moving right");
        driver.findElement(rightArrow).click();
        Thread.sleep(2000);
        System.out.println("Photo 3 seen. Moving right");
        driver.findElement(rightArrow).click();
    }
}