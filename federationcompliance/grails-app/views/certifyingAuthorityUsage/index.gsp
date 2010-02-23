
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="compliance" />
        <title><g:message code="fedreg.view.compliance.cautilization.title"/></title>
    </head>
    <body>
		<h2><g:message code="fedreg.view.compliance.cautilization.heading"/></h2>
		<div id="cautilization">
			<h3><g:message code="fedreg.label.externalca"/></h3>
			<table class="cleantable buttons">
				<tbody>
					<g:each in="${causage.keySet()}" status="i" var="k">
					<tr>
						<td>
							<table>
								<tbody>
									<tr>
										<th><g:message code="fedreg.label.authority"/></th>
										<td>${k.encodeAsHTML()}</td>
									</tr>
									<tr>
										<th><g:message code="fedreg.label.usagecount"/></th>
										<td>${causage.get(k).size()}</td>
									</tr>
									<tr>
										<th><g:message code="fedreg.label.usedby"/></th>
										<td>
											<g:each in="${causage.get(k)}" status="j" var="rd">
												${rd.entityID.encodeAsHTML()}<br/>
											</g:each>
										</td>
									</tr>
								</tbody>
							</table>
						</td>
					</tr>
					</g:each>
				</tbody>
			</table>

			<h3><g:message code="fedreg.label.selfsigningca"/></h3>
			<table class="cleantable buttons">
				<thead>
					<tr>
						<th><g:message code="fedreg.label.entitydescriptor"/></th>
					</tr>
				</thead>
                   <tbody>
				<g:each in="${ssusage.keySet()}" status="i" var="k">
					<tr>
							<td>
							<g:each in="${ssusage.get(k)}" status="j" var="rd">
								${rd.entityID.encodeAsHTML()}<br/>
							</g:each>
							</td>
					</tr>
				</g:each>
                   </tbody>
			</table>
		</div>
    </body>
</html>
