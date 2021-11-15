package org.springframework.samples.petclinic.model.priceCalculators;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.UserType;

import java.time.Instant;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CustomerDependentPriceCalculatorTest {
	private CustomerDependentPriceCalculator customerDependentPriceCalculator;

	Pet dummyPet1, dummyPet2, dummyPet3, dummyPet4;
	PetType mockPetType1;
	PetType mockPetType2;

	@BeforeEach
	void setup(){
		customerDependentPriceCalculator = new CustomerDependentPriceCalculator();
		dummyPet1 = new Pet();
		dummyPet2 = new Pet();
		dummyPet3 = new Pet();
		dummyPet4 = new Pet();

		dummyPet1.setBirthDate(Date.from(Instant.now()));
		dummyPet2.setBirthDate(Date.from(Instant.now()));
		dummyPet3.setBirthDate(new GregorianCalendar(2000, Calendar.DECEMBER, 11).getTime());
		dummyPet4.setBirthDate(new GregorianCalendar(2000, Calendar.DECEMBER, 11).getTime());

		mockPetType1 = Mockito.mock(PetType.class);
		mockPetType2 = Mockito.mock(PetType.class);
		dummyPet1.setType(mockPetType1);
		dummyPet3.setType(mockPetType1);
		dummyPet2.setType(mockPetType2);
		dummyPet4.setType(mockPetType2);
	}

	@Test
	void test_path_1() {
		Mockito.doReturn(true).when(mockPetType1).getRare();
		Mockito.doReturn(false).when(mockPetType2).getRare();
		double calculatedPrice = customerDependentPriceCalculator.calcPrice(Arrays.asList(dummyPet1, dummyPet2,
			dummyPet3, dummyPet4), 1000, 1000, UserType.GOLD);
	}

	@Test
	void test_path_2() {
		Mockito.doReturn(true).when(mockPetType1).getRare();
		Mockito.doReturn(false).when(mockPetType2).getRare();
		double calculatedPrice = customerDependentPriceCalculator.calcPrice(Arrays.asList(dummyPet1, dummyPet2,
			dummyPet3, dummyPet4), 1000, 1000, UserType.NEW);
	}

	@Test
	void test_path_3() {
		Mockito.doReturn(true).when(mockPetType1).getRare();
		double calculatedPrice = customerDependentPriceCalculator.calcPrice(Arrays.asList(dummyPet1, dummyPet1,
			dummyPet1, dummyPet1, dummyPet1), 1000, 1000, UserType.NEW);
	}

	@Test
	void test_path_4() {
		Mockito.doReturn(true).when(mockPetType1).getRare();
		double calculatedPrice = customerDependentPriceCalculator.calcPrice(Arrays.asList(dummyPet1, dummyPet1,
			dummyPet1, dummyPet1, dummyPet1), 1000, 1000, UserType.GOLD);
	}
}
