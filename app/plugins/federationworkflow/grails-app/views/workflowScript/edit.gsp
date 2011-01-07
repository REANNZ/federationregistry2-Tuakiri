
<html>
	<head>
		<r:use modules="codemirror"/>
		<meta name="layout" content="workflow" />
		<title><g:message code="fedreg.view.workflow.script.edit.title" /></title>
	</head>
	<body>
		<section>
			<h2><g:message code="fedreg.view.workflow.script.edit.heading" args="[script.name]"/></h2>
		
			<g:if test="${script.hasErrors()}">
				<div class="error">
				<ul>
				<g:eachError bean="${script}">
				    <li>${it}</li>
				</g:eachError>
				</ul>
				</div>
			</g:if>
		
			<g:form action="update" id="${script.id}">			
				<table>
					<tbody>
						<tr>
							<td><label for="name"><g:message code="label.name" /></label></td>
							<td><g:textField name="name" value="${script.name ?: ''}" /></td>
						</tr>
						<tr>
							<td><label for="description"><g:message code="label.description" /></label></td>
							<td><g:textField name="description" value="${script.description ?: ''}" /></td>
						</tr>
					</tbody>
				</table>
				<g:textArea name="definition" value="${(script.definition ?: '// Script definition')}" rows="5" cols="40"/>
				<button type="submit" class="save-button"/><g:message code="label.update" /></button>
			</g:form>
		
			<r:script>
				var textarea = $("#definition");
				var editor = CodeMirror.fromTextArea('definition',  {
					height: "600px",
					path: "",
					stylesheet: "${r.resource(dir:'/js/codemirror/css', file:'groovycolors.css', plugin:'federationregistry') }",
					basefiles: ["${r.resource(dir:'/js/codemirror/js', file:'codemirror.groovy.inframe.min.js', plugin:'federationregistry') }"],
					parserfile: [],
					content: textarea.value,
					autoMatchParens: true,
					disableSpellcheck: true,
					lineNumbers: true,
					tabMode: 'shift',
				});
			</r:script>
		</section>
	</body>
</html>