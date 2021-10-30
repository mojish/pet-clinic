package org.springframework.samples.petclinic.owner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.springframework.samples.petclinic.owner.DummyGenerator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OwnerTest {
	private Owner owner;

	@Before
	public void setup() {
		owner = new Owner();
	}

	@Test
	public void addPetTestStateVerification() {
		Pet newPet = new Pet();

		owner.addPet(newPet);

		assertEquals(newPet.getOwner(), owner);
		assertTrue(owner.getPets().contains(newPet));
	}

	@Test
	public void addPetTestBehaviorVerification() {
		Pet mockPet = mock(Pet.class);

		when(mockPet.isNew()).thenReturn(true);

		owner.addPet(mockPet);

		verify(mockPet).setOwner(owner);
		assertTrue(owner.getPets().contains(mockPet));
	}
}
