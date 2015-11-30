package io.egen.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.egen.beans.ReservationBean;
import io.egen.beans.ReservationStatusBean;
import io.egen.dao.owner.ProfileDAO;
import io.egen.dao.owner.SeatingDAO;
import io.egen.utils.DAOException;
import io.egen.utils.DBUtils;
import io.egen.utils.DateTImeUtil;

public class ReservationDAO {

	public ReservationBean create(String dateString, String timeString, String partySize, String contactNumber)
			throws DAOException {
		try (Connection con = DBUtils.connect()) {
			java.sql.Date reservationDate = DateTImeUtil.parseDate(dateString);
			java.sql.Time reservationTime = DateTImeUtil.parseTime(timeString);
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			con.setAutoCommit(false);
			ReservationBean reservationBean = insert(reservationDate, reservationTime, Integer.parseInt(partySize),
					contactNumber, con);
			con.commit();
			return reservationBean;
		} catch (ParseException | SQLException e) {
			throw new DAOException(e);
		}
	}

	public ReservationBean edit(String confirmationCode, String dateString, String timeString, int partySize,
			String contactNumber) throws DAOException {
		try (Connection con = DBUtils.connect()) {
			java.sql.Date reservationDate = DateTImeUtil.parseDate(dateString);
			java.sql.Time reservationTime = DateTImeUtil.parseTime(timeString);
			con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			con.setAutoCommit(false);
			ReservationBean reservationBean = update(confirmationCode, reservationDate, reservationTime, partySize,
					contactNumber, con);
			con.commit();
			return reservationBean;
		} catch (ParseException | SQLException e) {
			throw new DAOException(e);
		}
	}

	private List<String> getOccupiedTables(Date reservationDate, Time reservationTime, Connection con)
			throws SQLException {
		List<String> occupiedTables = new ArrayList<String>();
		try (PreparedStatement s = con.prepareStatement(getPotentiallyOccupiedTables)) {
			s.setDate(1, reservationDate);
			s.setTime(2, reservationTime);
			s.setTime(3, reservationTime);
			System.out.println(s);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				occupiedTables.add(rs.getString(1));
			}
			return occupiedTables;
		}
	}

	private int getNumberOfReservations(Date reservationDate, Time reservationTime, Connection con)
			throws SQLException {
		try (PreparedStatement s = con.prepareStatement(getReservationsQuery)) {
			s.setDate(1, reservationDate);
			s.setTime(2, reservationTime);
			System.out.println(s);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				return rs.getInt(1);
			}
			throw new AssertionError("Invalid Server state");
		}

	}

	private ReservationBean insert(Date reservationDate, Time reservationTime, int partySize, String contactNumber,
			Connection con) throws SQLException, DAOException {
		try (PreparedStatement s = con.prepareStatement(insertQuery)) {
			ReservationBean returnStatus = null;
			List<String> occupiedTables = getOccupiedTables(reservationDate, reservationTime, con);
			List<String> tableNames = new SeatingDAO().getTableNames(con);
			/*
			 * If there are free tables, confirm reservations. If not, give up
			 * to 3 wait lists
			 */
			if (occupiedTables.size() < tableNames.size()) {
				returnStatus = setValuesOnPreparedStatement(reservationDate, reservationTime, partySize, contactNumber,
						s, 0);
			} else {
				int numberOfReservations = getNumberOfReservations(reservationDate, reservationTime, con)
						- tableNames.size() + 1;
				if (numberOfReservations == 4) {
					return new ReservationBean(reservationDate.toString(), reservationTime.toString(), partySize,
							contactNumber, new ReservationStatusBean(ReservationStatusBean.Status.WAITLIST_FULL, "", 0),
							"");
				}
				returnStatus = setValuesOnPreparedStatement(reservationDate, reservationTime, partySize, contactNumber,
						s, numberOfReservations);
			}
			System.out.println(s);
			s.execute();
			/*
			 * Auto assign table If auto assign is true in profile, update
			 * reservation with the first empty table.
			 */
			if (new ProfileDAO().getProfileDetails().getAutoAssign() == 1) {
				tableNames.removeAll(occupiedTables);
				new SeatingDAO().updateTable(returnStatus.getReservationStatus().getConfirmationCode(),
						tableNames.get(0), con);
			}

			return returnStatus;
		}
	}

	private ReservationBean setValuesOnPreparedStatement(Date reservationDate, Time reservationTime, int partySize,
			String contactNumber, PreparedStatement s, int numberOfReservations) throws SQLException {
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
					contactNumber, new ReservationStatusBean(ReservationStatusBean.Status.WAITING, confirmationCode,
							numberOfReservations),
					"");
		} else {
			s.setString(6, ReservationStatusBean.Status.CONFIRMED.toString());
			s.setInt(7, 0);
			returnStatus = new ReservationBean(reservationDate.toString(), reservationTime.toString(), partySize,
					contactNumber,
					new ReservationStatusBean(ReservationStatusBean.Status.CONFIRMED, confirmationCode, 0), "");
		}
		return returnStatus;
	}

	private ReservationBean update(String confirmationCode, Date reservationDate, Time reservationTime, int partySize,
			String contactNumber, Connection con) throws DAOException {
		try (PreparedStatement s = con.prepareStatement(insertQuery)) {
			new ReservationCancellationDAO().cancel(confirmationCode);
			return insert(reservationDate, reservationTime, partySize, contactNumber, con);
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	private static final String insertQuery = "insert into rrs_db.reservation "
			+ "(DATE,TIME,PARTYSIZE,PHONENUMBER,CONFIRMATIONCODE,STATUS,QUEUENUMBER) values" + "(?,?,?,?,?,?,?)";

	private static final String getReservationsQuery = "select count(*) from rrs_db.reservation where DATE = ? "
			+ "AND TIME = ? AND CANCELLED = 0";

	private static final String getPotentiallyOccupiedTables = "select tablename from rrs_db.reservation r where date = ? "
			+ "AND cancelled =0 AND (time + INTERVAL 2 HOUR) >= ? " + "AND (time - INTERVAL 2 HOUR) <= ?";

}