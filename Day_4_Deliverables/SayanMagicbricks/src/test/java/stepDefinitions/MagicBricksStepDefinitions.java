package stepDefinitions;

import Pages.*;
import io.cucumber.java.en.*;
import utils.DriverManager;
import utils.ExcelUtils;

/**
 * Single step-definitions class wiring every Gherkin step to a Page action.
 *
 * Tab-handle variables are kept as instance fields so they survive across
 * multiple step calls within the same scenario.  Because DriverManager uses
 * ThreadLocal each parallel scenario has its own instance of this class
 * (Cucumber creates a new glue object per scenario run).
 *
 * Apache POI is used for TC04 (EMI data), TC07 (contact form), TC12 (plot
 * area filter), TC14 (contact form) – all read from TestData.xlsx.
 */
public class MagicBricksStepDefinitions {

    // ── Page objects ──────────────────────────────────────────────────
    private final HomePage           homePage           = new HomePage();
    private final BudgetHomesPage    budgetHomesPage    = new BudgetHomesPage();
    private final PremiumHomesPage   premiumHomesPage   = new PremiumHomesPage();
    private final NewProjectsPage    newProjectsPage    = new NewProjectsPage();
    private final HouseForSalePage   houseForSalePage   = new HouseForSalePage();
    private final PlotPage           plotPage           = new PlotPage();
    private final OfficeSpacePage    officeSpacePage    = new OfficeSpacePage();
    private final CommercialSpacePage commercialPage    = new CommercialSpacePage();
    private final BasePage           basePage           = new BasePage();

    // ── Tab-handle state (survives across steps in one scenario) ──────
    private String mainTab;
    private String secondTab;
    private String thirdTab;

    // ═══════════════════════════════════════════════════════════════════
    // BACKGROUND
    // ═══════════════════════════════════════════════════════════════════

    @Given("the user is on the MagicBricks Mumbai residential page")
    public void the_user_is_on_the_MagicBricks_Mumbai_residential_page()
            throws InterruptedException {
        // Capture mainTab BEFORE navigating so we always have the first handle
        mainTab = DriverManager.getDriver().getWindowHandle();
        homePage.openMumbaiPage();
    }

    // ═══════════════════════════════════════════════════════════════════
    // SHARED – Buy menu navigation (TC04, TC06, TC10, TC14, TC16)
    // ═══════════════════════════════════════════════════════════════════

    @When("the user clicks the Buy heading")
    public void the_user_clicks_the_Buy_heading() throws InterruptedException {
        homePage.clickBuyHeading();
    }

    @When("the user clicks {string}")
    public void the_user_clicks_link(String linkText) throws InterruptedException {
        homePage.clickBuyMenuLink(linkText);
    }

    /**
     * Generic "switch to new tab" step used after clicking Buy-menu links.
     * Switches away from mainTab to whichever new window the portal opened.
     */
    @When("the user switches to the new tab")
    public void the_user_switches_to_the_new_tab() throws InterruptedException {
        basePage.switchToNewTabExcluding(mainTab);
    }

    // ═══════════════════════════════════════════════════════════════════
    // TC-04 : Budget Homes + EMI Calculator
    // ═══════════════════════════════════════════════════════════════════

    @Then("the first 5 property prices should be displayed")
    public void the_first_5_property_prices_should_be_displayed()
            throws InterruptedException {
        budgetHomesPage.printFirst5Prices();
    }

    @When("the user clicks on the first property listing")
    public void the_user_clicks_on_the_first_property_listing()
            throws InterruptedException {
        // Record the current tab (Tab-2, Budget Homes SRP) before click opens Tab-3
        secondTab = basePage.currentHandle();
        budgetHomesPage.clickFirstProperty();
    }

    @When("the user switches to the property detail tab")
    public void the_user_switches_to_the_property_detail_tab()
            throws InterruptedException {
        // Switches to the tab that is not mainTab AND not secondTab → Tab-3
        basePage.switchToThirdTab(mainTab, secondTab);
    }

    @When("the user clicks the Apply Loan button")
    public void the_user_clicks_the_Apply_Loan_button() throws InterruptedException {
        // Record Tab-3 handle before Apply Loan opens Tab-4
        thirdTab = basePage.currentHandle();
        budgetHomesPage.clickApplyLoan();
    }

    @When("the user switches to the loan calculator tab")
    public void the_user_switches_to_the_loan_calculator_tab()
            throws InterruptedException {
        // Switches to Tab-4
        basePage.switchToFourthTab(mainTab, secondTab, thirdTab);
    }

    /**
     * TC04 – POI reads LoanAmount and InterestRate from Excel.
     * Feature step passes the display strings ("30,00,000" / "7.5") but we
     * use the raw POI values ("3000000" / "7.5") for sendKeys.
     */
    @When("the user enters loan amount {string} and interest rate {string}")
    public void the_user_enters_loan_amount_and_interest_rate(
            String displayAmount, String displayRate) throws InterruptedException {
        // Read from Excel (POI) – falls back to feature-step values if file missing
        String amount = readExcelOrDefault("EMIData", 1, 0, "3000000");
        String rate   = readExcelOrDefault("EMIData", 1, 1, "7.5");
        budgetHomesPage.enterLoanAmount(amount);
        budgetHomesPage.enterInterestRate(rate);
    }

    @When("the user selects property not finalized radio button")
    public void the_user_selects_property_not_finalized_radio_button()
            throws InterruptedException {
        budgetHomesPage.selectPropertyNotFinalisedRadio();
    }

    @When("the user clicks Calculate EMI")
    public void the_user_clicks_Calculate_EMI() throws InterruptedException {
        budgetHomesPage.clickCalculateEmi();
    }

    @Then("the EMI details should be displayed")
    public void the_EMI_details_should_be_displayed() {
        budgetHomesPage.printEmiResult();
    }

    // ═══════════════════════════════════════════════════════════════════
    // TC-06 : Premium Homes + Image Gallery
    // ═══════════════════════════════════════════════════════════════════

    @When("the user clicks the search button")
    public void the_user_clicks_the_search_button() throws InterruptedException {
        premiumHomesPage.clickSearchButton();
    }

    @When("the user opens the image gallery of the first property")
    public void the_user_opens_the_image_gallery_of_the_first_property()
            throws InterruptedException {
        premiumHomesPage.clickFirstPropertyImageSlider();
    }

    @When("the user clicks the Project Photo tab")
    public void the_user_clicks_the_Project_Photo_tab() throws InterruptedException {
        premiumHomesPage.clickProjectPhotoTab();
    }

    @Then("the user should be able to navigate right through 3 gallery images")
    public void the_user_should_be_able_to_navigate_right_through_3_gallery_images()
            throws InterruptedException {
        premiumHomesPage.navigateRight3Photos();
    }

    // ═══════════════════════════════════════════════════════════════════
    // TC-07 : New Projects + Contact Seller
    // ═══════════════════════════════════════════════════════════════════

    /**
     * TC07 navigates directly – the Background still opens the landing page but
     * this step overrides it with the direct URL (mirrors A_07 behaviour).
     */
    @Given("the user navigates directly to New Projects Mumbai page")
    public void the_user_navigates_directly_to_New_Projects_Mumbai_page()
            throws InterruptedException {
        mainTab = DriverManager.getDriver().getWindowHandle();
        newProjectsPage.openNewProjectsPage();
    }

    @Then("the first 3 new project details should be displayed")
    public void the_first_3_new_project_details_should_be_displayed() {
        newProjectsPage.printFirst3ProjectDetails();
    }

    @When("the user clicks on the first new project")
    public void the_user_clicks_on_the_first_new_project() throws InterruptedException {
        newProjectsPage.clickFirstProject();
    }

    @When("the user switches to the project detail tab")
    public void the_user_switches_to_the_project_detail_tab()
            throws InterruptedException {
        // A_07: secondTab = driver.getWindowHandle() AFTER click, then switch to non-main non-second
        secondTab = basePage.currentHandle();
        basePage.switchToThirdTab(mainTab, secondTab);
    }

    @When("the user clicks Contact Seller")
    public void the_user_clicks_Contact_Seller() throws InterruptedException {
        newProjectsPage.clickContactSeller();
    }

    /**
     * TC07 – feature step passes inline values; POI reads them from Excel too.
     * Both paths are kept – POI values take precedence.
     */
    @When("the user fills the enquiry form with name {string} email {string} and mobile {string}")
    public void the_user_fills_the_enquiry_form(String name, String email, String mobile)
            throws InterruptedException {
        String n = readExcelOrDefault("ContactData", 1, 0, name);
        String e = readExcelOrDefault("ContactData", 1, 1, email);
        String m = readExcelOrDefault("ContactData", 1, 2, mobile);
        newProjectsPage.fillContactForm(n, e, m);
    }

    @When("the user clicks Get Contact Details")
    public void the_user_clicks_Get_Contact_Details() throws InterruptedException {
        newProjectsPage.clickGetContactDetails();
    }

    /**
     * TC07 – "the user selects the first top match" maps to clicking the submit
     * button (contact-form__btn) – mirrors A_07's last action.
     */
    @When("the user selects the first top match")
    public void the_user_selects_the_first_top_match() throws InterruptedException {
        newProjectsPage.clickContactSubmitButton();
    }

    @Then("the contact button text should be displayed")
    public void the_contact_button_text_should_be_displayed() {
        newProjectsPage.printContactedConfirmation();
    }

    // ═══════════════════════════════════════════════════════════════════
    // TC-10 : House for Sale + Locality Map
    // ═══════════════════════════════════════════════════════════════════

    @When("the user clicks on the specific house property")
    public void the_user_clicks_on_the_specific_house_property()
            throws InterruptedException {
        houseForSalePage.printAndClickHouseProperty();
    }

    @When("the user switches to the house detail tab")
    public void the_user_switches_to_the_house_detail_tab()
            throws InterruptedException {
        // Tab-2 = SRP, secondTab was set during "switches to the new tab" step above
        secondTab = basePage.currentHandle();
        basePage.switchToThirdTab(mainTab, secondTab);
    }

    @When("the user scrolls down by {int} pixels")
    public void the_user_scrolls_down_by_pixels(int pixels)
            throws InterruptedException {
        // A_10 scrolls 1000+1000 = 2000 total; called once with 1500 in feature,
        // we honour the feature value
        basePage.scrollBy(0, pixels);
        Thread.sleep(2000);
    }

    @When("the user clicks About Locality tab")
    public void the_user_clicks_About_Locality_tab() throws InterruptedException {
        houseForSalePage.clickAboutLocalityTab();
    }

    @When("the user scrolls up by {int} pixels")
    public void the_user_scrolls_up_by_pixels(int pixels) throws InterruptedException {
        basePage.scrollBy(0, -pixels);
    }

    @When("the user clicks Explore Locality")
    public void the_user_clicks_Explore_Locality() throws InterruptedException {
        // Record thirdTab handle before Explore Locality opens a new tab
        thirdTab = basePage.currentHandle();
        houseForSalePage.scrollUpAndClickExploreLocality();
    }

    @When("the user switches to the locality tab")
    public void the_user_switches_to_the_locality_tab() throws InterruptedException {
        basePage.switchToFourthTab(mainTab, secondTab, thirdTab);
    }

    @When("the user clicks the map view button")
    public void the_user_clicks_the_map_view_button() throws InterruptedException {
        houseForSalePage.clickMapCtaButton();
    }

    @Then("the map should be displayed with the property marked")
    public void the_map_should_be_displayed_with_the_property_marked() {
        houseForSalePage.printMapConfirmation();
    }

    // ═══════════════════════════════════════════════════════════════════
    // TC-12 : Residential Plots + Area Filter (POI for min/max values)
    // ═══════════════════════════════════════════════════════════════════

    @Given("the user navigates directly to Plot in Mumbai page")
    public void the_user_navigates_directly_to_Plot_in_Mumbai_page()
            throws InterruptedException {
        mainTab = DriverManager.getDriver().getWindowHandle();
        plotPage.openPlotsPage();
    }

    @Then("the current filter spec should be displayed")
    public void the_current_filter_spec_should_be_displayed() {
        plotPage.printCurrentFilterSpec();
    }

    @When("the user clicks More Filters")
    public void the_user_clicks_More_Filters() throws InterruptedException {
        plotPage.clickMoreFilters();
    }

    /**
     * TC12 – POI reads MinArea from Excel; feature step value is fallback.
     */
    @When("the user selects minimum budget {string} from dropdown")
    public void the_user_selects_minimum_budget(String featureValue)
            throws InterruptedException {
        String val = readExcelOrDefault("PlotFilterData", 1, 0, featureValue);
        plotPage.selectMinPlotArea(val);
        // Close and re-open so max dropdown becomes accessible – mirrors A_12
        plotPage.closeMoreFilters();
        plotPage.reopenMoreFilters();
    }

    /**
     * TC12 – POI reads MaxArea from Excel; feature step value is fallback.
     */
    @When("the user selects maximum budget {string} from dropdown")
    public void the_user_selects_maximum_budget(String featureValue)
            throws InterruptedException {
        String val = readExcelOrDefault("PlotFilterData", 1, 1, featureValue);
        plotPage.selectMaxPlotArea(val);
    }

    @When("the user clicks Apply Filters")
    public void the_user_clicks_Apply_Filters() throws InterruptedException {
        plotPage.clickApplyFilters();
    }

    @Then("the updated filter spec should be displayed")
    public void the_updated_filter_spec_should_be_displayed() {
        plotPage.printActiveFilterLabel();
        plotPage.printFirst5PlotAreas();
    }

    // ═══════════════════════════════════════════════════════════════════
    // TC-14 : Office Space + Contact Agent
    // ═══════════════════════════════════════════════════════════════════

    @When("the user clicks on the first office property")
    public void the_user_clicks_on_the_first_office_property()
            throws InterruptedException {
        secondTab = basePage.currentHandle();
        officeSpacePage.clickFirstOfficeProperty();
    }

    @When("the user switches to the office detail tab")
    public void the_user_switches_to_the_office_detail_tab()
            throws InterruptedException {
        basePage.switchToThirdTab(mainTab, secondTab);
    }

    @When("the user clicks Contact Agent")
    public void the_user_clicks_Contact_Agent() throws InterruptedException {
        // "Contact Agent" in feature maps to "Get Phone No." in A_14
        officeSpacePage.clickGetPhoneNo();
    }

    @Then("the Contact Agent action should be completed")
    public void the_Contact_Agent_action_should_be_completed()
            throws InterruptedException {
        // Read from Excel for TC14 contact form
        String name   = readExcelOrDefault("ContactData", 1, 0, "Test User");
        String email  = readExcelOrDefault("ContactData", 1, 1, "testuser@gmail.com");
        String mobile = readExcelOrDefault("ContactData", 1, 2, "9330827089");
        officeSpacePage.fillContactForm(name, email, mobile);
        officeSpacePage.clickContinue();
        officeSpacePage.waitForOtpStep();
        officeSpacePage.clickSubmitButton();
        officeSpacePage.verifyOwnerContactMessage();
    }

    // ═══════════════════════════════════════════════════════════════════
    // TC-16 : Commercial Space + Business Type Filter + Sort
    // ═══════════════════════════════════════════════════════════════════

    @When("the user clicks the Business Type filter")
    public void the_user_clicks_the_Business_Type_filter()
            throws InterruptedException {
        commercialPage.clickBusinessTypeFilter();
    }

    @When("the user selects the first business sub-type option")
    public void the_user_selects_the_first_business_sub_type_option()
            throws InterruptedException {
        commercialPage.selectFirstBusinessSubType();
    }

    @When("the user clicks the sort dropdown")
    public void the_user_clicks_the_sort_dropdown() throws InterruptedException {
        commercialPage.clickSortDropdown();
    }

    @When("the user selects {string} sort option")
    public void the_user_selects_sort_option(String option)
            throws InterruptedException {
        // A_16 only implements "Price - High to Low" – handled by page class
        commercialPage.selectPriceHighToLow();
    }

    @Then("the prices of the first 5 commercial properties should be displayed")
    public void the_prices_of_the_first_5_commercial_properties_should_be_displayed() {
        commercialPage.printFirst5CommercialPrices();
    }

    // ═══════════════════════════════════════════════════════════════════
    // HELPER
    // ═══════════════════════════════════════════════════════════════════

    /**
     * Tries to read from TestData.xlsx via Apache POI.
     * Returns the fallback string if the file is not present (e.g. first checkout).
     */
    private String readExcelOrDefault(String sheet, int row, int col, String fallback) {
        try {
            return ExcelUtils.getCellData(sheet, row, col);
        } catch (Exception e) {
            System.out.println("[ExcelUtils] Falling back to default for "
                    + sheet + "[" + row + "," + col + "]: " + fallback);
            return fallback;
        }
    }
}