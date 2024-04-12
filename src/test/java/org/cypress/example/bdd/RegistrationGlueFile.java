package org.cypress.example.bdd;

import io.cucumber.junit.platform.engine.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
//@SelectClasspathResource("io/cucumber/skeleton")
//@SelectDirectories("src/test/resources/featureFiles")
@ConfigurationParameter(key = Constants.FEATURES_PROPERTY_NAME,value = "src/test/resources/featureFiles/RegistrationBdd.feature")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME,value = "org/cypress/example/bdd")
@ConfigurationParameter(key = Constants.EXECUTION_DRY_RUN_PROPERTY_NAME,value = "false")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME,value = "pretty, html:target/cucumber-report/cucumber4.html")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty,io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm")
@ConfigurationParameter(key = Constants.PLUGIN_PUBLISH_QUIET_PROPERTY_NAME, value = "true")
public class RegistrationGlueFile {
}
