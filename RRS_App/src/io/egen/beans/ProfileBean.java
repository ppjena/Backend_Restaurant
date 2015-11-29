package io.egen.beans;

public class ProfileBean {
	
	public ProfileBean(String name, String contact, String email, String address, int autoAssign, String opening,
			String closing, String openingDays) {
		super();
		this.name = name;
		this.contact = contact;
		this.email = email;
		this.address = address;
		this.autoAssign = autoAssign;
		this.opening = opening;
		this.closing = closing;
		this.openingDays = openingDays;
	}
	
	private String name;
	public String getName() {
		return name;
	}
	public String getContact() {
		return contact;
	}
	public String getEmail() {
		return email;
	}
	public String getAddress() {
		return address;
	}
	public int getAutoAssign() {
		return autoAssign;
	}
	public String getOpening() {
		return opening;
	}
	public String getClosing() {
		return closing;
	}
	public String getOpeningDays() {
		return openingDays;
	}

	private String contact;
	private String email;
	private String address;
	private int autoAssign;
	private String opening;
	private String closing;
	private String openingDays;
	

}
