package org.springframework.samples.petclinic.owner;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.utility.PetTimedCache;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;

@WebMvcTest(value = PetController.class,
	includeFilters = {
		@ComponentScan.Filter(value = PetTypeFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = PetService.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = LoggerConfig.class, type = FilterType.ASSIGNABLE_TYPE),
		@ComponentScan.Filter(value = PetTimedCache.class, type = FilterType.ASSIGNABLE_TYPE),
	}
)
class PetControllerTests {
	private static final String petName = "mamad";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PetRepository pets;

	@MockBean
	private OwnerRepository owners;



	@BeforeEach
	void setup() {
		Pet pet = new Pet();
		pet.setId(30);
		PetType type = new PetType();
		type.setName(petName);
		given(this.pets.findPetTypes()).willReturn(Lists.newArrayList(type));
		given(this.owners.findById(1)).willReturn(new Owner());
		given(this.pets.findById(1)).willReturn(pet);
	}

	@Test
	void test_init_create() throws Exception {
		MvcResult result = mockMvc.perform(get("/owners/1/pets/new")).andExpect(
				MockMvcResultMatchers.status().isOk()).andExpect(model().attributeExists("pet"))
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"))
				.andReturn();
		Assertions.assertTrue(result.getResponse().getContentAsString().contains("<html>"));
	}

	@Test
	void test_process_creation_ok() throws Exception {
		mockMvc.perform(post("/owners/1/pets/new").param("name",
			"salam")
			.param("type", petName).param("birthDate", "2000-01-01"))
			.andExpect(status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void test_process_creation_with_error() throws Exception {
		mockMvc.perform(post("/owners/1/pets/new").param("name", "mamad").param("birthDate",
				"2000-01-01")).andExpect(model().attributeHasNoErrors("owner"))
			.andExpect(model().attributeHasErrors("pet")).andExpect(model().attributeHasFieldErrors("pet", "type"))
			.andExpect(model().attributeHasFieldErrorCode("pet", "type", "required")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void test_init_update() throws Exception {
		mockMvc.perform(get("/owners/1/pets/1/edit"))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(model().attributeExists("pet"))
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void test_proccess_update_ok() throws Exception {
		mockMvc.perform(post("/owners/1/pets/1/edit").param("name", "mamad")
				.param("type", petName).param("birthDate", "2000-01-01"))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@Test
	void test_proccess_update_fail() throws Exception {
		mockMvc.perform(post("/owners/1/pets/1/edit").param("name", "mamad")
				).andExpect(model().attributeHasNoErrors("owner"))
			.andExpect(model().attributeHasErrors("pet")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

}
