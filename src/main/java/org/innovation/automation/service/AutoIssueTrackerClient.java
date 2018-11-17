package org.innovation.automation.service;

import org.innovation.automation.model.Request;

public interface AutoIssueTrackerClient {
	public void execute(Request request) throws Exception;
}
