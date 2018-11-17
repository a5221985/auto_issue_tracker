package org.innovation.automation.controller;

import java.util.Deque;
import java.util.Iterator;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.innovation.automation.model.Request;
import org.innovation.automation.service.AutoIssueTrackerClientFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@Path("/issuetracker")
public class IssueTrackingController implements AutoIssueTrackingController {
	
	@Autowired
	AutoIssueTrackerClientFactory clientFactory;

	@POST
	public void process(Deque<Request> requests) throws Exception {
		for (Iterator<Request> iterator = requests.descendingIterator(); iterator.hasNext();) {
			Request request = iterator.next();
			clientFactory.getClient(request.getLayer()).execute(request);
		}
	}

}
