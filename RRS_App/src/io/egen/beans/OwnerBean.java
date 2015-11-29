package io.egen.beans;

public class OwnerBean {

	public OwnerBean(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	private String firstName;
	private String lastName;
}
