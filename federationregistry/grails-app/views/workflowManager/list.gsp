<%@ page import="fedreg.core.Organization" %>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="workflow" />
		<title><g:message code="fedreg.view.workflow.manager.show.title" /></title>
		
		<script type="text/javascript">
			$(function() {
				$("#tabs").tabs();
			});
		</script>
	</head>
	<body>
		<g:each in="${processList}">
			${it.name}
		</g:each>
	</body>
</html>