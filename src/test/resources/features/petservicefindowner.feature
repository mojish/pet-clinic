@find_owner
Feature: finding owner

  Scenario: we can find owner
    Given valid owner id is provided is 1
    When finding owner of the pet
    Then a valid owner with the same id is returned

  Scenario: we can't find owner
    Given valid owner id is provided is 11
    When finding owner of the pet
    Then then returned owner is null
