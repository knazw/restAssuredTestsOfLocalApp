# src/test/resources/features/api.feature

#Feature: Mock API Endpoints
#
#  As a test automation engineer
#  I want to verify that mock API endpoints behave as expected
#  So I can reliably simulate and test API integrations
#
#  Scenario: Verify API health endpoint
#    Given mock API server is running
#    When I send GET request to "/health"
#    Then I should receive status code 200
#
#  @user
#  Scenario: Verify user endpoint returns correct data for valid user id
#    Given mock API server is running
#    And I have configured mock endpoint for user "123"
#    When I send GET request to "/users/123"
#    Then I should receive status code 200
#    And response should contain user name "Test User 123"
#
#  @user @negative
#  Scenario: Verify user endpoint returns 404 for unknown user id
#    Given mock API server is running
#    And I have configured mock endpoint for user "123"
#    When I send GET request to "/users/999"
#    Then I should receive status code 404
