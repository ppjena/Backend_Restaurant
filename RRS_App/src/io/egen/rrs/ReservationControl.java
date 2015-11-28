package io.egen.rrs;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.egen.beans.ReservationStatus;
import io.egen.dao.ReservationDAO;
import io.egen.utils.DAOException;

@Path("reservation")
public class ReservationControl {

	/**
	 * Test method. We can delete after we test.
	 * 
	 * @return
	 */
	@Path("createGet/{date}/{time}/{partySize}/{contactNumber}")
	@GET
	@Produces("text/html")
	public String createGet(@PathParam("date") String date, @PathParam("time") String time,
			@PathParam("partySize") String partySize, @PathParam("contactNumber") String contactNumber) {
		System.out.println(date);
		return create(date, time, partySize, contactNumber);
	}

	/**
	 * Actual method that creates a reservation
	 * 
	 * @return
	 */
	@Path("create/{date}/{time}/{partySize}/{contactNumber}")
	@POST
	@Produces("text/html")
	public String create(@PathParam("date") String date, @PathParam("time") String time,
			@PathParam("partySize") String partySize, @PathParam("contactNumber") String contactNumber) {
		
		try {
			ReservationStatus reservationStatus = new ReservationDAO()
					.create(date, time, partySize, contactNumber);
			return new ObjectMapper().writeValueAsString(reservationStatus);
			
		} catch (DAOException | JsonProcessingException e) {
			e.printStackTrace();
		}
		return "Hello";
	}

}