package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Runner for Commercial Space – Sort Filter (TC16)
 *
 * Reports generated automatically after each run:
 *
 *   target/cucumber-reports/TC16-CommercialSpace/
 *     ├── index.html          → Cucumber native HTML report
 *     ├── TC16-CommercialSpace.json       → JSON (feed to Jenkins / Allure / custom dashboards)
 *     ├── TC16-CommercialSpace.xml        → JUnit XML (CI test result parsing)
 *     └── timeline/           → Step-level execution timeline
 *
 *   target/extent-reports/SparkReport/
 *     └── SparkReport.html    → Extent Reports rich HTML dashboard
 *                               (configured via src/test/resources/extent.properties)
 *
 * CLI shortcuts:
 *   Run this TC only : mvn test -Dtest=RunTC16Test
 *   Run all TCs      : mvn test
 */
@CucumberOptions(
        features   = "src/test/resources/features/MagicBricks.feature",
        glue       = {"stepDefinitions"},
        tags       = "@TC16",
        monochrome = true,
        plugin     = {
                // ── Console output ─────────────────────────────────────
                "pretty",

                // ── Cucumber native HTML (self-contained) ──────────────
                "html:target/cucumber-reports/TC16-CommercialSpace/index.html",

                // ── JSON for Jenkins Cucumber plugin / Allure ──────────
                "json:target/cucumber-reports/TC16-CommercialSpace/TC16-CommercialSpace.json",

                // ── JUnit XML for CI test result parsing ───────────────
                "junit:target/cucumber-reports/TC16-CommercialSpace/TC16-CommercialSpace.xml",

                // ── Step-level execution timeline ──────────────────────
                "timeline:target/cucumber-reports/TC16-CommercialSpace/timeline",

                // ── Extent Reports rich HTML dashboard ─────────────────
                // Reads config from src/test/resources/extent.properties
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)
public class RunTC16Test extends AbstractTestNGCucumberTests {

    /**
     * Set parallel = true to run scenarios concurrently (requires thread-safe driver init).
     */
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}