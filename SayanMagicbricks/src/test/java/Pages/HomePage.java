//package Pages;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//
///**
// * HomePage – handles landing page load and Buy dropdown menu navigation.
// *
// * TC10 FIX – ElementNotInteractableException on "House for sale in Mumbai"
// * ─────────────────────────────────────────────────────────────────────────
// * Root cause:
// *   The Buy dropdown is rendered via a CSS opacity/transform transition.
// *   When clickBuyHeading() returns, the transition may still be running —
// *   the sub-links are in the DOM but the CSS state hasn't reached
// *   "fully visible + interactable" yet. A plain waitClickable().click()
// *   fires mid-transition and Chrome throws ElementNotInteractableException.
// *
// * Fix applied in clickBuyMenuLink():
// *   1. Re-hover the Buy heading (ensures the dropdown stays open and the
// *      CSS transition completes from a clean hover state).
// *   2. waitVisible(target) – waits for the specific link to be rendered.
// *   3. jsScrollIntoView()  – scrolls the link into viewport centre.
// *   4. Wait for full clickability via ExpectedConditions.
// *   5. staleProofClick()   – retries on any mid-animation stale reference.
// */
//public class HomePage extends BasePage {
//
//    // ─── URL ────────────────────────────────────────────────────────────────
//    private static final String BASE_URL =
//        "https://www.magicbricks.com/property-for-sale-rent-in-Mumbai/residential-real-estate-Mumbai";
//
//    // ─── Locators ────────────────────────────────────────────────────────────
//    private final By buyHeading       = By.xpath("//a[@id='buyheading']");
//    private final By budgetHomesLink  = By.xpath("//a[contains(text(),'Budget Homes')]");
//    private final By premiumHomesLink = By.xpath("//a[contains(text(),'Premium Homes')]");
//    private final By officeSpaceLink  = By.xpath("//a[contains(text(),'Office Space in Mumbai')]");
//    private final By commercialLink   = By.xpath("//a[contains(text(),'Commercial Space in Mumbai')]");
//    private final By houseForSaleLink = By.xpath("//a[contains(text(),'House for sale in Mumbai')]");
//
//    // ─── Constructor ─────────────────────────────────────────────────────────
//    public HomePage() {
//        super();
//    }
//
//    // ─── Page actions ────────────────────────────────────────────────────────
//
//    /**
//     * Navigates to the Mumbai residential listings page.
//     * Waits for document.readyState and the Buy heading to be visible
//     * before returning so that the first step always finds the page ready.
//     */
//    public void openHomePage() {
//        driver.get(BASE_URL);
//        waitPageReady();
//        waitVisible(buyHeading);
//    }
//
//    /** Alias used by the step-definition Background step. */
//    public void openMumbaiPage() {
//        openHomePage();
//    }
//
//    /**
//     * Clicks the Buy heading and waits until the dropdown is populated.
//     * Returns only after at least one sub-link is visible so the next step
//     * can safely interact with the dropdown.
//     */
//    public void clickBuyHeading() {
//        waitClickable(buyHeading).click();
//        waitVisible(budgetHomesLink); // confirm dropdown is open
//    }
//
//    /**
//     * Clicks a Buy-menu sub-link by its display text.
//     * Used by the shared Gherkin step: "the user clicks {string}".
//     *
//     * Fix strategy (see class-level JavaDoc for full root-cause analysis):
//     *   1. Re-hover Buy heading → guarantees dropdown stays open.
//     *   2. waitVisible(target) → waits for link to be in the DOM.
//     *   3. jsScrollIntoView()  → centres the link in the viewport.
//     *   4. elementToBeClickable → waits for full CSS transition to finish.
//     *   5. staleProofClick()   → handles any last-millisecond stale reference.
//     */
//    public void clickBuyMenuLink(String linkText) {
//        By target = getLinkLocator(linkText);
//
//        // Step 1 – Re-hover to ensure the dropdown transition completes
//        new Actions(driver)
//            .moveToElement(waitClickable(buyHeading))
//            .perform();
//
//        // Step 2 – Wait for the specific link to appear in the dropdown
//        WebElement link = waitVisible(target);
//
//        // Step 3 – Scroll the link into the viewport centre
//        jsScrollIntoView(link);
//
//        // Step 4 – Wait for full interactability (CSS transition done)
//        wait.until(ExpectedConditions.elementToBeClickable(target));
//
//        // Step 5 – Click with stale-reference retry
//        staleProofClick(target);
//    }
//
//    /**
//     * Maps feature-step link text to its By locator.
//     * Falls back to a contains-text XPath for any unlisted link.
//     */
//    private By getLinkLocator(String linkText) {
//        switch (linkText) {
//            case "Budget Homes":               return budgetHomesLink;
//            case "Premium Homes":              return premiumHomesLink;
//            case "Office Space in Mumbai":     return officeSpaceLink;
//            case "Commercial Space in Mumbai": return commercialLink;
//            case "House for sale in Mumbai":   return houseForSaleLink;
//            default:
//                return By.xpath("//a[contains(text(),'" + linkText + "')]");
//        }
//    }
//}

package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * HomePage – handles landing page load and Buy dropdown menu navigation.
 */
public class HomePage extends BasePage {

    private static final String BASE_URL =
        "https://www.magicbricks.com/property-for-sale-rent-in-Mumbai/residential-real-estate-Mumbai";

    private final By buyHeading       = By.xpath("//a[@id='buyheading']");
    private final By budgetHomesLink  = By.xpath("//a[contains(text(),'Budget Homes')]");
    private final By premiumHomesLink = By.xpath("//a[contains(text(),'Premium Homes')]");
    private final By officeSpaceLink  = By.xpath("//a[contains(text(),'Office Space in Mumbai')]");
    private final By commercialLink   = By.xpath("//a[contains(text(),'Commercial Space in Mumbai')]");
    private final By houseForSaleLink = By.xpath("//a[contains(text(),'House for sale in Mumbai')]");

    public HomePage() {
        super();
    }

    public void openHomePage() {
        driver.get(BASE_URL);
        waitPageReady();
        waitVisible(buyHeading);
    }

    public void openMumbaiPage() {
        openHomePage();
    }

    /**
     * FIXED: Re-hover after click ensures CSS transition completes.
     */
    public void clickBuyHeading() {
        WebElement heading = waitClickable(buyHeading);
        heading.click();

        // Re-hover guarantees dropdown is fully visible + stable
        new Actions(driver)
            .moveToElement(heading)
            .perform();

        waitVisible(budgetHomesLink);
    }

    public void clickBuyMenuLink(String linkText) {
        By target = getLinkLocator(linkText);

        new Actions(driver)
            .moveToElement(waitClickable(buyHeading))
            .perform();

        WebElement link = waitVisible(target);
        jsScrollIntoView(link);
        wait.until(ExpectedConditions.elementToBeClickable(target));
        staleProofClick(target);
    }

    private By getLinkLocator(String linkText) {
        switch (linkText) {
            case "Budget Homes":               return budgetHomesLink;
            case "Premium Homes":              return premiumHomesLink;
            case "Office Space in Mumbai":     return officeSpaceLink;
            case "Commercial Space in Mumbai": return commercialLink;
            case "House for sale in Mumbai":   return houseForSaleLink;
            default:
                return By.xpath("//a[contains(text(),'" + linkText + "')]");
        }
    }
}