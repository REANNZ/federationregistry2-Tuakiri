
<html>
	<head>
		
		<meta name="layout" content="metadata" />
		<title><g:message code="fedreg.view.metadata.current.title" /></title>
		
		<script src="${request.contextPath}/js/codemirror/js/codemirror.js" type="text/javascript" charset="utf-8"></script>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.metadata.current.heading" /></h2>
			<p><g:message code="fedreg.view.metadata.current.details" /></p>
			<g:textArea id="metadata" name="metadata" value="${md}" rows="25" cols="40"/>
		</section>

		<script type="text/javascript">
			 var textarea = $("#metadata");
			  var editor = CodeMirror.fromTextArea('metadata',  {
		        height: "600px",
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