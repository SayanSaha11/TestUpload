package hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import utils.DriverManager;

/**
 * Cucumber hooks:
 *   @Before  → spins up a fresh ChromeDriver for each scenario
 *   @After   → quits the browser after each scenario
 *
 * Because DriverManager uses a ThreadLocal, parallel scenarios each get
 * their own browser instance with zero cross-contamination.
 */
public class Hooks {

    @Before
    public void setUp(Scenario scenario) {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("Starting scenario: " + scenario.getName());
        System.out.println("Tags: " + scenario.getSourceTagNames());
        System.out.println("══════════════════════════════════════════\n");
        DriverManager.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        System.out.println("\n══════════════════════════════════════════");
        System.out.println("Finished scenario: " + scenario.getName());
        System.out.println("Status: " + scenario.getStatus());
        System.out.println("══════════════════════════════════════════\n");
        DriverManager.quitDriver();
    }
}