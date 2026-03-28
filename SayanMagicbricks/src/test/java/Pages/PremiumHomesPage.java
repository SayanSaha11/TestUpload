package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * PremiumHomesPage – search results + image gallery navigation (TC06).
 *
 * All waits are explicit:
 *   - clickSearchButton: waits for property cards after search loads.
 *   - clickFirstPropertyImageSlider: waits for gallery/lightbox to open.
 *   - clickProjectPhotoTab: waits for right-arrow before returning.
 *   - navigateGalleryRight: uses staleProofClick because the gallery widget
 *     re-renders the arrow element after each image transition; waits for
 *     the arrow to be clickable again before the next iteration.
 */
public class PremiumHomesPage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────────────────
    private final By searchButton    = By.xpath("//div[@class='mb-search__btn']");
    private final By propertyCards   = By.xpath("//div[@class='mb-srp__card']");
    private final By imageSlider     = By.xpath(".//div[@class='mb-srp__card__photo__fig--slider']");
//    private final By propertyCards = By.xpath("//div[contains(@class,'mb-srp__card')]");
//    private final By imageSlider = By.xpath(".//div[contains(@class,'mb-srp__card__photo__fig--slider')]");
    private final By projectPhotoTab = By.xpath("//li[@data-category='Project Photo'][1]");
    private final By rightArrow      = By.xpath("//div[@class='arrow rightArrow']");

    // ─── Constructor ─────────────────────────────────────────────────────────
    public PremiumHomesPage() {
        super();
    }

    // ─── Page actions ────────────────────────────────────────────────────────

    /**
     * Clicks the search button on the Premium Homes SRP.
     * Waits for at least one property card to be visible after search.
     */
//    public void clickSearchButton() {
//        waitClickable(searchButton).click();
//        waitAllVisible(propertyCards); // confirm results loaded
//    }
    
    public void clickSearchButton() {
        waitPageReady();
        jsClick(waitClickable(searchButton));
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(propertyCards, 0));
    }

    /**
     * Opens the image slider/gallery of the first property card.
     * The slider is a child element within the first card — fetched via
     * a relative findElement to avoid index mismatches.
     */
    public void clickFirstPropertyImageSlider() {
        List<WebElement> cards = waitAllVisible(propertyCards);
        WebElement slider = cards.get(0).findElement(imageSlider);
        waitForClickable(slider);
        slider.click();
        waitVisible(projectPhotoTab); // confirm lightbox/gallery opened
    }

    /**
     * Clicks the "Project Photo" tab inside the gallery.
     * Waits for the right-arrow navigation control to appear before returning.
     */
    public void clickProjectPhotoTab() {
        waitClickable(projectPhotoTab).click();
        waitVisible(rightArrow); // confirm photo view is active
        System.out.println("Photo 1 seen. Moving right");
    }

    /**
     * Navigates the gallery to the right by the given number of clicks.
     *
     * staleProofClick is used because the gallery widget destroys and
     * re-creates the arrow element after each image transition.
     * After each click we wait for the arrow to be clickable again
     * (confirms the transition animation has completed) before the next click.
     *
     * @param times number of right-arrow clicks to perform
     */
    public void navigateGalleryRight(int times) {
        for (int i = 1; i <= times; i++) {
            jsScrollIntoView(waitVisible(rightArrow));
            staleProofClick(rightArrow);
            waitClickable(rightArrow); // wait for next image to load
            System.out.println("Photo " + (i + 1) + " seen. Moving right");
        }
    }
    
    public List<WebElement> getFreshPropertyCards() {
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(propertyCards, 0));
        return driver.findElements(propertyCards);
    }
}