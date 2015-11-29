package io.egen.rrs;

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
import io.egen.dao.owner.LoginDAO;
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
		return login(date);
	}

	

}
