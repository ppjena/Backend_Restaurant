package io.egen.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.egen.beans.ReservationBean;
import io.egen.beans.ReservationStatusBean;
import io.egen.utils.DAOException;
import io.egen.utils.DBUtils;

public class ReservationGetterDAO {

	public ReservationBean getReservation(String confirmationCode) throws DAOException {
		try (Connection con = DBUtils.connect(); PreparedStatement s = con.prepareStatement(getQuery)) {
			s.setString(1, confirmationCode);
			System.out.println(s);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				ReservationBean reservationBean = new ReservationBean(rs.getString("date"), rs.getString("time"),
						rs.getInt("partySize"), rs.getString("phoneNumber"), formReservationStatus(rs),rs.getString("tableName"));
				return reservationBean;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return null;
	}

	private ReservationStatusBean formReservationStatus(ResultSet rs) throws SQLException {
		ReservationStatusBean.Status status = rs.getString("status").equals("CONFIRMED")
				? ReservationStatusBean.Status.CONFIRMED : ReservationStatusBean.Status.WAITING;
		return new ReservationStatusBean(status, rs.getString("confirmationcode"), rs.getInt("queuenumber"));
	}

	private static final String getQuery = "select * from rrs_db.reservation where confirmationcode = ?";
}
