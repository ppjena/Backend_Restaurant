package io.egen.beans;

public class ReservationStatusBean {
	
	public ReservationStatusBean() {
	}
	
	public ReservationStatusBean(Status reStatus,String confirmationCode,int waitingNumber) {
		this.reservationStatus = reStatus;
		this.confirmationCode = confirmationCode;
		this.waitingNumber = waitingNumber;
	}

	public enum Status {
		WAITING, CONFIRMED, WAITLIST_FULL
	};

	private Status reservationStatus;
	private String confirmationCode;
	private int waitingNumber;
	
	public Status getReservationStatus() {
		return reservationStatus;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public int getWaitingNumber() {
		return waitingNumber;
	}
}
