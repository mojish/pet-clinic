package org.springframework.samples.petclinic.owner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.LoggerFactory;
import org.springframework.samples.petclinic.utility.PetTimedCache;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.*;


@RunWith(Parameterized.class)
public class PetServiceTest {
	public Integer findingPetId;
	public Pet expectedPet;
	public static PetService petService;
	private static PetTimedCache mockCache;

	public static final Integer NOT_EXISTING_ID = 10;


	private static final List<Pet> pets = Arrays.asList(new Pet(), new Pet(), new Pet());

	public PetServiceTest(Integer findingPetId, Pet expectedPet) {
		this.findingPetId = findingPetId;
		this.expectedPet = expectedPet;
		petService = new PetService(mockCache, null, LoggerFactory.getLogger(PetService.class));
	}


	@Parameterized.Parameters
	public static Object[][] parameters()
	{
		mockCache = mock(PetTimedCache.class);
		for(int i = 0; i < pets.size(); i++) {
			pets.get(i).setId(i);
			when(mockCache.get(i)).thenReturn(pets.get(i));
		}
		when(mockCache.get(NOT_EXISTING_ID)).thenReturn(null);
		return new Object[][]{{0, pets.get(0)}, {1, pets.get(1)}, {NOT_EXISTING_ID, null}};
	}

	@Test
	public void testGetPet() {
		assertEquals(expectedPet, petService.findPet(findingPetId));
	}
}
