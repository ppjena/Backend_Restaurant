package io.egen.dao.owner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import io.egen.utils.DAOException;
import io.egen.utils.DBUtils;

public class SeatingDAO {

	public void updateTable(String confirmationCode, String tableName) throws DAOException {
		try (Connection con = DBUtils.connect();) {
			updateTable(confirmationCode, tableName, con);
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	public void updateTable(String confirmationCode, String tableName, Connection con) throws DAOException {
		try (PreparedStatement s = con.prepareStatement(updateQuery)) {
			s.setString(1, tableName);
			s.setString(2, confirmationCode);
			System.out.println(s);
			s.execute();
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	private static final String updateQuery = "update rrs_db.reservation set tablename = ? where confirmationcode = ?";
}
