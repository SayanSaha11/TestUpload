//package Pages;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebElement;
//
//import java.util.List;
//
///**
// * BudgetHomesPage – search results + property detail + EMI Calculator (TC04).
// *
// * TC04 FIX – StaleElementReferenceException on clickApplyLoan()
// * ─────────────────────────────────────────────────────────────
// * Root cause:
// *   After switching to the property detail tab, MagicBricks executes JS
// *   analytics, lazy-loads widgets, and rebuilds the CTA section. The element
// *   reference captured by waitClickable(applyLoanBtn) becomes stale before
// *   .click() fires because the CTA DOM node was replaced mid-render.
// *
// * Fix applied in clickApplyLoan():
// *   1. waitPageReady()     – waits for document.readyState == "complete"
// *                            so all JS rendering is finished first.
// *   2. jsScrollIntoView()  – scrolls the button into viewport centre
// *                            (it is often below the fold).
// *   3. staleProofClick()   – re-fetches + clicks with up to 3 retries on
// *                            StaleElementReferenceException (500 ms pause
// *                            between retries for DOM to settle).
// */
//public class BudgetHomesPage extends BasePage {
//
//    // ─── Locators ────────────────────────────────────────────────────────────
//    private final By propertyCardList  = By.xpath("//div[@class='mb-srp__list']");
//    private final By cardPriceAmount   = By.xpath(".//div[@class='mb-srp__card__price--amount']");
//    private final By applyLoanBtn      = By.xpath("//a[@class='mb-ldp__dtls__applyLoan'][1]");
//    private final By loanAmountInput   = By.xpath("//input[@id='amountRequiredEmiCal']");
//    private final By interestRateInput = By.xpath("//input[@id='interestRateEmiCal']");
//    private final By notFinalizedRadio = By.xpath("//label[@for='emiPropFinalizedNo']");
//    private final By calculateEmiBtn   = By.xpath("//a[@id='submitbuttonEmiCalid']");
//    private final By emiResultBox      = By.xpath("//div[@class='hl__calc__rt__box']");
//
//    // ─── Constructor ─────────────────────────────────────────────────────────
//    public BudgetHomesPage() {
//        super();
//    }
//
//    // ─── Page actions ────────────────────────────────────────────────────────
//
//    /**
//     * Returns all property listing containers.
//     * Waits until all cards are visible before returning.
//     */
//    public List<WebElement> getPropertyList() {
//        return waitAllVisible(propertyCardList);
//    }
//
//    /**
//     * Prints the price text of the first 5 Budget Home listing cards.
//     * Each price is read via a relative XPath from its parent container.
//     */
//    public void printFirst5Prices() {
//        List<WebElement> cards = getPropertyList();
//        for (int i = 0; i < 5; i++) {
//            String price = cards.get(i).findElement(cardPriceAmount).getText();
//            System.out.println("Price of Budget property " + (i + 1) + " : " + price);
//        }
//    }
//
//    /** Clicks the first property card to open its detail page in a new tab. */
//    public void clickFirstProperty() {
//        List<WebElement> cards = getPropertyList();
//        waitForClickable(cards.get(0));
//        cards.get(0).click();
//    }
//
//    /**
//     * Clicks the "Apply Loan" button on the property detail page.
//     *
//     * See class-level JavaDoc for the full explanation of the TC04 fix.
//     * Three-step approach: waitPageReady → scroll → staleProofClick.
//     */
//    public void clickApplyLoan() {
//        waitPageReady();                              // ensure DOM is stable
//        jsScrollIntoView(waitVisible(applyLoanBtn));  // bring into viewport
//        staleProofClick(applyLoanBtn);                // retry-safe click
//    }
//
//    /** Clears and fills the Loan Amount input field. */
//    public void enterLoanAmount(String amount) {
//        WebElement field = waitClickable(loanAmountInput);
//        field.clear();
//        field.sendKeys(amount);
//    }
//
//    /** Clears and fills the Interest Rate input field. */
//    public void enterInterestRate(String rate) {
//        WebElement field = waitClickable(interestRateInput);
//        field.clear();
//        field.sendKeys(rate);
//    }
//
//    /**
//     * Selects the "Property Not Finalised" radio button.
//     * Uses jsClick because radio buttons on this page occasionally reject
//     * standard Selenium clicks due to label overlap.
//     */
//    public void selectPropertyNotFinalisedRadio() {
//        jsClick(waitVisible(notFinalizedRadio));
//    }
//
//    /** Clicks the Calculate EMI button and waits for the result to appear. */
//    public void clickCalculateEmi() {
//        waitClickable(calculateEmiBtn).click();
//        waitVisible(emiResultBox); // wait for result before next step
//    }
//
//    /**
//     * Returns the full text content of the EMI result box.
//     * Called by the Then step to print and assert.
//     */
//    public String getEmiResult() {
//        return waitVisible(emiResultBox).getText();
//    }
//}

package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * BudgetHomesPage – search results + property detail + EMI Calculator (TC04).
 */
public class BudgetHomesPage extends BasePage {

    private final By propertyCardList = By.xpath("//div[@class='mb-srp__list']");
    private final By cardPriceAmount  = By.xpath(".//div[@class='mb-srp__card__price--amount']");
    private final By applyLoanBtn     = By.xpath("//a[@class='mb-ldp__dtls__applyLoan'][1]");
    private final By loanAmountInput  = By.xpath("//input[@id='amountRequiredEmiCal']");
    private final By interestRateInput= By.xpath("//input[@id='interestRateEmiCal']");
    private final By notFinalizedRadio= By.xpath("//label[@for='emiPropFinalizedNo']");
    private final By calculateEmiBtn  = By.xpath("//a[@id='submitbuttonEmiCalid']");
    private final By emiResultBox     = By.xpath("//div[@class='hl__calc__rt__box']");

    public BudgetHomesPage() {
        super();
    }

    /**
     * FIXED: Robust stale-safe list fetch for dynamic MagicBricks SRP.
     */
    public List<WebElement> getPropertyList() {
        waitPageReady();

        return wait.until(driver -> {
            try {
                List<WebElement> cards = driver.findElements(propertyCardList);
                if (cards.size() >= 5) {
                    // Quick stale check
                    for (WebElement card : cards) {
                        card.isDisplayed();
                    }
                    return cards;
                }
            } catch (org.openqa.selenium.StaleElementReferenceException ignored) {
                // retry
            }
            return null;
        });
    }

    public void printFirst5Prices() {
        List<WebElement> cards = getPropertyList();
        for (int i = 0; i < 5 && i < cards.size(); i++) {
            String price = cards.get(i).findElement(cardPriceAmount).getText();
            System.out.println("Price of Budget property " + (i + 1) + " : " + price);
        }
    }

    public void clickFirstProperty() {
        List<WebElement> cards = getPropertyList();
        waitForClickable(cards.get(0));
        cards.get(0).click();
    }

    public void clickApplyLoan() {
        waitPageReady();
        jsScrollIntoView(waitVisible(applyLoanBtn));
        staleProofClick(applyLoanBtn);
    }

    public void enterLoanAmount(String amount) {
        WebElement field = waitClickable(loanAmountInput);
        field.clear();
        field.sendKeys(amount);
    }

    public void enterInterestRate(String rate) {
        WebElement field = waitClickable(interestRateInput);
        field.clear();
        field.sendKeys(rate);
    }

    public void selectPropertyNotFinalisedRadio() {
        jsClick(waitVisible(notFinalizedRadio));
    }

    public void clickCalculateEmi() {
        waitClickable(calculateEmiBtn).click();
        waitVisible(emiResultBox);
    }

    public String getEmiResult() {
        return waitVisible(emiResultBox).getText();
    }
}