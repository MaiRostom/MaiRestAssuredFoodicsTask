package org.example.stepDefinitions;

import io.cucumber.java.en.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class MyStepDefs {

    public String username;
    public String password;
    private String authToken;
    private Response response;

    @Given("the user has valid credentials")
    public void theUserHasValidCredentials() {
        username = "merchant@foodics.com";
        password = "123456";
    }

    @When("the user logs in with valid username {string} and password {string}")
    public void theUserLogsInWithValidUsernameAndPassword(String username, String password) {
        response = RestAssured.given()
                .contentType("application/json")
                .body("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}")
                .when()
                .post("https://pay2.foodics.dev/cp_internal/login");

        assertThat(response.getStatusCode(), equalTo(302));
       JsonPath jsonPath = response.jsonPath();
//
//// Extract the token using JSON path expression
      authToken = jsonPath.getString("XSRF-TOKEN");
//        String html = response.then().extract().asString();
//
//// parse the HTML using Jsoup
//        Document doc = Jsoup.parse(html);
//
//// get the content attribute of the meta tag with name="csrf-token"
//        Element csrfToken = doc.select("meta[name=csrf-token]").first();
//        String authToken = csrfToken.attr("content");

//        assertThat(authToken, notNullValue());
//        assertThat(authToken.trim().length(), greaterThan(0));
    }

    @Then("the user should receive a successful response")
    public void theUserShouldReceiveASuccessfulResponse() {
        assertThat(response.getStatusCode(), equalTo(200));
    }

    @And("the response should contain a valid authentication token")
    public void theResponseShouldContainAValidAuthenticationToken() {
        assertThat(authToken, notNullValue());
        assertThat(authToken.trim().length(), greaterThan(0));
    }

    @Given("the user is not authenticated")
    public void theUserIsNotAuthenticated() {
        authToken = null;
    }

    @When("the user requests a protected resource")
    public void theUserRequestsAProtectedResource() {
        response = RestAssured.given()
                .contentType("application/json")
                .when()
                .get("https://pay2.foodics.dev/cp_internal/whoami");

//        assertThat(response.getStatusCode(), equalTo(401));
//        assertThat(response.getBody().jsonPath().getString("message"), equalTo("Unauthorized"));
    }

    @Then("the user should receive an unauthorized access response")
    public void theUserShouldReceiveAnUnauthorizedAccessResponse() {
        assertThat(response.getStatusCode(), equalTo(401));
    }

    @And("the response should contain an error message indicating unauthorized access")
    public void theResponseShouldContainAnErrorMessageIndicatingUnauthorizedAccess() {
        assertThat(response.getBody().jsonPath().getString("error.message"), equalTo("Unauthorized"));
    }

    @Given("the user has a valid authentication token with username{string} and password{string}")
    public void theUserHasAValidAuthenticationTokenWithUsernameAndPassword(String username, String password) {
        theUserHasValidCredentials();
        theUserLogsInWithValidUsernameAndPassword(username, password);
        assertThat(authToken, notNullValue());
    }

    @When("the user requests a protected resources")
    public void theUserRequestsAProtectedResources() {
        response = RestAssured.given()
                .contentType("application/json")
                .header("Authorization", "Bearer " + authToken)
                .when()
                .get("https://pay2.foodics.dev/cp_internal/...");
        //assertThat(response.getStatusCode(), equalTo(200));
    }

    @Then("the user should receive  successful response")
    public void theUserShouldReceiveASuccessfulResponse2() {
        assertThat(response.getStatusCode(), equalTo(200));
    }



}
