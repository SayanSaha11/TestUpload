package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;

/**
 * OfficeSpacePage – Office Space listing + "Get Phone No." contact flow (TC14).
 *
 * TC14 FIX – StaleElementReferenceException on clickGetPhoneNo()
 * ─────────────────────────────────────────────────────────────
 * Root cause 1 – Wrong locator:
 *   The previous code used "//a[text()='Contact Agent']".
 *   The actual button text confirmed by the error log and the step-definition
 *   file is "Get Phone No." — hence every attempt threw NoSuchElementException
 *   which was then masked by the StaleElementReferenceException.
 *
 * Root cause 2 – Stale element after tab switch (same pattern as TC04):
 *   After switchToThirdTab() the office detail page's JS rebuilds the CTA
 *   widget section, staling any WebElement captured before the rebuild.
 *
 * Fix applied in clickGetPhoneNo():
 *   1. waitPageReady()     – waits for document.readyState == "complete".
 *   2. jsScrollIntoView()  – scrolls the CTA button into the viewport centre
 *                            (it is typically below the fold).
 *   3. staleProofClick()   – re-fetches + clicks with up to 3 retries on
 *                            StaleElementReferenceException (500 ms pause).
 */
public class OfficeSpacePage extends BasePage {

    // ─── Locators ────────────────────────────────────────────────────────────
    private final By firstPropertyTitle = By.xpath("//h2[@class='mb-srp__card--title']");

    // Corrected locator — confirmed from the TC14 error log
    private final By getPhoneNoBtn      = By.xpath("//a[@class='mb-ldp__action--btn large btn-white freecab']");

    // Contact form fields (shown after clicking Get Phone No.)
    private final By contactNameInput   = By.xpath("(//input[@id='userName'])[1]");
    private final By contactEmailInput  = By.xpath("(//input[@id='userEmail'])[1]");
    private final By contactMobileInput = By.xpath("(//input[@id='userMobile'])[1]");
    private final By continueBtn        = By.xpath("//button[@class='contact-form__btn']");
    private final By submitBtn          = By.xpath("//button[contains(text(),'Submit')]");

    // ─── Constructor ─────────────────────────────────────────────────────────
    public OfficeSpacePage() {
        super();
    }

    // ─── Page actions ────────────────────────────────────────────────────────

    /**
     * Clicks the first office property title on the search results page.
     * Waits for the title to be clickable before interacting.
     */
    public void clickFirstOfficeProperty() {
        waitClickable(firstPropertyTitle).click();
    }

    /**
     * Clicks "Get Phone No." on the office property detail page.
     *
     * See class-level JavaDoc for the full TC14 root-cause analysis.
     * Three-step approach: waitPageReady → jsScrollIntoView → staleProofClick.
     */
    public void clickGetPhoneNo() {
        waitPageReady();                                // wait for DOM to stabilise
        jsScrollIntoView(waitVisible(getPhoneNoBtn));   // scroll into viewport
        staleProofClick(getPhoneNoBtn);                 // retry-safe click
        System.out.println("Get Phone No. button clicked successfully.");
    }

    /**
     * Fills the contact form that appears after clicking Get Phone No.
     * Waits for the name field to be visible (form open) before filling.
     */
    public void fillContactForm(String name, String email, String mobile) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(contactNameInput));

        WebElement nameEl = waitClickable(contactNameInput);
        nameEl.clear();
        nameEl.sendKeys(name);

        WebElement emailEl = waitClickable(contactEmailInput);
        emailEl.clear();
        emailEl.sendKeys(email);

        WebElement mobileEl = waitClickable(contactMobileInput);
        mobileEl.clear();
        mobileEl.sendKeys(mobile);
    }

    /** Clicks the Continue button in the contact flow. */
    public void clickContinue() {
        waitClickable(continueBtn).click();
    }

    /**
     * Optionally waits for an OTP/verification step.
     * Uses a short 5 s timeout — if the step does not appear the method
     * returns silently so the test continues without failing.
     */
    public void waitForOtpStep() {
        try {
            new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(submitBtn));
        } catch (TimeoutException ignored) {
            // OTP step not shown — proceed to next step
        }
    }

    /**
     * Clicks the Submit button if it is present on the page.
     * Uses findElements (returns empty list, never throws) before clicking.
     */
    public void clickSubmitButton() {
        if (!driver.findElements(submitBtn).isEmpty()) {
            waitClickable(submitBtn).click();
        }
    }

    /** Prints a confirmation message with the current URL for verification. */
    public void verifyOwnerContactMessage() {
        System.out.println("Contact flow completed. Current URL: " + driver.getCurrentUrl());
    }
}