Feature: Notification scenarios

  Scenario Outline: User with account is able to like a transaction
    Given Following user "<username>"
    And "<username>" is created
    And "<username>" starts to login with credentials
    And 200 response code is received
    And Cookie can be obtained from response header
    And Following user "<username1>"
    And "<username1>" is created
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
    And This is possible to obtain notificationId from get notification response
    And Notification is read
    And 204 response code is received
    And It is possible to send get notification request
    And 200 response code is received
    And 0 objects are returned after get notification request for "<username>"

    Examples:
      | username   | username1  | transaction | amount | description |
      | usernamep5 | usernamep6 | payment     | 100    | note 1      |
      | usernamep7 | usernamep8 | payment     | 200    | note 1      |


  Scenario Outline: Only one notification is read from a group of 3 notifications
    Given Following user "<username>"
    And "<username>" is created
    And "<username>" starts to login with credentials
    And 200 response code is received
    And Cookie can be obtained from response header
    And Following user "<username1>"
    And "<username1>" is created
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
    And 3 objects are returned after get notification request for "<username>"
    And <notificationNr> is choosen
    When Notification is read
    Then 204 response code is received
    And It is possible to send get notification request
    And 200 response code is received
    And 2 objects are returned after get notification request for "<username>"
    Examples:
      | username    | username1   | transaction | amount | description | notificationNr |
      | usernamep9  | usernamep10 | payment     | 100    | note 1      | 1              |
      | usernamep11 | usernamep12 | payment     | 200    | note 1      | 2              |