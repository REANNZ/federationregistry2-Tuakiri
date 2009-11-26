
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="aaf" />
        <g:set var="entityName" value="${message(code: 'identityProviderAttributeCompliance.label')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
	
	<div class="categories">
<g:each in="${categorySupport}" status="i" var="currentStatus">
	<div class="category">
		<h3>${currentStatus.name}</h3>
		<div class="numeric">
			<strong>${currentStatus.supported}</strong> / <em>${currentStatus.total}</em>
		</div>
		<div class="graphic">	
			<g:if test="${ (currentStatus.total != currentStatus.supported) && (currentStatus.supported > 0)}">
				<img src="http://chart.apis.google.com/chart?
				chs=225x50
				&chco=61D317,D3261D
				&chd=t:${currentStatus.supported},${currentStatus.total - currentStatus.supported}
				&chds=0,${currentStatus.total},0,${currentStatus.total}
				&cht=p3
				&chl=Supported|Unsupported"
				alt="${currentStatus.name} chart" />
			</g:if>
			<g:if test="${currentStatus.total == currentStatus.supported}">
				<img src="http://chart.apis.google.com/chart?
				chs=225x50
				&chco=61D317,D3261D
				&chd=t:${currentStatus.supported}
				&chds=0,${currentStatus.total}
				&cht=p3
				&chl=Supported"
				alt="${currentStatus.name} chart" />
			</g:if>
			<g:if test="${currentStatus.supported == 0}">
				<img src="http://chart.apis.google.com/chart?
				chs=225x50
				&chco=D3261D
				&chd=t:${currentStatus.total}
				&chds=0,${currentStatus.total}
				&cht=p3
				&chl=Unsupported"
				alt="${currentStatus.name} chart" />
			</g:if>
		</div>
	</div>
</g:each>
</div>

</body>
</html>

