package pet.store.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pet.store.controller.model.PetStoreCustomerDTO;
import pet.store.controller.model.PetStoreData;
import pet.store.controller.model.PetStoreEmployee;
import pet.store.dao.CustomerDao;
import pet.store.dao.EmployeeDao;
import pet.store.dao.PetStoreDao;
import pet.store.entity.Customer;
import pet.store.entity.Employee;
import pet.store.entity.PetStore;

@Service
public class PetStoreService {

	@Autowired
	private PetStoreDao petStoreDao;

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private CustomerDao customerDao;

	@Transactional(readOnly = false)
	public PetStoreData savePetStore(PetStoreData petStoreData) {
		Long petStoreId = petStoreData.getPetStoreId();
		PetStore petStore = findOrCreatePetStore(petStoreId);

		copyPetStoreFields(petStore, petStoreData);
		return new PetStoreData(petStoreDao.save(petStore));
	}

	private PetStore findOrCreatePetStore(Long petStoreId) {
		if (petStoreId == null) {
			return new PetStore();
		} else {
			return findPetStoreById(petStoreId);
		}
	}

	private PetStore findPetStoreById(Long petStoreId) {
		return petStoreDao.findById(petStoreId)
				.orElseThrow(() -> new NoSuchElementException("Pet store with ID= " + petStoreId + " was not found."));
	}

	private void copyPetStoreFields(PetStore petStore, PetStoreData petStoreData) {
		petStore.setPetStoreName(petStoreData.getPetStoreName());
		petStore.setPetStoreAddress(petStoreData.getPetStoreAddress());
		petStore.setPetStoreCity(petStoreData.getPetStoreCity());
		petStore.setPetStoreState(petStoreData.getPetStoreState());
		petStore.setPetStoreZip(petStoreData.getPetStoreZip());
		petStore.setPetStorePhone(petStoreData.getPetStorePhone());

	}

	@Transactional(readOnly = false)
	public PetStoreEmployee saveEmployee(Long petStoreId, PetStoreEmployee petStoreEmployee) {
		PetStore petStore = findPetStoreById(petStoreId);
		Employee employee = findOrCreateEmployee(petStoreId, petStoreEmployee.getEmployeeId());

		copyEmployeeFields(employee, petStoreEmployee);
		employee.setPetStore(petStore);

		petStore.getEmployees().add(employee);

		return new PetStoreEmployee(employeeDao.save(employee));
	}

	private Employee findOrCreateEmployee(Long petStoreId, Long employeeId) {
		if (employeeId == null) {
			return new Employee();
		} else {
			return findEmployeeById(petStoreId, employeeId);
		}
	}

	private Employee findEmployeeById(Long petStoreId, Long employeeId) {
		Employee employee = employeeDao.findById(employeeId).orElseThrow(
				() -> new NoSuchElementException("Employee with ID= " + employeeId + " could not be found."));

		if (!employee.getPetStore().getPetStoreId().equals(petStoreId)) {
			throw new IllegalArgumentException(
					"Employee with ID= " + employeeId + " does not belong to Pet Store with ID= " + petStoreId);
		}
		return employee;
	}

	private void copyEmployeeFields(Employee employee, PetStoreEmployee petStoreEmployee) {
		employee.setEmployeeFirstName(petStoreEmployee.getEmployeeFirstName());
		employee.setEmployeeLastName(petStoreEmployee.getEmployeeLastName());
		employee.setEmployeePhone(petStoreEmployee.getEmployeePhone());
		employee.setEmployeeJobTittle(petStoreEmployee.getEmployeeJobTittle());

	}

	@Transactional(readOnly = false)
	public PetStoreCustomerDTO saveCustomer(Long petStoreId, PetStoreCustomerDTO petStoreCustomerDTO) {
		PetStore petStore = findPetStoreById(petStoreId);
		Customer customer = findOrCreateCustomer(petStoreId, petStoreCustomerDTO.getCustomerId());

		copyCustomerFields(customer, petStoreCustomerDTO);
		addPetStoreToCustomer(customer, petStore);

		petStore.getCustomers().add(customer);

		return new PetStoreCustomerDTO(customerDao.save(customer));

	}

	private void addPetStoreToCustomer(Customer customer, PetStore petStore) {
		customer.getPetStores().add(petStore);
	}

	private Customer findOrCreateCustomer(Long petStoreId, Long customerId) {
		if (customerId == null) {
			return new Customer();
		} else {
			return findCustomerById(petStoreId, customerId);
		}
	}

	private Customer findCustomerById(Long petStoreId, Long customerId) {
		Customer customer = customerDao.findById(customerId).orElseThrow(
				() -> new NoSuchElementException("Customer with ID= " + customerId + " could not be found."));

		for (PetStore petStore : customer.getPetStores()) {
			if (petStore.getPetStoreId().equals(petStoreId)) {
				return customer;
			}
		}
		throw new IllegalArgumentException(
				"Customer with ID= " + customerId + " is not associated with Pet Store with ID= " + petStoreId);
	}

	private void copyCustomerFields(Customer customer, PetStoreCustomerDTO petStoreCustomerDTO) {
		customer.setCustomerFirstName(petStoreCustomerDTO.getCustomerFirstName());
		customer.setCustomerLastName(petStoreCustomerDTO.getCustomerLastName());
		customer.setCustomerEmail(petStoreCustomerDTO.getCustomerEmail());

	}

	@Transactional(readOnly = true)
	public List<PetStoreData> retrieveAllPetStores() {
		List<PetStore> petStores = petStoreDao.findAll();
		List<PetStoreData> result = new LinkedList<>();

		for (PetStore petStore : petStores) {
			PetStoreData psd = new PetStoreData(petStore);

			psd.getCustomers().clear();
			psd.getEmployees().clear();

			result.add(psd);

		}
		return result;
	}

	@Transactional(readOnly = true)
	public PetStoreData retrievePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);

		if (petStore != null) {
			PetStoreData petStoreData = new PetStoreData();
			petStoreData.setPetStoreId(petStore.getPetStoreId());
			petStoreData.setPetStoreName(petStore.getPetStoreName());
			petStoreData.setPetStoreAddress(petStore.getPetStoreAddress());
			petStoreData.setPetStoreCity(petStore.getPetStoreCity());
			petStoreData.setPetStoreState(petStore.getPetStoreState());
			petStoreData.setPetStoreZip(petStore.getPetStoreZip());
			petStoreData.setPetStorePhone(petStore.getPetStorePhone());

			return petStoreData;
		} else {
			return null;
		}
	}

	@Transactional(readOnly = false)
	public void deletePetStoreById(Long petStoreId) {
		PetStore petStore = findPetStoreById(petStoreId);
		petStoreDao.delete(petStore);
	}

}
