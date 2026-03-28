//package Pages;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.StaleElementReferenceException;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import utils.DriverManager;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
///**
// * BasePage – foundation for all Page Object classes.
// *
// * KEY DESIGN DECISIONS
// * ────────────────────
// * 1. No-arg constructor: driver is retrieved from DriverManager.getDriver()
// *    so every subclass can be constructed with new PageClass() — no WebDriver
// *    parameter needed anywhere.
// *
// * 2. Two WebDriverWait instances:
// *    - wait     (30 s) : standard interactions
// *    - longWait (60 s) : slow API/network responses (e.g. top-match panel)
// *
// * 3. waitPageReady(): polls document.readyState == "complete" after every
// *    tab switch to let JS-heavy pages finish rendering before the next step.
// *
// * 4. staleProofClick(): re-fetches and retries a click up to 3 times on
// *    StaleElementReferenceException (500 ms DOM-settle between each retry).
// *    This is the root-cause fix for TC04 and TC14.
// *
// * 5. jsScrollIntoView(): scrolls an element to the viewport centre before
// *    interaction. Fixes ElementNotInteractableException for below-fold or
// *    CSS-transitioning elements (TC10, TC12, TC14).
// *
// * 6. switchToNewTab() / switchToThirdTab() / switchToFourthTab():
// *    poll for the expected handle count before switching, then call
// *    waitPageReady() to ensure the new page is fully loaded.
// */
//public class BasePage {
//
//    // Protected fields accessible to all subclasses
//    protected final WebDriver     driver;
//    protected final WebDriverWait wait;
//    protected final WebDriverWait longWait;
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Constructor
//    // ─────────────────────────────────────────────────────────────────────────
//
//    public BasePage() {
//        this.driver   = DriverManager.getDriver();
//        this.wait     = new WebDriverWait(driver, Duration.ofSeconds(30));
//        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(60));
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Page-load guard
//    // ─────────────────────────────────────────────────────────────────────────
//
//    /**
//     * Waits until document.readyState == "complete".
//     * Always call this after switching to a new tab or triggering a full-page
//     * navigation so that lazy-loaded JS components finish building the DOM
//     * before the next interaction.
//     */
//    protected void waitPageReady() {
//        wait.until(driver -> ((JavascriptExecutor) driver)
//                .executeScript("return document.readyState").equals("complete"));
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // JavaScript helpers
//    // ─────────────────────────────────────────────────────────────────────────
//
//    /** Clicks element via JavaScript – bypasses CSS interactability checks. */
//    protected void jsClick(WebElement element) {
//        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
//    }
//
//    /**
//     * Scrolls an element to the centre of the visible viewport.
//     * Fixes "element not interactable" for buttons below the fold or
//     * partially hidden by a sticky header/overlay.
//     */
//    protected void jsScrollIntoView(WebElement element) {
//        ((JavascriptExecutor) driver).executeScript(
//            "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
//    }
//
//    /** Scrolls the window by (x, y) pixels. */
//    public void scrollBy(int x, int y) {
//        ((JavascriptExecutor) driver)
//            .executeScript("window.scrollBy(" + x + ", " + y + ");");
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Explicit-wait wrappers
//    // ─────────────────────────────────────────────────────────────────────────
//
//    /** Waits up to 30 s for the element to be visible, then returns it. */
//    protected WebElement waitVisible(By locator) {
//        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
//    }
//
//    /** Waits up to 30 s for the element to be clickable, then returns it. */
//    protected WebElement waitClickable(By locator) {
//        return wait.until(ExpectedConditions.elementToBeClickable(locator));
//    }
//
//    /** Waits up to 60 s for the element to be visible (slow API responses). */
//    protected WebElement longWaitVisible(By locator) {
//        return longWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
//    }
//
//    /** Waits up to 60 s for the element to be clickable (slow API responses). */
//    protected WebElement longWaitClickable(By locator) {
//        return longWait.until(ExpectedConditions.elementToBeClickable(locator));
//    }
//
//    /** Waits until ALL matching elements are visible, then returns the list. */
//    protected List<WebElement> waitAllVisible(By locator) {
//        return wait.until(
//            ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
//    }
//
//    /** Waits until matching elements are present in the DOM (not necessarily visible). */
//    protected List<WebElement> waitPresence(By locator) {
//        return wait.until(
//            ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
//    }
//
//    /** Waits for an already-fetched WebElement to be visible. */
//    protected void waitForVisibility(WebElement element) {
//        wait.until(ExpectedConditions.visibilityOf(element));
//    }
//
//    /** Waits for an already-fetched WebElement to be clickable. */
//    protected void waitForClickable(WebElement element) {
//        wait.until(ExpectedConditions.elementToBeClickable(element));
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Stale-element safe click
//    // ─────────────────────────────────────────────────────────────────────────
//
//    /**
//     * Clicks a locator with automatic retry on StaleElementReferenceException.
//     *
//     * Root cause this solves (TC04, TC14):
//     *   After switching to a new tab, MagicBricks continues executing JS that
//     *   rebuilds parts of the DOM (lazy widgets, analytics injections).
//     *   A WebElement captured by waitClickable() becomes stale before .click()
//     *   fires. Re-fetching the element from the DOM on each retry is the
//     *   correct fix — no Thread.sleep delays needed.
//     *
//     * Strategy:
//     *   Attempt 1-3 : waitClickable(locator).click()
//     *   On each StaleElementReferenceException : pause 500 ms → retry
//     *   If still stale after 3 attempts : re-throw the exception
//     */
//    protected void staleProofClick(By locator) {
//        staleProofClick(locator, 3);
//    }
//
//    protected void staleProofClick(By locator, int maxAttempts) {
//        StaleElementReferenceException lastException = null;
//        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
//            try {
//                waitClickable(locator).click();
//                return; // success
//            } catch (StaleElementReferenceException ex) {
//                lastException = ex;
//                // 500 ms DOM-settle pause before re-fetching the element
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException ie) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//        }
//        throw new StaleElementReferenceException(
//            "Element still stale after " + maxAttempts + " retries: " + locator,
//            lastException);
//    }
//
//    // ─────────────────────────────────────────────────────────────────────────
//    // Tab-switching helpers
//    // ─────────────────────────────────────────────────────────────────────────
//
//    /**
//     * Polls until a window handle appears that is NOT in knownHandles,
//     * then switches to it and waits for the new page to be fully loaded.
//     *
//     * Using a poll-based wait (instead of Thread.sleep) means we switch
//     * as soon as the tab is ready — fast on fast machines, patient on slow ones.
//     *
//     * @param knownHandles one or more handles to exclude
//     */
//    public void switchToNewTab(String... knownHandles) {
//        Set<String> known = new HashSet<>(Arrays.asList(knownHandles));
//        // Poll until the total handle count exceeds the number of known handles
//        new WebDriverWait(driver, Duration.ofSeconds(20))
//            .until(d -> d.getWindowHandles().size() > known.size());
//        for (String tab : driver.getWindowHandles()) {
//            if (!known.contains(tab)) {
//                driver.switchTo().window(tab);
//                waitPageReady(); // ensure the new page finishes loading
//                return;
//            }
//        }
//    }
//
//    /**
//     * Switches to the only tab that is not the single known handle.
//     * Used after any Buy-menu click that opens a new tab.
//     */
//    public void switchToNewTabExcluding(String knownHandle) {
//        switchToNewTab(knownHandle);
//    }
//
//    /**
//     * Switches to the tab that is neither mainTab nor secondTab.
//     * Used for property detail pages (third window in the session).
//     */
//    public void switchToThirdTab(String mainTab, String secondTab) {
//        switchToNewTab(mainTab, secondTab);
//    }
//
//    /**
//     * Switches to the tab that is none of the three known handles.
//     * Used for nested navigation (TC04: loan calculator; TC10: locality map).
//     */
//    public void switchToFourthTab(String mainTab, String secondTab, String thirdTab) {
//        switchToNewTab(mainTab, secondTab, thirdTab);
//    }
//
//    /** Returns the handle of the currently focused window. */
//    public String currentHandle() {
//        return driver.getWindowHandle();
//    }
//
//    /** Returns all open window handles as a List (order preserved). */
//    protected List<String> getAllWindowHandles() {
//        return new ArrayList<>(driver.getWindowHandles());
//    }
//
//    /** Performs an Actions hover over the element at the given locator. */
//    protected void hoverElement(By locator) {
//        new Actions(driver).moveToElement(waitVisible(locator)).perform();
//    }
//    
//    protected void waitForPageStable() {
//        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
//            webDriver -> ((JavascriptExecutor) webDriver)
//                .executeScript("return document.readyState").equals("complete"));
//    }
//}

package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.DriverManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * BasePage – Core foundation with strong anti-stale protection for MagicBricks.
 */
public class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;
    protected final WebDriverWait longWait;

    public BasePage() {
        this.driver = DriverManager.getDriver();
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        this.longWait = new WebDriverWait(driver, Duration.ofSeconds(60));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Page Load Stability
    // ─────────────────────────────────────────────────────────────────────────
    public void waitPageReady() {
        wait.until(d -> ((JavascriptExecutor) d)
                .executeScript("return document.readyState").equals("complete"));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // JavaScript Helpers
    // ─────────────────────────────────────────────────────────────────────────
    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    protected void jsScrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center', inline:'nearest'});", element);
    }

    public void scrollBy(int x, int y) {
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(" + x + ", " + y + ");");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Explicit Wait Wrappers
    // ─────────────────────────────────────────────────────────────────────────
    protected WebElement waitVisible(By locator) {
        return staleSafeVisible(locator);
    }

    protected WebElement waitClickable(By locator) {
        return staleSafeClickable(locator);
    }

    protected WebElement longWaitVisible(By locator) {
        return longWait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement longWaitClickable(By locator) {
        return longWait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected List<WebElement> waitAllVisible(By locator) {
        return waitAllVisibleStaleSafe(locator);
    }

    protected List<WebElement> waitPresence(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Stale-Safe Methods (Core Fix)
    // ─────────────────────────────────────────────────────────────────────────
    protected WebElement staleSafeVisible(By locator) {
        return wait.until(driver -> {
            for (int i = 0; i < 3; i++) {
                try {
                    WebElement el = driver.findElement(locator);
                    if (el.isDisplayed()) return el;
                } catch (StaleElementReferenceException e) {
                    if (i == 2) throw e;
                    sleep(400);
                }
            }
            return null;
        });
    }

    protected WebElement staleSafeClickable(By locator) {
        return wait.until(driver -> {
            for (int i = 0; i < 3; i++) {
                try {
                    return ExpectedConditions.elementToBeClickable(locator).apply(driver);
                } catch (StaleElementReferenceException e) {
                    if (i == 2) throw e;
                    sleep(400);
                }
            }
            return null;
        });
    }

    protected List<WebElement> waitAllVisibleStaleSafe(By locator) {
        return wait.until(driver -> {
            try {
                List<WebElement> elements = driver.findElements(locator);
                if (elements.isEmpty()) return null;
                // Quick validation to trigger stale check
                for (int i = 0; i < Math.min(3, elements.size()); i++) {
                    elements.get(i).isDisplayed();
                }
                return elements;
            } catch (StaleElementReferenceException e) {
                return null; // retry
            }
        });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Stale Proof Click
    // ─────────────────────────────────────────────────────────────────────────
    protected void staleProofClick(By locator) {
        staleProofClick(locator, 3);
    }

    protected void staleProofClick(By locator, int maxAttempts) {
        StaleElementReferenceException lastEx = null;
        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                waitClickable(locator).click();
                return;
            } catch (StaleElementReferenceException ex) {
                lastEx = ex;
                sleep(500);
            }
        }
        throw new StaleElementReferenceException(
                "Element still stale after " + maxAttempts + " retries: " + locator, lastEx);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Tab Switching
    // ─────────────────────────────────────────────────────────────────────────
    public void switchToNewTab(String... knownHandles) {
        Set<String> known = new HashSet<>(Arrays.asList(knownHandles));
        new WebDriverWait(driver, Duration.ofSeconds(20))
                .until(d -> d.getWindowHandles().size() > known.size());

        for (String tab : driver.getWindowHandles()) {
            if (!known.contains(tab)) {
                driver.switchTo().window(tab);
                waitPageReady();
                return;
            }
        }
    }

    public void switchToNewTabExcluding(String knownHandle) {
        switchToNewTab(knownHandle);
    }

    public void switchToThirdTab(String mainTab, String secondTab) {
        switchToNewTab(mainTab, secondTab);
    }

    public void switchToFourthTab(String mainTab, String secondTab, String thirdTab) {
        switchToNewTab(mainTab, secondTab, thirdTab);
    }

    public String currentHandle() {
        return driver.getWindowHandle();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Utility
    // ─────────────────────────────────────────────────────────────────────────
    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    protected void hoverElement(By locator) {
        new Actions(driver).moveToElement(waitVisible(locator)).perform();
    }

    protected void waitForPageStable() {
        waitPageReady();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Legacy support (to fix your current compilation errors)
    // ─────────────────────────────────────────────────────────────────────────
    /**
     * Legacy method to support existing calls like waitForClickable(element)
     * in BudgetHomesPage and PremiumHomesPage.
     */
    protected void waitForClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    protected void waitForVisibility(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }
}