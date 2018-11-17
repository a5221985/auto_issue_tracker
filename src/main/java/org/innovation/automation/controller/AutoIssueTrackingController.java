package org.innovation.automation.controller;

import java.util.Deque;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;

import org.innovation.automation.model.Request;

@Consumes("application/json, application/xml")
@Produces("application/json, application/xml")
public interface AutoIssueTrackingController {
	public void process(Deque<Request> requests) throws Exception;
}
