package io.egen.beans;

public class ReservationBean {

	public ReservationBean(String date, String time, int partySize, String contactNumber,
			ReservationStatusBean reservationStatus) {
		this.date = date;
		this.time = time;
		this.partySize = partySize;
		this.contactNumber = contactNumber;
		this.reservationStatus = reservationStatus;
	}

	public String getDate() {
		return date;
	}
	public String getTime() {
		return time;
	}
	public int getPartySize() {
		return partySize;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public ReservationStatusBean getReservationStatus() {
		return reservationStatus;
	}

	private String date;
	private String time;
	private int partySize;
	private String contactNumber;
	private ReservationStatusBean reservationStatus;
}
