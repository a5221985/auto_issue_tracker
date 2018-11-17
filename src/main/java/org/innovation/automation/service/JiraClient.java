package org.innovation.automation.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.innovation.automation.common.JiraConfigProperties;
import org.innovation.automation.model.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.domain.BasicVotes;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;

@Service("jira")
@Scope("prototype")
public class JiraClient implements AutoIssueTrackerClient, JiraClientInterface {
	private JiraRestClient restClient;

	@Autowired
	JiraConfigProperties configProperties;

	public JiraClient() {
		super();
	}

	public JiraClient(String username, String password, String url) {
		super();
		configProperties.setUsername(username);
		configProperties.setPassword(password);
		configProperties.setUrl(url);
		this.restClient = getRestClient();
	}

	private JiraRestClient getRestClient() {
		return new AsynchronousJiraRestClientFactory().createWithBasicHttpAuthentication(getJiraUri(),
				configProperties.getUsername(), configProperties.getPassword());
	}

	private URI getJiraUri() {
		return URI.create(configProperties.getUrl());
	}

	@Override
	public String createIssue(String projectKey, Long issueType, String issueSummary) {
		IssueRestClient issueClient = restClient.getIssueClient();
		IssueInput newIssue = new IssueInputBuilder(projectKey, issueType, issueSummary).build();
		return issueClient.createIssue(newIssue).claim().getKey();
	}

	@Override
	public void updateIssueDescription(String issueKey, String newDescription) {
		IssueInput input = new IssueInputBuilder().setDescription(newDescription).build();
		restClient.getIssueClient().updateIssue(issueKey, input).claim();
	}

	@Override
	public Issue getIssue(String issueKey) {
		return restClient.getIssueClient().getIssue(issueKey).claim();
	}

	@Override
	public void voteForAnIssue(Issue issue) {
		restClient.getIssueClient().vote(issue.getVotesUri()).claim();
	}

	@Override
	public int getTotalVotesCount(String issueKey) {
		BasicVotes votes = getIssue(issueKey).getVotes();
		return votes == null ? 0 : votes.getVotes();
	}

	@Override
	public void addComment(Issue issue, String commentBody) {
		restClient.getIssueClient().addComment(issue.getCommentsUri(), Comment.valueOf(commentBody));
	}

	@Override
	public List<Comment> getAllComments(String issueKey) {
		return StreamSupport.stream(getIssue(issueKey).getComments().spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public void deleteIssue(String issueKey, boolean deleteSubtasks) {
		restClient.getIssueClient().deleteIssue(issueKey, deleteSubtasks).claim();
	}

	@Override
	public void execute(Request request) throws Exception {
		if (this.restClient == null)
			this.restClient = getRestClient();
		
		String summary = request.getHeaders().get("summary");
		String issueKey = createIssue(configProperties.getProjectKey(), configProperties.getIssueType(), summary);
		
		String description = request.getBody();
		updateIssueDescription(issueKey, description);
		
		String assignee = request.getHeaders().get("assignee");
		updateAssignee(issueKey, assignee);
		
		System.out.println("Issue " + issueKey + " created");
	}

	@Override
	public void updateAssignee(String issueKey, String assignee) throws URISyntaxException {
		IssueInput input = new IssueInputBuilder().setAssigneeName(assignee).build();
		restClient.getIssueClient().updateIssue(issueKey, input).claim();
	}
}
