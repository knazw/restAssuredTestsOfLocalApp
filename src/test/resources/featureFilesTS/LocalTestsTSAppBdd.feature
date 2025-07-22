Feature: Local Typescript app scenarios

  Scenario Outline: It is possible to get result from endpoint
    Given Set value "<valueNameA>": <valueA>
    And <statusCodeA> response code is received from endpoint
    And Set value "<valueNameB>": <valueB>
    And <statusCodeB> response code is received from endpoint
    When Compute "<operation>" operation
    Then <statusCodeResult> response code is received from endpoint
    And Json in response body matches "computedValue.json"
    And Message is : "<message>"
    And Result is: <result>
    And It is possible to send "<method>" request to endpoint "<endpoint>" with key "<key>"
    And <statusCodeResultGET> response code is received from TS endpoint
    And Sleep for 2 seconds

    Examples:
      | valueNameA | statusCodeA | valueNameB | statusCodeB | valueA | valueB | operation | result | message | statusCodeResult | method | endpoint    | key                                    | statusCodeResultGET |
      | A          | 201         | B          | 201         | 100    | 100    | -         | 0      | OK      | 201              | GET    | /get-result | ?key=b47n5ib5578juto97b573vy35y352n73m | 200                 |
      | A          | 201         | B          | 201         | 200    | 100    | -         | 100    | OK      | 201              | get    | /get-result | ?key=b47n5ib5578juto97b573vy35y352n73m | 200                 |
      | A          | 201         | B          | 201         | 200    | 200    | -         | 0      | OK      | 201              | get    | /get-result | ?key=b47n5ib5578juto97b573vy35y352n73m | 200                 |
      | A          | 201         | B          | 201         | 300    | 123    | -         | 177    | OK      | 201              | get    | /get-result | ?key=b47n5ib5578juto97b573vy35y352n73m | 200                 |
      | A          | 201         | B          | 201         | 400    | 4      | -         | 396    | OK      | 201              | get    | /get-result | ?key=b47n5ib5578juto97b573vy35y352n73m | 200                 |