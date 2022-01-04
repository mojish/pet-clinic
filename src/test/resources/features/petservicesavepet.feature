@save_pet
Feature: saving pet

  Scenario: we can save a pet
    Given valid owner with id 2 and valid pet with id 2
    When owner saves pet
    Then pet is saved to owner's pet list
