package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
	    features = "src/test/resources/features",
	    glue = {"stepDefinitions", "hooks"},
	    tags = "@TC04 or @TC06 or @TC07 or @TC10 or @TC12 or @TC14 or @TC16",
	    plugin = {"pretty"}
	)

public class MasterRunner extends AbstractTestNGCucumberTests {

}
