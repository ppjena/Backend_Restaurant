package io.egen.dao.owner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;

import io.egen.beans.ProfileBean;
import io.egen.utils.DAOException;
import io.egen.utils.DBUtils;
import io.egen.utils.DateTImeUtil;

public class ProfileDAO {

	public ProfileBean getProfileDetails() throws DAOException {
		try (Connection con = DBUtils.connect()) {
			return getProfileDetails(con);
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public ProfileBean getProfileDetails(Connection con) throws DAOException {
		try (PreparedStatement s = con.prepareStatement(getQuery)) {
			System.out.println(s);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				ProfileBean profileBean = new ProfileBean(rs.getString("name"), rs.getString("contact"),
						rs.getString("email"), rs.getString("address"), rs.getInt("autoAssignTable"),
						rs.getString("opening"), rs.getString("closing"), rs.getString("openDays"));
				return profileBean;
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return null;
	}

	public void updateProfileDetails(ProfileBean profile) throws DAOException {
		try (Connection con = DBUtils.connect(); PreparedStatement s = con.prepareStatement(updateQuery)) {
			s.setString(1, profile.getName());
			s.setString(2, profile.getContact());
			s.setString(3, profile.getEmail());
			s.setString(4, profile.getAddress());
			s.setInt(5, profile.getAutoAssign());
			s.setTime(6, DateTImeUtil.parseTime24(profile.getOpening()));
			s.setTime(7, DateTImeUtil.parseTime24(profile.getClosing()));
			s.setString(8, profile.getOpeningDays());
			System.out.println(s);
			s.execute();
		} catch (SQLException | ParseException e) {
			throw new DAOException(e);
		}
	}

	private static final String getQuery = "select * from rrs_db.profile";
	private static final String updateQuery = "update rrs_db.profile set name = ?," + "contact = ?," + "email = ?,"
			+ "address = ?," + "autoAssigntable = ?," + "opening = ?," + "closing = ?," + "openDays = ?";
}
