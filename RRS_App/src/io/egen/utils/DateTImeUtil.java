package io.egen.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTImeUtil {
	public static Time parseTime(String timeString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
		java.util.Date date = sdf.parse(timeString);
		return new Time(date.getTime());
	}
	
	public static Time parseTime24(String timeString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("h:mm:ss");
		java.util.Date date = sdf.parse(timeString);
		return new Time(date.getTime());
	}

	public static java.sql.Date parseDate(String dateString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		java.util.Date date = sdf.parse(dateString);
		return new java.sql.Date(date.getTime());
	}

}
