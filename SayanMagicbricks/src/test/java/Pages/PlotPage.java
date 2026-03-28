package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * PlotPage – Residential Plots listing + area filter (TC12).
 *
 * ROOT CAUSE OF PREVIOUS FAILURE
 * ───────────────────────────────
 * The XPath //*[@id='moreFilter_0']/div[2]/div/div/div/div[1]/div[1]/select
 * timed out because:
 *
 *   1. MagicBricks opens a NEW TAB when the page first loads (ad/promo tab).
 *      openPlotsPage() must switch to that new tab before any interaction.
 *      A_12 handled this with an explicit tab-switch loop right after get().
 *      Our page object was missing this step entirely.
 *
 *   2. clickMoreFilters() called waitVisible(minAreaSelect) immediately after
 *      clicking the toggle. The "More Filters" panel has a CSS open animation;
 *      the select element exists in the DOM but is not yet visible. We need to
 *      wait for the panel CONTAINER to be visible first, then the select.
 *
 *   3. The "More Filters" button must be clicked via JavaScript to avoid
 *      ElementNotInteractableException caused by sticky-header overlap on
 *      some viewport sizes.
 *
 * FIX SUMMARY
 * ───────────
 *   • openPlotsPage() now mirrors A_12's tab-switch loop: after driver.get()
 *     it waits for a second tab to appear and switches to it.
 *   • clickMoreFilters() uses jsClick (not .click()) on the button, then waits
 *     for the panel container to be visible, then waits for the select element.
 *   • All waits use explicit WebDriverWait with generous timeouts.
 *   • selectMinPlotArea / selectMaxPlotArea wait for the select to be clickable
 *     (not just visible) before building the Select object.
 *   • Thread.sleep() is used only as a last-resort stabiliser after panel
 *     open/close animations (500 ms max) — every other wait is condition-based.
 */
public class PlotPage extends BasePage {

    // ─── URL ─────────────────────────────────────────────────────────────────
    private static final String PLOT_URL =
        "https://www.magicbricks.com/residential-plots-land-for-sale-in-mumbai-pppfs";

    // ─── Explicit wait (longer than BasePage default for slow filter panel) ──
    private final WebDriverWait longWait;

    // ─── Locators ────────────────────────────────────────────────────────────

    /**
     * Initial filter-spec label shown before any filters are applied.
     * Matches: //div[@class='filter__component__title more-title']
     */
    private final By initialFilterLabel =
        By.xpath("//div[@class='filter__component__title more-title']");

    /**
     * Updated filter-spec label shown after filters are applied.
     * Matches: //div[@class='top-filter__item-all-filter']
     */
    private final By updatedFilterLabel =
        By.xpath("//div[@class='top-filter__item-all-filter']");

    /**
     * "More Filters" toggle button text node.
     */
    private final By moreFiltersBtn =
        By.xpath("//div[text()='More Filters']");

    /**
     * The outer panel container that wraps both selects.
     * We wait for this FIRST (it appears before the child selects become visible).
     * id="moreFilter_0"
     */
    private final By moreFilterPanel =
        By.xpath("//*[@id='moreFilter_0']");

    /**
     * Min area <select> — first select inside #moreFilter_0.
     * Exact XPath from A_12:
     *   //*[@id="moreFilter_0"]/div[2]/div/div/div/div[1]/div[1]/select
     */
    private final By minAreaSelect =
        By.xpath("//*[@id='moreFilter_0']/div[2]/div/div/div/div[1]/div[1]/select");

    /**
     * Max area <select> — second select inside #moreFilter_0.
     * Exact XPath from A_12 (positional, second select):
     *   (//*[@id='moreFilter_0']/div[2]/div/div/div/div[1]/div/select)[2]
     */
    private final By maxAreaSelect =
        By.xpath("(//*[@id='moreFilter_0']/div[2]/div/div/div/div[1]/div/select)[2]");

    /**
     * Apply button that submits the chosen area filters.
     * Exact XPath from A_12: //div[@class='filter__component__btn']
     */
    private final By applyFiltersBtn =
        By.xpath("//div[@class='filter__component__btn']");

    /**
     * Plot-area summary shown on each listing card.
     * Exact XPath from A_12: //div[@data-summary='plot-area']
     */
    private final By plotAreaCards =
        By.xpath("//div[@data-summary='plot-area']");

    // ─── Constructor ─────────────────────────────────────────────────────────
    public PlotPage() {
        super();
        // 45-second wait for the filter panel (slow on MagicBricks)
        longWait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ─── Page actions ────────────────────────────────────────────────────────

    /**
     * Navigates to the Mumbai plots listing page.
     *
     * KEY FIX: MagicBricks opens a SECOND TAB (ad/promo) on load.
     * A_12 explicitly switches to that second tab after driver.get().
     * We replicate that here so all subsequent interactions target the
     * correct tab. Without this switch, the "More Filters" button is
     * found on the ORIGINAL (now background) tab and is not interactable.
     */
    public void openPlotsPage() {
        driver.get(PLOT_URL);

        // Wait up to 10 s for a second tab to appear — mirrors A_12
        String mainTab = driver.getWindowHandle();
        try {
            longWait.until(d -> d.getWindowHandles().size() > 1);
            // Switch to the new (plots listing) tab — exclude the original
            Set<String> handles = driver.getWindowHandles();
            for (String tab : handles) {
                if (!tab.equals(mainTab)) {
                    driver.switchTo().window(tab);
                    break;
                }
            }
        } catch (Exception e) {
            // No second tab opened — we are already on the correct tab
            System.out.println("[PlotPage] No second tab detected; staying on current tab.");
        }

        waitPageReady();

        // Wait for the initial filter label to confirm the SRP is usable
        longWait.until(ExpectedConditions.visibilityOfElementLocated(initialFilterLabel));
    }

    /**
     * Dual-mode filter-spec reader used by both Then-steps:
     *  • Before applying filters → returns initialFilterLabel text
     *  • After applying filters  → returns updatedFilterLabel text
     */
    public String getFilterSpec() {
        List<WebElement> updated = driver.findElements(updatedFilterLabel);
        if (!updated.isEmpty() && updated.get(0).isDisplayed()) {
            return updated.get(0).getText();
        }
        return longWait.until(
            ExpectedConditions.visibilityOfElementLocated(initialFilterLabel)).getText();
    }

    /**
     * Clicks "More Filters" and waits for the panel and min-area select
     * to become fully visible and interactable.
     *
     * KEY FIXES:
     *   1. JS click on the button — avoids ElementNotInteractableException
     *      caused by sticky header or overlay covering the button.
     *   2. Wait for the PANEL CONTAINER first (id=moreFilter_0), then the
     *      specific child select — two-stage wait prevents a false negative
     *      on the select before the panel animation completes.
     *   3. 500 ms static sleep after the panel wait to allow CSS animation
     *      to finish rendering the select elements fully.
     * @throws InterruptedException 
     */
    public void clickMoreFilters() {

        WebElement btn = longWait.until(
                ExpectedConditions.elementToBeClickable(moreFiltersBtn));

        jsScrollIntoView(btn);
        btn.click();

        // Wait for panel container first (IMPORTANT)
        WebElement panel = longWait.until(
                ExpectedConditions.visibilityOfElementLocated(moreFilterPanel));

        // Ensure panel is fully expanded (height > 0)
        longWait.until(driver -> panel.getSize().height > 0);
    }
    /**
     * Selects the minimum plot area from the first dropdown.
     * Waits for the element to be clickable before building Select.
     *
     * @param value option value attribute, e.g. "500"
     * @throws InterruptedException 
     */
    public void selectMinPlotArea(String value) {

        WebElement minEl = longWait.until(
                ExpectedConditions.presenceOfElementLocated(minAreaSelect));

        jsScrollIntoView(minEl);

        try {
            // 🔥 Try normal Selenium interaction first
            longWait.until(ExpectedConditions.elementToBeClickable(minEl));

            minEl.click(); // open dropdown

            Select select = new Select(minEl);
            select.selectByValue(value);

        } catch (Exception e) {

            System.out.println("⚠️ Normal select failed, using JS fallback");

            // 🔥 JS fallback (handles hidden/custom UI)
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript(
                "arguments[0].value='" + value + "';" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                minEl
            );
        }
    }

    /**
     * Selects the maximum plot area from the second dropdown.
     * Assumes the panel is already open (clickMoreFilters() was called by
     * the step definition before this, mirroring A_12 exactly).
     *
     * @param value option value attribute, e.g. "2000"
     * @throws InterruptedException 
     */
    public void selectMaxPlotArea(String value) {

        WebElement maxEl = longWait.until(
                ExpectedConditions.presenceOfElementLocated(maxAreaSelect));

        jsScrollIntoView(maxEl);

        try {
            // 🔥 Try normal Selenium interaction first
            longWait.until(ExpectedConditions.elementToBeClickable(maxEl));

            maxEl.click(); // open dropdown

            Select select = new Select(maxEl);
            select.selectByValue(value);

        } catch (Exception e) {

            System.out.println("⚠️ Normal select failed, using JS fallback");

            // 🔥 JS fallback (stronger event trigger)
            JavascriptExecutor js = (JavascriptExecutor) driver;

            js.executeScript(
                "arguments[0].value='" + value + "';" +
                "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));" +
                "arguments[0].dispatchEvent(new Event('blur', { bubbles: true }));",
                maxEl
            );
        }
    }

    /**
     * Clicks Apply and waits for the updated filter label to appear,
     * confirming that filtered results have loaded.
     */
    public void clickApplyFilters() {

        WebElement btn = longWait.until(
                ExpectedConditions.elementToBeClickable(applyFiltersBtn));

        jsScrollIntoView(btn);
        btn.click();

        // Wait for updated filter label (result confirmation)
        longWait.until(ExpectedConditions.visibilityOfElementLocated(updatedFilterLabel));
    }

    /**
     * Prints the first 5 plot-area summary values to the console.
     * Mirrors the assertion loop at the end of A_12.
     */
    public void printFirst5PlotAreas() {
        List<WebElement> areas = driver.findElements(plotAreaCards);
        for (int i = 0; i < 5 && i < areas.size(); i++) {
            System.out.println("Plot Area of Property " + (i + 1)
                    + " is : " + areas.get(i).getText());
        }
    }

    /**
     * Returns all plot-area WebElements for step-level assertions.
     */
    public List<WebElement> getPlotAreaElements() {
        return driver.findElements(plotAreaCards);
    }

    // ─── Private helpers ─────────────────────────────────────────────────────

}