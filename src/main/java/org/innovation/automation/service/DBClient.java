package org.innovation.automation.service;

import org.innovation.automation.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service("data")
@Scope("prototype")
public class DBClient implements AutoIssueTrackerClient {
	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void execute(Request request) throws Exception {
		jdbcTemplate.execute(request.getBody());
	}

}
