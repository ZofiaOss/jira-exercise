package com.example.plugins.tutorial.jira.workflow;

import java.util.Map;

import com.atlassian.jira.bc.issue.IssueService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueInputParameters;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.workflow.function.issue.AbstractJiraFunctionProvider;
import com.opensymphony.module.propertyset.PropertySet;
import com.opensymphony.workflow.WorkflowException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the post-function class that gets executed at the end of the transition.
 * Any parameters that were saved in your factory class will be available in the transientVars Map.
 */
public class CreateSubtask extends AbstractJiraFunctionProvider
{
    private static final Logger log = LoggerFactory.getLogger(CreateSubtask.class);
    public static final String FIELD_MESSAGE = "messageField";

    public void execute(Map transientVars, Map args, PropertySet ps) throws WorkflowException
    {
        MutableIssue issue = getIssue(transientVars);
        String message = (String)transientVars.get(FIELD_MESSAGE);

        if (null == message) {
            message = "";
        }

        IssueService service = ComponentAccessor.getIssueService();
        IssueInputParameters inputParameters = service.newIssueInputParameters();
        inputParameters
                .setProjectId(issue.getProjectId())
                .setIssueTypeId("subtask")
                .setSummary("Remember to merge your changes!")
                .setReporterId(issue.getAssigneeId())
                .setAssigneeId(issue.getAssigneeId())
                .setDescription("Very much please remember oh wow so amaze")
                .setStatusId("10000")
                .setPriorityId("2");
        IssueService.CreateValidationResult cvr = service.validateCreate(issue.getAssignee(), inputParameters);
        IssueService.IssueResult result = service.create(issue.getAssignee(), cvr);
        result.getIssue().setParentId(issue.getId());
        issue.setDescription(issue.getDescription() + message);
    }
}