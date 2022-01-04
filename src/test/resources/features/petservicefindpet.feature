@find_pet
Feature: Finding pet

  Scenario: we can find a pet by id
    Given valid searching petid is 10
    When trying to find a pet
    Then founded pet is returned

  Scenario: we can't find a pet by id
    Given valid searching petid is 14
    When trying to find a pet
    Then returned pet is null
