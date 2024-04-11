Feature: Transaction scenarios

  Scenario Outline: User is able to create transaction
    Given Following user "<username>"
    And "<username>" is created
    And "<username>" starts to login with credentials
    And 200 response code is received
    And Cookie can be obtained from response header
    And Following user "<username1>"
    And "<username1>" is created
    When "<username>" creates a "<transaction>" transaction from user "<username>" to "<username1>" with <amount> and description "<description>"
    Then 200 response code is received
    And Transaction object is obtained from response
    And Correct transaction data are present in this object: "<username>", "<username1>", "<transaction>", <amount> and "<description>"
#    And TransactionId of this transaction is saved for later use
    And It is possible to obtain transactions list by get transaction request
    And It is possible to obtain transaction from transactions list
    And It is possobie to compare obtained transaction with data: "<username>", "<username1>", "<transaction>", <amount> and "<description>"
    Examples:
      | username    | username1   | transaction | amount | description |
      | usernamep13 | usernamep14 | payment     | 100    | note 1      |
      | usernamep15 | usernamep16 | payment     | 200    | note 1      |