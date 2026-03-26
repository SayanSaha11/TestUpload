package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

/**
 * TC14 – Office Space in Mumbai SRP + Property Detail + Contact Agent form.
 * Tab flow (mirrors A_14):
 *   Tab-1 (mainTab)   → MagicBricks Mumbai landing
 *   Tab-2 (secondTab) → Office SRP       ← switch here after clicking link
 *   Tab-3 (thirdTab)  → Property Detail  ← switch here after clicking first property
 */
public class OfficeSpacePage extends BasePage {

    // ── SRP locators ──────────────────────────────────────────────────
    private final By firstPropertyTitle = By.xpath("//h2[@class='mb-srp__card--title']");

    // ── Detail page locators (A_14 exact xpaths) ─────────────────────
    private final By getPhoneBtn    = By.xpath("//a[text()='Get Phone No.']");

    // ── Contact form locators (same as A_07 / A_14) ──────────────────
    private final By nameField      = By.xpath("(//input[@id=\"userName\"])[1]");
    private final By emailField     = By.xpath("(//input[@id=\"userEmail\"])[1]");
    private final By mobileField    = By.xpath("(//input[@id='userMobile'])[1]");
    private final By continueBtn    = By.xpath("//button[text()='Continue']");
    private final By submitBtn      = By.xpath("//button[@class='contact-form__btn']");
    private final By confirmationMsg = By.xpath("//div[@class='cont_sec_am']");

    // ── Actions ───────────────────────────────────────────────────────

    public void clickFirstOfficeProperty() throws InterruptedException {
        driver.findElement(firstPropertyTitle).click();
    }

    /**
     * Clicks "Get Phone No." using explicit wait – mirrors A_14.
     */
    public void clickGetPhoneNo() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.elementToBeClickable(getPhoneBtn)).click();
        Thread.sleep(5000);
    }

    /**
     * Fills the contact form – mirrors A_14 field-interaction order exactly:
     *   clear name → sendKeys name → click name → sendKeys email
     *   → click name → sendKeys mobile
     */
    public void fillContactForm(String name, String email, String mobile)
            throws InterruptedException {
        WebElement nf = driver.findElement(nameField);
        Thread.sleep(2000);
        nf.clear();
        nf.sendKeys(name);

        WebElement ef = driver.findElement(emailField);
        nf.click();
        Thread.sleep(2000);
        ef.sendKeys(email);

        WebElement mf = driver.findElement(mobileField);
        nf.click();
        Thread.sleep(2000);
        mf.sendKeys(mobile);
        Thread.sleep(2000);
    }

    public void clickContinue() throws InterruptedException {
        driver.findElement(continueBtn).click();
    }

    /** Thread.sleep(40000) – mirrors A_14 comment "don't use explicit wait here". */
    public void waitForOtpStep() throws InterruptedException {
        Thread.sleep(40000);
    }

    public void clickSubmitButton() throws InterruptedException {
        driver.findElement(submitBtn).click();
    }

    /**
     * Verifies the confirmation message using Assert.assertEquals – mirrors A_14.
     */
    public void verifyOwnerContactMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebElement msgEl = wait.until(
                ExpectedConditions.visibilityOfElementLocated(confirmationMsg));
        String reply = msgEl.getText();
        System.out.println("Actual text: " + reply);
        Assert.assertEquals(reply, "Your request is being shared with the Owner.");
    }
}