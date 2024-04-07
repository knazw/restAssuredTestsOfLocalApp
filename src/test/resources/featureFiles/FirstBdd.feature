Feature: Login scenarios

  Scenario Outline: User without account is not able to login
    Given I have the following user "<username>"
    When When I start to login with credentials
    Then I receive 401 response code
    Examples:
    | username        |
    | notexistingUser |
    | username1       |
    | username        |

  Scenario Outline: User with account is not able to login when incorrect data are send
    Given I have the following user "<username>"
    When I start to login with credentials from file "<file>"
    Then I receive <status code> response code
    And Response message "<response>"
    Examples:
      | username | file                                          | status code | response    |
      | username | UsersInvalid/UsernamePasswordEmpty.json       | 400         | Bad Request |
      | username | UsersInvalid/UsernamePasswordNotProvided.json | 400         | Bad Request |
      | username | UsersInvalid/UsernameAndPasswordEmpty.json    | 400         | Bad Request |

  Scenario Outline: User with account is not able to login when no data are send
    Given I have the following user "<username>"
    When I start to login with no credentials
    Then I receive <status code> response code
    And Response message "<response>"
    Examples:
      | username | status code | response    |
      | username |  400        | Bad Request |


  Scenario Outline: User with account is able to login
    Given I have the following user "<username>"
    And "<username>" is created
    When When I start to login with credentials
    Then I receive 200 response code
    And Correct user object
    Examples:
      | username        |
      | notexistingUser |
      | username1       |
      | username        |

