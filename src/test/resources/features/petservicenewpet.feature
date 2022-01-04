@new_pet
Feature: Adding new pet to an owner

  Scenario: we can add a new pet to a valid owner
    Given valid owner is provided
    When adding new pet for the owner
    Then a new valid pet is added to owner
