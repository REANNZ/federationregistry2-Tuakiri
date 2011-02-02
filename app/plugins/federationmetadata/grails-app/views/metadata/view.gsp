
<html>
	<head>
		<r:use modules="codemirror"/>
		<meta name="layout" content="metadata" />
		<title><g:message code="fedreg.view.metadata.current.title" /></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.metadata.current.heading" /></h2>
			<p><g:message code="fedreg.view.metadata.current.details" /></p>
			<g:textArea id="metadata" name="metadata" value="${md}" rows="25" cols="40"/>
		</section>

		<r:script>
			var textarea = $("#metadata");
			var editor = CodeMirror.fromTextArea("metadata",  {
				height: "600px",
				path: "",
				stylesheet: "${r.resource(dir:'/js/codemirror/css', file:'groovycolors.css', plugin:'federationregistry') }",
				basefiles: ["${r.resource(dir:'/js/codemirror/js', file:'codemirror.groovy.inframe.min.js', plugin:'federationregistry') }"],
				parserfile: [],
				content: textarea.value,
				autoMatchParens: true,
				disableSpellcheck: true,
				lineNumbers: true,
				tabMode: 'shift'
			});
		</r:script>	
	</body>
</html>