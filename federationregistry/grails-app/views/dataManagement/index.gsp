
<html>
	<head>
		
		<meta name="layout" content="datamanagement" />
		<title><g:message code="fedreg.view.host.datamanagement.title"/></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.host.datamanagement.heading"/></h2>
			
			<g:message code="fedreg.view.host.datamanagement.descriptive"/>
			
			<table>
				<thead>
					<tr>						
						<th><g:message code="label.username" /></th>
						<th><g:message code="label.remoteaddr" /></th>
						<th><g:message code="label.remotehost" /></th>
						<th><g:message code="label.datecreated" /></th>
						<th/>						 
					</tr>
				</thead>
				<tbody>
				<g:each in="${records}" status="i" var="rec">
					<tr>
						<g:if test="${rec.invoker != null}">
							<g:if test="${rec.invoker?.username?.length() > 50}">
								<td><g:link controller="user" action="show" id="${rec.invoker?.id}">${rec.invoker?.username?.substring(0,50).encodeAsHTML()}...</g:link></td>
							</g:if>
							<g:else>
								<td><g:link controller="user" action="show" id="${rec.invoker?.id}">${rec.invoker?.username?.encodeAsHTML()}</g:link></td>
							</g:else>
						</g:if>
						<g:else>
							<td><g:message code="fedreg.view.host.datamanagement.initialbootstrap" /></td>
						</g:else>
						<td>${rec.remoteAddr}</td>
						<td>${rec.remoteHost}</td>
						<td>${rec.dateCreated}</td>
					</tr>
				</g:each>
				</tbody>
			</table>
		</section>
	</body>
</html>