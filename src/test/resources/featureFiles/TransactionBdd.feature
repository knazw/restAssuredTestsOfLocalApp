Feature: Transaction scenarios

  Scenario Outline: User is able to create "<transaction>" transaction
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
    When "<username>" creates a "<transaction>" transaction from user "<username>" to "<username1>" with <amount> and description "<description>"
    Then 200 response code is received
    And Transaction object is obtained from response
    And Correct transaction data are present in this object: "<username>", "<username1>", "<transaction>", <amount>, "<description>", "<status>" and "<requestStatus>"
#    And TransactionId of this transaction is saved for later use
    And It is possible to obtain transactions list by get transaction request
    And It is possible to obtain transaction from transactions list
    And It is possible to compare obtained transaction with data: "<username>", "<username1>", "<transaction>", <amount>, "<description>", "<status>" and "<requestStatus>"
    Examples:
      | username  | username1 | transaction | amount | description | status   | requestStatus |
      | username  | username1 | payment     | 100    | note 1      | complete |               |
      | username1 | username  | payment     | 200    | note 2      | complete |               |
      | username  | username1 | request     | 300    | note 3      | pending  | pending       |
      | username1 | username  | request     | 400    | note 4      | pending  | pending       |


  Scenario Outline: User is able to accept request transaction
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
    And Correct transaction data are present in this object: "<username>", "<username1>", "<transaction>", <amount>, "<description>", "<status>" and "<requestStatus>"
#    And TransactionId of this transaction is saved for later use
    And It is possible to obtain transactions list by get transaction request
    And It is possible to obtain transaction from transactions list
    And It is possible to compare obtained transaction with data: "<username>", "<username1>", "<transaction>", <amount>, "<description>", "<status>" and "<requestStatus>"
    When User "<username1>" accepts transaction
    Then 204 response code is received
    And It is possible to obtain transactions list by get transaction request
    Then 200 response code is received
    And It is possible to obtain transaction from transactions list
    And It is possible to compare obtained transaction with data: "<username>", "<username1>", "<transaction>", <amount>, "<description>", "<statusUpdated>" and "<requestStatusUpdated>"
    Examples:
      | username  | username1 | transaction | amount | description | status   | requestStatus | statusUpdated | requestStatusUpdated |
      | username  | username1 | request     | 300    | note 3      | pending  | pending       | complete      | accepted             |
      | username1 | username  | request     | 400    | note 4      | pending  | pending       | complete      | accepted             |

  Scenario Outline: User is able to accept his request transaction (bug of this software)
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
    And Correct transaction data are present in this object: "<username>", "<username1>", "<transaction>", <amount>, "<description>", "<status>" and "<requestStatus>"
#    And TransactionId of this transaction is saved for later use
    And It is possible to obtain transactions list by get transaction request
    And It is possible to obtain transaction from transactions list
    And It is possible to compare obtained transaction with data: "<username>", "<username1>", "<transaction>", <amount>, "<description>", "<status>" and "<requestStatus>"
    When User "<username>" accepts transaction
    Then 204 response code is received
    And It is possible to obtain transactions list by get transaction request
    Then 200 response code is received
    And It is possible to obtain transaction from transactions list
    And It is possible to compare obtained transaction with data: "<username>", "<username1>", "<transaction>", <amount>, "<description>", "<statusUpdated>" and "<requestStatusUpdated>"
    Examples:
      | username  | username1 | transaction | amount | description | status   | requestStatus | statusUpdated | requestStatusUpdated |
      | username  | username1 | request     | 300    | note 3      | pending  | pending       | complete      | accepted             |
      | username1 | username  | request     | 400    | note 4      | pending  | pending       | complete      | accepted             |

  # todo add scenarios: it is not possible:
  # - change type of transaction: payment -> request, request -> payment
  # - change the status of complete transaction: (payment) complete -> pending, (request) complete -> pending

#  Scenario Outline: User is able to create request transaction
#    Given Following user "<username>"
#    And "<username>" is created
#    And 201 response code is received
#    And Json in response body matches createdUser.json
#    And Response object is properly validated as an user object of an user "<username>"
#    And "<username>" starts to login with credentials
#    And 200 response code is received
#    And Cookie can be obtained from response header
#    And Following user "<username1>"
#    And "<username1>" is created
#    And 201 response code is received
#    And Json in response body matches createdUser.json
#    And Response object is properly validated as an user object of an user "<username1>"
#    When "<username>" creates a "<transaction>" transaction from user "<username>" to "<username1>" with <amount> and description "<description>"
#    Then 200 response code is received
#    And Transaction object is obtained from response
#    And Correct transaction data are present in this object: "<username>", "<username1>", "<transaction>", <amount>, "<description>", "<status>" and "<requestStatus>"
##    And TransactionId of this transaction is saved for later use
#    And It is possible to obtain transactions list by get transaction request
#    And It is possible to obtain transaction from transactions list
#    And It is possible to compare obtained transaction with data: "<username>", "<username1>", "<transaction>", <amount>, "<description>", "<status>" and "<requestStatus>"
#    Examples:
#      | username  | username1 | transaction | amount | description | status  | requestStatus |
#      | username  | username1 | request     | 300    | note 3      | pending | pending       |
#      | username1 | username  | request     | 400    | note 4      | pending | pending       |