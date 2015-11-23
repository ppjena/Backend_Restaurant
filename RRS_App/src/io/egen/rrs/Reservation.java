package io.egen.rrs;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("reservation")
public class Reservation {

	/**
	 * Test method. We can delete after we test.
	 * 
	 * @return
	 */
	@Path("createGet/{date}/{time}/{partySize}/{contactNumber}")
	@GET
	@Produces("text/html")
	public String createGet(@QueryParam("date") String date, @QueryParam("time") String time,
			@QueryParam("partySize") String partySize, @QueryParam("contactNumber") String contactNumber) {
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
	public String create(@QueryParam("date") String date, @QueryParam("time") String time,
			@QueryParam("partySize") String partySize, @QueryParam("contactNumber") String contactNumber) {
		return "Hello";
	}

}