package io.egen.beans;

public class ReservationStatus {
	enum Status {
		WAITING, CONFIRMED
	};

	private Status reservationStatus;
	private int confirmationCode;
	private int waitingNumber;
}
