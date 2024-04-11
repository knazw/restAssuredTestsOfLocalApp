Feature: Login scenarios

  Scenario Outline: User without account is not able to login
    Given Following user "<username>"
    When "<username>" starts to login with credentials
    Then 401 response code is received
    And Response message contains Unathorized
    Examples:
    | username        |
    | notexistingUser |
    | username1       |
    | username        |

  Scenario Outline: User with account is not able to login when incorrect data are send
    Given Following user "<username>"
    And "<username>" is created
    When "<username>" starts to login with credentials from file "<file>"
    Then <status code> response code is received
    And Response message "<response>"
    Examples:
      | username  | file                                          | status code | response    |
      | usernamep | UsersInvalid/UsernamePasswordEmpty.json       | 400         | Bad Request |
      | usernamep | UsersInvalid/UsernamePasswordNotProvided.json | 400         | Bad Request |
      | usernamep | UsersInvalid/UsernameAndPasswordEmpty.json    | 400         | Bad Request |

  Scenario Outline: User with account is not able to login when no data are send
    Given Following user "<username>"
    When "<username>" starts to login with no credentials
    Then <status code> response code is received
    And Response message "<response>"
    Examples:
      | username   | status code | response    |
      | usernamep1 |  400        | Bad Request |


  Scenario Outline: User with account is able to login
    Given Following user "<username>"
    And "<username>" is created
    When "<username>" starts to login with credentials
    Then 200 response code is received
    And Correct user object is received
    And Cookie can be obtained from response header
    Examples:
      | username    |
      | usernamep2  |
      | usernamep3  |
      | usernamep4  |

