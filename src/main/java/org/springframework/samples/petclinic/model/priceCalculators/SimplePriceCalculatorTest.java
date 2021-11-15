package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;
import java.util.Arrays;

class SimplePriceCalculatorTest {

	private SimplePriceCalculator simplePriceCalculator;

	@BeforeEach
	void setup(){
		simplePriceCalculator = new SimplePriceCalculator();
	}

	@Test
	void test_path_1() {
		// setup
		Pet dummyPet1 = new Pet();
		Pet dummyPet2 = new Pet();
		PetType mockPetType1 = Mockito.mock(PetType.class);
		PetType mockPetType2 = Mockito.mock(PetType.class);
		dummyPet1.setType(mockPetType1);
		dummyPet2.setType(mockPetType2);
		Mockito.doReturn(true).when(mockPetType1).getRare();
		Mockito.doReturn(false).when(mockPetType2).getRare();
		double calculatedPrice = simplePriceCalculator.calcPrice(Arrays.asList(dummyPet1, dummyPet2), 1000, 1000, UserType.GOLD);
		Assertions.assertEquals(3200, calculatedPrice);
	}

	@Test
	void testpath_2() {
		double calculatedPrice = simplePriceCalculator.calcPrice(Arrays.asList(), 1000, 1000, UserType.NEW);
		Assertions.assertEquals(950, calculatedPrice);
	}
}
