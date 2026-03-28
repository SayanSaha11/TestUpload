package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;

/**
 * DriverManager - Chrome works with WebDriverManager, Edge uses manual driver path.
 * This avoids the msedgedriver.azureedge.net DNS issue.
 */
public class DriverManager {

    private static final ThreadLocal<WebDriver> driverHolder = new ThreadLocal<>();
    private static final ThreadLocal<String> browserHolder = ThreadLocal.withInitial(() -> "chrome");

    private DriverManager() {}

    public static void setBrowser(String browser) {
        browserHolder.set((browser != null && !browser.isBlank()) 
                ? browser.trim().toLowerCase() : "chrome");
    }

    public static void initDriver() {
        String browser = browserHolder.get();
        System.out.println("=== Initializing " + browser.toUpperCase() + " browser ===");

        WebDriver driver;
        if ("edge".equals(browser)) {
            driver = createEdgeDriver();
        } else if ("firefox".equals(browser)) {
            driver = createFirefoxDriver();
        } else {
            driver = createChromeDriver();
        }

        driver.manage().window().maximize();
        driverHolder.set(driver);
        System.out.println("✅ " + browser.toUpperCase() + " launched successfully.");
    }

    private static WebDriver createChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "--remote-allow-origins=*");
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("dom.webnotifications.enabled", false);
        return new FirefoxDriver(options);
    }

    /**
     * Edge: Uses manual driver path to bypass DNS issue with azureedge.net
     */
    private static WebDriver createEdgeDriver() {
        String driverPath = "C:\\drivers\\msedgedriver.exe";

        if (!new File(driverPath).exists()) {
            throw new RuntimeException("❌ msedgedriver.exe not found at " + driverPath + 
                "\nPlease download it from https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/ " +
                "and place it in C:\\drivers\\msedgedriver.exe");
        }

        System.setProperty("webdriver.edge.driver", driverPath);

        EdgeOptions options = new EdgeOptions();
        options.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage", "inprivate");

        System.out.println("Edge: Using manual driver at " + driverPath);
        return new EdgeDriver(options);
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverHolder.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver not initialised. Check Hooks and DriverManager.");
        }
        return driver;
    }

    public static String getBrowser() {
        return browserHolder.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverHolder.get();
        if (driver != null) {
            try {
                driver.quit();
            } catch (Exception ignored) {}
            driverHolder.remove();
        }
    }
}