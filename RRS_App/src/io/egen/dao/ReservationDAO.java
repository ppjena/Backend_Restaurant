package io.egen.dao;

import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import io.egen.beans.Reservation;
import io.egen.beans.ReservationStatus;
import io.egen.utils.DAOException;

public class ReservationDAO {

	public ReservationStatus create(String dateString, String timeString, int partySize, int contactNumber)
			throws DAOException {
		try {
			java.sql.Date reservationDate = parseDate(dateString);
			java.sql.Time reservationTime = parseTime(timeString);
			return insert(reservationDate, reservationTime, partySize, contactNumber);
		} catch (ParseException e) {
			throw new DAOException(e);
		}
	}

	public ReservationStatus edit(String dateString, String timeString, int partySize) throws DAOException {
		try {
			java.sql.Date reservationDate = parseDate(dateString);
			java.sql.Time reservationTime = parseTime(timeString);
			return update(reservationDate, reservationTime, partySize);
		} catch (ParseException e) {
			throw new DAOException(e);
		}
	}

	public Reservation get(int confirmationCode) {
		return null;
	}

	public Reservation cancel(int confirmationCode) {
		return null;
	}

	private ReservationStatus insert(Date reservationDate, Time reservationTime, int partySize, int contactNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	private ReservationStatus update(Date reservationDate, Time reservationTime, int partySize) {
		// TODO Auto-generated method stub
		return null;
	}

	private Time parseTime(String timeString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
		java.util.Date date = sdf.parse(timeString);
		return new Time(date.getTime());
	}

	private java.sql.Date parseDate(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date date = sdf.parse(dateString);
		return new java.sql.Date(date.getTime());
	}
}
