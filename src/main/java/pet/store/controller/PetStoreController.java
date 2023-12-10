package pet.store.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import pet.store.controller.model.PetStoreCustomerDTO;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.service.PetStoreService;

@RestController
@RequestMapping("/pet_store")
@Slf4j
public class PetStoreController {

	@Autowired
	private PetStoreService petStoreService;

	@PostMapping("/pet_store")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreData createPetStore(@RequestBody PetStoreData petStoreData) {
		log.info("Creating Pet Store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}

	@PutMapping("/pet_store/{petStoreId}")
	public PetStoreData updatePetStore(@PathVariable Long petStoreId, @RequestBody PetStoreData petStoreData) {
		petStoreData.setPetStoreId(petStoreId);
		log.info("Updating pet store {}", petStoreData);
		return petStoreService.savePetStore(petStoreData);
	}

	@PostMapping("pet_store/{petStoreId}/employee")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreEmployee addAnEmployee(@PathVariable Long petStoreId,
			@RequestBody PetStoreEmployee petStoreEmployee) {
		log.info("Creating an Employee to pet Store ID= {} , Employee: {}", petStoreId, petStoreEmployee);
		return petStoreService.saveEmployee(petStoreId, petStoreEmployee);
	}

	@PostMapping("pet_store/{petStoreId}/customer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public PetStoreCustomerDTO addACustomer(@PathVariable Long petStoreId,
			@RequestBody PetStoreCustomerDTO petStoreCustomerDTO) {
		log.info("Creating a Customer to pet store ID= {}, Customer: {}", petStoreId, petStoreCustomerDTO);
		return petStoreService.saveCustomer(petStoreId, petStoreCustomerDTO);
	}

	@GetMapping("pet_store")
	public List<PetStoreData> getAllPetStores() {
		log.info("Retrieving all Pet Stores");
		return petStoreService.retrieveAllPetStores();
	}

	@GetMapping("pet_store/{petStoreId}")
	public PetStoreData getPetStoreWithId(@PathVariable Long petStoreId) {
		log.info("Retrieving Pet Store with ID= " + petStoreId);
		return petStoreService.retrievePetStoreById(petStoreId);
	}

	@DeleteMapping("pet_store/{petStoreId}")
	public Map<String, String> deletePetStoreById(@PathVariable Long petStoreId) {
		log.info("Deleting contributor with ID= {}", petStoreId);
		petStoreService.deletePetStoreById(petStoreId);
		return Map.of("message", "Deletion of Pet Store with ID= " + petStoreId + " was successful.");

	}
}
