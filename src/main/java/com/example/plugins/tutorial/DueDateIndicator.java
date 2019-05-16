package com.example.plugins.tutorial;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.plugin.webfragment.contextproviders.AbstractJiraContextProvider;
import com.atlassian.jira.plugin.webfragment.model.JiraHelper;
import com.atlassian.jira.user.ApplicationUser;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

public class DueDateIndicator extends AbstractJiraContextProvider {

    @Override
    public Map<String, Object> getContextMap(ApplicationUser applicationUser, JiraHelper jiraHelper) {
        Map<String, Object> contextMap = new HashMap<>();
        Issue currentIssue = (Issue) jiraHelper.getContextParams().get("issue");
        Timestamp dueDate = currentIssue.getDueDate();
        contextMap.put("issue", currentIssue);
        if (dueDate != null) {
            LocalDate currentTimeInDays = LocalDate.now();
            LocalDate dueDateTimeInDays = dueDate.toLocalDateTime().toLocalDate();
            long daysAwayFromDueDateCalc = DAYS.between(currentTimeInDays, dueDateTimeInDays);
            contextMap.put("daysAwayFromDueDate", daysAwayFromDueDateCalc);
        }
        return contextMap;
    }
}