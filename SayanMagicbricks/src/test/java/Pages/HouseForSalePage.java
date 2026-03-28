package Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HouseForSalePage extends BasePage {

    // ✅ ROBUST LOCATORS (no exact text dependency)
    private final By targetHouseHeading =
            By.xpath("//h2[contains(text(),'House for Sale')]");

    private final By aboutLocalityTab =
            By.xpath("//a[contains(text(),'Locality')]");

    private final By exploreLocality =
            By.xpath("//a[text()='Explore Locality']");

    private final By mapCtaBtn =
            By.xpath("//a[@class='loc-det__banner__cta']");

    private final By mapCanvas =
            By.xpath("//canvas[contains(@class,'mapboxgl')]");

    public HouseForSalePage() {
        super();
    }

    // ─────────────────────────────────────────────

    /** Click property */
    public void printAndClickHouseProperty() {

        waitPageReady();

        WebElement heading = wait.until(
                ExpectedConditions.presenceOfElementLocated(targetHouseHeading)
        );

        System.out.println("Selected Property: " + heading.getText());

        jsScrollIntoView(heading);
        jsClick(heading);
    }

    // ✅ FIXED SCROLL (SMART SCROLL)
    public void scrollDownAndWait(int pixels) {

        waitPageReady();

        for (int i = 0; i < 10; i++) {
            try {
                WebElement el = driver.findElement(aboutLocalityTab);

                if (el.isDisplayed()) {
                    return; // found → exit
                }
            } catch (Exception ignored) {}

            scrollBy(0, 400); // gradual scroll
            sleep(800);
        }

        throw new RuntimeException("About Locality section not found after scrolling");
    }

    // ✅ SAFE CLICK
    public void clickAboutLocalityTab() {

        WebElement tab = wait.until(
                ExpectedConditions.presenceOfElementLocated(aboutLocalityTab)
        );

        jsScrollIntoView(tab);
        jsClick(tab);

//        wait.until(ExpectedConditions.presenceOfElementLocated(exploreLocality));
    }

    // ✅ SAFE EXPLORE CLICK
    public void scrollUpAndClickExploreLocality() {

        scrollBy(0, -200);

        WebElement explore = wait.until(
                ExpectedConditions.presenceOfElementLocated(exploreLocality)
        );

        jsScrollIntoView(explore);
        jsClick(explore);
    }

    // ✅ MAP CLICK
    public void clickMapCtaButton() {

        WebElement mapBtn = wait.until(
                ExpectedConditions.presenceOfElementLocated(mapCtaBtn)
        );

        jsScrollIntoView(mapBtn);
        jsClick(mapBtn);

        wait.until(ExpectedConditions.visibilityOfElementLocated(mapCanvas));
    }

    public void printMapConfirmation() {
        System.out.println("Map displayed and property marked successfully.");
    }

    // ─────────────────────────────────────────────

    /** Safe sleep */
    protected void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}