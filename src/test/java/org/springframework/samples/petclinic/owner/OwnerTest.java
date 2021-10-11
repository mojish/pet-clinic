package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OwnerTest {
	private Owner owner;

	private static final String TEST_ADDRESS = "Kamranieh";
	private static final String TEST_CITY = "Tehran";
	private static final String TEST_TELEPHONE = "26129999";

	private static Pet pet1 = new Pet();
	private static Pet pet2 = new Pet();
	private static Pet pet3 = new Pet();
	private static Pet pet4 = new Pet();

	private static List<Pet> pets = new ArrayList<>(Arrays.asList(pet1, pet2, pet3, pet4));

	@BeforeEach
	public void setup() {
		owner = new Owner();
	}

	public void setUpPets() {
		pet1.setName("B");
		pet2.setName("d");
		pet3.setName("A");
		pet4.setName("c");

		for (Pet pet: pets) {
			owner.addPet(pet);
		}
	}

	@Test
	public void testSetGetAddress() {
		owner.setAddress(TEST_ADDRESS);
		assertEquals(owner.getAddress(), TEST_ADDRESS);
	}
	@Test
	public void testSetGetCity() {
		owner.setCity(TEST_CITY);
		assertEquals(owner.getCity(), TEST_CITY);
	}
	@Test
	public void testSetGetTelephone() {
		owner.setTelephone(TEST_TELEPHONE);
		assertEquals(owner.getTelephone(), TEST_TELEPHONE);
	}

	@Test
	public void testGetPetsShouldContainAllPets() {
		setUpPets();
		assertEquals(pets.size(), owner.getPets().size());
		assertTrue(owner.getPets().containsAll(pets));
		assertTrue(pets.containsAll(owner.getPets()));
	}

	@Test
	public void testGetPetsShouldReturnSortedPets()
	{
		setUpPets();
		PropertyComparator.sort(pets, new MutableSortDefinition("name", true, true));
		assertTrue(owner.getPets().equals(pets));
	}

	@Test
	public void testAddPetShouldContainPet() {
		Pet pet = new Pet();
		List<Pet> petsBefore = owner.getPets();
		List<Pet> petsAfter = new ArrayList<>(petsBefore);
		petsAfter.add(pet);
		owner.addPet(pet);
		assertEquals(petsAfter.size(), owner.getPets().size());
		assertTrue(owner.getPets().containsAll(petsAfter));
		assertTrue(petsAfter.containsAll(owner.getPets()));
	}

	@Test
	public  void testAddPetShouldSetOwner()
	{
		Pet pet = new Pet();
		owner.addPet(pet);
		assertEquals(owner, pet.getOwner());
	}

	@Test
	public void testAddPetShouldNotAddExisting()
	{
		Pet pet = new Pet();
		pet.setId(1);
		List<Pet> petsBefore = owner.getPets();
		owner.addPet(pet);
		assertEquals(petsBefore.size(), owner.getPets().size());
		assertTrue(owner.getPets().containsAll(petsBefore));
		assertTrue(petsBefore.containsAll(owner.getPets()));
	}

	@Test
	public void testRemovePet() {
		setUpPets();
		List<Pet> petsBefore = owner.getPets();
		owner.removePet(pet1);
		List<Pet> petsAfter = new ArrayList<>(petsBefore);
		petsAfter.remove(pet1);
		assertEquals(petsAfter.size(), owner.getPets().size());
		assertTrue(owner.getPets().containsAll(petsAfter));
		assertTrue(petsAfter.containsAll(owner.getPets()));
	}

	@Test
	public void testGetPetShouldReturnNamedPet() {
		Pet pet1 = new Pet();
		pet1.setName("Mahsa");
		owner.addPet(pet1);
		assertEquals(pet1, owner.getPet("Mahsa"));
	}

	@Test
	public void testGetPetShouldReturnNullForUnnamedPet() {
		assertEquals(null, owner.getPet("Bo"));
	}

	@Test
	public void testGetPetShouldReturnNullForExisting()
	{
		Pet pet = new Pet();
		pet.setName("Mahsa");
		pet.setId(1);
		owner.addPet(pet);
		assertEquals(null, owner.getPet("Mahsa", false));
		assertEquals(null, owner.getPet("Mahsa", true));
	}

	@Test
	public void testGetPetShouldReturnNullForNewPetWithIgnoreNewTrue()
	{
		Pet pet = new Pet();
		pet.setName("Mahsa");
		owner.addPet(pet);
		assertEquals(null, owner.getPet("Mahsa", true));
	}

	@Test
	public void testGetPetShouldReturnPetForNewPetWithIgnoreNewFalse()
	{
		Pet pet = new Pet();
		pet.setName("Mahsa");
		owner.addPet(pet);
		assertEquals(pet, owner.getPet("Mahsa", false));
	}

	@Test
	public void testGetPetShouldReturnPetForNewPetWithSetIDAfter()
	{
		Pet pet = new Pet();
		pet.setName("Mahsa");
		owner.addPet(pet);
		pet.setId(1);
		assertEquals(pet, owner.getPet("Mahsa", false));
		assertEquals(pet, owner.getPet("Mahsa", true));
	}
}
