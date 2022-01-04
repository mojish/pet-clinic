package bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetService;
import org.springframework.samples.petclinic.utility.PetTimedCache;

public class PetservicefindpetFeatureSteps {
	@Autowired
	private PetTimedCache pets;

	@Autowired
	private OwnerRepository owners;

	@Autowired
	private Logger logger;

	@Autowired
	private PetService petService;

	private Integer searchingId;

	private Pet foundedPet;

	@Given("valid searching petid is {int}")
	public void validSearchingPetidIs(int arg0) {
		searchingId = arg0;
	}

	@When("trying to find a pet")
	public void tryingToFindAPet() {
		foundedPet = petService.findPet(searchingId);
	}

	@Then("founded pet is returned")
	public void foundedPetIsReturned() {
		Assertions.assertEquals(foundedPet.getName(), "Mulligan");
	}

	@Then("returned pet is null")
	public void returnedPetIsNull()  {
		Assertions.assertEquals(foundedPet, null);
	}
}
