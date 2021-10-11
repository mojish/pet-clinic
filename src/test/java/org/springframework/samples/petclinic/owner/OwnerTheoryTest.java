package org.springframework.samples.petclinic.owner;

import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(Theories.class)
public class OwnerTheoryTest {

	private static Owner FirstOwner, SecondOwner, ThirdOwner;
	private static Pet FirstPet, SecondPet, ThirdPet, StrayPet;

	private static final Map<Pet, Owner> petOwnerMap = new HashMap<>();
	private static final Set<Pet> NotNewPets = new HashSet<>();

	private static void setPetOwnership(Pet pet, Owner owner) {
		owner.addPet(pet);
		petOwnerMap.put(pet, owner);
	}

	@BeforeClass
	public static void setup() {
		FirstPet = new Pet();
		SecondPet = new Pet();
		ThirdPet = new Pet();
		StrayPet = new Pet();

		FirstPet.setName("first pet name");
		SecondPet.setName("second pet name");
		ThirdPet.setName("third pet name");
		StrayPet.setName("ownerless pet name");

		FirstOwner = new Owner();
		SecondOwner = new Owner();
		ThirdOwner = new Owner();

		setPetOwnership(FirstPet, FirstOwner);
		setPetOwnership(SecondPet, SecondOwner);
		setPetOwnership(ThirdPet, SecondOwner);

		SecondPet.setId(1);
		NotNewPets.add(SecondPet);
	}

	@DataPoints
	public static Pet[] Pets() {
		return new Pet[]{FirstPet, SecondPet, ThirdPet, StrayPet};
	}

	@DataPoints
	public static Owner[] Owners() {
		return new Owner[]{FirstOwner, SecondOwner, ThirdOwner};
	}

	@Theory
	public void get_existing_pet_with_dont_ignore_new_pets_should_not_ignore_new_pets(Owner owner, Pet pet) {
		Assume.assumeTrue(owner == petOwnerMap.get(pet));
		assertEquals(pet, owner.getPet(pet.getName(), false));
	}

	@Theory
	public void get_existing_pet_with_ignore_new_pets_should_ignore_new_pets(Owner owner, Pet pet) {
		Assume.assumeTrue(owner == petOwnerMap.get(pet));
		Assume.assumeTrue(NotNewPets.contains(pet));
		assertEquals(pet, owner.getPet(pet.getName(), true));
		assertEquals(pet, owner.getPet(pet.getName(), false));
	}

	@Theory
	public void get_non_existing_pet_should_return_null(Owner owner, Pet pet) {
		Assume.assumeTrue(owner != petOwnerMap.get(pet));
		assertNull(owner.getPet(pet.getName(), true));
		assertNull(owner.getPet(pet.getName(), false));
	}
}
