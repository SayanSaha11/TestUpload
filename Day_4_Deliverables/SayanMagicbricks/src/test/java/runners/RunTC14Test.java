package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

/**
 * Runner for Office Space – Contact Agent (TC14)
 *
 * Reports generated automatically after each run:
 *
 *   target/cucumber-reports/TC14-OfficeSpace/
 *     ├── index.html          → Cucumber native HTML report
 *     ├── TC14-OfficeSpace.json       → JSON (feed to Jenkins / Allure / custom dashboards)
 *     ├── TC14-OfficeSpace.xml        → JUnit XML (CI test result parsing)
 *     └── timeline/           → Step-level execution timeline
 *
 *   target/extent-reports/SparkReport/
 *     └── SparkReport.html    → Extent Reports rich HTML dashboard
 *                               (configured via src/test/resources/extent.properties)
 *
 * CLI shortcuts:
 *   Run this TC only : mvn test -Dtest=RunTC14Test
 *   Run all TCs      : mvn test
 */
@CucumberOptions(
        features   = "src/test/resources/features/MagicBricks.feature",
        glue       = {"stepDefinitions"},
        tags       = "@TC14",
        monochrome = true,
        plugin     = {
                // ── Console output ─────────────────────────────────────
                "pretty",

                // ── Cucumber native HTML (self-contained) ──────────────
                "html:target/cucumber-reports/TC14-OfficeSpace/index.html",

                // ── JSON for Jenkins Cucumber plugin / Allure ──────────
                "json:target/cucumber-reports/TC14-OfficeSpace/TC14-OfficeSpace.json",

                // ── JUnit XML for CI test result parsing ───────────────
                "junit:target/cucumber-reports/TC14-OfficeSpace/TC14-OfficeSpace.xml",

                // ── Step-level execution timeline ──────────────────────
                "timeline:target/cucumber-reports/TC14-OfficeSpace/timeline",

                // ── Extent Reports rich HTML dashboard ─────────────────
                // Reads config from src/test/resources/extent.properties
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)
public class RunTC14Test extends AbstractTestNGCucumberTests {

    /**
     * Set parallel = true to run scenarios concurrently (requires thread-safe driver init).
     */
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}