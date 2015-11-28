package io.egen.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import io.egen.beans.Reservation;
import io.egen.beans.ReservationStatus;
import io.egen.utils.DAOException;
import io.egen.utils.DBUtils;

public class ReservationDAO {

	public ReservationStatus create(String dateString, String timeString, String partySize, String contactNumber)
			throws DAOException {
		try {
			java.sql.Date reservationDate = parseDate(dateString);
			java.sql.Time reservationTime = parseTime(timeString);
			return insert(reservationDate, reservationTime, 
					Integer.parseInt(partySize), contactNumber);
		} catch (ParseException | SQLException e) {
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

	private int getNumberOfReservations(Date reservationDate, Time reservationTime, Connection con)
			throws SQLException {
		try (PreparedStatement s = con.prepareStatement(getReservationsQuery)) {
			s.setDate(1, reservationDate);
			s.setTime(2, reservationTime);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
			throw new AssertionError("Invalid Server state");
		}
		
	}
	private ReservationStatus insert(Date reservationDate, Time reservationTime, 
			int partySize, String contactNumber)
			throws SQLException {
		try (Connection con = DBUtils.connect();
			PreparedStatement s = con.prepareStatement(insertQuery))
		{
			ReservationStatus returnStatus = null;
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			con.setAutoCommit(false);
			int numberOfReservations = getNumberOfReservations(reservationDate, reservationTime, con);
			if(numberOfReservations == 4){
				return new ReservationStatus(ReservationStatus.Status.WAITLIST_FULL,"",0);
			}
			returnStatus = setValuesOnPreparedStatement
					(reservationDate, reservationTime, partySize,
							contactNumber, s, numberOfReservations);
			System.out.println(s);
			s.execute();
			con.commit();
			return returnStatus;
		}
	}

	private ReservationStatus setValuesOnPreparedStatement(Date reservationDate, Time reservationTime, int partySize,
			String contactNumber, PreparedStatement s, int numberOfReservations) throws SQLException {
		ReservationStatus returnStatus;
		s.setDate(1, reservationDate);
		s.setTime(2, reservationTime);
		s.setInt(3, partySize);
		s.setString(4, contactNumber);
		String UUIDString = UUID.randomUUID().toString();
		String confirmationCode = String.valueOf(UUIDString);
		s.setString(5, confirmationCode);
		if(numberOfReservations != 0){
			s.setString(6, ReservationStatus.Status.WAITING.toString());
			s.setInt(7, numberOfReservations);
			returnStatus = new ReservationStatus(ReservationStatus.Status.WAITING,confirmationCode,numberOfReservations);
		}else{
			s.setString(6, ReservationStatus.Status.CONFIRMED.toString());
			s.setInt(7, 0);
			returnStatus = new ReservationStatus(ReservationStatus.Status.CONFIRMED,confirmationCode,0);
		}
		return returnStatus;
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
		System.out.println(dateString);
		java.util.Date date = sdf.parse(dateString);
		return new java.sql.Date(date.getTime());
	}
	private static final String insertQuery = "insert into rrs_db.reservation "
			+ "(DATE,TIME,PARTYSIZE,PHONENUMBER,CONFIRMATIONCODE,STATUS,QUEUENUMBER) values"
			+ "(?,?,?,?,?,?,?)";
	
	private static final String getReservationsQuery = "select count(*) from rrs_db.reservation where DATE = ? AND TIME = ?";
}
