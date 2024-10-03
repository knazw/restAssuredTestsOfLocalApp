Feature: Notification scenarios

  Scenario Outline: User with account is able to like a "<transaction>" transaction
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
      | username1 | username  | payment     | 200    | note 2      |
      | username  | username1 | request     | 300    | note 3      |
      | username1 | username  | request     | 400    | note 4      |

  Scenario Outline: User with account is not able to like a not existing transaction
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
    And Not existing transaction object is stored
    When "<username>" likes this not existing transaction
    Then 422 response code is received
    And Response message contains "Invalid value"
    And It is possible to obtain this like by get transaction request
    And 200 response code is received
    And It is not possible to find this like
    And It is possible to send get notification request
    And 200 response code is received
    And 0 objects are returned after get notification request
    And It is not possible to obtain notificationId from get notification response
    And Notification is read
    And 422 response code is received
    And It is possible to send get notification request
    And 200 response code is received
    And 0 objects are returned after get notification request

    Examples:
      | username  | username1 | transaction | amount | description |
      | username  | username1 | payment     | 100    | note 1      |
      | username1 | username  | payment     | 200    | note 1      |
      | username  | username1 | request     | 100    | note 1      |
      | username1 | username  | request     | 200    | note 1      |


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
      | username  | username1 | request     | 100    | note 1      | 1              |
      | username1 | username  | request     | 200    | note 1      | 2              |


  Scenario Outline: It is possible to read not mine notification
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
    And Following user "<username2>"
    And "<username2>" is created
    And 201 response code is received
    And Json in response body matches createdUser.json
    And Response object is properly validated as an user object of an user "<username2>"

    And "<username2>" starts to login with credentials
    And 200 response code is received
    And Cookie can be obtained from response header for "<username2>"

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
    When Notification is read by "<username2>"
    Then 204 response code is received
    And It is possible to send get notification request
    And 200 response code is received
    And 2 objects are returned after get notification request
    Examples:
      | username  | username1 | username2 | transaction | amount | description | notificationNr |
      | username  | username1 | username2 | payment     | 100    | note 1      | 1              |
      | username1 | username  | username3 | payment     | 200    | note 1      | 2              |
      | username1 | username  | username3 | payment     | 200    | note 1      | 0              |
      | username  | username1 | username2 | request     | 100    | note 1      | 1              |
      | username1 | username  | username3 | request     | 200    | note 1      | 2              |
      | username1 | username  | username3 | request     | 200    | note 1      | 0              |


  Scenario Outline: It is not possible to read not existing notification
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
    And Following user "<username2>"
    And "<username2>" is created
    And 201 response code is received
    And Json in response body matches createdUser.json
    And Response object is properly validated as an user object of an user "<username2>"
    And "<username2>" starts to login with credentials
    And 200 response code is received
    And Cookie can be obtained from response header for "<username2>"
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
    And Not existing notification is choosen
    When Notification is read
    Then 422 response code is received
    And It is possible to send get notification request
    And 200 response code is received
    And 3 objects are returned after get notification request
    Examples:
      | username  | username1 | username2 | transaction | amount | description | notificationNr |
      | username  | username1 | username2 | payment     | 100    | note 1      | 1              |
      | username1 | username  | username3 | payment     | 200    | note 1      | 2              |
      | username1 | username  | username3 | payment     | 200    | note 1      | 0              |
      | username  | username1 | username2 | request     | 100    | note 1      | 1              |
      | username1 | username  | username3 | request     | 200    | note 1      | 2              |
      | username1 | username  | username3 | request     | 200    | note 1      | 0              |

  Scenario Outline: User with account is not able to create a "<transaction>" transaction to not existing user
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
    When "<username>" creates a "<transaction>" transaction from user "<username>" to userId "<userId>" with <amount> and description "<description>"
    Then 500 response code is received
    And It is possible to obtain transactions list by get transaction request
    And 500 response code is received
    And Error contains "TypeError: Cannot read properties of undefined"
    And It is possible to send get notification request
    And 200 response code is received
    And 0 objects are returned after get notification request

    Examples:
      | username  | username1 | transaction | amount | description | userId | status   | requestStatus |
      | username  | username1 | payment     | 100    | note 1      | 0a     | complete |               |
      | username1 | username  | payment     | 200    | note 1      | 1a     | complete |               |


  Scenario Outline: User with account is partialy able to create a "<transaction>" transaction to not existing user (bug?)
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
    When "<username>" creates a "<transaction>" transaction from user "<username>" to userId "<userId>" with <amount> and description "<description>"
    Then 200 response code is received
    And Transaction object is obtained from response
    And Correct transaction data are present in this object: "<username>", "<username1>", "<transaction>", <amount>, "<description>", "<status>" and "<requestStatus>"
    And It is possible to obtain transactions list by get transaction request
    And 500 response code is received
    And Error contains "TypeError: Cannot read properties of undefined"
    And It is possible to send get notification request
    And 200 response code is received
    And 0 objects are returned after get notification request

    Examples:
      | username  | username1 | transaction | amount | description | userId | status | requestStatus |
      | username  | username1 | request     | 100    | note 1      | 0a     | pending | pending      |
      | username1 | username  | request     | 200    | note 1      | 1a     | pending | pending      |