
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'identityProviderAttributeCompliance.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
	
	<div class="container">
		<h2>Identity provider ${idp?.organization?.displayName} attribute support</h2>
		<div class="categorysummary">
		<g:each in="${categorySupport}" status="i" var="currentStatus">
			<div class="category">
				<h3>${currentStatus.name}</h3>
				<div class="numeric">
					<strong>${currentStatus.supportedCount}<span class="total"> / ${currentStatus.totalCount}</span></strong>
				</div>
				<div class="graphic">	
					<g:if test="${ (currentStatus.totalCount != currentStatus.supportedCount) && (currentStatus.supportedCount > 0)}">
						<img src="http://chart.apis.google.com/chart?
						chs=225x50
						&chco=2dac3f,f57210
						&chd=t:${currentStatus.supportedCount},${currentStatus.totalCount - currentStatus.supportedCount}
						&chds=0,${currentStatus.totalCount},0,${currentStatus.totalCount}
						&cht=p3
						&chl=Supported|Unsupported"
						alt="${currentStatus.name} chart" />
					</g:if>
					<g:if test="${currentStatus.totalCount == currentStatus.supportedCount}">
						<img src="http://chart.apis.google.com/chart?
						chs=225x50
						&chco=2dac3f
						&chd=t:${currentStatus.supportedCount}
						&chds=0,${currentStatus.totalCount}
						&cht=p3
						&chl=Supported"
						alt="${currentStatus.name} chart" />
					</g:if>
					<g:if test="${currentStatus.supportedCount == 0}">
						<img src="http://chart.apis.google.com/chart?
						chs=225x50
						&chco=f57210
						&chd=t:${currentStatus.totalCount}
						&chds=0,${currentStatus.totalCount}
						&cht=p3
						&chl=Unsupported"
						alt="${currentStatus.name} chart" />
					</g:if>
				</div>
			</div>
		</g:each>
		</div>
		<div class="categorydetail">

			<g:each in="${categorySupport}" status="i" var="currentStatus">
				<div class="category">
					<div class="numeric">
						<strong>${currentStatus.name} - ${currentStatus.supportedCount}<span class="total"> / ${currentStatus.totalCount}</span></strong>
					</div>
				
					<table class="cleantable buttons">
	                    <thead>
	                        <tr>
                        
	                            <th></th>
								<th/>
							                        
	                        </tr>
	                    </thead>
	                    <tbody>
	                    <g:each in="${currentStatus.available}" status="j" var="attr">
	                        <tr class="${(j % 2) == 0 ? 'odd' : 'even'}">
                        
	                            <td>${attr.friendlyName}</td>
                        
	                            <td>
									<g:if test="${idp.attributes.contains(attr)}">
										<span class="icon icon_tick">Supported</span>
									</g:if>
									<g:else>
										<span class="icon icon_cross">Unsupported</span>
									</g:else>
								</td>
	                        </tr>
	                    </g:each>
	                    </tbody>
	                </table>
				</div>

			</g:each>
		</div>
	</div>

	</body>
</html>

