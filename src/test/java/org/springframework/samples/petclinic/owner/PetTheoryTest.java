package org.springframework.samples.petclinic.owner;


import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;
import org.springframework.samples.petclinic.visit.Visit;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(Theories.class)
public class PetTheoryTest {

	private static final int NUMBER_OF_VISITS = 100;
	private static List<Visit> visits;
	private static Pet pet;

	private static final Set<Visit> VISIT_SET = new HashSet<>();

	private static void addVisit(Visit visit) {
		pet.addVisit(visit);
		VISIT_SET.add(visit);
	}

	@BeforeClass
	public static void setup() {
		pet = new Pet();
		pet.setId(1);

		visits = new ArrayList<>();
		for (int i = 0; i < NUMBER_OF_VISITS; ++i) {
			Visit visit = new Visit();
			visit.setDate(LocalDate.now().plusDays(new Random().nextInt(100000)));
			visits.add(visit);
			if (i % 2 == 0) {
				addVisit(visit);
			}
		}
	}

	@DataPoints
	public static List<Visit> Visits() {
		return visits;
	}

	@Theory
	public void get_visits_should_return_visits_in_sorted_order()
	{
		List<Visit> sortedVisits = pet.getVisits();

		for(int i = 0; i < sortedVisits.size() - 1; i++){
			Assert.assertFalse(!sortedVisits.get(i).getDate().isBefore(sortedVisits.get(i + 1).getDate()));
		}
	}

	@Theory
	public void get_visits_should_contain_added_visit(Visit visit) {
		Assume.assumeTrue(VISIT_SET.contains(visit));
		List<Visit> sortedVisits = pet.getVisits();
		assertTrue(sortedVisits.contains(visit));
	}

	@Theory
	public void get_visits_should_not_contain_not_added_visit(Visit visit) {
		Assume.assumeFalse(VISIT_SET.contains(visit));
		List<Visit> sortedVisits = pet.getVisits();
		assertFalse(sortedVisits.contains(visit));
	}
}
