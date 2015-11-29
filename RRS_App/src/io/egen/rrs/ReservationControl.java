package io.egen.rrs;

import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.egen.beans.ReservationBean;
import io.egen.beans.ReservationStatusBean;
import io.egen.dao.ReservationCancellationDAO;
import io.egen.dao.ReservationDAO;
import io.egen.dao.ReservationGetterDAO;
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
	@Produces(MediaType.APPLICATION_JSON)
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
	@Produces(MediaType.APPLICATION_JSON)
	public String create(@PathParam("date") String date, @PathParam("time") String time,
			@PathParam("partySize") String partySize, @PathParam("contactNumber") String contactNumber) {

		try {
			ReservationBean reservationStatus = new ReservationDAO().create(date, time, partySize, contactNumber);
			return new ObjectMapper().writeValueAsString(reservationStatus);

		} catch (DAOException | JsonProcessingException e) {
			e.printStackTrace();
		}
		return "Error";
	}

	/**
	 * Actual method that creates a reservation
	 * 
	 * @return
	 */
	@Path("cancel/{confirmationcode}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String cancel(@PathParam("confirmationcode") String confirmationCode) {
		try {
			new ReservationCancellationDAO().cancel(confirmationCode);
			return "Success";
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return "Error";
	}

	/**
	 * Actual method that creates a reservation
	 * 
	 * @return
	 */
	@Path("cancelGet/{confirmationcode}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String cancelGet(@PathParam("confirmationcode") String confirmationCode) {
		return cancel(confirmationCode);
	}

	@Path("reservation/{confirmationcode}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public ReservationBean reservation(@PathParam("confirmationcode") String confirmationCode) {
		try {
			return new ReservationGetterDAO().getReservation(confirmationCode);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Actual method that creates a reservation
	 * 
	 * @return
	 */
	@Path("reservationGet/{confirmationcode}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ReservationBean reservationGet(@PathParam("confirmationcode") String confirmationCode) {
		return reservation(confirmationCode);
	}
}