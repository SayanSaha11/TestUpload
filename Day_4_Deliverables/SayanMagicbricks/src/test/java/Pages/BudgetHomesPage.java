package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * TC04 – Budget Homes search results page + EMI Calculator page.
 * Tab flow:
 *   Tab-1 (mainTab)   → MagicBricks Mumbai landing
 *   Tab-2 (newTab)    → Budget Homes SRP  ← switch here after clicking link
 *   Tab-3 (thirdTab)  → Property Detail   ← switch here after clicking first property
 *   Tab-4 (fourthTab) → EMI Calculator    ← switch here after clicking Apply Loan
 */
public class BudgetHomesPage extends BasePage {

    // ── SRP locators ──────────────────────────────────────────────────
    private final By propertyList     = By.xpath("//div[@class=\"mb-srp__list\"]");
    private final By priceAmount      = By.xpath("//div[@class=\"mb-srp__card__price--amount\"]");

    // ── EMI Calculator locators ───────────────────────────────────────
    private final By applyLoanBtn     = By.xpath("//a[@class=\"mb-ldp__dtls__applyLoan\"][1]");
    private final By loanAmountInput  = By.xpath("//input[@id=\"amountRequiredEmiCal\"]");
    private final By interestInput    = By.xpath("//input[@id=\"interestRateEmiCal\"]");
    private final By radioBtnLabel    = By.xpath("//label[@for='emiPropFinalizedNo']");
    private final By submitEmiBtn     = By.xpath("//a[@id=\"submitbuttonEmiCalid\"]");
    private final By emiResultBox     = By.xpath("//div[@class=\"hl__calc__rt__box\"]");

    // ── SRP Actions ───────────────────────────────────────────────────

    /** Prints prices of the first 5 budget properties (mirrors A_04 loop). */
    public void printFirst5Prices() throws InterruptedException {
        Thread.sleep(2000);
        List<WebElement> properties = driver.findElements(propertyList);
        for (int i = 0; i < 5; i++) {
            String price = properties.get(i)
                    .findElement(priceAmount)
                    .getText();
            System.out.println("Price of Budget property " + (i + 1) + " : " + price);
        }
    }

    /** Clicks the first property card in the SRP list. Returns handle BEFORE click. */
    public String clickFirstProperty() throws InterruptedException {
        Thread.sleep(2000);
        String handleBeforeClick = driver.getWindowHandle();
        List<WebElement> properties = driver.findElements(propertyList);
        properties.get(0).click();
        return handleBeforeClick;
    }

    // ── Property Detail Actions ───────────────────────────────────────

    /** Clicks the Apply Loan button on the property detail page. */
    public void clickApplyLoan() throws InterruptedException {
        driver.findElement(applyLoanBtn).click();
        Thread.sleep(2000);
    }

    // ── EMI Calculator Actions ────────────────────────────────────────

    public void enterLoanAmount(String amount) throws InterruptedException {
        WebElement amt = driver.findElement(loanAmountInput);
        amt.clear();
        amt.sendKeys(amount);
    }

    public void enterInterestRate(String rate) throws InterruptedException {
        WebElement rate_ = driver.findElement(interestInput);
        rate_.clear();
        rate_.sendKeys(rate);
        Thread.sleep(2000);
    }

    /** Uses JS click to select the radio button – mirrors A_04 exactly. */
    public void selectPropertyNotFinalisedRadio() throws InterruptedException {
        WebElement radio = driver.findElement(radioBtnLabel);
        jsClick(radio);
        Thread.sleep(2000);
    }

    public void clickCalculateEmi() throws InterruptedException {
        driver.findElement(submitEmiBtn).click();
        Thread.sleep(2000);
    }

    public void printEmiResult() {
        String emiDetails = driver.findElement(emiResultBox).getText();
        System.out.println(emiDetails);
    }
}