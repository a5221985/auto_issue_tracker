package org.innovation.automation.controller;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Controller;

@Controller
@Path("/error")
public class ErrorController {

	@POST
	public Response process() {
		return Response.serverError().build();
	}

}
