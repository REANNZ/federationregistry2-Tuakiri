<g:message code="fedreg.templates.mail.workflow.requestapproval.descriptive" />

<table>
	<thead>
		<tr>
			<th><g:message code="label.name" /></th>
			<th><g:message code="label.description" /></th>
			<th><g:message code="label.processinstance" /></th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>${fieldValue(bean: taskInstance, field: "task.name")} (${fieldValue(bean: taskInstance, field: "id")})</td>
			<td>${fieldValue(bean: taskInstance, field: "task.description")}</td>
			<td>${fieldValue(bean: taskInstance, field: "processInstance.description")}</td>
		</tr>
	</tbody>
</table>

<g:link controller="workflowApproval" action="list"><g:message code="fedreg.templates.mail.workflow.requestapproval.access" /></g:link>

<g:message code="fedreg.templates.mail.get.support" />