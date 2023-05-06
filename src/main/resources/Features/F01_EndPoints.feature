Feature: Authentication and Access Control

  Scenario: Verify successful login
    Given the user has valid credentials
    When the user logs in with valid username "merchant@foodics.com" and password "123456"
    Then the user should receive a successful response
    And the response should contain a valid authentication token

  Scenario: Verify unauthorized access to protected resource
    Given the user is not authenticated
    When the user requests a protected resource
    Then the user should receive an unauthorized access response
    And the response should contain an error message indicating unauthorized access

  Scenario: Verify successful access to protected resource
    Given the user has a valid authentication token with username"merchant@foodics.com" and password"123456"
    When the user requests a protected resources
    Then the user should receive  successful response
