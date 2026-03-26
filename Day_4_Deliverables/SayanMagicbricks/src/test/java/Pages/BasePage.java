package Pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.DriverManager;

import java.util.ArrayList;
import java.util.Set;

/**
 * Shared low-level helpers.
 * Tab-switching logic mirrors exactly what the original A_xx classes do:
 *   • keep a reference to the current "known" handles
 *   • iterate driver.getWindowHandles() and pick the one not yet seen
 */
public class BasePage {

    protected WebDriver driver;

    public BasePage() {
        this.driver = DriverManager.getDriver();
    }

    // ── JavaScript helpers ────────────────────────────────────────────

    public void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void scrollBy(int x, int y) {
        ((JavascriptExecutor) driver).executeScript(
                "window.scrollBy(" + x + ", " + y + ");");
    }

    // ── Tab-switching helpers (exact logic from A_xx originals) ───────

    /**
     * Switches to the FIRST tab that is not equal to mainTab.
     * Mirrors the pattern used in every A_xx class after the first new-tab opens.
     */
    public void switchToNewTabExcluding(String mainTab) throws InterruptedException {
        Thread.sleep(2000);
        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(mainTab)) {
                driver.switchTo().window(tab);
                return;
            }
        }
    }

    /**
     * Switches to the tab that is NOT mainTab AND NOT secondTab.
     * Used to reach tab #3 (property detail / loan calculator etc.).
     */
    public void switchToThirdTab(String mainTab, String secondTab) throws InterruptedException {
        Thread.sleep(2000);
        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(mainTab) && !tab.equals(secondTab)) {
                driver.switchTo().window(tab);
                return;
            }
        }
    }

    /**
     * Switches to the tab that is NOT mainTab, NOT secondTab, AND NOT thirdTab.
     * Used to reach tab #4 (loan / EMI calculator).
     */
    public void switchToFourthTab(String mainTab, String secondTab, String thirdTab)
            throws InterruptedException {
        Thread.sleep(2000);
        for (String tab : driver.getWindowHandles()) {
            if (!tab.equals(mainTab) && !tab.equals(secondTab) && !tab.equals(thirdTab)) {
                driver.switchTo().window(tab);
                return;
            }
        }
    }

    /** Returns the currently focused window handle. */
    public String currentHandle() {
        return driver.getWindowHandle();
    }
}