package bdd;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.*;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import java.time.LocalDate;

public class PetservicesavepetFeatureSteps {
	@Autowired
	private PetTimedCache pets;

	@Autowired
	private OwnerRepository owners;

	@Autowired
	private Logger logger;

	@Autowired
	private PetService petService;

	private Integer ownerSearchingId;
	private Integer petSearchingId;

	private Owner foundOwner;
	private Pet pet;

	@Given("valid owner with id {int} and valid pet with id {int}")
	public void validOwnerWithIdAndValidPetWithId(int arg0, int arg1) {
		ownerSearchingId = arg0;
		petSearchingId = arg1;
		foundOwner = petService.findOwner(ownerSearchingId);
		pet = new Pet();
		pet.setName("Mojtaba");
		pet.setBirthDate(LocalDate.of(1998, 12, 13));

		PetType petType = new PetType();
		petType.setId(1);
		pet.setType(petType);
	}

	@When("owner saves pet")
	public void ownerIntSavesPetInt() {
		petService.savePet(pet, foundOwner);
	}


	@Then("pet is saved to owner's pet list")
	public void petIsSavedToOwnerSPetList() {
		Assertions.assertTrue(foundOwner.getPets().contains(pet));
	}
}
