
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="workflow" />
		<title><g:message code="fedreg.view.workflow.process.edit.title" /></title>
		
		<script src="${request.contextPath}/js/codemirror/js/codemirror.js" type="text/javascript" charset="utf-8"></script>
	</head>
	<body>
		<h2><g:message code="fedreg.view.workflow.process.edit.heading" args="[process.name]"/></h2>
		
		<g:if test="${process.hasErrors()}">
			<div class="error">
			<ul>
			<g:eachError bean="${process}">
			    <li>${it}</li>
			</g:eachError>
			</ul>
			</div>
		</g:if>
		
		<g:form action="update" id="${process.id}">
			<g:textArea name="code" value="${process.definition.encodeAsHTML()}" rows="5" cols="40"/>
			<br>
			<button type="submit" class="button icon icon_add"/><g:message code="fedreg.link.update" /></button>
		</g:form>
		
		<script type="text/javascript">
			 var textarea = $("#code");
			  var editor = CodeMirror.fromTextArea('code',  {
	            height: "300px",
	            content: textarea.value,
	            parserfile: ["tokenizegroovy.js", "parsegroovy.js"],
	            stylesheet: "${request.contextPath}/js/codemirror/css/groovycolors.css",
	            path: "${request.contextPath}/js/codemirror/js/",
	            autoMatchParens: true,
	            disableSpellcheck: true,
	            lineNumbers: true,
	            tabMode: 'shift'
	         });
		</script>
		
	</body>
</html>