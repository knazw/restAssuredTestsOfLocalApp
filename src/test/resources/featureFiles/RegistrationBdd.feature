Feature: Registration of an user

  Background:
    Given Following user "userForChecks"
    And "userForChecks" is created
    And 201 response code is received
    And Json in response body matches createdUser.json
    And Response object is properly validated as an user object of an user "userForChecks"
    When "userForChecks" starts to login with credentials
    Then 200 response code is received
    And Correct user object is received
    And Cookie can be obtained from response header


  Scenario Outline: Registration of an user without all data is possible (bug? of this software)
    Given Following user "<username>"
    When "<username>" is created with not all data provided in request based on "<file>"
    Then <status code> response code is received
    And Response object is validated with a file "<schema>"

#    Given Following user "<username1>"
#    And "<username1>" is created
#    And 201 response code is received
#    And Json in response body matches createdUser.json
#    And Response object is properly validated as an user object of an user "<username1>"
#    When "<username1>" starts to login with credentials
#    Then 200 response code is received
#    And Correct user object is received
#    And Cookie can be obtained from response header

    And Get request for users list is sent
    And Response contains "<username>"
    Examples:
      | username | file                                    | status code | username1 | schema                                                            |
      | username | RegistrationInvalid/EmptyFirstName.json | 201         | username1 | testDataResources/RegistrationInvalid/schemas/EmptyFirstName.json |
      | username | RegistrationInvalid/EmptyLastname.json  | 201         | username1 | testDataResources/RegistrationInvalid/schemas/EmptyLastName.json  |
#      | username | RegistrationInvalid/EmptyUsername.json  | 201         | Bad Request | username1 | testDataResources/RegistrationInvalid/schemas/EmptyUserName.json  |

  Scenario: Registration of an user without password is not possible
    Given Following user "username"
    When "username" is created with not all data provided in request based on "RegistrationInvalid/EmptyPassword.json"
    Then 500 response code is received
    And Get request for users list is sent
    And Response does not contain object for "<username>"