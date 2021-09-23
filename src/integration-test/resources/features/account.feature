Feature: Account

  Scenario: Retrieving the holders of an account
    Given I have an Iban code "NL37ABNA9999888877"
    When I try and retrieve the holders of the account
    Then I get the expected holder names
      | A Wassink |