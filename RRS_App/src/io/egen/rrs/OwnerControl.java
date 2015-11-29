package io.egen.rrs;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.egen.beans.OwnerBean;
import io.egen.beans.ProfileBean;
import io.egen.beans.ReservationBean;
import io.egen.dao.owner.LoginDAO;
import io.egen.dao.owner.ProfileDAO;
import io.egen.dao.owner.ReservationListDAO;
import io.egen.utils.DAOException;

@Path("owner")
public class OwnerControl {

	@Path("loginGet/{username}/{password}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String loginGet(@PathParam("username") String username, @PathParam("password") String password) {
		return login(username, password);
	}

	@Path("login")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String login(@QueryParam("username") String username, @QueryParam("password") String password) {
		try {
			OwnerBean ownerBean = new LoginDAO().login(username, password);
			return new ObjectMapper().writeValueAsString(ownerBean);
		} catch (JsonProcessingException | DAOException e) {
			e.printStackTrace();
		}
		return "Error";
	}

	@Path("listReservations/{date}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String listReservations(@PathParam("date") String date) {
		try {
			List<ReservationBean> reservationList = new ReservationListDAO().generateReservationList(date);
			return new ObjectMapper().writeValueAsString(reservationList);
		} catch (DAOException | JsonProcessingException e) {
			e.printStackTrace();
		}
		return "Error";
	}

	@Path("getProfile")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getProfile() {
		try {
			ProfileBean profile = new ProfileDAO().getProfileDetails();
			return new ObjectMapper().writeValueAsString(profile);
		} catch (DAOException | JsonProcessingException e) {
			e.printStackTrace();
		}
		return "Error";
	}

	@Path("editProfileGet/{name}/{contact}/{email}/{address}/{autoAssign}/{opening}/{closing}/{openDays}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String editProfileGet(@PathParam("name") String name, @PathParam("contact") String contact,
			@PathParam("email") String email, @PathParam("address") String address,
			@PathParam("autoAssign") int autoAssign, @PathParam("opening") String opening,
			@PathParam("closing") String closing, @PathParam("openDays") String openDays) {
		return editProfile(name, contact, email, address, autoAssign, opening, closing, openDays);
	}

	@Path("editProfile")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String editProfile(@QueryParam("name") String name, @QueryParam("contact") String contact,
			@QueryParam("email") String email, @QueryParam("address") String address,
			@QueryParam("autoAssign") int autoAssign, @QueryParam("opening") String opening,
			@QueryParam("closing") String closing, @QueryParam("openDays") String openDays) {
		try {
			ProfileBean profileBean = new ProfileBean(name, contact, email, address, autoAssign, opening, closing,
					openDays);
			new ProfileDAO().updateProfileDetails(profileBean);
			return "Success";
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return "Error";
	}

}
