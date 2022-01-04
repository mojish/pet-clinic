package bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetService;
import org.springframework.samples.petclinic.utility.PetTimedCache;

public class PetservicefindownerFeatureSteps {
	@Autowired
	private PetTimedCache pets;

	@Autowired
	private OwnerRepository owners;

	@Autowired
	private Logger logger;

	@Autowired
	private PetService petService;

	private Integer searchingId;

	private Owner foundOwner;

	@Given("valid owner id is provided is {int}")
	public void validSearchingOwneridIs(int arg0) {
		searchingId = arg0;
	}

	@When("finding owner of the pet")
	public void tryingToFindAPet() {
		foundOwner = petService.findOwner(searchingId);
	}

	@Then("a valid owner with the same id is returned")
	public void foundedPetIsReturned() {
		Assertions.assertEquals(foundOwner.getFirstName(), "George");
	}


	@Then("then returned owner is null")
	public void thenReturnedOwnerIsNull() {
		Assertions.assertEquals(foundOwner, null);
	}
}
