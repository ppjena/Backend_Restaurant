package io.egen.dao.owner;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import io.egen.beans.TableBean;
import io.egen.utils.DAOException;
import io.egen.utils.DBUtils;
import io.egen.utils.DateTImeUtil;

public class TableDAO {
	
	public List<TableBean> generateTableLists(String date) throws DAOException{
		List<TableBean> tableList = new ArrayList<TableBean>();
		try (Connection con = DBUtils.connect(); PreparedStatement s = con.prepareStatement(getQuery)) {
			Date dateAsDate = DateTImeUtil.parseDate(date);
			s.setDate(1, dateAsDate);
			s.setDate(2, dateAsDate);
			System.out.println(s);
			ResultSet rs = s.executeQuery();
		  while (rs.next()) {
			  TableBean tableBean = new TableBean(rs.getString("tablename"), rs.getString("confirmationcode"),
						rs.getInt("size"), rs.getString("time"));
			  tableList.add(tableBean);
			}
		  return tableList;
		} catch (SQLException | ParseException e) {
			throw new DAOException(e);
		}
		
	}
	

	private static final String getQuery = "select t.tablename,t.size,res.time,res.ConfirmationCode from rrs_db.table t"
			+" left outer join" 
			+"(select r.time,r.confirmationcode,r.tablename from rrs_db.reservation r,"
			+ "(select max(time) time,tablename from rrs_db.reservation where date = ? And "
			+ "tablename IS NOT NULL AND status='confirmed' AND cancelled =0 group by tablename) mr"
			+ " where r.time = mr.time and r.date = ? and r.tablename = mr.tablename) res on t.tablename = res.tablename";

}
