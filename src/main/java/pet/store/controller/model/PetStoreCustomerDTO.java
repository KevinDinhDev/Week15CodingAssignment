package pet.store.controller.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import pet.store.entity.Customer;

@Data
@AllArgsConstructor
public class PetStoreCustomerDTO {

	private Long customerId;
	private String customerFirstName;
	private String customerLastName;
	private String customerEmail;

	public PetStoreCustomerDTO(Customer customer) {
		this.customerId = customer.getCustomerId();
		this.customerFirstName = customer.getCustomerFirstName();
		this.customerLastName = customer.getCustomerLastName();
		this.customerEmail = customer.getCustomerEmail();
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
}
