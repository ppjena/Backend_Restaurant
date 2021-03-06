package io.egen.rrs;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.egen.beans.ReservationBean;
import io.egen.dao.ReservationCancellationDAO;
import io.egen.dao.ReservationDAO;
import io.egen.dao.ReservationGetterDAO;
import io.egen.utils.DAOException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("reservation")
@Api(tags = { "reservation" })

public class ReservationControl {

	/**
	 * Test method to create a reservation through get
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
	 * Create a reservation
	 */
	@Path("create/{date}/{time}/{partySize}/{contactNumber}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Create Reservation", notes = " Create Reservation for a customer/Owner")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Service Error") })

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
	 * edit a reservation through Get
	 */
	@Path("editGet/{confirmationcode}/{date}/{time}/{partySize}/{contactNumber}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ReservationBean editGet(@PathParam("confirmationcode") String confirmationcode,
			@PathParam("date") String date, @PathParam("time") String time, @PathParam("partySize") String partySize,
			@PathParam("contactNumber") String contactNumber) {
		return edit(confirmationcode, date, time, partySize, contactNumber);
	}

	/**
	 * edit a reservation
	 */
	@Path("edit")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Edit Reservation", notes = " Update/Edit a Reservation Detail")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Service Error") })
	public ReservationBean edit(@QueryParam("confirmationcode") String confirmationCode,
			@QueryParam("date") String date, @QueryParam("time") String time, @QueryParam("partySize") String partySize,
			@QueryParam("contactNumber") String contactNumber) {
		try {
			return new ReservationDAO().edit(confirmationCode, date, time, Integer.parseInt(partySize), contactNumber);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Cancel a reservation
	 */
	@Path("cancel/{confirmationcode}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Cancel Reservation", notes = " Update/Cancel a Reservation Detail")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Service Error") })
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
	 * Cancel a reservation with Get
	 */
	@Path("cancelGet/{confirmationcode}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String cancelGet(@PathParam("confirmationcode") String confirmationCode) {
		return cancel(confirmationCode);
	}

	/**
	 * Get reservation details for the confirmationcode
	 * 
	 * @param confirmationCode
	 * @return
	 */
	@Path("reservation/{confirmationcode}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Reservation details", notes = " Reservation details for the confirmationcode")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Service Error") })
	public ReservationBean reservation(@PathParam("confirmationcode") String confirmationCode) {
		try {
			return new ReservationGetterDAO().getReservation(confirmationCode);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
}