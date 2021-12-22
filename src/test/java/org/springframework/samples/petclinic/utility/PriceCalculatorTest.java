package org.springframework.samples.petclinic.utility;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.YEARS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.samples.petclinic.utility.PriceCalculator.*;

class PriceCalculatorTest {
	private static final double RARE_INFANCY_COEF = 1.4;
	private static final double BASE_RARE_COEF = 1.2;
	private static final int DISCOUNT_MIN_SCORE = 10;
	private static final int DISCOUNT_PRE_VISIT = 2;
	private static final int BASE_CHARGE = 15000;
	private static final int BASE_PRICE_PER_PET = 20000;
	private static final double DELTA = 0.01;
	private static final int ADULT_AGE = 3;
	private static final int INFANT_AGE = 1;
	private static final int OLD_VISIT_THRESH = 100;
	private static final double SINGLE_PET_PRICE = BASE_PRICE_PER_PET * BASE_RARE_COEF;
	private static final double SINGLE_INFANT_PET_PRICE = BASE_PRICE_PER_PET * BASE_RARE_COEF * RARE_INFANCY_COEF;

	@Test
	public void test_1() {
		final double actualPrice = calcPrice(Collections.emptyList(), BASE_CHARGE, BASE_PRICE_PER_PET);
		assertEquals(0, actualPrice, DELTA);
	}

	@Test
	public void test_2() {
		Pet pet = mock(Pet.class);

		when(pet.getBirthDate()).thenReturn(LocalDate.now().minusYears(ADULT_AGE));
		when(pet.getVisitsUntilAge(ADULT_AGE)).thenReturn(Collections.emptyList());

		final double actualPrice = calcPrice(Collections.singletonList(pet), BASE_CHARGE, BASE_PRICE_PER_PET);
		assertEquals(SINGLE_PET_PRICE, actualPrice, DELTA);
	}

	@Test
	public void test_3() {
		Pet pet = mock(Pet.class);

		when(pet.getBirthDate()).thenReturn(LocalDate.now().minusYears(ADULT_AGE));
		when(pet.getVisitsUntilAge(ADULT_AGE)).thenReturn(Collections.emptyList());

		List<Pet> pets = new ArrayList<>();
		for (int i = 0; i < DISCOUNT_MIN_SCORE - 1; ++i)
			pets.add(pet);
		final double actualPrice = calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET);
		final double expectedPrice = pets.size() * SINGLE_PET_PRICE;
		assertEquals(expectedPrice, actualPrice, DELTA);
	}


	@Test
	public void test_4() {
		Pet pet2 = mock(Pet.class);

		when(pet2.getBirthDate()).thenReturn(LocalDate.now().minusYears(INFANT_AGE));
		when(pet2.getVisitsUntilAge(INFANT_AGE)).thenReturn(Collections.emptyList());

		final double actualPrice = calcPrice(Collections.singletonList(pet2), BASE_CHARGE, BASE_PRICE_PER_PET);
		assertEquals(SINGLE_INFANT_PET_PRICE, actualPrice, DELTA);
	}



	@Test
	public void test_5() {
		Pet pet2 = mock(Pet.class);

		when(pet2.getBirthDate()).thenReturn(LocalDate.now().minusYears(INFANT_AGE));
		Visit visit = new Visit();
		List<Visit> visits = Arrays.asList(visit, visit, visit);

		when(pet2.getVisitsUntilAge(INFANT_AGE)).thenReturn(visits);
		List<Pet> pets = new ArrayList<>();
		for (int i = 0; i < DISCOUNT_MIN_SCORE/2; ++i)
			pets.add(pet2);
		final double actualPrice = calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET);
		final double priceBeforeMinScore = (pets.size() - 1) * SINGLE_INFANT_PET_PRICE;
		final double expectedPrice = priceBeforeMinScore * DISCOUNT_PRE_VISIT + BASE_CHARGE + SINGLE_INFANT_PET_PRICE;
		assertEquals(expectedPrice, actualPrice, DELTA);
	}

	@Test
	public void test_6() {
		Pet pet = mock(Pet.class);

		when(pet.getBirthDate()).thenReturn(LocalDate.now().minusYears(ADULT_AGE));

		Visit visit = new Visit();
		List<Visit> visits = Arrays.asList(visit, visit, visit);

		when(pet.getVisitsUntilAge(ADULT_AGE)).thenReturn(visits);

		List<Pet> pets = new ArrayList<>();
		for (int i = 0; i < DISCOUNT_MIN_SCORE; ++i)
			pets.add(pet);
		final double actualPrice = calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET);
		final double priceBeforeMinScore = (DISCOUNT_MIN_SCORE - 1) * SINGLE_PET_PRICE;
		final double expectedPrice = priceBeforeMinScore * DISCOUNT_PRE_VISIT + BASE_CHARGE + SINGLE_PET_PRICE;
		assertEquals(expectedPrice, actualPrice, DELTA);
	}

	@Test
	public void test_7() {

		Pet pet2 = mock(Pet.class);

		when(pet2.getBirthDate()).thenReturn(LocalDate.now().minusYears(INFANT_AGE));
		when(pet2.getVisitsUntilAge(INFANT_AGE)).thenReturn(Collections.emptyList());

		Visit visit = new Visit();

		final int OLD_VISIT_DAYS = OLD_VISIT_THRESH + 1;
		final Visit oldVisit = new Visit().setDate(LocalDate.now().minusDays(OLD_VISIT_DAYS));
		final List<Visit> visits = Arrays.asList(visit, visit, oldVisit, oldVisit);
		when(pet2.getVisitsUntilAge(INFANT_AGE)).thenReturn(visits);
		List<Pet> pets = new ArrayList<>();
		for (int i = 0; i < DISCOUNT_MIN_SCORE/2; ++i)
			pets.add(pet2);
		final double actualPrice = calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET);
		final double priceBeforeMinScore = (pets.size() - 1) * SINGLE_INFANT_PET_PRICE;
		final int oldVisitDiscount = OLD_VISIT_DAYS/OLD_VISIT_THRESH + visits.size();
		final double expectedPrice = (priceBeforeMinScore + BASE_CHARGE) * oldVisitDiscount + SINGLE_INFANT_PET_PRICE;
		assertEquals(expectedPrice, actualPrice, DELTA);
	}

	@Test
	public void test_8() {
		Pet pet = mock(Pet.class);

		when(pet.getBirthDate()).thenReturn(LocalDate.now().minusYears(ADULT_AGE));
//		when(pet.getVisitsUntilAge(ADULT_AGE)).thenReturn(Collections.emptyList());

		Visit visit = new Visit();
		List<Visit> visits = Arrays.asList(visit, visit, visit);
		when(pet.getVisitsUntilAge(ADULT_AGE)).thenReturn(visits);
		List<Pet> pets = new ArrayList<>();
		for (int i = 0; i < DISCOUNT_MIN_SCORE + 2; ++i)
			pets.add(pet);
		final double actualPrice = calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET);
		final double priceBeforeMinScore = (DISCOUNT_MIN_SCORE - 1) * SINGLE_PET_PRICE;
		final double priceAfterFirstDiscount = priceBeforeMinScore * DISCOUNT_PRE_VISIT + BASE_CHARGE + SINGLE_PET_PRICE;
		final double priceAfterSecondDiscount = priceAfterFirstDiscount * DISCOUNT_PRE_VISIT + BASE_CHARGE + SINGLE_PET_PRICE;
		final double expectedPrice = priceAfterSecondDiscount * DISCOUNT_PRE_VISIT + BASE_CHARGE + SINGLE_PET_PRICE;
		assertEquals(expectedPrice, actualPrice, DELTA);
	}

	@Test
	public void test_10() {
		Pet pet = mock(Pet.class);

		when(pet.getBirthDate()).thenReturn(LocalDate.now().minusYears(ADULT_AGE));
		final int VERY_OLD_VISIT_DAYS = OLD_VISIT_THRESH + 279;
		final Visit veryOldVisit = new Visit().setDate(LocalDate.now().minusDays(VERY_OLD_VISIT_DAYS));
		final List<Visit> visits = Collections.singletonList(veryOldVisit);
		when(pet.getVisitsUntilAge(ADULT_AGE)).thenReturn(visits);
		List<Pet> pets = new ArrayList<>();
		for (int i = 0; i < DISCOUNT_MIN_SCORE; ++i)
			pets.add(pet);
		final double actualPrice = calcPrice(pets, BASE_CHARGE, BASE_PRICE_PER_PET);
		final double priceBeforeMinScore = (DISCOUNT_MIN_SCORE - 1) * SINGLE_PET_PRICE;
		final int oldVisitDiscount = VERY_OLD_VISIT_DAYS/OLD_VISIT_THRESH + visits.size();
		final double expectedPrice = (priceBeforeMinScore + BASE_CHARGE) * oldVisitDiscount + SINGLE_PET_PRICE;
		assertEquals(expectedPrice, actualPrice, DELTA);
	}
}
