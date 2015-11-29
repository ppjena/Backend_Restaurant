package io.egen.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import io.egen.beans.ReservationBean;
import io.egen.beans.ReservationStatusBean;
import io.egen.utils.DAOException;
import io.egen.utils.DBUtils;
import io.egen.utils.DateTImeUtil;

public class ReservationDAO {

	public ReservationBean create(String dateString, String timeString, String partySize, String contactNumber)
			throws DAOException {
		try {
			java.sql.Date reservationDate = DateTImeUtil.parseDate(dateString);
			java.sql.Time reservationTime = DateTImeUtil.parseTime(timeString);
			return insert(reservationDate, reservationTime, Integer.parseInt(partySize), contactNumber);
		} catch (ParseException | SQLException e) {
			throw new DAOException(e);
		}
	}

	public ReservationStatusBean edit(String confirmationCode, String dateString, String timeString, int partySize)
			throws DAOException {
		try {
			java.sql.Date reservationDate = DateTImeUtil.parseDate(dateString);
			java.sql.Time reservationTime = DateTImeUtil.parseTime(timeString);
			return update(confirmationCode, reservationDate, reservationTime, partySize);
		} catch (ParseException e) {
			throw new DAOException(e);
		}
	}

	public ReservationBean get(int confirmationCode) {
		return null;
	}

	public ReservationBean cancel(int confirmationCode) {
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

	private ReservationBean insert(Date reservationDate, Time reservationTime, int partySize, String contactNumber)
			throws SQLException {
		try (Connection con = DBUtils.connect(); PreparedStatement s = con.prepareStatement(insertQuery)) {
			ReservationBean returnStatus = null;
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			con.setAutoCommit(false);
			int numberOfReservations = getNumberOfReservations(reservationDate, reservationTime, con);
			if (numberOfReservations == 4) {
				return new ReservationBean(reservationDate.toString(), reservationTime.toString(), partySize,
						contactNumber, new ReservationStatusBean(ReservationStatusBean.Status.WAITLIST_FULL, "", 0));
			}
			returnStatus = setValuesOnPreparedStatement(reservationDate, reservationTime, partySize, contactNumber, s,
					numberOfReservations);
			System.out.println(s);
			s.execute();
			con.commit();
			return returnStatus;
		}
	}

	private ReservationBean setValuesOnPreparedStatement(Date reservationDate, Time reservationTime,
			int partySize, String contactNumber, PreparedStatement s, int numberOfReservations) throws SQLException {
		ReservationBean returnStatus;
		s.setDate(1, reservationDate);
		s.setTime(2, reservationTime);
		s.setInt(3, partySize);
		s.setString(4, contactNumber);
		String UUIDString = UUID.randomUUID().toString();
		String confirmationCode = String.valueOf(UUIDString);
		s.setString(5, confirmationCode);
		if (numberOfReservations != 0) {
			s.setString(6, ReservationStatusBean.Status.WAITING.toString());
			s.setInt(7, numberOfReservations);
			returnStatus = new ReservationBean(reservationDate.toString(), reservationTime.toString(), partySize,
					contactNumber,
					new ReservationStatusBean(ReservationStatusBean.Status.WAITING, confirmationCode,
					numberOfReservations));
		} else {
			s.setString(6, ReservationStatusBean.Status.CONFIRMED.toString());
			s.setInt(7, 0);
			returnStatus = new ReservationBean(reservationDate.toString(), reservationTime.toString(), partySize,
					contactNumber,
					new ReservationStatusBean(ReservationStatusBean.Status.CONFIRMED, confirmationCode, 0));
		}
		return returnStatus;
	}

	private ReservationStatusBean update(String confirmationCode, Date reservationDate, Time reservationTime,
			int partySize) {
		return null;
	}

	private static final String insertQuery = "insert into rrs_db.reservation "
			+ "(DATE,TIME,PARTYSIZE,PHONENUMBER,CONFIRMATIONCODE,STATUS,QUEUENUMBER) values" + "(?,?,?,?,?,?,?)";

	private static final String getReservationsQuery = "select count(*) from rrs_db.reservation where DATE = ? AND TIME = ? AND CANCELLED = 0";
	// private static final String getReservationFromCCQuery = "select * from
	// rrs_db.reservation where DATE = ? AND TIME = ? AND CANCELLED = 0";
}