package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Runner for Premium Homes – Image Gallery (TC06)
 *
 * Reports generated automatically after each run:
 *
 *   target/cucumber-reports/TC06-PremiumHomes/
 *     ├── index.html          → Cucumber native HTML report
 *     ├── TC06-PremiumHomes.json       → JSON (feed to Jenkins / Allure / custom dashboards)
 *     ├── TC06-PremiumHomes.xml        → JUnit XML (CI test result parsing)
 *     └── timeline/           → Step-level execution timeline
 *
 *   target/extent-reports/SparkReport/
 *     └── SparkReport.html    → Extent Reports rich HTML dashboard
 *                               (configured via src/test/resources/extent.properties)
 *
 * CLI shortcuts:
 *   Run this TC only : mvn test -Dtest=RunTC06Test
 *   Run all TCs      : mvn test
 */
@CucumberOptions(
        features   = "src/test/resources/features/MagicBricks.feature",
        glue       = {"stepDefinitions"},
        tags       = "@TC06",
        monochrome = true,
        plugin     = {
                // ── Console output ─────────────────────────────────────
                "pretty",

                // ── Cucumber native HTML (self-contained) ──────────────
                "html:target/cucumber-reports/TC06-PremiumHomes/index.html",

                // ── JSON for Jenkins Cucumber plugin / Allure ──────────
                "json:target/cucumber-reports/TC06-PremiumHomes/TC06-PremiumHomes.json",

                // ── JUnit XML for CI test result parsing ───────────────
                "junit:target/cucumber-reports/TC06-PremiumHomes/TC06-PremiumHomes.xml",

                // ── Step-level execution timeline ──────────────────────
                "timeline:target/cucumber-reports/TC06-PremiumHomes/timeline",

                // ── Extent Reports rich HTML dashboard ─────────────────
                // Reads config from src/test/resources/extent.properties
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)
public class RunTC06Test extends AbstractTestNGCucumberTests {

    /**
     * Set parallel = true to run scenarios concurrently (requires thread-safe driver init).
     */
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}