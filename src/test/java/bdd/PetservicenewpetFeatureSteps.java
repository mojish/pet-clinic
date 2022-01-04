package bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetService;
import org.springframework.samples.petclinic.utility.PetTimedCache;

public class PetservicenewpetFeatureSteps {
	@Autowired
	private PetTimedCache pets;

	@Autowired
	private OwnerRepository owners;

	@Autowired
	private Logger logger;

	@Autowired
	private PetService petService;

	private Owner owner;

	private Pet newPet;

	@Given("valid owner is provided")
	public void valid_owner_is_provided() {
		owner = Mockito.spy(owners.findById(1));
	}

	@When("adding new pet for the owner")
	public void addingNewPetForTheOwner() {
		newPet = petService.newPet(owner);
	}

	@Then("a new valid pet is added to owner")
	public void aNewValidPetIsAddedToOwner() {
		Mockito.verify(owner).addPet(Mockito.isA(Pet.class));
		Assertions.assertNotNull(newPet);
	}
}
