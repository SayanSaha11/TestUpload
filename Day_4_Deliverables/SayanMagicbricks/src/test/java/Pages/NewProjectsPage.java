package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * TC07 – New Projects Mumbai page + Contact Seller form.
 * Tab flow (mirrors A_07):
 *   Tab-1 (mainTab)  → new-projects-Mumbai (direct navigation – no new tab opened by link)
 *   Tab-2 (secondTab)→ Project Detail  ← switch here after clicking first project
 *
 * NOTE: A_07 navigates directly via driver.get() – no Buy-menu tab switch before the SRP.
 */
public class NewProjectsPage extends BasePage {

    private static final String NEW_PROJECTS_URL =
            "https://www.magicbricks.com/new-projects-Mumbai";

    // ── SRP locators ──────────────────────────────────────────────────
    private final By projectCards    = By.xpath("//div[@class=\"mghome__prjblk__txtsec \"]");

    // ── Detail page locators ──────────────────────────────────────────
    private final By contactSellerBtn = By.xpath("//span[text()=\"Contact Seller\"][1]");

    // ── Contact form locators (exact from A_07) ───────────────────────
    private final By nameField       = By.xpath("(//input[@id=\"userName\"])[1]");
    private final By emailField      = By.xpath("(//input[@id=\"userEmail\"])[1]");
    private final By mobileField     = By.xpath("(//input[@id='userMobile'])[1]");
    private final By getContactBtn   = By.xpath("//button[text()='Get Contact Details']");
    private final By submitBtn       = By.xpath("(//button[@class='contact-form__btn'])[1]");

    // ── Actions ───────────────────────────────────────────────────────

    public void openNewProjectsPage() throws InterruptedException {
        driver.get(NEW_PROJECTS_URL);
        Thread.sleep(5000);   // A_07 uses Thread.sleep(5000)
    }

    /** Prints details of first 3 projects – mirrors A_07 exactly. */
    public void printFirst3ProjectDetails() {
        List<WebElement> properties = driver.findElements(projectCards);
        System.out.println("\nPROPERTY 1 DETAILS :--\n");
        System.out.println(properties.get(0).getText());
        System.out.println("\nPROPERTY 2 DETAILS :--\n");
        System.out.println(properties.get(1).getText());
        System.out.println("\nPROPERTY 3 DETAILS :--\n");
        System.out.println(properties.get(2).getText());
    }

    /** Clicks the first new project. Returns the handle BEFORE the click (mainTab). */
    public void clickFirstProject() throws InterruptedException {
        List<WebElement> properties = driver.findElements(projectCards);
        properties.get(0).click();
        Thread.sleep(2000);
    }

    public void clickContactSeller() throws InterruptedException {
        Thread.sleep(5000);   // A_07 uses Thread.sleep(5000) before clicking
        driver.findElement(contactSellerBtn).click();
        Thread.sleep(2000);
    }

    /**
     * Fills the enquiry form.
     * Mirrors the exact field-interaction order in A_07:
     *   clear name → sendKeys name → click name to trigger blur → sendKeys email
     *   → click name again → sendKeys mobile
     */
    public void fillContactForm(String name, String email, String mobile)
            throws InterruptedException {
        WebElement nf = driver.findElement(nameField);
        Thread.sleep(2000);
        nf.clear();
        nf.sendKeys(name);

        WebElement ef = driver.findElement(emailField);
        nf.click();            // trigger blur/validation on name field
        Thread.sleep(2000);
        ef.sendKeys(email);

        WebElement mf = driver.findElement(mobileField);
        nf.click();            // trigger blur/validation on email field
        Thread.sleep(2000);
        mf.sendKeys(mobile);
        Thread.sleep(2000);
    }

    public void clickGetContactDetails() throws InterruptedException {
        driver.findElement(getContactBtn).click();
        Thread.sleep(50000);   // A_07 comment: "don't use explicit wait here, keep as-is"
    }

    public void clickContactSubmitButton() throws InterruptedException {
        driver.findElement(submitBtn).click();
    }

    public void printContactedConfirmation() {
        System.out.println("Contacted");
    }
}