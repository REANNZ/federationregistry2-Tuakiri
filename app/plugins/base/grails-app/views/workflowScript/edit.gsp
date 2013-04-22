
<html>
  <head>
    <r:require modules="codemirror"/>
    <meta name="layout" content="workflow" />
  </head>
  <body>
      <h2><g:message encodeAs="HTML" code="views.fr.workflow.script.edit.heading" args="[script.name]"/></h2>

      <g:render template="/templates/flash" />
    
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
              <td><label for="name"><g:message encodeAs="HTML" code="label.name" /></label></td>
              <td><g:textField name="name" value="${script.name ?: ''}" /></td>
            </tr>
            <tr>
              <td><label for="description"><g:message encodeAs="HTML" code="label.description" /></label></td>
              <td><g:textField name="description" value="${script.description ?: ''}" /></td>
            </tr>
          </tbody>
        </table>
        <g:textArea name="definition" value="${(script.definition ?: '// Script definition')}" rows="5" cols="40"/>
        <br>
        <button type="submit" class="btn"/><g:message encodeAs="HTML" code="label.update" /></button>
        <g:link class="btn" controller="workflowScript" action="show" id="${script.id}"><g:message encodeAs="HTML" code="label.cancel" default="Cancel"/></g:link>
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
          tabMode: 'shift'
        });
      </r:script>
  </body>
</html>
