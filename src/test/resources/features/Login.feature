Feature: User Login

  Scenario: User login with hybrid framework
    Given the user launches the browser
    When the user enter username  "<username>"
    And the user enter password "<password>"
    And the user clicks the login button
    Then login should be "<result>"

    Examples:
      | username      | password     | result |
      | standard_user | secret_sauce | Valid  |
