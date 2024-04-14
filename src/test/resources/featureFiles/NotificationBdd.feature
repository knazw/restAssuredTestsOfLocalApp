Feature: Notification scenarios

  Scenario Outline: User with account is able to like a transaction
    Given Following user "<username>"
    And "<username>" is created
    And 201 response code is received
    And Json in response body matches createdUser.json
    And Response object is properly validated as an user object of an user "<username>"
    And "<username>" starts to login with credentials
    And 200 response code is received
    And Cookie can be obtained from response header
    And Following user "<username1>"
    And "<username1>" is created
    And 201 response code is received
    And Json in response body matches createdUser.json
    And Response object is properly validated as an user object of an user "<username1>"
    And "<username>" creates a "<transaction>" transaction from user "<username>" to "<username1>" with <amount> and description "<description>"
    And 200 response code is received
    And Transaction object is obtained from response
    When "<username>" likes this transaction
    Then 200 response code is received
    And Response message contains OK
    And It is possible to obtain this like by get transaction request
    And 200 response code is received
    And This like response contains correct userId for "<username>"
    And This like response contains correct transactionId
    And It is possible to send get notification request
    And 200 response code is received
    And It is possible to obtain notificationId from get notification response
    And Notification is read
    And 204 response code is received
    And It is possible to send get notification request
    And 200 response code is received
    And 0 objects are returned after get notification request

    Examples:
      | username  | username1 | transaction | amount | description |
      | username  | username1 | payment     | 100    | note 1      |
      | username1 | username  | payment     | 200    | note 1      |


  Scenario Outline: Only one notification is read from a group of 3 notifications
    Given Following user "<username>"
    And "<username>" is created
    And 201 response code is received
    And Json in response body matches createdUser.json
    And Response object is properly validated as an user object of an user "<username>"
    And "<username>" starts to login with credentials
    And 200 response code is received
    And Cookie can be obtained from response header
    And Following user "<username1>"
    And "<username1>" is created
    And 201 response code is received
    And Json in response body matches createdUser.json
    And Response object is properly validated as an user object of an user "<username1>"
    And "<username>" creates a "<transaction>" transaction from user "<username>" to "<username1>" with <amount> and description "<description>"
    And 200 response code is received
    And "<username>" likes this transaction
    And "<username>" creates a "<transaction>" transaction from user "<username>" to "<username1>" with <amount> and description "<description>"
    And 200 response code is received
    And "<username>" likes this transaction
    And "<username>" creates a "<transaction>" transaction from user "<username>" to "<username1>" with <amount> and description "<description>"
    And 200 response code is received
    And "<username>" likes this transaction
    And It is possible to send get notification request
    And 200 response code is received
    And 3 objects are returned after get notification request
    And <notificationNr> is choosen
    When Notification is read
    Then 204 response code is received
    And It is possible to send get notification request
    And 200 response code is received
    And 2 objects are returned after get notification request
    Examples:
      | username  | username1 | transaction | amount | description | notificationNr |
      | username  | username1 | payment     | 100    | note 1      | 1              |
      | username1 | username  | payment     | 200    | note 1      | 2              |