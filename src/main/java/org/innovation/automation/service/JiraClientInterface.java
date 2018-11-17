package org.innovation.automation.service;

import java.net.URISyntaxException;
import java.util.List;

import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;

public interface JiraClientInterface {
	public String createIssue(String projectKey, Long issueType, String issueSummary);
	public void updateIssueDescription(String issueKey, String newDescription);
	public Issue getIssue(String issueKey);
	public void voteForAnIssue(Issue issue);
	public int getTotalVotesCount(String issueKey);
	public void addComment(Issue issue, String commentBody);
	public List<Comment> getAllComments(String issueKey);
	public void deleteIssue(String issueKey, boolean deleteSubtasks);
	public void updateAssignee(String issueKey, String assignee) throws URISyntaxException;
}
