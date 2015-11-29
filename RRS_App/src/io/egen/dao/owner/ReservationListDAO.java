package io.egen.dao.owner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.egen.beans.ReservationBean;
import io.egen.beans.ReservationStatusBean;
import io.egen.utils.DAOException;
import io.egen.utils.DBUtils;

public class ReservationListDAO {

	public List<ReservationBean> generateReservationList(String date) throws DAOException {
		List<ReservationBean> reservationList = new ArrayList<ReservationBean>();
		try (Connection con = DBUtils.connect(); PreparedStatement s = con.prepareStatement(getQuery)) {
			java.sql.Date dateAsDate = parseDate(date);
			s.setDate(1, dateAsDate);
			System.out.println(s);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				ReservationBean reservationBean = new ReservationBean(rs.getString("date"), rs.getString("time"),
						rs.getInt("partySize"), rs.getString("phoneNumber"), formReservationStatus(rs),
						rs.getString("tableName"));
				reservationList.add(reservationBean);
			}
			return reservationList;
		} catch (SQLException | ParseException e) {
			throw new DAOException(e);
		}

	}

	public List<ReservationBean> generateReservationListforContacts(String phonenumber) throws DAOException {
		List<ReservationBean> reservationList = new ArrayList<ReservationBean>();
		try (Connection con = DBUtils.connect(); PreparedStatement s = con.prepareStatement(getQueryReservationList)) {
			s.setString(1, phonenumber);
			System.out.println(s);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				ReservationBean reservationBean = new ReservationBean(rs.getString("date"), rs.getString("time"),
						rs.getInt("partySize"), rs.getString("phoneNumber"), formReservationStatus(rs),
						rs.getString("tableName"));
				reservationList.add(reservationBean);
			}
			return reservationList;
		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}

	private java.sql.Date parseDate(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		System.out.println(dateString);
		java.util.Date date = sdf.parse(dateString);
		return new java.sql.Date(date.getTime());
	}

	private ReservationStatusBean formReservationStatus(ResultSet rs) throws SQLException {
		ReservationStatusBean.Status status = rs.getString("status").equals("CONFIRMED")
				? ReservationStatusBean.Status.CONFIRMED : ReservationStatusBean.Status.WAITING;
		return new ReservationStatusBean(status, rs.getString("confirmationcode"), rs.getInt("queuenumber"));
	}

	private static final String getQuery = "select * from rrs_db.reservation where date = ? and cancelled = 0";

	private static final String getQueryReservationList = "select * from rrs_db.reservation where phonenumber = ?";
}
