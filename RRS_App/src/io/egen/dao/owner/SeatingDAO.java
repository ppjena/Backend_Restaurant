package io.egen.dao.owner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

	public List<String> getTableNames(Connection con) throws DAOException {
		List<String> tables = new ArrayList<String>();
		try (PreparedStatement s = con.prepareStatement(getQuery)) {
			System.out.println(s);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				tables.add(rs.getString("tablename"));
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return tables;
	}

	private static final String updateQuery = "update rrs_db.reservation set tablename = ? where confirmationcode = ?";
	private static final String getQuery = "select tablename from rrs_db.table";

}
