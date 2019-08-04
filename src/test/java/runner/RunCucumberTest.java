package runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"},
		features = "src/test/resources/feature",
		tags = {"@In-Progress"},
		glue = "com.citibike.automation.bdd.stepDef")
public class RunCucumberTest {

}
