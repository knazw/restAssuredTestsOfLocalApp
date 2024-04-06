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

  Scenario: Correct non-zero number of books found by author without transformer
    Given I have the following books in the store without transformer 2
      | title                                | author      | price |
      | The Devil in the White City          | Erik Larson | 10.99 |
      | The Lion, the Witch and the Wardrobe | C.S. Lewis  | 8.79  |
      | In the Garden of Beasts              | Erik Larson | 6.99  |
    When I add prices of all books
    Then I have price of all books
      | overall price |
      | 26.77         |

  Scenario: Correct computer overall price is computed without transformer
    Given I have the following items in the store without transformer
      | name        | producer  | price  | monthly costs |
      | processor   | Intel     | 200.99 | 11.24         |
      | motherboard | ASUS      | 160.79 | 6.92          |
      | ram         | Kingstone | 100.99 | 2.53          |
    When I add prices of all items
    Then I have price of computer equal to 462.77
    And I have overall monthly costs equal to 20.69