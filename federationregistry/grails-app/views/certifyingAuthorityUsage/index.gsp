
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="compliance" />
        <title><g:message code="fedreg.view.compliance.summary.title"/></title>
    </head>
    <body>
	
			<h2>Certifying Authority Utilisation Summary</h2>
					
					<h3>Trusted External Certifying Authorities</h3>
                	<table class="cleantable buttons">
	                    <tbody>
						<g:each in="${causage.keySet()}" status="i" var="k">
							<tr>
								<td valign="top">
									${k.encodeAsHTML()}<br/><br/>
									<strong>Number of entities using this CA: ${causage.get(k).size()}</strong>
									<br/><br/>
									<table>
										<thead>
											<tr>
												<th>Entity Descriptor</th>
											</tr>
										</thead>
										<tbody>
											<g:each in="${causage.get(k)}" status="j" var="rd">
											<tr>
												<td>${rd.entityID}</td>
											</tr>
											</g:each>
										</tbody>
									</table>
								</td>
							</tr>
						</g:each>
	                    </tbody>
	                </table>
	
					<h3>Self Signing Authorities</h3>
					<table class="cleantable buttons">
	                    <tbody>
						<g:each in="${ssusage.keySet()}" status="i" var="k">
							<tr>
								<td valign="top">
									<table>
										<thead>
											<tr>
												<th>Entity Descriptor</th>
											</tr>
										</thead>
										<tbody>
											<g:each in="${ssusage.get(k)}" status="j" var="rd">
											<tr>
												<td>${rd.entityID}</td>
											</tr>
											</g:each>
										</tbody>
									</table>
								</td>
							</tr>
						</g:each>
	                    </tbody>
	                </table>

				</div>
				
            </div>

    </body>
</html>
