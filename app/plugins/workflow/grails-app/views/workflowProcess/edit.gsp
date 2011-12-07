
<html>
	<head>
		<r:use modules="codemirror"/>
		<meta name="layout" content="workflow" />
		<title><g:message code="fedreg.view.workflow.process.edit.title" /></title>
	</head>
	<body>
		<section>
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
				<button type="submit" class="save-button"/><g:message code="label.update" /></button>
			</g:form>
		
			<r:script>
				var textarea = $("#code");
				var editor = CodeMirror.fromTextArea('code',  {
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
		</section>
	</body>
</html>