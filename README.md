First you need to install and run
https://learn.cypress.io/testing-your-first-application/app-install-and-overview


flaky tests?:
LoginBdd:
Scenario Outline: User with account is not able to create a "<transaction>" transaction to not existing user
NotificationBdd:
It is possible to read not mine notification
Scenario Outline: It is possible to read not mine notification


npm update allure-commandline

gradlew test
1. npx allure-commandline generate
2. npx allure-commandline open
3. copy ./allure-report/history into .allure-results/history (overwritte)
4. npx allure-commandline generate --clean
5. npx allure-commandline open