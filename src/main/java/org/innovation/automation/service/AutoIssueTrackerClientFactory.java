package org.innovation.automation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AutoIssueTrackerClientFactory {

	@Autowired
	@Qualifier("rest")
	private AutoIssueTrackerClient restClient;

	@Autowired
	@Qualifier("data")
	private AutoIssueTrackerClient dbClient;

	public AutoIssueTrackerClient getClient(String layer) {
		if (layer.equalsIgnoreCase("SERVICES") || layer.equalsIgnoreCase("CMS") || layer.equalsIgnoreCase("OJ"))
			return restClient;
		if (layer.equalsIgnoreCase("DATA"))
			return dbClient;
		return null;
	}

}
