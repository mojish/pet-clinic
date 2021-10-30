package org.springframework.samples.petclinic.owner;

public class DummyGenerator {

	private static Integer id = 0;

	public static Pet getNewDummyPet(){
		Pet pet = new Pet();
		pet.setId(id);
		PetType petType =  new PetType();
		petType.setName("type" + id);
		pet.setType(petType);
		pet.setName("name" + id);
		id++;
		return pet;
	}
}
