package io.egen.dao.owner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import io.egen.beans.OwnerBean;
import io.egen.utils.DAOException;
import io.egen.utils.DBUtils;

public class LoginDAO {

	public OwnerBean login(String username, String password) throws DAOException {
		try (Connection con = DBUtils.connect(); PreparedStatement s = con.prepareStatement(loginQuery)) {
			s.setString(1, username);
			s.setString(2, password);
			System.out.println(s);
			ResultSet rs = s.executeQuery();
			if (rs.next()) {
				return new OwnerBean(rs.getString("firstname"), rs.getString("firstname"));
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}
		return null;
	}

	private static final String loginQuery = "select firstname,lastname from rrs_db.owner where "
			+ "username = ? and password = ?";
}
