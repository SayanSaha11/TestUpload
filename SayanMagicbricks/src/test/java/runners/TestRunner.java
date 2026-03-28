//package runners;
//
//import io.cucumber.testng.AbstractTestNGCucumberTests;
//import io.cucumber.testng.CucumberOptions;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.DataProvider;
//import org.testng.annotations.Optional;
//import org.testng.annotations.Parameters;
//import utils.DriverManager;
//
///**
// * Cross-browser Cucumber runner for TestNG parallel execution.
// *
// * HOW IT WORKS
// * ─────────────
// * testng-crossbrowser.xml defines 3 <test> blocks (Chrome, Edge, Firefox),
// * each passing a <parameter name="browser" value="..."/>.
// *
// * TestNG injects that value into the @Parameters("browser") @BeforeClass below.
// * @BeforeClass fires ONCE per <test> block, in the thread that owns that block,
// * BEFORE any scenario runs — making it the correct place to bind the browser
// * name to the thread via DriverManager.setBrowser().
// *
// * Hooks.@Before then calls DriverManager.initDriver(), which reads the ThreadLocal
// * that was set here. This is the standard industry pattern for TestNG + Selenium
// * parallel cross-browser testing.
// *
// * THREADING MODEL (for i3 + slow network)
// * ─────────────────────────────────────────
// *  parallel="tests"  in XML     → 3 threads run simultaneously (one per browser)
// *  @DataProvider(parallel=false) → 7 scenarios run sequentially inside each thread
// *
// * This means:
// *   Thread-1: Chrome  → TC04 → TC06 → TC07 → TC10 → TC12 → TC14 → TC16
// *   Thread-2: Edge    → TC04 → TC06 → TC07 → TC10 → TC12 → TC14 → TC16
// *   Thread-3: Firefox → TC04 → TC06 → TC07 → TC10 → TC12 → TC14 → TC16
// *
// * No two scenarios compete for CPU within the same browser — eliminates
// * StaleElementReferenceException caused by DOM thrash under CPU load.
// */
//@CucumberOptions(
//    features  = "src/test/resources/features/MagicBricks.feature",
//    glue      = {"stepDefinitions", "hooks"},
//    plugin    = {
//        "pretty",
//        "html:target/cucumber-reports/crossbrowser/report.html",
//        "json:target/cucumber-reports/crossbrowser/report.json",
//        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
//    },
//    monochrome = true,
//    tags       = "not @ignore"
//)
//public class TestRunner extends AbstractTestNGCucumberTests {
//
//    /**
//     * Reads the "browser" parameter from the TestNG XML <test> block and
//     * registers it with DriverManager for the current thread.
//     *
//     * @Optional("chrome") means the suite still works if you run the runner
//     * directly (without XML) — it will default to Chrome.
//     *
//     * WHY @BeforeClass and not @BeforeTest or @BeforeSuite?
//     *   @BeforeClass runs in the SAME thread as the test methods.
//     *   @BeforeSuite and @BeforeTest may run in a different thread,
//     *   which would set the ThreadLocal in the wrong thread.
//     */
//    @BeforeClass(alwaysRun = true)
//    @Parameters("browser")
//    public void setBrowserFromXml(@Optional("chrome") String browser) {
//        System.out.printf("%n╔══════════════════════════════════════════════╗%n");
//        System.out.printf("║  Browser : %-33s║%n", browser.toUpperCase());
//        System.out.printf("║  Thread  : %-33s║%n", Thread.currentThread().getName());
//        System.out.printf("╚══════════════════════════════════════════════╝%n%n");
//        DriverManager.setBrowser(browser);
//    }
//
//    /**
//     * parallel = false → scenarios run one-at-a-time inside each browser thread.
//     * This prevents resource contention on weak hardware.
//     */
//    @Override
//    @DataProvider(parallel = false)
//    public Object[][] scenarios() {
//        return super.scenarios();
//    }
//}

package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.DriverManager;

@CucumberOptions(
    features = "src/test/resources/features/MagicBricks.feature",
    glue = {"stepDefinitions", "hooks"},
    plugin = {
        "pretty",
        "html:target/cucumber-reports/crossbrowser/report.html",
        "json:target/cucumber-reports/crossbrowser/cucumber.json",
        "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
    },
    monochrome = true,
    tags = "not @ignore"
)
public class TestRunner extends AbstractTestNGCucumberTests {

    @BeforeClass(alwaysRun = true)
    @Parameters("browser")
    public void setBrowserFromXml(@Optional("chrome") String browser) {
        System.out.printf("%n╔══════════════════════════════════════════════╗%n");
        System.out.printf("║  Browser : %-33s║%n", browser.toUpperCase());
        System.out.printf("║  Thread  : %-33s║%n", Thread.currentThread().getName());
        System.out.printf("╚══════════════════════════════════════════════╝%n%n");

        DriverManager.setBrowser(browser);

        // Set system property so Extent Report can pick up the browser name
        System.setProperty("browser", browser.toUpperCase());
    }

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}