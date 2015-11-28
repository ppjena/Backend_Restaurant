package io.egen.utils;

import java.text.ParseException;

public class DAOException extends Exception {

	public DAOException(ParseException e) {
		super(e);
	}

}
