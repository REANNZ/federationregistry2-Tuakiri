
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="datamanagement" />
		<title><g:message code="fedreg.view.host.datamanagement.title"/></title>
	</head>
	<body>
			
			<h2><g:message code="fedreg.view.host.datamanagement.heading"/></h2>
			
			<g:message code="fedreg.view.host.datamanagement.descriptive"/>
			
			<table class="cleantable buttons">
                <thead>
                    <tr>                        
						<th><g:message code="fedreg.label.username" /></th>
                        <th><g:message code="fedreg.label.remoteaddr" /></th>
						<th><g:message code="fedreg.label.remotehost" /></th>
						<th><g:message code="fedreg.label.datecreated" /></th>
						<th/>                        
                    </tr>
                </thead>
                <tbody>
				<g:each in="${records}" status="i" var="rec">
					<tr>
						<g:if test="${rec.invoker?.username?.length() > 50}">
				        	<td>${rec.invoker?.username?.substring(0,50).encodeAsHTML()}...</td>
						</g:if>
						<g:else>
							<td>${rec.invoker?.username?.encodeAsHTML()}</td>
						</g:else>
						<td>${rec.remoteAddr}</td>
						<td>${rec.remoteHost}</td>
						<td>${rec.dateCreated}</td>
					</tr>
				</g:each>
				</tbody>
			</table>
	</body>
</html>