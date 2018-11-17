package org.innovation.automation.common;

import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.innovation.automation.model.Request;
import org.innovation.automation.service.AutoIssueTrackerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
@Configurable
public class ExceptionHandler {

	@Autowired
	@Qualifier("jira")
	AutoIssueTrackerClient jiraClient;

	@AfterThrowing(pointcut = "execution(** org.innovation.automation.controller.*.*(..)) && args(requests)", throwing = "ex")
	public void process(Deque<Request> requests, Exception ex) {
		System.out.println(ex.getMessage());

		Request request = new Request();

		Map<String, String> headers = new HashMap<>();
		String summary = (ex.getMessage().length() >= 250) ? ex.getMessage().substring(0, 250) + "..."
				: ex.getMessage();
		headers.put("summary", summary);
		headers.put("assignee", "admin");
		request.setHeaders(headers);

		String body = "h1. " + ex.getMessage() + ":";
		
		body += "\n\n";
		body += "{panel:title=Exception Details|borderStyle=dashed|borderColor=#ccc|titleBGColor=#F7D6C1|bgColor=#FFFFCE}";
		body += "*Cause:* " + ex.getCause();
		body += "\n";
		body += "*Exception Class:* " + ex.getClass();
		body += "{panel}";
		
		if (requests != null) {
			body += "\n";
			body += "||Layer||Request||";
			for (Request originalRequest: requests) {
				System.out.println(originalRequest);
				body += "\n";
				body += "|" + originalRequest.getLayer() + "|";
				body += (originalRequest.getType() != null && originalRequest.getType().length() != 0)? originalRequest.getType() + "\n": "";
				body += (originalRequest.getUrl() != null && originalRequest.getUrl().length() != 0)? originalRequest.getUrl() + "\n": "";
				body += (originalRequest.getHeaders() != null && originalRequest.getHeaders().size() != 0)? "\\" + originalRequest.getHeaders() + "\n": "";
				body += (originalRequest.getBody() != null && originalRequest.getBody().length() != 0)? originalRequest.getBody(): "";
				body += "|";
			}
			body += "\n";
		}
		
		body += "\n";
		body += "{code:title=Stack Trace}";
		for (StackTraceElement ste : ex.getStackTrace())
			body += "\n" + ste.toString();
		body += "\n";
		body += "{code}";
		
		request.setBody(body);

		try {
			jiraClient.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
