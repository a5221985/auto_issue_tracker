package org.innovation.automation.service;

import java.net.HttpURLConnection;
import java.net.URL;

import org.innovation.automation.model.Request;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("rest")
@Scope("prototype")
public class RESTClient implements AutoIssueTrackerClient {

	@Override
	public void execute(Request request) throws Exception {
		URL url = new URL(request.getUrl());
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod(request.getType());

		if (request.getHeaders() != null)
			for (String key : request.getHeaders().keySet())
				conn.setRequestProperty(key, request.getHeaders().get(key));

		Integer code = conn.getResponseCode();
		System.out.println(code);
	}

}
