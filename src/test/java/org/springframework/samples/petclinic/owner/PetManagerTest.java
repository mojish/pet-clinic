package org.springframework.samples.petclinic.owner;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class PetManagerTest {
	//Spy
	@Spy
	private Logger logger;

	//Stub
	@Mock
	private OwnerRepository owners;

	//Mock
	@Mock
	private PetTimedCache petTimedCache;

	private static Map<Integer, Pet> pets = new HashMap<>();
	private static int currentPetCnt = 0;

	private PetManager petManager;

	private static final Integer TEST_PET_ID = 1;
	private static final Integer TEST_OWNER_ID = 1;

	@BeforeAll
	static void makePets(){
		Pet pet1 = DummyGenerator.getNewDummyPet();
		pets.put(1, Mockito.spy(pet1));
		Pet pet2 = DummyGenerator.getNewDummyPet();
		pets.put(2, Mockito.spy(pet2));
		pets.put(3, null);
		currentPetCnt = 3;
	}

	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		Mockito.doAnswer(invocationOnMock -> pets.get((Integer) invocationOnMock.getArgument(0))).when(petTimedCache).get(anyInt());
		Mockito.doAnswer(invocationOnMock -> pets.put(++currentPetCnt, invocationOnMock.getArgument(0)))
			.when(petTimedCache).save(any(Pet.class));
		Mockito.doNothing().when(logger).info(Mockito.anyString(), any(Object.class));
		petManager = new PetManager(petTimedCache, owners, logger);
	}

	/*
	 * Test doubles: mock, dummy
	 * Verification method: behavior
	 */
	@Test
	void findValidOwnerTest(){
		// Setup
		Owner dummyOwner = new Owner();
		when(owners.findById(TEST_OWNER_ID)).thenReturn(dummyOwner);

		// Exercise
		Owner foundOwner = petManager.findOwner(TEST_OWNER_ID);

		// Verify
		assertNotNull(foundOwner);
		assertEquals(foundOwner, dummyOwner);
	}

	// State Verification
	// using a Stub
	@Test
	void findInValidOwnerTest(){
		// Setup
		when(owners.findById(TEST_OWNER_ID)).thenReturn(null);

		// Exercise
		Owner foundOwner = petManager.findOwner(TEST_OWNER_ID);

		// Verify
		assertNull(foundOwner);
	}

	/*
	 * Test doubles: mock
	 * Verification method: behavior
	 */	@Test()
	void newPetTestForNullOwner(){
		// Setup
		Owner owner = null;

		// Exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.newPet(owner));
	}

	/*
	 * Test doubles: mock, spy
	 * Verification method: behavior
	 */
	@Test()
	void newPetTestForValidOwner(){
		// Setup
		Owner owner = Mockito.spy(Owner.class);
		Mockito.doNothing().when(owner).addPet(any(Pet.class));

		// Exercise
		Pet createdPet = petManager.newPet(owner);

		// Verify
		Mockito.verify(owner).addPet(createdPet);
	}


	/*
	 * Test doubles: mock
	 * Verification method: behavior + verification
	 */
	@Test()
	void findInvalidPetTest(){
		// Exercise
		Pet foundPet = petManager.findPet(currentPetCnt + 100);

		// Verify
		Mockito.verify(petTimedCache).get(currentPetCnt + 100);
		assertNull(foundPet);
	}

	/*
	 * Test doubles: mock
	 * Verification method: behavior + verification
	 */
	@Test()
	void findValidPetTest(){
		// Exercise
		Pet pet = petManager.findPet(TEST_PET_ID);

		// Verify
		assertNotNull(pet);
		Mockito.verify(petTimedCache).get(TEST_PET_ID);
		assertEquals(pet, pets.get(TEST_PET_ID));
	}

	/*
	 * Test doubles: mock, dummy
	 * Verification method: behavior
	 */
	@Test
	void savePetInValidOwnerTest(){
		// Setup
		Pet dummyPet = Mockito.mock(Pet.class);

		// Exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.savePet(dummyPet, null));
	}

	/*
	 * Test doubles: mock, dummy
	 * Verification method: behavior
	 */
	@Test
	void savePetInValidPetTest(){
		// Setup
		Owner owner = Mockito.spy(Owner.class);
		Mockito.doNothing().when(owner).addPet(any(Pet.class));

		// Exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.savePet(null, owner));
	}


	/*
	 * Test doubles: mock, dummy
	 * Verification method: behavior
	 */
	@Test
	void savePetValidTest(){
		// Setup - data (Spy and Dummy)
		Owner owner = Mockito.spy(Owner.class);
		Pet dummyPet = Mockito.mock(Pet.class);
		Mockito.doNothing().when(owner).addPet(any(Pet.class));

		// Exercise
		petManager.savePet(dummyPet, owner);

		// Verify
		assertEquals(dummyPet,pets.get(currentPetCnt));
		Mockito.verify(owner).addPet(dummyPet);
	}

	/*
	 * Test doubles: mock, dummy
	 * Verification method: behavior
	 */
	@Test
	void getOwnerPetsValidTest(){
		// Setup
		Owner mockedOwner = Mockito.mock(Owner.class);
		Mockito.doReturn(Arrays.asList(pets.get(2), pets.get(1))).when(mockedOwner).getPets();
		Mockito.doReturn(mockedOwner).when(owners).findById(TEST_OWNER_ID);

		// Exercise
		List<Pet> foundPets = petManager.getOwnerPets(TEST_OWNER_ID);

		// Verify
		assertEquals(2, foundPets.size());
		Mockito.verify(mockedOwner).getPets();
	}

	/*
	 * Test doubles: stub
	 * Verification method: behavior
	 */
	@Test
	void getOwnerPetsInValidTest()
	{
		// Setup
		Mockito.doReturn(null).when(owners).findById(TEST_OWNER_ID);

		// Exercise & verify
		assertThrows(NullPointerException.class, () -> petManager.getOwnerPets(1));
	}

	/*
	 * Test doubles: stub
	 * Verification method: behavior
	 */
	@Test
	void getOwnerPetTypessValidTest(){
		// Setup
		Owner stubbedOwner = Mockito.mock(Owner.class);
		List<Pet> stubbedOwnerPetsList = new ArrayList<>();
		Pet pet1 = DummyGenerator.getNewDummyPet();
		Pet pet2 = DummyGenerator.getNewDummyPet();
		stubbedOwnerPetsList.add(Mockito.spy(pet1));
		stubbedOwnerPetsList.add(Mockito.spy(pet2));
		Mockito.doReturn(stubbedOwner).when(owners).findById(TEST_OWNER_ID);
		Mockito.doReturn(stubbedOwnerPetsList).when(stubbedOwner).getPets();

		// Exercise
		Set<PetType> petTypeSet = petManager.getOwnerPetTypes(TEST_OWNER_ID);

		// Verify
		assertEquals(2, petTypeSet.size());
	}

	/*
	 * Test doubles: spy, dummy, mock
	 * Verification method: behavior
	 */
	@Test
	void getVisitsBetweenTest(){
		// Setup
		Visit visit1 = Mockito.mock(Visit.class);
		Visit visit2 = Mockito.mock(Visit.class);
		LocalDate date = LocalDate.now();
		List<Visit> petVisits = new ArrayList<>(Arrays.asList(visit1, visit2));
		Mockito.doReturn(petVisits).when(pets.get(1)).getVisitsBetween(any(LocalDate.class), any(LocalDate.class));


		// Exercise
		List<Visit> returnedVisits = petManager.getVisitsBetween(TEST_PET_ID, date, date);

		// Verify
		assertEquals(returnedVisits, petVisits);
		Mockito.verify(pets.get(1)).getVisitsBetween(date,date);
	}
}
