package io.egen.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.egen.utils.DAOException;
import io.egen.utils.DBUtils;

public class ReservationCancellationDAO {

	public void cancel(String confirmationCode) throws DAOException {
		try (Connection con = DBUtils.connect(); PreparedStatement s = con.prepareStatement(cancelQuery)) {
			s.setString(1, confirmationCode);
			System.out.println(s);
			s.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	
	private static final String cancelQuery = "update rrs_db.reservation set cancelled=1 where confirmationcode = ?";
}
