package io.egen.dao.owner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import io.egen.beans.ProfileBean;
import io.egen.utils.DAOException;
import io.egen.utils.DBUtils;

public class ContactListDAO {
	
	public List<String> getContactList() throws DAOException {
		try (Connection con = DBUtils.connect(); PreparedStatement s = con.prepareStatement(getQuery)) {
			List<String>  contactList = new ArrayList<String>();
			System.out.println(s);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				String contact = rs.getString("phonenumber");
				contactList.add(contact);
			}
			return contactList;
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}
	
	
	
	private static final String getQuery = "select distinct phonenumber from rrs_db.reservation";
	
	
}
