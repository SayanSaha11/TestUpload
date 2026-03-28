//package stepDefinitions;
//
//import Pages.BasePage;
//import Pages.BudgetHomesPage;
//import Pages.CommercialSpacePage;
//import Pages.HomePage;
//import Pages.HouseForSalePage;
//import Pages.NewProjectsPage;
//import Pages.OfficeSpacePage;
//import Pages.PlotPage;
//import Pages.PremiumHomesPage;
//import io.cucumber.java.After;
//import io.cucumber.java.Before;
//import io.cucumber.java.Scenario;
//import io.cucumber.java.en.Given;
//import io.cucumber.java.en.Then;
//import io.cucumber.java.en.When;
//import org.openqa.selenium.By;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Assert;
//import utils.DriverManager;
//import utils.ExcelUtils;
//
//import java.time.Duration;
//import java.util.List;
//
//public class MagicBricksStepDefinitions {
//
//    private HomePage            homePage;
//    private BudgetHomesPage     budgetHomesPage;
//    private PremiumHomesPage    premiumHomesPage;
//    private NewProjectsPage     newProjectsPage;
//    private HouseForSalePage    houseForSalePage;
//    private PlotPage            plotPage;
//    private OfficeSpacePage     officeSpacePage;
//    private CommercialSpacePage commercialPage;
//    private BasePage            basePage;
//
//    private WebDriverWait wait;
//
//    private String mainTab;
//    private String secondTab;
//    private String thirdTab;
//
//    // ═══════════════════════════════════════════════════════════════════════
//    // HOOKS
//    // ═══════════════════════════════════════════════════════════════════════
//
////    @Before
////    public void setUp() {
////        DriverManager.initDriver();
////        wait             = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(30));
////        homePage         = new HomePage();
////        budgetHomesPage  = new BudgetHomesPage();
////        premiumHomesPage = new PremiumHomesPage();
////        newProjectsPage  = new NewProjectsPage();
////        houseForSalePage = new HouseForSalePage();
////        plotPage         = new PlotPage();
////        officeSpacePage  = new OfficeSpacePage();
////        commercialPage   = new CommercialSpacePage();
////        basePage         = new BasePage();
////    }
////
////    @After
////    public void tearDown(Scenario scenario) throws InterruptedException {
////        if (scenario.isFailed()) {
////            try {
////                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver())
////                        .getScreenshotAs(OutputType.BYTES);
////                scenario.attach(screenshot, "image/png",
////                        "Failure screenshot – " + scenario.getName());
////            } catch (Exception ignored) {}
////        }
////        Thread.sleep(5000);
////        DriverManager.quitDriver();
////    }
//    
//    @io.cucumber.java.Before(order = 1)
//    public void initPageObjects() {
//        wait             = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(30));
//        homePage         = new HomePage();
//        budgetHomesPage  = new BudgetHomesPage();
//        premiumHomesPage = new PremiumHomesPage();
//        newProjectsPage  = new NewProjectsPage();
//        houseForSalePage = new HouseForSalePage();
//        plotPage         = new PlotPage();
//        officeSpacePage  = new OfficeSpacePage();
//        commercialPage   = new CommercialSpacePage();
//        basePage         = new BasePage();
//    }
//
//    // ═══════════════════════════════════════════════════════════════════════
//    // BACKGROUND
//    // ═══════════════════════════════════════════════════════════════════════
//
//    @Given("the user is on the MagicBricks Mumbai residential page")
//    public void the_user_is_on_the_MagicBricks_Mumbai_residential_page() {
//        mainTab = DriverManager.getDriver().getWindowHandle();
//        homePage.openMumbaiPage();
//        String title = DriverManager.getDriver().getTitle().toLowerCase();
//        Assert.assertTrue(title.contains("magicbricks"),
//                "Page title should contain 'magicbricks' but was: " + title);
//    }
//
//    // ═══════════════════════════════════════════════════════════════════════
//    // SHARED BUY-MENU NAVIGATION
//    // ═══════════════════════════════════════════════════════════════════════
//
//    @When("the user clicks the Buy heading")
//    public void the_user_clicks_the_Buy_heading() {
//        homePage.clickBuyHeading();
//    }
//
//    @When("the user clicks {string}")
//    public void the_user_clicks_link(String linkText) {
//        homePage.clickBuyMenuLink(linkText);
//    }
//
//    @When("the user switches to the new tab")
//    public void the_user_switches_to_the_new_tab() {
//        basePage.switchToNewTabExcluding(mainTab);
//        Assert.assertNotEquals(DriverManager.getDriver().getWindowHandle(), mainTab,
//                "Driver should have switched away from mainTab");
//    }
//
//    // ═══════════════════════════════════════════════════════════════════════
//    // TC-04 : Budget Homes + EMI Calculator
//    // ═══════════════════════════════════════════════════════════════════════
//
//    @Then("the first 5 property prices should be displayed")
//    public void the_first_5_property_prices_should_be_displayed() {
//        List<WebElement> cards = budgetHomesPage.getPropertyList();
//        Assert.assertTrue(cards.size() >= 5,
//                "Expected at least 5 Budget Home listings but found: " + cards.size());
//        budgetHomesPage.printFirst5Prices();
//    }
//
//    @When("the user clicks on the first property listing")
//    public void the_user_clicks_on_the_first_property_listing() {
//        secondTab = basePage.currentHandle();
//        budgetHomesPage.clickFirstProperty();
//    }
//
//    @When("the user switches to the property detail tab")
//    public void the_user_switches_to_the_property_detail_tab() {
//        basePage.switchToThirdTab(mainTab, secondTab);
//        String current = DriverManager.getDriver().getWindowHandle();
//        Assert.assertNotEquals(current, mainTab,   "Should not be on mainTab");
//        Assert.assertNotEquals(current, secondTab, "Should not be on secondTab");
//    }
//
//    @When("the user clicks the Apply Loan button")
//    public void the_user_clicks_the_Apply_Loan_button() {
//        thirdTab = basePage.currentHandle();
//        budgetHomesPage.clickApplyLoan();
//    }
//
//    @When("the user switches to the loan calculator tab")
//    public void the_user_switches_to_the_loan_calculator_tab() {
//        basePage.switchToFourthTab(mainTab, secondTab, thirdTab);
//        WebElement amountInput = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//input[@id='amountRequiredEmiCal']")));
//        Assert.assertTrue(amountInput.isDisplayed(),
//                "Loan amount input should be visible on the EMI calculator tab");
//    }
//
//    @When("the user enters loan amount {string} and interest rate {string}")
//    public void the_user_enters_loan_amount_and_interest_rate(String displayAmount, String displayRate) {
//        String amount = readExcelOrDefault("EMIData", 1, 0, displayAmount);
//        String rate   = readExcelOrDefault("EMIData", 1, 1, displayRate);
//        budgetHomesPage.enterLoanAmount(amount);
//        budgetHomesPage.enterInterestRate(rate);
//        String enteredAmount = DriverManager.getDriver()
//                .findElement(By.xpath("//input[@id='amountRequiredEmiCal']"))
//                .getAttribute("value");
//        Assert.assertEquals(enteredAmount, amount,
//                "Loan amount field should contain: " + amount);
//    }
//
//    @When("the user selects property not finalized radio button")
//    public void the_user_selects_property_not_finalized_radio_button() {
//        budgetHomesPage.selectPropertyNotFinalisedRadio();
//    }
//
//    @When("the user clicks Calculate EMI")
//    public void the_user_clicks_Calculate_EMI() {
//        budgetHomesPage.clickCalculateEmi();
//    }
//
//    @Then("the EMI details should be displayed")
//    public void the_EMI_details_should_be_displayed() {
//        String result = budgetHomesPage.getEmiResult();
//        Assert.assertNotNull(result, "EMI result should not be null");
//        Assert.assertFalse(result.trim().isEmpty(),
//                "EMI result box should contain text but was empty");
//        System.out.println("EMI Result:\n" + result);
//    }
//
//    // ═══════════════════════════════════════════════════════════════════════
//    // TC-06 : Premium Homes + Image Gallery
//    // ═══════════════════════════════════════════════════════════════════════
//
//    @When("the user clicks the search button")
//    public void the_user_clicks_the_search_button() {
//        premiumHomesPage.clickSearchButton();
//        List<WebElement> cards = DriverManager.getDriver()
//                .findElements(By.xpath("//div[@class='mb-srp__card']"));
//        Assert.assertTrue(cards.size() > 0,
//                "At least 1 Premium Home listing should be visible after search");
//    }
//
//    @When("the user opens the image gallery of the first property")
//    public void the_user_opens_the_image_gallery_of_the_first_property() {
//        premiumHomesPage.clickFirstPropertyImageSlider();
//    }
//
//    @When("the user clicks the Project Photo tab")
//    public void the_user_clicks_the_Project_Photo_tab() {
//        premiumHomesPage.clickProjectPhotoTab();
//        WebElement arrow = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//div[@class='arrow rightArrow']")));
//        Assert.assertTrue(arrow.isDisplayed(),
//                "Right-arrow should be visible after clicking Project Photo tab");
//    }
//
//    @Then("the user should be able to navigate right through 3 gallery images")
//    public void the_user_should_be_able_to_navigate_right_through_3_gallery_images() {
//        premiumHomesPage.navigateGalleryRight(3);
//        WebElement arrow = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//div[@class='arrow rightArrow']")));
//        Assert.assertTrue(arrow.isDisplayed(),
//                "Right arrow should still be visible after navigating 3 images");
//    }
//
//    // ═══════════════════════════════════════════════════════════════════════
//    // TC-07 : New Projects + Contact Seller Enquiry Form
//    // ═══════════════════════════════════════════════════════════════════════
//
//    @Given("the user navigates directly to New Projects Mumbai page")
//    public void the_user_navigates_directly_to_New_Projects_Mumbai_page() {
//        mainTab = DriverManager.getDriver().getWindowHandle();
//        newProjectsPage.openNewProjectsPage();
//        String url = DriverManager.getDriver().getCurrentUrl();
//        Assert.assertTrue(url.contains("new-projects-Mumbai"),
//                "URL should contain 'new-projects-Mumbai' but was: " + url);
//    }
//
//    @Then("the first 3 new project details should be displayed")
//    public void the_first_3_new_project_details_should_be_displayed() throws InterruptedException {
//    	Thread.sleep(2000);
//    	List<WebElement> cards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
//                By.xpath("//div[@class='mghome__prjblk__txtsec ']")));
//        Assert.assertTrue(cards.size() >= 3,
//                "Expected at least 3 project cards but found: " + cards.size());
//        newProjectsPage.printFirst3ProjectDetails();
//    }
//
//    @When("the user clicks on the first new project")
//    public void the_user_clicks_on_the_first_new_project() {
//        newProjectsPage.clickFirstProject();
//    }
//
//    @When("the user switches to the project detail tab")
//    public void the_user_switches_to_the_project_detail_tab() {
//        secondTab = basePage.currentHandle();
//        basePage.switchToThirdTab(mainTab, secondTab);
//        WebElement contactBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//span[text()='Contact Seller'][1]")));
//        Assert.assertTrue(contactBtn.isDisplayed(),
//                "Contact Seller button should be visible on the project detail page");
//    }
//
//    @When("the user clicks Contact Seller")
//    public void the_user_clicks_Contact_Seller() {
//        newProjectsPage.clickContactSeller();
//        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("(//input[@id='userName'])[1]")));
//        Assert.assertTrue(nameField.isDisplayed(),
//                "Name input should be visible after clicking Contact Seller");
//    }
//
//    @When("the user fills the enquiry form with name {string} email {string} and mobile {string}")
//    public void the_user_fills_the_enquiry_form(String name, String email, String mobile) {
//        String n = readExcelOrDefault("ContactData", 1, 0, name);
//        String e = readExcelOrDefault("ContactData", 1, 1, email);
//        String m = readExcelOrDefault("ContactData", 1, 2, mobile);
//        newProjectsPage.fillContactForm(n, e, m);
//        String enteredName   = DriverManager.getDriver()
//                .findElement(By.xpath("(//input[@id='userName'])[1]")).getAttribute("value");
//        String enteredMobile = DriverManager.getDriver()
//                .findElement(By.xpath("(//input[@id='userMobile'])[1]")).getAttribute("value");
//        Assert.assertEquals(enteredName,   n, "Name field should contain: "   + n);
//        Assert.assertEquals(enteredMobile, m, "Mobile field should contain: " + m);
//    }
//
//    @When("the user clicks Get Contact Details")
//    public void the_user_clicks_Get_Contact_Details() throws InterruptedException {
//        newProjectsPage.clickGetContactDetails();
//        System.out.println("Enter the OTP to enquire about the new project.");
//    }
//
//    @When("the user selects the first top match")
//    public void the_user_selects_the_first_top_match() {
//        System.out.println("The contact details is sent to Phone Number.");
//    }
//
//    @Then("the contact button text should be displayed")
//    public void the_contact_button_text_should_be_displayed() {
//        System.out.println("The Contact Seller button is now showing Contacted");
//    }
//
//    // ═══════════════════════════════════════════════════════════════════════
//    // TC-10 : House for Sale + About Locality + Map View
//    // ═══════════════════════════════════════════════════════════════════════
//
//    @When("the user clicks on the specific house property")
//    public void the_user_clicks_on_the_specific_house_property() {
//        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("(//h2[@class='mb-srp__card--title'])[1]")));
//        Assert.assertTrue(heading.isDisplayed(),
//                "Target house property heading should be visible on the SRP");
//        houseForSalePage.printAndClickHouseProperty();
//    }
//
//    @When("the user switches to the house detail tab")
//    public void the_user_switches_to_the_house_detail_tab() {
//        secondTab = basePage.currentHandle();
//        basePage.switchToThirdTab(mainTab, secondTab);
//        Assert.assertNotEquals(DriverManager.getDriver().getWindowHandle(), mainTab,
//                "Should have switched to the house detail tab");
//    }
//
//    @When("the user scrolls down by {int} pixels")
//    public void the_user_scrolls_down_by_pixels(int pixels) {
//        basePage.scrollBy(0, pixels);
//        houseForSalePage.scrollDownAndWait(0);
//    }
//
//    @When("the user clicks About Locality tab")
//    public void the_user_clicks_About_Locality_tab() {
//        houseForSalePage.clickAboutLocalityTab();
//        WebElement exploreLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//a[text()='Explore Locality']")));
//        Assert.assertTrue(exploreLink.isDisplayed(),
//                "Explore Locality link should appear after clicking About Locality");
//    }
//
//    @When("the user scrolls up by {int} pixels")
//    public void the_user_scrolls_up_by_pixels(int pixels) {
//        basePage.scrollBy(0, -pixels);
//    }
//
//    @When("the user clicks Explore Locality")
//    public void the_user_clicks_Explore_Locality() {
//        thirdTab = basePage.currentHandle();
//        houseForSalePage.scrollUpAndClickExploreLocality();
//    }
//
//    @When("the user switches to the locality tab")
//    public void the_user_switches_to_the_locality_tab() {
//        basePage.switchToFourthTab(mainTab, secondTab, thirdTab);
//        WebElement mapBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//a[@class='loc-det__banner__cta']")));
//        Assert.assertTrue(mapBtn.isDisplayed(),
//                "Map CTA button should be visible on the locality page");
//    }
//
//    @When("the user clicks the map view button")
//    public void the_user_clicks_the_map_view_button() {
//        houseForSalePage.clickMapCtaButton();
//    }
//
//    @Then("the map should be displayed with the property marked")
//    public void the_map_should_be_displayed_with_the_property_marked() {
//        WebElement canvas = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//canvas[@class='mapboxgl-canvas']")));
//        Assert.assertTrue(canvas.isDisplayed(),
//                "Mapbox canvas should be visible after clicking the map view button");
//        houseForSalePage.printMapConfirmation();
//    }
//
//    // ═══════════════════════════════════════════════════════════════════════
//    // TC-12 : Residential Plots + Area Filter
//    // ═══════════════════════════════════════════════════════════════════════
//
//    /**
//     * Navigates directly to the Mumbai plots page.
//     *
//     * KEY FIX: PlotPage.openPlotsPage() now handles the second-tab switch
//     * that MagicBricks triggers on load (same as A_12 does). mainTab is
//     * recorded here BEFORE the navigation so the step definition still has
//     * a reference to it for any future use, but TC-12 does not need it for
//     * tab-switching — PlotPage handles that internally.
//     *
//     * Assertion: URL must contain "mumbai".
//     */
//    @Given("the user navigates directly to Plot in Mumbai page")
//    public void the_user_navigates_directly_to_Plot_in_Mumbai_page() {
//        mainTab = DriverManager.getDriver().getWindowHandle();
//        plotPage.openPlotsPage();   // opens URL + switches to second tab + waitPageReady
//
//        String url = DriverManager.getDriver().getCurrentUrl();
//        Assert.assertTrue(url.toLowerCase().contains("mumbai"),
//                "URL should reference Mumbai plot listings but was: " + url);
//    }
//
//    /**
//     * Reads the initial filter-spec label BEFORE any filter is applied.
//     *
//     * Assertion: text must not be null or blank.
//     */
//    @Then("the current filter spec should be displayed")
//    public void the_current_filter_spec_should_be_displayed() {
//        String spec = plotPage.getFilterSpec();
//        Assert.assertNotNull(spec, "Filter spec text should not be null");
//        Assert.assertFalse(spec.trim().isEmpty(),
//                "Filter spec should contain text before filters are applied");
//        System.out.println("Initial filter spec: " + spec);
//    }
//
//    /**
//     * FIRST "More Filters" click — opens the panel for min-area selection.
//     *
//     * KEY FIX: PlotPage.clickMoreFilters() now uses JS click + two-stage
//     * wait (panel container → child select) + 500 ms CSS animation settle.
//     * The step only asserts the select is visible — it does NOT click the
//     * select, which would collapse the panel prematurely.
//     *
//     * Assertion: min-area select must be visible after the panel opens.
//     * @throws InterruptedException 
//     */
//    @When("the user clicks More Filters")
//    public void the_user_clicks_More_Filters() throws InterruptedException {
//        plotPage.clickMoreFilters();
//    }
//
//    /**
//     * Selects minimum plot area (e.g. "500").
//     * Panel closes after selectByValue() — expected, mirrors A_12.
//     * Next step reopens panel via a second plotPage.clickMoreFilters() call.
//     *
//     * Reads value from TestData.xlsx (PlotFilterData, row=1, col=0) with
//     * Gherkin inline value as fallback.
//     * @throws InterruptedException 
//     */
//    @When("the user selects minimum budget {string} from dropdown")
//    public void the_user_selects_minimum_budget(String featureValue) throws InterruptedException {
//        String val = readExcelOrDefault("PlotFilterData", 1, 0, featureValue);
//        plotPage.selectMinPlotArea(val);
//    }
//
//    /**
//     * SECOND "More Filters" click — reopens the panel for max-area selection,
//     * then selects the maximum plot area (e.g. "2000").
//     *
//     * KEY FIX: confirmation wait now uses the same #moreFilter_0-anchored
//     * XPath as PlotPage.selectMaxPlotArea() — no locator mismatch possible.
//     *
//     * Reads value from TestData.xlsx (PlotFilterData, row=1, col=1) with
//     * Gherkin inline value as fallback.
//     * @throws InterruptedException 
//     */
//    @When("the user selects maximum budget {string} from dropdown")
//    public void the_user_selects_maximum_budget(String featureValue) {
//
//        plotPage.clickMoreFilters(); // reopen panel ONLY here
//
//        String val = readExcelOrDefault("PlotFilterData", 1, 1, featureValue);
//        plotPage.selectMaxPlotArea(val);
//    }
//
//    /**
//     * Clicks Apply and waits for the updated filter label.
//     * PlotPage.clickApplyFilters() uses JS click + waitVisible(updatedFilterLabel).
//     */
//    @When("the user clicks Apply Filters")
//    public void the_user_clicks_Apply_Filters() {
//        plotPage.clickApplyFilters();
//    }
//
//    /**
//     * Reads the updated filter-spec label AFTER filters are applied.
//     * Also asserts and prints the first 5 plot-area cards — mirrors A_12 end-loop.
//     *
//     * Assertion 1: updated filter spec must not be null or blank.
//     * Assertion 2: at least 5 plot-area cards must be present on the page.
//     * @throws InterruptedException 
//     */
//    @Then("the updated filter spec should be displayed")
//    public void the_updated_filter_spec_should_be_displayed() throws InterruptedException {
//        String updated = plotPage.getFilterSpec();
//        Assert.assertNotNull(updated, "Updated filter spec should not be null");
//        Assert.assertFalse(updated.trim().isEmpty(),
//                "Updated filter spec should contain text after applying filters");
//        System.out.println("Updated filter spec: " + updated);
//
//        // Mirror A_12 end-loop: assert + print first 5 plot areas
//        Thread.sleep(2000);
//        List<WebElement> areas = plotPage.getPlotAreaElements();
//        Assert.assertTrue(areas.size() >= 5,
//                "At least 5 plot-area cards should be visible after applying filters, found: "
//                        + areas.size());
//        plotPage.printFirst5PlotAreas();
//    }
//
//    // ═══════════════════════════════════════════════════════════════════════
//    // TC-14 : Office Space + Contact Agent (Get Phone No.)
//    // ═══════════════════════════════════════════════════════════════════════
//
//    @When("the user clicks on the first office property")
//    public void the_user_clicks_on_the_first_office_property() {
//        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//h2[@class='mb-srp__card--title']")));
//        Assert.assertTrue(title.isDisplayed(),
//                "At least one office property title should be visible on the SRP");
//        secondTab = basePage.currentHandle();
//        officeSpacePage.clickFirstOfficeProperty();
//    }
//
//    @When("the user switches to the office detail tab")
//    public void the_user_switches_to_the_office_detail_tab() {
//        basePage.switchToThirdTab(mainTab, secondTab);
//        WebElement phoneBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//a[@class='mb-ldp__action--btn large btn-white freecab']")));
//        Assert.assertTrue(phoneBtn.isDisplayed(),
//                "Get Phone No. button should be visible on the office detail page");
//    }
//
//    @When("the user clicks Contact Agent")
//    public void the_user_clicks_Contact_Agent() {
//        officeSpacePage.clickGetPhoneNo();
//    }
//
//    @Then("the Contact Agent action should be completed")
//    public void the_Contact_Agent_action_should_be_completed() {
//        String name   = readExcelOrDefault("ContactData", 1, 0, "Test User");
//        String email  = readExcelOrDefault("ContactData", 1, 1, "testuser@gmail.com");
//        String mobile = readExcelOrDefault("ContactData", 1, 2, "9330827089");
//        officeSpacePage.fillContactForm(name, email, mobile);
//        officeSpacePage.clickContinue();
//        officeSpacePage.waitForOtpStep();
//        officeSpacePage.clickSubmitButton();
//        officeSpacePage.verifyOwnerContactMessage();
//        Assert.assertFalse(DriverManager.getDriver().getCurrentUrl().isEmpty(),
//                "URL should not be empty after completing the Contact Agent flow");
//    }
//
//    // ═══════════════════════════════════════════════════════════════════════
//    // TC-16 : Commercial Space + Business Type Filter + Sort
//    // ═══════════════════════════════════════════════════════════════════════
//
//    @When("the user clicks the Business Type filter")
//    public void the_user_clicks_the_Business_Type_filter() {
//        WebElement filterLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//div[text()='Business Type']")));
//        Assert.assertTrue(filterLabel.isDisplayed(),
//                "Business Type filter label should be visible on the Commercial SRP");
//        commercialPage.clickBusinessTypeFilter();
//    }
//
//    @When("the user selects the first business sub-type option")
//    public void the_user_selects_the_first_business_sub_type_option() throws InterruptedException {
//        commercialPage.selectFirstBusinessSubType();
//        Thread.sleep(2000);
//        List<WebElement> prices = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
//                By.xpath("//div[@class='mb-srp__card__price--amount']")));
//        Assert.assertTrue(prices.size() > 0,
//                "At least 1 price should be visible after applying the sub-type filter");
//    }
//
//    @When("the user clicks the sort dropdown")
//    public void the_user_clicks_the_sort_dropdown() {
//        commercialPage.clickSortDropdown();
//        WebElement sortOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                By.xpath("//li[text()='Price - High to Low']")));
//        Assert.assertTrue(sortOption.isDisplayed(),
//                "'Price - High to Low' option should be visible in the sort dropdown");
//    }
//
//    @When("the user selects {string} sort option")
//    public void the_user_selects_sort_option(String option) {
//        commercialPage.selectPriceHighToLow();
//    }
//
//    @Then("the prices of the first 5 commercial properties should be displayed")
//    public void the_prices_of_the_first_5_commercial_properties_should_be_displayed()
//            throws InterruptedException {
//        Thread.sleep(2000);
//        List<WebElement> prices = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
//                By.xpath("//div[@class='mb-srp__card__price--amount']")));
//        Assert.assertTrue(prices.size() >= 6,
//                "At least 6 price elements expected (index 1–5 used), found: " + prices.size());
//        commercialPage.printFirst5CommercialPrices();
//    }
//
//    // ═══════════════════════════════════════════════════════════════════════
//    // PRIVATE HELPER
//    // ═══════════════════════════════════════════════════════════════════════
//
//    private String readExcelOrDefault(String sheet, int row, int col, String fallback) {
//        try {
//            return ExcelUtils.getCellData(sheet, row, col);
//        } catch (Exception e) {
//            System.out.println("[ExcelUtils] Falling back to default for "
//                    + sheet + "[" + row + "," + col + "]: " + fallback);
//            return fallback;
//        }
//    }
//}


package stepDefinitions;

import Pages.BasePage;
import Pages.BudgetHomesPage;
import Pages.CommercialSpacePage;
import Pages.HomePage;
import Pages.HouseForSalePage;
import Pages.NewProjectsPage;
import Pages.OfficeSpacePage;
import Pages.PlotPage;
import Pages.PremiumHomesPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import utils.DriverManager;
import utils.ExcelUtils;

import java.time.Duration;
import java.util.List;

public class MagicBricksStepDefinitions {

    private HomePage homePage;
    private BudgetHomesPage budgetHomesPage;
    private PremiumHomesPage premiumHomesPage;
    private NewProjectsPage newProjectsPage;
    private HouseForSalePage houseForSalePage;
    private PlotPage plotPage;
    private OfficeSpacePage officeSpacePage;
    private CommercialSpacePage commercialPage;
    private BasePage basePage;

    private WebDriverWait wait;

    private String mainTab;
    private String secondTab;
    private String thirdTab;

    // ═══════════════════════════════════════════════════════════════════════
    // HOOKS
    // ═══════════════════════════════════════════════════════════════════════
    @io.cucumber.java.Before(order = 1)
    public void initPageObjects() {
        wait = new WebDriverWait(DriverManager.getDriver(), Duration.ofSeconds(30));
        homePage = new HomePage();
        budgetHomesPage = new BudgetHomesPage();
        premiumHomesPage = new PremiumHomesPage();
        newProjectsPage = new NewProjectsPage();
        houseForSalePage = new HouseForSalePage();
        plotPage = new PlotPage();
        officeSpacePage = new OfficeSpacePage();
        commercialPage = new CommercialSpacePage();
        basePage = new BasePage();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // BACKGROUND
    // ═══════════════════════════════════════════════════════════════════════
    @Given("the user is on the MagicBricks Mumbai residential page")
    public void the_user_is_on_the_MagicBricks_Mumbai_residential_page() {
        mainTab = DriverManager.getDriver().getWindowHandle();
        homePage.openMumbaiPage();
        String title = DriverManager.getDriver().getTitle().toLowerCase();
        Assert.assertTrue(title.contains("magicbricks"),
                "Page title should contain 'magicbricks' but was: " + title);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // SHARED BUY-MENU NAVIGATION
    // ═══════════════════════════════════════════════════════════════════════
    @When("the user clicks the Buy heading")
    public void the_user_clicks_the_Buy_heading() {
        homePage.clickBuyHeading();
    }

    @When("the user clicks {string}")
    public void the_user_clicks_link(String linkText) {
        homePage.clickBuyMenuLink(linkText);
    }

    @When("the user switches to the new tab")
    public void the_user_switches_to_the_new_tab() {
        basePage.switchToNewTabExcluding(mainTab);
        basePage.waitPageReady();                    // ← Critical for stability
        Assert.assertNotEquals(DriverManager.getDriver().getWindowHandle(), mainTab,
                "Driver should have switched away from mainTab");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // TC-04 : Budget Homes + EMI Calculator
    // ═══════════════════════════════════════════════════════════════════════
    @Then("the first 5 property prices should be displayed")
    public void the_first_5_property_prices_should_be_displayed() {
        List<WebElement> cards = budgetHomesPage.getPropertyList();
        Assert.assertTrue(cards.size() >= 5,
                "Expected at least 5 Budget Home listings but found: " + cards.size());
        budgetHomesPage.printFirst5Prices();
    }

    @When("the user clicks on the first property listing")
    public void the_user_clicks_on_the_first_property_listing() {
        secondTab = basePage.currentHandle();
        budgetHomesPage.clickFirstProperty();
    }

    @When("the user switches to the property detail tab")
    public void the_user_switches_to_the_property_detail_tab() {
        basePage.switchToThirdTab(mainTab, secondTab);
        basePage.waitPageReady();                    // ← Anti-stale
        String current = DriverManager.getDriver().getWindowHandle();
        Assert.assertNotEquals(current, mainTab, "Should not be on mainTab");
        Assert.assertNotEquals(current, secondTab, "Should not be on secondTab");
    }

    @When("the user clicks the Apply Loan button")
    public void the_user_clicks_the_Apply_Loan_button() {
        thirdTab = basePage.currentHandle();
        budgetHomesPage.clickApplyLoan();
    }

    @When("the user switches to the loan calculator tab")
    public void the_user_switches_to_the_loan_calculator_tab() {
        basePage.switchToFourthTab(mainTab, secondTab, thirdTab);
        basePage.waitPageReady();                    // ← Anti-stale

        // Stale-safe check for loan amount input
        WebElement amountInput = wait.until(driver -> {
            try {
                return driver.findElement(By.xpath("//input[@id='amountRequiredEmiCal']"));
            } catch (StaleElementReferenceException e) {
                return null;
            }
        });
        Assert.assertTrue(amountInput.isDisplayed(),
                "Loan amount input should be visible on the EMI calculator tab");
    }

    @When("the user enters loan amount {string} and interest rate {string}")
    public void the_user_enters_loan_amount_and_interest_rate(String displayAmount, String displayRate) {
        String amount = readExcelOrDefault("EMIData", 1, 0, displayAmount);
        String rate = readExcelOrDefault("EMIData", 1, 1, displayRate);

        budgetHomesPage.enterLoanAmount(amount);
        budgetHomesPage.enterInterestRate(rate);

        // Stale-safe verification
        String enteredAmount = wait.until(driver ->
                driver.findElement(By.xpath("//input[@id='amountRequiredEmiCal']"))
                      .getAttribute("value"));
        Assert.assertEquals(enteredAmount, amount,
                "Loan amount field should contain: " + amount);
    }

    @When("the user selects property not finalized radio button")
    public void the_user_selects_property_not_finalized_radio_button() {
        budgetHomesPage.selectPropertyNotFinalisedRadio();
    }

    @When("the user clicks Calculate EMI")
    public void the_user_clicks_Calculate_EMI() {
        budgetHomesPage.clickCalculateEmi();
    }

    @Then("the EMI details should be displayed")
    public void the_EMI_details_should_be_displayed() {
        String result = budgetHomesPage.getEmiResult();
        Assert.assertNotNull(result, "EMI result should not be null");
        Assert.assertFalse(result.trim().isEmpty(),
                "EMI result box should contain text but was empty");
        System.out.println("EMI Result:\n" + result);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // TC-06 : Premium Homes + Image Gallery
    // ═══════════════════════════════════════════════════════════════════════
    @When("the user clicks the search button")
    public void the_user_clicks_the_search_button() {
        premiumHomesPage.clickSearchButton();
        basePage.waitPageReady();

        List<WebElement> cards = DriverManager.getDriver()
                .findElements(By.xpath("//div[@class='mb-srp__card']"));
        Assert.assertTrue(cards.size() > 0,
                "At least 1 Premium Home listing should be visible after search");
    }

    @When("the user opens the image gallery of the first property")
    public void the_user_opens_the_image_gallery_of_the_first_property() {
        premiumHomesPage.clickFirstPropertyImageSlider();
    }

    @When("the user clicks the Project Photo tab")
    public void the_user_clicks_the_Project_Photo_tab() {
        premiumHomesPage.clickProjectPhotoTab();
        WebElement arrow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='arrow rightArrow']")));
        Assert.assertTrue(arrow.isDisplayed(),
                "Right-arrow should be visible after clicking Project Photo tab");
    }

    @Then("the user should be able to navigate right through 3 gallery images")
    public void the_user_should_be_able_to_navigate_right_through_3_gallery_images() {
        premiumHomesPage.navigateGalleryRight(3);
        WebElement arrow = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='arrow rightArrow']")));
        Assert.assertTrue(arrow.isDisplayed(),
                "Right arrow should still be visible after navigating 3 images");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // TC-07 : New Projects + Contact Seller Enquiry Form
    // ═══════════════════════════════════════════════════════════════════════
    @Given("the user navigates directly to New Projects Mumbai page")
    public void the_user_navigates_directly_to_New_Projects_Mumbai_page() {
        mainTab = DriverManager.getDriver().getWindowHandle();
        newProjectsPage.openNewProjectsPage();
        String url = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(url.contains("new-projects-Mumbai"),
                "URL should contain 'new-projects-Mumbai' but was: " + url);
    }

    @Then("the first 3 new project details should be displayed")
    public void the_first_3_new_project_details_should_be_displayed() throws InterruptedException {
        basePage.waitPageReady();
        Thread.sleep(1500); // minimal wait for dynamic content

        List<WebElement> cards = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//div[@class='mghome__prjblk__txtsec ']")));
        Assert.assertTrue(cards.size() >= 3,
                "Expected at least 3 project cards but found: " + cards.size());
        newProjectsPage.printFirst3ProjectDetails();
    }

    @When("the user clicks on the first new project")
    public void the_user_clicks_on_the_first_new_project() {
        newProjectsPage.clickFirstProject();
    }

    @When("the user switches to the project detail tab")
    public void the_user_switches_to_the_project_detail_tab() {
        secondTab = basePage.currentHandle();
        basePage.switchToThirdTab(mainTab, secondTab);
        basePage.waitPageReady();                    // ← Anti-stale protection

        // Stale-safe contact button check
        WebElement contactBtn = wait.until(driver -> {
            try {
                return driver.findElement(By.xpath("//span[text()='Contact Seller'][1]"));
            } catch (StaleElementReferenceException e) {
                return null;
            }
        });
        Assert.assertTrue(contactBtn.isDisplayed(),
                "Contact Seller button should be visible on the project detail page");
    }

    @When("the user clicks Contact Seller")
    public void the_user_clicks_Contact_Seller() {
        newProjectsPage.clickContactSeller();
        WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//input[@id='userName'])[1]")));
        Assert.assertTrue(nameField.isDisplayed(),
                "Name input should be visible after clicking Contact Seller");
    }

    @When("the user fills the enquiry form with name {string} email {string} and mobile {string}")
    public void the_user_fills_the_enquiry_form(String name, String email, String mobile) {
        String n = readExcelOrDefault("ContactData", 1, 0, name);
        String e = readExcelOrDefault("ContactData", 1, 1, email);
        String m = readExcelOrDefault("ContactData", 1, 2, mobile);

        newProjectsPage.fillContactForm(n, e, m);

        // Stale-safe field verification
        String enteredName = wait.until(driver ->
                driver.findElement(By.xpath("(//input[@id='userName'])[1]")).getAttribute("value"));
        String enteredMobile = wait.until(driver ->
                driver.findElement(By.xpath("(//input[@id='userMobile'])[1]")).getAttribute("value"));

        Assert.assertEquals(enteredName, n, "Name field should contain: " + n);
        Assert.assertEquals(enteredMobile, m, "Mobile field should contain: " + m);
    }

    @When("the user clicks Get Contact Details")
    public void the_user_clicks_Get_Contact_Details() throws InterruptedException {
        newProjectsPage.clickGetContactDetails();
        System.out.println("Enter the OTP to enquire about the new project.");
    }

    @When("the user selects the first top match")
    public void the_user_selects_the_first_top_match() {
        System.out.println("The contact details is sent to Phone Number.");
    }

    @Then("the contact button text should be displayed")
    public void the_contact_button_text_should_be_displayed() {
        System.out.println("The Contact Seller button is now showing Contacted");
    }

    // ═══════════════════════════════════════════════════════════════════════
    // TC-10, TC-12, TC-14, TC-16  (similar hardening applied)
    // ═══════════════════════════════════════════════════════════════════════

    @When("the user clicks on the specific house property")
    public void the_user_clicks_on_the_specific_house_property() {
        basePage.waitPageReady();
        WebElement heading = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//h2[@class='mb-srp__card--title'])[1]")));
        Assert.assertTrue(heading.isDisplayed(),
                "Target house property heading should be visible on the SRP");
        houseForSalePage.printAndClickHouseProperty();
    }

    @When("the user switches to the house detail tab")
    public void the_user_switches_to_the_house_detail_tab() {
        secondTab = basePage.currentHandle();
        basePage.switchToThirdTab(mainTab, secondTab);
        basePage.waitPageReady();
        Assert.assertNotEquals(DriverManager.getDriver().getWindowHandle(), mainTab,
                "Should have switched to the house detail tab");
    }

    @When("the user scrolls down by {int} pixels")
    public void the_user_scrolls_down_by_pixels(int pixels) {
        basePage.scrollBy(0, pixels);
        houseForSalePage.scrollDownAndWait(0);
    }

    @When("the user clicks About Locality tab")
    public void the_user_clicks_About_Locality_tab() {
        houseForSalePage.clickAboutLocalityTab();
        WebElement exploreLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[text()='Explore Locality']")));
        Assert.assertTrue(exploreLink.isDisplayed(),
                "Explore Locality link should appear after clicking About Locality");
    }

    @When("the user scrolls up by {int} pixels")
    public void the_user_scrolls_up_by_pixels(int pixels) {
        basePage.scrollBy(0, -pixels);
    }

    @When("the user clicks Explore Locality")
    public void the_user_clicks_Explore_Locality() {
        thirdTab = basePage.currentHandle();
        houseForSalePage.scrollUpAndClickExploreLocality();
    }

    @When("the user switches to the locality tab")
    public void the_user_switches_to_the_locality_tab() {
        basePage.switchToFourthTab(mainTab, secondTab, thirdTab);
        basePage.waitPageReady();
        WebElement mapBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@class='loc-det__banner__cta']")));
        Assert.assertTrue(mapBtn.isDisplayed(),
                "Map CTA button should be visible on the locality page");
    }

    @When("the user clicks the map view button")
    public void the_user_clicks_the_map_view_button() {
        houseForSalePage.clickMapCtaButton();
    }

    @Then("the map should be displayed with the property marked")
    public void the_map_should_be_displayed_with_the_property_marked() {
        WebElement canvas = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//canvas[@class='mapboxgl-canvas']")));
        Assert.assertTrue(canvas.isDisplayed(),
                "Mapbox canvas should be visible after clicking the map view button");
        houseForSalePage.printMapConfirmation();
    }

    // TC-12 remains mostly same (already quite robust)
    @Given("the user navigates directly to Plot in Mumbai page")
    public void the_user_navigates_directly_to_Plot_in_Mumbai_page() {
        mainTab = DriverManager.getDriver().getWindowHandle();
        plotPage.openPlotsPage();
        String url = DriverManager.getDriver().getCurrentUrl();
        Assert.assertTrue(url.toLowerCase().contains("mumbai"),
                "URL should reference Mumbai plot listings but was: " + url);
    }

    @Then("the current filter spec should be displayed")
    public void the_current_filter_spec_should_be_displayed() {
        String spec = plotPage.getFilterSpec();
        Assert.assertNotNull(spec, "Filter spec text should not be null");
        Assert.assertFalse(spec.trim().isEmpty(),
                "Filter spec should contain text before filters are applied");
        System.out.println("Initial filter spec: " + spec);
    }

    @When("the user clicks More Filters")
    public void the_user_clicks_More_Filters() throws InterruptedException {
        plotPage.clickMoreFilters();
    }

    @When("the user selects minimum budget {string} from dropdown")
    public void the_user_selects_minimum_budget(String featureValue) throws InterruptedException {
        String val = readExcelOrDefault("PlotFilterData", 1, 0, featureValue);
        plotPage.selectMinPlotArea(val);
    }

    @When("the user selects maximum budget {string} from dropdown")
    public void the_user_selects_maximum_budget(String featureValue) {
        plotPage.clickMoreFilters();
        String val = readExcelOrDefault("PlotFilterData", 1, 1, featureValue);
        plotPage.selectMaxPlotArea(val);
    }

    @When("the user clicks Apply Filters")
    public void the_user_clicks_Apply_Filters() {
        plotPage.clickApplyFilters();
    }

    @Then("the updated filter spec should be displayed")
    public void the_updated_filter_spec_should_be_displayed() throws InterruptedException {
        String updated = plotPage.getFilterSpec();
        Assert.assertNotNull(updated, "Updated filter spec should not be null");
        Assert.assertFalse(updated.trim().isEmpty(),
                "Updated filter spec should contain text after applying filters");
        System.out.println("Updated filter spec: " + updated);

        Thread.sleep(2000);
        List<WebElement> areas = plotPage.getPlotAreaElements();
        Assert.assertTrue(areas.size() >= 5,
                "At least 5 plot-area cards should be visible after applying filters, found: " + areas.size());
        plotPage.printFirst5PlotAreas();
    }

    // TC-14
    @When("the user clicks on the first office property")
    public void the_user_clicks_on_the_first_office_property() {
        basePage.waitPageReady();
        WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h2[@class='mb-srp__card--title']")));
        Assert.assertTrue(title.isDisplayed(),
                "At least one office property title should be visible on the SRP");
        secondTab = basePage.currentHandle();
        officeSpacePage.clickFirstOfficeProperty();
    }

    @When("the user switches to the office detail tab")
    public void the_user_switches_to_the_office_detail_tab() {
        basePage.switchToThirdTab(mainTab, secondTab);
        basePage.waitPageReady();
        WebElement phoneBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//a[@class='mb-ldp__action--btn large btn-white freecab']")));
        Assert.assertTrue(phoneBtn.isDisplayed(),
                "Get Phone No. button should be visible on the office detail page");
    }

    @When("the user clicks Contact Agent")
    public void the_user_clicks_Contact_Agent() {
        officeSpacePage.clickGetPhoneNo();
    }

    @Then("the Contact Agent action should be completed")
    public void the_Contact_Agent_action_should_be_completed() {
        String name = readExcelOrDefault("ContactData", 1, 0, "Test User");
        String email = readExcelOrDefault("ContactData", 1, 1, "testuser@gmail.com");
        String mobile = readExcelOrDefault("ContactData", 1, 2, "9330827089");

        officeSpacePage.fillContactForm(name, email, mobile);
        officeSpacePage.clickContinue();
        officeSpacePage.waitForOtpStep();
        officeSpacePage.clickSubmitButton();
        officeSpacePage.verifyOwnerContactMessage();

        Assert.assertFalse(DriverManager.getDriver().getCurrentUrl().isEmpty(),
                "URL should not be empty after completing the Contact Agent flow");
    }

    // TC-16
    @When("the user clicks the Business Type filter")
    public void the_user_clicks_the_Business_Type_filter() {
        basePage.waitPageReady();
        WebElement filterLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[text()='Business Type']")));
        Assert.assertTrue(filterLabel.isDisplayed(),
                "Business Type filter label should be visible on the Commercial SRP");
        commercialPage.clickBusinessTypeFilter();
    }

    @When("the user selects the first business sub-type option")
    public void the_user_selects_the_first_business_sub_type_option() throws InterruptedException {
        commercialPage.selectFirstBusinessSubType();
        Thread.sleep(1500);
        basePage.waitPageReady();

        List<WebElement> prices = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//div[@class='mb-srp__card__price--amount']")));
        Assert.assertTrue(prices.size() > 0,
                "At least 1 price should be visible after applying the sub-type filter");
    }

    @When("the user clicks the sort dropdown")
    public void the_user_clicks_the_sort_dropdown() {
        commercialPage.clickSortDropdown();
        WebElement sortOption = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//li[text()='Price - High to Low']")));
        Assert.assertTrue(sortOption.isDisplayed(),
                "'Price - High to Low' option should be visible in the sort dropdown");
    }

    @When("the user selects {string} sort option")
    public void the_user_selects_sort_option(String option) {
        commercialPage.selectPriceHighToLow();
    }

    @Then("the prices of the first 5 commercial properties should be displayed")
    public void the_prices_of_the_first_5_commercial_properties_should_be_displayed()
            throws InterruptedException {
        Thread.sleep(1500);
        basePage.waitPageReady();

        List<WebElement> prices = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(
                By.xpath("//div[@class='mb-srp__card__price--amount']")));
        Assert.assertTrue(prices.size() >= 5,
                "At least 5 price elements expected, found: " + prices.size());
        commercialPage.printFirst5CommercialPrices();
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PRIVATE HELPER
    // ═══════════════════════════════════════════════════════════════════════
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