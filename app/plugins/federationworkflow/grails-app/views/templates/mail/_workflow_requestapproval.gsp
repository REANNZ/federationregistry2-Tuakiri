<%@ page contentType="text/html"%>

<g:applyLayout name="email">
	<g:message code="fedreg.templates.mail.workflow.requestapproval.descriptive" />

	<g:link controller="workflowApproval" action="list" absolute="true"><g:message code="fedreg.templates.mail.workflow.requestapproval.access" /></g:link>

	<p><strong><g:message code="label.name" /></strong>: ${fieldValue(bean: taskInstance, field: "task.name")} (ID: ${fieldValue(bean: taskInstance, field: "id")})</p>

	<p><strong><g:message code="label.description" /></strong>: ${fieldValue(bean: taskInstance, field: "task.description")}</p>

	<p><strong><g:message code="label.processinstance" /></strong>: ${fieldValue(bean: taskInstance, field: "processInstance.description")}</p>

	<p><g:message code="fedreg.templates.mail.get.support" /></p>
</g:applyLayout>
